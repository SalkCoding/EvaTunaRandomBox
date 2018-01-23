package net.alkarb.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.alkarb.boxmanager.Box;
import net.alkarb.boxmanager.BoxManager;
import net.alkarb.daily.DailyManager;
import net.alkarb.untill.Constants;
import net.alkarb.untill.RandomBox;
import net.md_5.bungee.api.ChatColor;

public class PlayerJoin implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (DailyManager.getPlayer(player) == null || DailyManager.getPlayer(player) == false) {
			for (Box box : BoxManager.getList()) {
				if (box.getDailyReward()) {
					RandomBox.giveBox(player, box, 1);
					DailyManager.setPlayer(player, true);
					player.sendMessage(Constants.Format + ChatColor.GREEN + "당일 보상이 지급되었습니다.");
				}
			}
			DailyManager.setPlayer(player, true);
		}
	}

}
