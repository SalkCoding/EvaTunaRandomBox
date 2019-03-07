package net.alkaonline.alkarandombox.listener;

import net.alkaonline.alkarandombox.boxmanager.Box;
import net.alkaonline.alkarandombox.boxmanager.BoxManager;
import net.alkaonline.alkarandombox.daily.DailyManager;
import net.alkaonline.alkarandombox.untill.Constants;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final BoxManager boxManager;
    private final DailyManager dailyManager;

    public PlayerJoinListener(BoxManager boxManager, DailyManager dailyManager) {
        this.boxManager = boxManager;
        this.dailyManager = dailyManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        dailyManager.updateTime();
        Player player = event.getPlayer();
        if (dailyManager.hasReceived(player)) {
            return;
        }

        boolean gave = false;
        for (Box box : boxManager.getBoxes()) {
            if (box.isDaily()) {
                box.give(player, 1);
                gave = true;
            }
        }
        if (gave) {
            player.sendMessage(Constants.Info_Format + "당일 보상이 지급되었습니다.");
        }
        dailyManager.setReceived(player, true);
    }

}
