package cc.isotopestudio.shulkergun.shulker;
/*
 * Created by Mars Tan on 7/14/2017.
 * Copyright ISOTOPE Studio
 */

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

import static cc.isotopestudio.shulkergun.ShulkerGun.plugin;
import static cc.isotopestudio.shulkergun.ShulkerGun.shulkerData;
import static cc.isotopestudio.shulkergun.util.Util.locationToString;

public class Gun extends BukkitRunnable {

    /* settings */
    public static final int INTmaxHealth = 20;
    public static final int INTrange = 3;
    public static final int INTattack = 4;
    public static final int INTspeed = 2;
    public static final int TOPmaxHealth = 40;
    public static final int TOPrange = 7;
    public static final int TOPattack = 8;
    public static final int TOPspeed = 8;
    public static final int STEPmaxHealth = 4;
    public static final int STEPrange = 1;
    public static final int STEPattack = 1;
    public static final int STEPspeed = 1;
    public static final ItemStack ITEMmaxHealth = new ItemStack(Material.IRON_INGOT, 5);
    public static final ItemStack ITEMrange = new ItemStack(Material.GOLD_INGOT, 5);
    public static final ItemStack ITEMattack = new ItemStack(Material.DIAMOND, 3);
    public static final ItemStack ITEMspeed = new ItemStack(Material.EMERALD, 3);

    public static final Map<Location, Gun> GUNS = new HashMap<>();
    public static final Map<UUID, Gun> UUIDGUNS = new HashMap<>();

    public static final ItemStack GUNITEM = new ItemStack(Material.MONSTER_EGG);

    private final Location loc;
    private final String playerName;
    private final List<String> blockedPlayer = new ArrayList<>();
    private final UUID uuid = UUID.randomUUID();

    /* upgrades */
    private int maxHealth = INTmaxHealth;
    private int range = INTrange;
    private int attack = INTattack;
    /**
     * attack attempt per 10 sec
     */
    private int speed = INTspeed;

    private final ConfigurationSection config;
    private int health = 20;

    /**
     * Use when load Gun
     */
    Gun(Location loc) {
        this.loc = loc;
        String locString = locationToString(loc);
        assert shulkerData.isConfigurationSection(locString);
        config = shulkerData.getConfigurationSection(locString);
        playerName = config.getString("player");
        blockedPlayer.addAll(config.getStringList("blocked"));
        health = maxHealth = config.getInt("maxHealth", INTmaxHealth);
        range = config.getInt("range", INTrange);
        attack = config.getInt("attack", INTattack);
        speed = config.getInt("speed", INTspeed);
        runTaskTimer(plugin, 0, 10 * 20 / speed);
        GUNS.put(loc, this);
        UUIDGUNS.put(uuid, this);
    }

    /**
     * Use when create Gun
     */
    public Gun(String playerName, Location loc) {
        this.loc = loc;
        this.playerName = playerName;
        String locString = locationToString(loc);
        config = shulkerData.createSection(locString);
        set("player", playerName);
        config.set("maxHealth", INTmaxHealth);
        config.set("range", INTrange);
        config.set("attack", INTattack);
        config.set("speed", INTspeed);
        shulkerData.save();
        runTaskTimer(plugin, 0, 10 * 20 / speed);
        GUNS.put(loc, this);
        UUIDGUNS.put(uuid, this);
    }

    private void set(String a, Object b) {
        config.set(a, b);
        shulkerData.save();
    }

    /**
     * @return the name of the owner of the gun
     */
    public String getPlayerName() {
        return playerName;
    }

    public List<String> getBlockedPlayer() {
        return blockedPlayer;
    }

    public void saveBlockedPlayer() {
        set("blocked", blockedPlayer);
    }

    /**
     * @return cloned location of the gun
     */
    public Location getLocation() {
        return loc.clone();
    }

    public static Gun getGunFromLocation(Location loc) {
        return GUNS.getOrDefault(loc, null);
    }

    public int getAttack() {
        return attack;
    }

    public boolean upgradeAttack() {
        if (attack == TOPattack) return false;
        this.attack += STEPattack;
        config.set("attack", this.attack);
        shulkerData.save();
        return true;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean upgradeSpeed() {
        if (speed == TOPspeed) return false;
        this.speed += STEPspeed;
        config.set("speed", this.speed);
        shulkerData.save();
        try {
            cancel();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        runTaskTimer(plugin, 0, 10 * 20 / speed);
        return true;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean upgradeMaxHealth() {
        if (maxHealth == TOPmaxHealth) return false;
        this.maxHealth += STEPmaxHealth;
        config.set("maxHealth", this.maxHealth);
        shulkerData.save();
        return true;
    }

    public int getRange() {
        return range;
    }

    public boolean upgradeRange() {
        if (range == TOPrange) return false;
        this.range += STEPrange;
        config.set("range", this.range);
        shulkerData.save();
        return true;
    }

    public int getHealth() {
        return health;
    }

    public void fullHealth() {
        health = maxHealth;
    }

    /**
     * @return if gun exists after alteration
     */
    public boolean alterHealth(int alt) {
        health = health + alt;
        if (health <= 0) {
            remove();
            return false;
        }
        return true;
    }

    public void remove() {
        loc.getBlock().setType(Material.AIR);
        shulkerData.set(locationToString(loc), null);
        shulkerData.save();
        try {
            cancel();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        GUNS.remove(loc);
        UUIDGUNS.remove(uuid);
    }

    @Override
    public void run() {
        Location loc = getLocation().add(0.5, 0.5, 0.5);
        Location center = loc;
        List<Player> nearbyPlayers = Bukkit.getOnlinePlayers().stream()
                .filter(player -> {
                    Location pLoc = player.getLocation();
                    return center.getWorld().equals(pLoc.getWorld())
                            && !(center.distance(pLoc) > getRange());
                }).collect(Collectors.toList());
        getBlockedPlayer().forEach(s -> {
            for (Player nearbyPlayer : nearbyPlayers) {
                if (nearbyPlayer.getName().equals(s)) {
                    nearbyPlayers.remove(nearbyPlayer);
                    break;
                }
            }
        });
        Player owner = Bukkit.getPlayerExact(getPlayerName());
        if (owner != null) nearbyPlayers.remove(owner);
        if (nearbyPlayers.size() < 1) {
            return;
        }
        for (int i = 0; i < 5; i++) {
            Collections.shuffle(nearbyPlayers);
            Player player = nearbyPlayers.get(0);
            loc = center.clone().add(player.getLocation()
                    .subtract(center).toVector().normalize().multiply(1.9));
            ShulkerBullet bullet =
                    ((ShulkerBullet) loc.getWorld().spawnEntity(loc, EntityType.SHULKER_BULLET));
            bullet.setCustomName(uuid.toString());
            bullet.setCustomNameVisible(false);
            bullet.getLocation().setDirection(player.getLocation().subtract(bullet.getLocation()).toVector());
            bullet.setVelocity(player.getLocation().subtract(bullet.getLocation()).toVector().normalize().multiply(2));
            bullet.setBounce(false);
//                bullet.setShooter((ProjectileSource) event.getEntity());
            bullet.setTarget(player);
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    bullet.setVelocity(bullet.getVelocity().multiply(2));
                    // To make it go faster
                }
            }.runTaskTimer(plugin, 2, 10);
            new BukkitRunnable() {
                @Override
                public void run() {
                    task.cancel();
                    bullet.remove();
                }
            }.runTaskLater(plugin, 40);
            System.out.println(uuid.toString() + ":" + health);
        }
    }
}
