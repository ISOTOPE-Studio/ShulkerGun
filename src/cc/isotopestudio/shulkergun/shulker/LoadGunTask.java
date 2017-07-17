package cc.isotopestudio.shulkergun.shulker;
/*
 * Created by Mars Tan on 7/14/2017.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.shulkergun.util.S;
import cc.isotopestudio.shulkergun.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static cc.isotopestudio.shulkergun.ShulkerGun.shulkerData;
import static cc.isotopestudio.shulkergun.shulker.Gun.GUNITEM;
import static cc.isotopestudio.shulkergun.shulker.Gun.GUNS;

public class LoadGunTask extends BukkitRunnable {
    @Override
    public void run() {
        SpawnEggMeta meta = (SpawnEggMeta) GUNITEM.getItemMeta();
        meta.setSpawnedType(EntityType.SHULKER);
        meta.setDisplayName(S.toBoldGold("创建炮台"));
        List<String> lore = new ArrayList<>();
        lore.add(S.toGreen("右键创建炮台"));
        meta.setLore(lore);
        GUNITEM.setItemMeta(meta);

        GUNS.clear();
        shulkerData.getKeys(false).forEach(locString -> {
            new Gun(Util.stringToLocation(locString));
        });
    }
}
