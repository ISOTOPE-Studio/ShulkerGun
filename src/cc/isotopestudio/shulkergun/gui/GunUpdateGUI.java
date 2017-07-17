/*
 * Copyright (c) 2016. ISOTOPE Studio
 */

package cc.isotopestudio.shulkergun.gui;

import cc.isotopestudio.shulkergun.shulker.Gun;
import cc.isotopestudio.shulkergun.util.S;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;


public class GunUpdateGUI extends GUI {

    public GunUpdateGUI(Player player, Gun gun) {
        super(S.toBoldGold("Éý¼¶") + "[" + player.getName() + "]", 3 * 9, player);


    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(name) && playerName.equals(event.getWhoClicked().getName())) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot < 0 || slot >= size) {
                return;
            }

            if (optionIcons[slot] != null) {

                player.closeInventory();
            }
        }
    }

}
