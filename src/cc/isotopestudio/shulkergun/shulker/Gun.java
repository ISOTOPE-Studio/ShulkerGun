package cc.isotopestudio.shulkergun.shulker;
/*
 * Created by Mars Tan on 7/14/2017.
 * Copyright ISOTOPE Studio
 */

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cc.isotopestudio.shulkergun.ShulkerGun.shulkerData;
import static cc.isotopestudio.shulkergun.util.Util.locationToString;

public class Gun {

    public static final Map<Location, Gun> GUNS = new HashMap<>();

    public static final ItemStack GUNITEM = new ItemStack(Material.MONSTER_EGG);

    private final Location loc;
    private final String playerName;
    private final List<String> blockedPlayer = new ArrayList<>();
    private final ConfigurationSection config;

    private int attack;
    private int health;

    /**
    * Use when load Gun
    */
    Gun(Location loc) {
        this.loc = loc;
        String locString = locationToString(loc);
        assert shulkerData.isConfigurationSection(locString);
        config = shulkerData.getConfigurationSection(locString);
        playerName = config.getString("player");
        GUNS.put(loc, this);
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
        GUNS.put(loc, this);
    }

    private void set(String a, Object b) {
        config.set(a, b);
        shulkerData.save();
    }

    public String getPlayerName() {
        return playerName;
    }

    public List<String> getBlockedPlayer() {
        return blockedPlayer;
    }

    public void saveBlockedPlayer() {
        set("blocked", blockedPlayer);
    }

    public Location getLocation() {
        return loc.clone();
    }

    public static Gun getGunFromLocation(Location loc) {

        return GUNS.getOrDefault(loc, null);
    }

    public void remove() {
        loc.getBlock().setType(Material.AIR);
        shulkerData.set(locationToString(loc), null);
        shulkerData.save();
        GUNS.remove(loc);
    }

    @Override
    public String toString() {
        return "Gun{" + "loc=" + loc +
                ", playerName='" + playerName + '\'' +
                ", blockedPlayer=" + blockedPlayer +
                ", attack=" + attack +
                ", health=" + health +
                '}';
    }
}
