package cc.isotopestudio.shulkergun;

import cc.isotopestudio.shulkergun.util.PluginFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class ShulkerGun extends JavaPlugin {

    private static final String pluginName = "ShulkerGun";
    public static final String prefix = (new StringBuilder()).append(ChatColor.GOLD).append(ChatColor.BOLD).append("[")
            .append("ShulkerGun").append("]").append(ChatColor.RED).toString();

    public static ShulkerGun plugin;

    public static PluginFile config;
    public static PluginFile playerData;

    @Override
    public void onEnable() {
        plugin = this;
//        config = new PluginFile(this, "config.yml", "config.yml");
//        config.setEditable(false);
//        playerData = new PluginFile(this, "player.yml");

        //this.getCommand("csclass").setExecutor(new CommandCsclass());

        Bukkit.getPluginManager().registerEvents(new ShulkerTest(), this);

        getLogger().info(pluginName + "成功加载!");
        getLogger().info(pluginName + "由ISOTOPE Studio制作!");
        getLogger().info("http://isotopestudio.cc");
    }

    public void onReload() {
    }

    @Override
    public void onDisable() {
        getLogger().info(pluginName + "成功卸载!");
    }

}
