package net.alkaonline.alkarandombox.daily;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class DailyManager {

    private int day;
    private final Set<UUID> playersReceivedDailyRewards;

    private DailyManager(int day, Set<UUID> playersReceivedDailyRewards) {
        this.day = day;
        this.playersReceivedDailyRewards = playersReceivedDailyRewards;
    }

    public static DailyManager loadFromConfig(Configuration config) {
        DailyManager result = new DailyManager(config.getInt("day", 0), config.getStringList("playersReceivedDailyRewards").stream().map(UUID::fromString).collect(Collectors.toSet()));
        result.updateTime();
        return result;
    }

    public void saveToConfig(Configuration config) {
        config.set("day", day);
        config.set("playersReceivedDailyRewards", playersReceivedDailyRewards.stream().map(UUID::toString).collect(Collectors.toList()));
    }

    public boolean hasReceived(Player player) {
        return playersReceivedDailyRewards.contains(player.getUniqueId());
    }

    public void setReceived(Player player, boolean received) {
        if (received) {
            playersReceivedDailyRewards.add(player.getUniqueId());
        } else {
            playersReceivedDailyRewards.remove(player.getUniqueId());
        }
    }

    public void updateTime() {
        Calendar calendar = Calendar.getInstance();
        if (day != calendar.get(Calendar.DAY_OF_YEAR)) {
            playersReceivedDailyRewards.clear();
            day = calendar.get(Calendar.DAY_OF_YEAR);
        }
    }

}
