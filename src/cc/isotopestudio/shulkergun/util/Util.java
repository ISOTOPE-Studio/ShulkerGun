package cc.isotopestudio.shulkergun.util;
/*
 * Created by Mars Tan on 7/5/2017.
 * Copyright ISOTOPE Studio
 */

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Util {

    public static String locationToString(Location loc) {
        if (loc == null) return "";
        return loc.getWorld().getName()
                + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
    }

    public static Location stringToLocation(String string) {
        if (string == null) return null;
        String[] s = string.split(" ");
        if (s.length != 4) return null;
        World world = Bukkit.getWorld(s[0]);
        int x = Integer.parseInt(s[1]);
        int y = Integer.parseInt(s[2]);
        int z = Integer.parseInt(s[3]);
        return new Location(world, x, y, z);
    }

    public static ItemStack buildItem(Material type, boolean enchant, String displayName, String... lore) {
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(Arrays.asList(lore));
        if (enchant) {
            meta.addEnchant(Enchantment.DURABILITY, 10, true);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack buildItem(Material type, short damage, boolean enchant, String displayName, String... lore) {
        ItemStack item = buildItem(type, enchant, displayName, lore);
        item.setDurability(damage);
        return item;
    }
}
