package cc.isotopestudio.shulkergun.cmd;
/*
 * Created by Mars Tan on 7/14/2017.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.shulkergun.shulker.Gun;
import cc.isotopestudio.shulkergun.util.S;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

import static cc.isotopestudio.shulkergun.shulker.Gun.GUNITEM;
import static cc.isotopestudio.shulkergun.shulker.Gun.GUNS;

public class CommandBattery implements CommandExecutor {

    private static final Set<Material> MATERIALS = new HashSet<>();

    public CommandBattery() {
        MATERIALS.add(Material.AIR);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("battery")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(S.toPrefixRed("���ִ�е�����"));
                return true;
            }
            Player player = (Player) sender;
//            if (!player.hasPermission("battery.player")) {
//                player.sendMessage(S.toPrefixRed("��û��Ȩ��"));
//                return true;
//            }
            if (args.length < 1) {
                player.sendMessage(S.toPrefixGreen("�����˵�"));
                if (player.isOp())
                    player.sendMessage(S.toYellow("/" + label + " get - ���һ��ǱӰ����̨"));
                player.sendMessage(S.toYellow("/" + label + " shut <�������> - ����ϵ�ǱӰ����̨�������"));
                player.sendMessage(S.toYellow("/" + label + " shutall <�������> - ����ǱӰ����̨�������"));
                player.sendMessage(S.toYellow("/" + label + " list - �鿴ǱӰ����̨�б�"));
                return true;
            }
            if (args[0].equalsIgnoreCase("shutall")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " shutall <�������> - ����ǱӰ����̨�������"));
                    return true;
                }
                Player shutPlayer = Bukkit.getPlayer(args[1]);
                String name;
                if (shutPlayer == null) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                    if (offlinePlayer == null) {
                        player.sendMessage(S.toPrefixRed("��Ҳ�����"));
                        return true;
                    } else {
                        name = offlinePlayer.getName();
                    }
                } else {
                    name = shutPlayer.getName();
                }
                final int[] count = {0};
                GUNS.values().stream().filter(gun -> gun.getPlayerName().equals(player.getName())).forEach(gun -> {
                    if (!gun.getBlockedPlayer().contains(name)) {
                        gun.getBlockedPlayer().add(name);
                        gun.saveBlockedPlayer();
                    }
                    count[0] = count[0] + 1;
                });
                if (count[0] == 0)
                    player.sendMessage(S.toPrefixRed("û�п����ε���̨"));
                else
                    player.sendMessage(S.toPrefixGreen("�ɹ�����" + name));
                return true;
            }
            if (args[0].equalsIgnoreCase("shut")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " shut <�������> - ����ϵ�ǱӰ����̨�������"));
                    return true;
                }
                Block targetBlock = player.getTargetBlock(MATERIALS, 5);
                Gun gun;
                if (targetBlock == null || targetBlock.getType() != Material.SILVER_SHULKER_BOX) {
                    player.sendMessage(S.toPrefixRed("�ⲻ����̨"));
                    return true;
                } else {
                    gun = Gun.getGunFromLocation(targetBlock.getLocation());
                    if (gun == null) {
                        player.sendMessage(S.toPrefixRed("�ⲻ����̨"));
                        return true;
                    }
                }
                Player shutPlayer = Bukkit.getPlayer(args[1]);
                String name;
                if (shutPlayer == null) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                    if (offlinePlayer == null) {
                        player.sendMessage(S.toPrefixRed("��Ҳ�����"));
                        return true;
                    } else {
                        name = offlinePlayer.getName();
                    }
                } else {
                    name = shutPlayer.getName();
                }
                if (gun.getBlockedPlayer().contains(name)) {
                    player.sendMessage(S.toPrefixRed("����Ѿ�������"));
                    return true;
                }
                gun.getBlockedPlayer().add(name);
                gun.saveBlockedPlayer();
                player.sendMessage(S.toPrefixGreen("�ɹ�����" + name));
                return true;
            }
            if (args[0].equalsIgnoreCase("get")) {
                if (!player.isOp()) {
                    player.sendMessage(S.toPrefixRed("��û��Ȩ��"));
                    return true;
                }
                player.getInventory().addItem(GUNITEM);
                player.sendMessage(S.toPrefixGreen("�ɹ�"));
                return true;
            }
            player.sendMessage(S.toPrefixRed("δָ֪��"));
            return true;
        }
        return false;
    }
}