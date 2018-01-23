package net.alkarb.daily;

import java.util.HashMap;

import org.bukkit.entity.Player;
import net.alkarb.config.PlayerConfig;

public class DailyManager {

	public static HashMap<String, Boolean> list = new HashMap<String, Boolean>();
	
	public static Boolean getPlayer(Player player) {
		return list.get(player.getName());
	}

	public static void setPlayer(Player player, Boolean set) {
		list.remove(player.getName());
		list.put(player.getName(), set);
		PlayerConfig.setPlayer(player, set);
	}

	public static void loadList() {
		list = PlayerConfig.loadList();
	}

}