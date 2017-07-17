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
                sender.sendMessage(S.toPrefixRed("玩家执行的命令"));
                return true;
            }
            Player player = (Player) sender;
//            if (!player.hasPermission("battery.player")) {
//                player.sendMessage(S.toPrefixRed("你没有权限"));
//                return true;
//            }
            if (args.length < 1) {
                player.sendMessage(S.toPrefixGreen("帮助菜单"));
                if (player.isOp())
                    player.sendMessage(S.toYellow("/" + label + " get - 获得一个潜影盒炮台"));
                player.sendMessage(S.toYellow("/" + label + " shut <玩家名字> - 光标上的潜影盒炮台屏蔽玩家"));
                player.sendMessage(S.toYellow("/" + label + " shutall <玩家名字> - 所有潜影盒炮台屏蔽玩家"));
                player.sendMessage(S.toYellow("/" + label + " list - 查看潜影盒炮台列表"));
                return true;
            }
            if (args[0].equalsIgnoreCase("shutall")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " shutall <玩家名字> - 所有潜影盒炮台屏蔽玩家"));
                    return true;
                }
                Player shutPlayer = Bukkit.getPlayer(args[1]);
                String name;
                if (shutPlayer == null) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                    if (offlinePlayer == null) {
                        player.sendMessage(S.toPrefixRed("玩家不存在"));
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
                    player.sendMessage(S.toPrefixRed("没有可屏蔽的炮台"));
                else
                    player.sendMessage(S.toPrefixGreen("成功屏蔽" + name));
                return true;
            }
            if (args[0].equalsIgnoreCase("shut")) {
                if (args.length < 2) {
                    player.sendMessage(S.toYellow("/" + label + " shut <玩家名字> - 光标上的潜影盒炮台屏蔽玩家"));
                    return true;
                }
                Block targetBlock = player.getTargetBlock(MATERIALS, 5);
                Gun gun;
                if (targetBlock == null || targetBlock.getType() != Material.SILVER_SHULKER_BOX) {
                    player.sendMessage(S.toPrefixRed("这不是炮台"));
                    return true;
                } else {
                    gun = Gun.getGunFromLocation(targetBlock.getLocation());
                    if (gun == null) {
                        player.sendMessage(S.toPrefixRed("这不是炮台"));
                        return true;
                    }
                }
                Player shutPlayer = Bukkit.getPlayer(args[1]);
                String name;
                if (shutPlayer == null) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                    if (offlinePlayer == null) {
                        player.sendMessage(S.toPrefixRed("玩家不存在"));
                        return true;
                    } else {
                        name = offlinePlayer.getName();
                    }
                } else {
                    name = shutPlayer.getName();
                }
                if (gun.getBlockedPlayer().contains(name)) {
                    player.sendMessage(S.toPrefixRed("玩家已经被屏蔽"));
                    return true;
                }
                gun.getBlockedPlayer().add(name);
                gun.saveBlockedPlayer();
                player.sendMessage(S.toPrefixGreen("成功屏蔽" + name));
                return true;
            }
            if (args[0].equalsIgnoreCase("get")) {
                if (!player.isOp()) {
                    player.sendMessage(S.toPrefixRed("你没有权限"));
                    return true;
                }
                player.getInventory().addItem(GUNITEM);
                player.sendMessage(S.toPrefixGreen("成功"));
                return true;
            }
            player.sendMessage(S.toPrefixRed("未知指令"));
            return true;
        }
        return false;
    }
}