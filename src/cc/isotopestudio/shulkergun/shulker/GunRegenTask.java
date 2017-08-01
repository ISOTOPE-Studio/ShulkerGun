package cc.isotopestudio.shulkergun.shulker;
/*
 * Created by Mars Tan on 7/17/2017.
 * Copyright ISOTOPE Studio
 */

import org.bukkit.scheduler.BukkitRunnable;

import static cc.isotopestudio.shulkergun.shulker.Gun.GUNS;

public class GunRegenTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Gun gun : GUNS.values()) {
            int health = gun.getHealth() + 2;
            if (health >= gun.getMaxHealth())
                gun.fullHealth();
            else
                gun.alterHealth(2);
        }
    }
}
