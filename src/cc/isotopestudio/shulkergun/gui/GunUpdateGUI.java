/*
 * Copyright (c) 2016. ISOTOPE Studio
 */

package cc.isotopestudio.shulkergun.gui;

import cc.isotopestudio.shulkergun.shulker.Gun;
import cc.isotopestudio.shulkergun.util.MaterialCN;
import cc.isotopestudio.shulkergun.util.S;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import static cc.isotopestudio.shulkergun.shulker.Gun.*;
import static cc.isotopestudio.shulkergun.util.Util.buildItem;


public class GunUpdateGUI extends GUI {

    private final static ItemStack GLASS = buildItem(Material.STAINED_GLASS_PANE, (short) 7, false, "---", "");
    private final static int SPEEDPOS = 10;
    private final static int RANGEPOS = 12;
    private final static int HEALTHPOS = 14;
    private final static int ATTACKPOS = 16;

    private final Gun gun;

    public GunUpdateGUI(Player player, Gun gun) {
        super(S.toBoldGold("��̨����") + "[" + player.getName() + "]", 3 * 9, player);
        this.gun = gun;
        for (int i = 0; i < 9; i++) {
            setOption(i, GLASS);
            setOption(i + 18, GLASS);
        }
        ItemStack SPEEDITEM = buildItem(Material.SUGAR, true,
                S.toBoldGold("��������"),
                S.toGreen("��ǰ") + " / " + S.toYellow("���"),
                S.toGreen("" + gun.getSpeed()) + " / " + S.toYellow("" + TOPspeed),
                S.toAqua("�������: " + MaterialCN.getMaterialChinese(ITEMspeed)));
        ItemStack RANGEITEM = buildItem(Material.GLOWSTONE_DUST, true,
                S.toBoldGold("����������Χ"),
                S.toGreen("��ǰ") + " / " + S.toYellow("���"),
                S.toGreen("" + gun.getRange()) + " / " + S.toYellow("" + TOPrange),
                S.toAqua("�������: " + MaterialCN.getMaterialChinese(ITEMrange)));
        ItemStack HEALTHITEM = buildItem(Material.REDSTONE, true,
                S.toBoldGold("����Ѫ��"),
                S.toGreen("��ǰ") + " / " + S.toYellow("���"),
                S.toGreen("" + gun.getMaxHealth()) + " / " + S.toYellow("" + TOPmaxHealth),
                S.toAqua("�������: " + MaterialCN.getMaterialChinese(ITEMmaxHealth)));
        ItemStack ATTACKITEM = buildItem(Material.DIAMOND_SWORD, true,
                S.toBoldGold("�����˺�"),
                S.toGreen("��ǰ") + " / " + S.toYellow("���"),
                S.toGreen("" + gun.getAttack()) + " / " + S.toYellow("" + TOPattack),
                S.toAqua("�������: " + MaterialCN.getMaterialChinese(ITEMattack)));
        setOption(SPEEDPOS, SPEEDITEM);
        setOption(RANGEPOS, RANGEITEM);
        setOption(HEALTHPOS, HEALTHITEM);
        setOption(ATTACKPOS, ATTACKITEM);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(name) && playerName.equals(event.getWhoClicked().getName())) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot <= 8 || slot >= 18) {
                return;
            }
            if (optionIcons[slot] != null) {
                switch (slot) {
                    case (SPEEDPOS):
                        int invPos = getContent(player.getInventory(), ITEMspeed);
                        if (invPos != -1) {
                            if (gun.upgradeSpeed()) {
                                ItemStack item = player.getInventory().getItem(invPos);
                                item.setAmount(item.getAmount() - ITEMspeed.getAmount());
                                player.getInventory().setItem(invPos, item);
                                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                            } else {
                                player.sendMessage(S.toPrefixRed("�Ѿ�����"));
                            }
                        } else {
                            player.sendMessage(S.toPrefixRed("��û���������"));
                        }
                        break;
                    case (RANGEPOS):
                        invPos = getContent(player.getInventory(), ITEMrange);
                        if (invPos != -1) {
                            if (gun.upgradeRange()) {
                                ItemStack item = player.getInventory().getItem(invPos);
                                item.setAmount(item.getAmount() - ITEMrange.getAmount());
                                player.getInventory().setItem(invPos, item);
                                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                            } else {
                                player.sendMessage(S.toPrefixRed("�Ѿ�����"));
                            }
                        } else {
                            player.sendMessage(S.toPrefixRed("��û���������"));
                        }
                        break;
                    case (HEALTHPOS):
                        invPos = getContent(player.getInventory(), ITEMmaxHealth);
                        if (invPos != -1) {
                            if (gun.upgradeMaxHealth()) {
                                ItemStack item = player.getInventory().getItem(invPos);
                                item.setAmount(item.getAmount() - ITEMmaxHealth.getAmount());
                                player.getInventory().setItem(invPos, item);
                                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                            } else {
                                player.sendMessage(S.toPrefixRed("�Ѿ�����"));
                            }
                        } else {
                            player.sendMessage(S.toPrefixRed("��û���������"));
                        }
                        break;
                    case (ATTACKPOS):
                        invPos = getContent(player.getInventory(), ITEMattack);
                        if (invPos != -1) {
                            if (gun.upgradeAttack()) {
                                ItemStack item = player.getInventory().getItem(invPos);
                                item.setAmount(item.getAmount() - ITEMattack.getAmount());
                                player.getInventory().setItem(invPos, item);
                                player.sendMessage(S.toPrefixGreen("�ɹ�����"));
                            } else {
                                player.sendMessage(S.toPrefixRed("�Ѿ�����"));
                            }
                        } else {
                            player.sendMessage(S.toPrefixRed("��û���������"));
                        }
                        break;
                }
                player.closeInventory();
            }
        }
    }

    private static int getContent(PlayerInventory inv, ItemStack item) {
        ItemStack[] contents = inv.getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack content = contents[i];
            if (content == null) continue;
            if (content.isSimilar(item)) {
                if (content.getAmount() >= item.getAmount()) {
                    return i;
                }
            }
        }
        return -1;
    }

}
