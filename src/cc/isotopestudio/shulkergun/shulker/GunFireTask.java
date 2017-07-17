package cc.isotopestudio.shulkergun.shulker;
/*
 * Created by Mars Tan on 7/17/2017.
 * Copyright ISOTOPE Studio
 */

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static cc.isotopestudio.shulkergun.ShulkerGun.plugin;
import static cc.isotopestudio.shulkergun.ShulkerGun.prefix;
import static cc.isotopestudio.shulkergun.shulker.Gun.GUNS;

public class GunFireTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Gun gun : GUNS.values()) {
            Location loc = gun.getLocation().add(0.5, 0.5, 0.5);
            Location center = loc;
            final int[] count = {0};
            new BukkitRunnable() {
                @Override
                public void run() {
                    count[0]++;
                    center.getWorld().spawnParticle(
                            Particle.FLAME, center.clone().add(0.5, 0.5, 0.5),
                            1, 0, 0, 0, 0);
                    center.getWorld().spawnParticle(
                            Particle.FLAME, center.clone().add(0.5, 0.5, -0.5),
                            1, 0, 0, 0, 0);
                    center.getWorld().spawnParticle(
                            Particle.FLAME, center.clone().add(-0.5, 0.5, -0.5),
                            1, 0, 0, 0, 0);
                    center.getWorld().spawnParticle(
                            Particle.FLAME, center.clone().add(-0.5, 0.5, 0.5),
                            1, 0, 0, 0, 0);
                    if (count[0] > 5) cancel();
                }
            }.runTaskTimer(plugin, 0, 5);


            List<Player> nearbyPlayers = Bukkit.getOnlinePlayers().stream()
                    .filter(player -> {
                        Location pLoc = player.getLocation();
                        return center.getWorld().equals(pLoc.getWorld())
                                && !(center.distance(pLoc) > 7);
                    }).collect(Collectors.toList());
            gun.getBlockedPlayer().forEach(s -> {
                for (Player nearbyPlayer : nearbyPlayers) {
                    if(nearbyPlayer.getName().equals(s)) {
                        nearbyPlayers.remove(nearbyPlayer);
                        break;
                    }
                }
            });
            Player owner = Bukkit.getPlayerExact(gun.getPlayerName());
            if (owner != null) nearbyPlayers.remove(owner);
            if (nearbyPlayers.size() < 1) {
                continue;
            }
            for (int i = 0; i < 5; i++) {
                Collections.shuffle(nearbyPlayers);
                Player player = nearbyPlayers.get(0);
                loc = center.clone().add(player.getLocation()
                        .subtract(center).toVector().normalize().multiply(1.9));
                ShulkerBullet bullet =
                        ((ShulkerBullet) loc.getWorld().spawnEntity(loc, EntityType.SHULKER_BULLET));
                bullet.setCustomName(prefix);
                bullet.setCustomNameVisible(false);
                bullet.getLocation().setDirection(player.getLocation().subtract(bullet.getLocation()).toVector());
                bullet.setVelocity(player.getLocation().subtract(bullet.getLocation()).toVector().normalize().multiply(2));
                bullet.setBounce(false);
//                bullet.setShooter((ProjectileSource) event.getEntity());
                bullet.setTarget(player);
                Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    bullet.setVelocity(bullet.getVelocity().multiply(2));
                    // To make it go faster
                }, 2, 10);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        bullet.remove();
                    }
                }.runTaskLater(plugin, 40);
            }
        }
    }
}
