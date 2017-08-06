package cc.isotopestudio.shulkergun;

import cc.isotopestudio.shulkergun.cmd.CommandBattery;
import cc.isotopestudio.shulkergun.listener.GunAttackListener;
import cc.isotopestudio.shulkergun.listener.PlayerInteractGunListener;
import cc.isotopestudio.shulkergun.shulker.GunParticleTask;
import cc.isotopestudio.shulkergun.shulker.GunRegenTask;
import cc.isotopestudio.shulkergun.shulker.LoadGunTask;
import cc.isotopestudio.shulkergun.util.PluginFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class ShulkerGun extends JavaPlugin {

    private static final String pluginName = "ShulkerGun";
    public static final String prefix = (new StringBuilder()).append(ChatColor.GOLD).append(ChatColor.BOLD).append("[")
            .append("炮台").append("]").append(ChatColor.RED).toString();

    public static ShulkerGun plugin;

    public static PluginFile config;
    public static PluginFile shulkerData;
    public static PluginFile playerData;

    @Override
    public void onEnable() {
        plugin = this;
        config = new PluginFile(this, "config.yml", "config.yml");
        config.setEditable(false);
        shulkerData = new PluginFile(this, "shulker.yml");
        playerData = new PluginFile(this, "player.yml");

        this.getCommand("battery").setExecutor(new CommandBattery());

//        Bukkit.getPluginManager().registerEvents(new ShulkerTest(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractGunListener(), this);
        Bukkit.getPluginManager().registerEvents(new GunAttackListener(), this);

        new LoadGunTask().runTaskLater(this, 1);
        new GunParticleTask().runTaskTimer(this, 20, 20);
        new GunRegenTask().runTaskTimer(this, 20, 60);

        getLogger().info(pluginName + " " + getDescription().getVersion() + " 成功加载!");
        getLogger().info(pluginName + "由ISOTOPE Studio制作!");
        getLogger().info("http://isotopestudio.cc");
    }

    public void onReload() {
        config.reload();
        shulkerData.reload();
        new LoadGunTask().runTaskLater(this, 1);
    }

    @Override
    public void onDisable() {
        getLogger().info(pluginName + "成功卸载!");
    }

}
