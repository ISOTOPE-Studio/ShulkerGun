package cc.isotopestudio.shulkergun.listener;
/*
 * Created by Mars Tan on 7/14/2017.
 * Copyright ISOTOPE Studio
 */

import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static cc.isotopestudio.shulkergun.ShulkerGun.prefix;

public class GunAttackListener implements Listener {


    @EventHandler
    public void onDamged(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof ShulkerBullet)) {
            return;
        }
        ShulkerBullet bullet = (ShulkerBullet) event.getDamager();
        if (!bullet.getCustomName().equals(prefix)) return;
        event.setCancelled(true);
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            player.damage(2/*TO-DO*/, bullet);
        }
        bullet.remove();
    }
}
