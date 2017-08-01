package cc.isotopestudio.shulkergun.listener;
/*
 * Created by Mars Tan on 7/14/2017.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.shulkergun.gui.GunUpdateGUI;
import cc.isotopestudio.shulkergun.shulker.Gun;
import cc.isotopestudio.shulkergun.util.S;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static cc.isotopestudio.shulkergun.shulker.Gun.GUNS;

public class PlayerInteractGunListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSpawn(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.isSimilar(Gun.GUNITEM)) {
            return;
        }
        event.setCancelled(true);
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getBlockFace() != BlockFace.UP) {
            return;
        }
        if (player.getGameMode() != GameMode.CREATIVE) {
            item.setAmount(item.getAmount() - 1);
            player.getInventory().setItemInMainHand(item);
        }
        Location loc = event.getClickedBlock().getLocation().add(0, 1, 0);
        loc.getBlock().setType(Material.SILVER_SHULKER_BOX);
        new Gun(player.getName(), loc);
        player.sendMessage(S.toPrefixGreen("成功创建炮台"));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRightClick(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (!GUNS.containsKey(event.getClickedBlock().getLocation())) {
            return;
        }
        event.setCancelled(true);
        Player player = event.getPlayer();
        Gun gun = GUNS.get(event.getClickedBlock().getLocation());
        if (!player.getName().equals(gun.getPlayerName())) {
            return;
        }
        new GunUpdateGUI(player, gun).open(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeftClick(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        Location loc = event.getClickedBlock().getLocation();
        if (!GUNS.containsKey(loc)) {
            return;
        }
        event.setCancelled(true);
        Player player = event.getPlayer();
        Gun gun = GUNS.get(loc);
        if (player.getName().equals(gun.getPlayerName()) &&
                player.getInventory().getItemInMainHand().getType() == Material.SNOW_BALL) {
            gun.remove();
            player.sendMessage(S.toPrefixGreen("成功删除炮台"));
            return;
        }
        if (gun.getPlayerName().equals(player.getName())) {
            player.sendMessage(S.toPrefixYellow("请使用雪球删除炮台"));
        } else {
            loc.add(0.5, 0.5, 0.5);
            loc.getWorld().spawnParticle(Particle.CRIT, loc, 10, 1, 1, 1, 0);
            loc.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, loc, 5, 1, 1, 1, 0);
            loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 5);
            ItemStack apple = new ItemStack(Material.REDSTONE);
            for (int i = 0; i < 20; i++) {
                loc.getWorld().spawnParticle(Particle.ITEM_CRACK,
                        loc.clone().add(Math.random() * 2 - 1, Math.random() * 2 - 1, Math.random() * 2 - 1), 1, 0, 0, 0, 0, apple);
            }
            if (gun.alterHealth(-2)) {
                player.sendMessage(S.toPrefixYellow("炮台: ") + gun.getHealth() + " / " + S.toRed("" + gun.getMaxHealth()));

            } else {
                player.sendMessage(S.toPrefixYellow("炮台已被破坏"));
            }
        }
        return;
    }

}
