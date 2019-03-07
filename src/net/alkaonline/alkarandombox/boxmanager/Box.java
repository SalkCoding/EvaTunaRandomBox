package net.alkaonline.alkarandombox.boxmanager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Box {

    private final String name;
    private final String displayName;
    private final List<String> lore;
    private final List<Double> rating;
    private final List<ItemStack> rewards;
    private final List<Sound> sounds;
    private final List<Boolean> epicSet;
    private final List<Boolean> random;
    private final String epic;
    private final boolean daily;


    public Box(String name, String displayName, List<String> lore, List<Double> rating, List<ItemStack> rewards,
               List<Sound> sounds, List<Boolean> epicSet, List<Boolean> random, String epic, boolean daily) {
        this.name = name;
        this.displayName = displayName;
        this.lore = lore;
        this.rating = rating;
        this.rewards = rewards;
        this.sounds = sounds;
        this.epicSet = epicSet;
        this.epic = epic;
        this.daily = daily;
        this.random = random;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    public List<Double> getRating() {
        return rating;
    }

    public List<ItemStack> getRewards() {
        return rewards;
    }

    public List<Sound> getSounds() {
        return sounds;
    }

    public List<Boolean> getEpicSet() {
        return epicSet;
    }

    public List<Boolean> getRandom() {
        return random;
    }

    public String getEpic() {
        return epic;
    }

    public boolean isDaily() {
        return daily;
    }

    public ItemStack createItemStack(int amount) {
        ItemStack item = new ItemStack(Material.CHEST);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getDisplayName());
        meta.setLore(getLore());
        item.setItemMeta(meta);
        item.setAmount(amount);
        return item;
    }

    public void give(Player player, int amount) {
        player.getInventory().addItem(createItemStack(amount));
    }

    public static Box fromConfig(Path file) throws IOException {
        Configuration config;
        try (BufferedReader br = Files.newBufferedReader(file)) {
            config = YamlConfiguration.loadConfiguration(br);
        }

        return Box.fromConfig(file.getFileName().toString().substring(0, file.getFileName().toString().length() - 4), config);
    }

    public static Box fromConfig(String name, Configuration config) {
        String displayName = ChatColor.translateAlternateColorCodes('&', config.getString("Name"));
        List<String> lore = new ArrayList<>();
        for (String str : config.getStringList("Lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', str));
        }
        List<String> item = config.getStringList("Rewards");
        List<ItemStack> rewards = new ArrayList<>();
        List<Sound> sounds = new ArrayList<>();
        List<Boolean> epicSet = new ArrayList<>();
        List<Boolean> randomList = new ArrayList<>();
        for (String material : item) {
            String[] split = material.split(" ");
            String random = split[1];
            if (random.startsWith("?")) {
                rewards.add(new ItemStack(Material.valueOf(split[0]),
                        Integer.parseInt(split[1].replace("?", ""))));
                sounds.add(Sound.valueOf(split[2]));
                epicSet.add(Boolean.parseBoolean(split[3]));
                randomList.add(true);
            } else {
                rewards.add(new ItemStack(Material.valueOf(split[0]),
                        Integer.parseInt(split[1])));
                sounds.add(Sound.valueOf(split[2]));
                epicSet.add(Boolean.parseBoolean(split[3]));
                randomList.add(false);
            }
        }
        boolean daily = config.getBoolean("SetDailyReward");
        List<Double> rating = config.getDoubleList("ChanceRating");
        String epic = ChatColor.translateAlternateColorCodes('&', config.getString("EpicMessage"));

        return new Box(name, displayName, lore, rating, rewards, sounds, epicSet, randomList, epic, daily);
    }
}
