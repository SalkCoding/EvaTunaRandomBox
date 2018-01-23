package net.alkarb.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.bukkit.entity.Player;

import net.alkarb.daily.DailyManager;
import net.alkarb.main.Main;

public class PlayerConfig {

	private static final File dir = Main.getInstance().getDataFolder();
	private static final File dir1 = new File(dir, "Player");

	public static void setPlayer(Player player, Boolean set) {
		if (!(dir1.exists())) {
			dir1.mkdirs();
		}
		File file = new File(dir1,player.getUniqueId() + ".yml");
		LinmaluYamlConfiguration config = LinmaluYamlConfiguration.loadConfiguration(file);
		config.set("Name", player.getName());
		config.set("DailyClearStatus", set);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void resetFiles() throws IOException {
		File[] filelist = dir1.listFiles();
		for (File file : filelist) {
			LinmaluYamlConfiguration config = LinmaluYamlConfiguration.loadConfiguration(file);
			config.set("Name", config.get("Name"));
			config.set("DailyClearStatus", false);
			config.save(file);
		}
		DailyManager.loadList();
	}
	
	public static HashMap<String,Boolean> loadList(){
		HashMap<String,Boolean> list = new HashMap<String,Boolean>();
		File[] filelist = dir1.listFiles();
		for (File file : filelist) {
			LinmaluYamlConfiguration config = LinmaluYamlConfiguration.loadConfiguration(file);
			list.put(config.getString("Name"), config.getBoolean("DailyClearStatus"));
		}
		return list;
	}
	
}
