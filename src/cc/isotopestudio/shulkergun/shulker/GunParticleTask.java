package cc.isotopestudio.shulkergun.shulker;
/*
 * Created by Mars Tan on 7/17/2017.
 * Copyright ISOTOPE Studio
 */

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import static cc.isotopestudio.shulkergun.ShulkerGun.plugin;
import static cc.isotopestudio.shulkergun.shulker.Gun.GUNS;

public class GunParticleTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Gun gun : GUNS.values()) {
            Location loc = gun.getLocation().add(0.5, 0.5, 0.5);
            final int[] count = {0};
            new BukkitRunnable() {
                @Override
                public void run() {
                    count[0]++;
                    loc.getWorld().spawnParticle(
                            Particle.FLAME, loc.clone().add(0.5, 0.5, 0.5),
                            1, 0, 0, 0, 0);
                    loc.getWorld().spawnParticle(
                            Particle.FLAME, loc.clone().add(0.5, 0.5, -0.5),
                            1, 0, 0, 0, 0);
                    loc.getWorld().spawnParticle(
                            Particle.FLAME, loc.clone().add(-0.5, 0.5, -0.5),
                            1, 0, 0, 0, 0);
                    loc.getWorld().spawnParticle(
                            Particle.FLAME, loc.clone().add(-0.5, 0.5, 0.5),
                            1, 0, 0, 0, 0);
                    if (count[0] > 5) cancel();
                }
            }.runTaskTimer(plugin, 0, 5);
        }
    }
}
