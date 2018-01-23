package net.alkarb.daily;

import java.io.IOException;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.alkarb.config.PlayerConfig;
import net.alkarb.untill.Constants;

public class TimeChecker implements Runnable{

	boolean timecheck = true;

	@Override
	public void run() {
		/*for(Player player:Bukkit.getOnlinePlayers()) {
			player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 50, 20);
		}*/
		Calendar time = Calendar.getInstance();
		int hour = time.get(Calendar.HOUR);
		if (hour == 0) {
			if (timecheck)
				return;
			try {
				PlayerConfig.resetFiles();
				DailyManager.loadList();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(Constants.Console
					+ "Daily reward timer is reset.if you are rejoin now, you can recive the daily reward");
			timecheck = true;
		} else if (hour != 0) {
			timecheck = false;
		}
	}

}
