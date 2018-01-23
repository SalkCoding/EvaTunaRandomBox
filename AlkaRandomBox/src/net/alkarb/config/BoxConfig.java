package net.alkarb.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import net.alkarb.boxmanager.Box;
import net.alkarb.main.Main;

public class BoxConfig {

	private static final File dir = Main.getInstance().getDataFolder();
	private static final File dir1 = new File(dir, "RandomBox");
	private static final File test = new File(dir1, "test.yml");

	public static ArrayList<Box> loadBoxList() throws IOException {
		if (!(dir.exists())) {
			dir.mkdirs();
			if (!(test.exists())) {
				writeTest();
			}
		}
		if (!(dir1.exists())) {
			dir1.mkdirs();
			if (!(test.exists())) {
				writeTest();
			}
		}
		File[] filelist = dir1.listFiles();
		ArrayList<Box> list = new ArrayList<Box>();
		for (File file : filelist) {
			LinmaluYamlConfiguration config = LinmaluYamlConfiguration.loadConfiguration(file);
			String name = ChatColor.translateAlternateColorCodes('&', config.getString("Name"));
			ArrayList<String> lore = new ArrayList<String>();
			for (String str : config.getStringList("Lore")) {
				lore.add(ChatColor.translateAlternateColorCodes('&', str));
			}
			List<String> item = config.getStringList("Rewards");
			ArrayList<ItemStack> rewards = new ArrayList<ItemStack>();
			ArrayList<Sound> sounds = new ArrayList<Sound>();
			ArrayList<Boolean> epicset = new ArrayList<Boolean>();
			ArrayList<Boolean> randomlist = new ArrayList<Boolean>();
			for (String material : item) {
				String random = material.split(" ")[1];
				if (random.startsWith("?")) {
					rewards.add(new ItemStack(Material.valueOf(material.split(" ")[0]),
							Integer.parseInt(material.split(" ")[1].replace("?", ""))));
					sounds.add(Sound.valueOf(material.split(" ")[2]));
					epicset.add(Boolean.parseBoolean(material.split(" ")[3]));
					randomlist.add(true);
				} else {
					rewards.add(new ItemStack(Material.valueOf(material.split(" ")[0]),
							Integer.parseInt(material.split(" ")[1])));
					sounds.add(Sound.valueOf(material.split(" ")[2]));
					epicset.add(Boolean.parseBoolean(material.split(" ")[3]));
					randomlist.add(false);
				}
			}
			Boolean daily = config.getBoolean("SetDailyReward");
			List<Double> rating = config.getDoubleList("ChanceRating");
			String epic = ChatColor.translateAlternateColorCodes('&', config.getString("EpicMessage"));
			list.add(new Box(file.getName().split(".yml")[0], name, lore, rating, rewards, sounds, epicset, randomlist,
					epic, daily));
		}

		return list;
	}

	private static void writeTest() {
		LinmaluYamlConfiguration config = LinmaluYamlConfiguration.loadConfiguration(test);
		config.set("Name", "TestBox");
		config.set("Lore", new String[] { "하", "기", "싫", "다" });
		config.set("Rewards", new String[] {
				Material.APPLE.toString() + " " + 5 + " " + Sound.BLOCK_ANVIL_DESTROY.toString() + " " + false,
				Material.GOLDEN_APPLE.toString() + " " + 5 + " " + Sound.BLOCK_ANVIL_DESTROY.toString() + " " + true });
		config.set("ChanceRating", new double[] { 50, 50 });
		config.set("EpicMessage", "<box>에서 <player>가 <item>을 뽑았다.");
		config.set("SetDailyReward", false);
		try {
			config.save(test);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
