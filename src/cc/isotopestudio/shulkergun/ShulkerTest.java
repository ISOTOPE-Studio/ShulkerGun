package cc.isotopestudio.shulkergun;
/*
 * Created by Mars Tan on 7/5/2017.
 * Copyright ISOTOPE Studio
 */

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import static cc.isotopestudio.shulkergun.ShulkerGun.plugin;

public class ShulkerTest implements Listener {

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent event) {
        if (event.getEntity() instanceof Shulker) {
            event.setCancelled(true);
            Location loc = event.getEntity().getLocation();
            for (int i = 0; i < 10; i++) {
                ShulkerBullet bullet =
                        ((ShulkerBullet) loc.getWorld().spawnEntity(loc, EntityType.SHULKER_BULLET));
                Player player = Bukkit.getOnlinePlayers().stream().findFirst().get();
                bullet.getLocation().setDirection(player.getLocation().subtract(bullet.getLocation()).toVector());
                bullet.setVelocity(player.getLocation().subtract(bullet.getLocation()).toVector().normalize().multiply(2));
                bullet.setBounce(false);
                bullet.setShooter((ProjectileSource) event.getEntity());

                bullet.setTarget(player);
                Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    bullet.setVelocity(bullet.getVelocity().multiply(2));
                    // To make it go faster
                }, 2, 10);
            }
        }
    }

}
