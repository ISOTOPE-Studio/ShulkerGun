package cc.isotopestudio.shulkergun.listener;
/*
 * Created by Mars Tan on 7/14/2017.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.shulkergun.shulker.Gun;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

import static cc.isotopestudio.shulkergun.shulker.Gun.UUIDGUNS;

public class GunAttackListener implements Listener {


    @EventHandler
    public void onDamged(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof ShulkerBullet)) {
            return;
        }
        ShulkerBullet bullet = (ShulkerBullet) event.getDamager();
        if (!UUIDGUNS.containsKey(UUID.fromString(bullet.getCustomName()))) return;
        event.setCancelled(true);
        if (event.getEntity() instanceof Player) {
            Gun gun = UUIDGUNS.get(UUID.fromString(bullet.getCustomName()));
            Player player = (Player) event.getEntity();
            player.damage(gun.getAttack(), bullet);
        }
        bullet.remove();
    }
}
