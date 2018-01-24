package net.alkaonline.alkarandombox;

import net.alkaonline.alkarandombox.boxmanager.BoxManager;
import net.alkaonline.alkarandombox.command.RandomBoxCommand;
import net.alkaonline.alkarandombox.daily.DailyManager;
import net.alkaonline.alkarandombox.listener.BlockPlaceListener;
import net.alkaonline.alkarandombox.listener.InventoryClickListener;
import net.alkaonline.alkarandombox.listener.PlayerInteractListener;
import net.alkaonline.alkarandombox.listener.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class AlkaRandomBox extends JavaPlugin {

    private BoxManager boxManager;
    private DailyManager dailyManager;

    @Override
    public void onEnable() {
        boxManager = new BoxManager(getDataFolder().toPath().resolve("RandomBox"));
        try {
            boxManager.loadBoxList();
        } catch (IOException e) {
            getLogger().severe("랜덤박스를 불러 오는 데 오류가 발생했습니다. 플러그인을 비활성화합니다.");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }

        dailyManager = DailyManager.loadFromConfig(getConfig());
        getCommand("랜덤박스").setExecutor(new RandomBoxCommand(boxManager));
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(boxManager, dailyManager), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(boxManager), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(boxManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(this), this);
    }

    @Override
    public void onDisable() {
        dailyManager.saveToConfig(getConfig());
        saveConfig();
    }

    public BoxManager getBoxManager() {
        return boxManager;
    }

    public DailyManager getDailyManager() {
        return dailyManager;
    }

}
