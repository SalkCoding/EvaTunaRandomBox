package net.alkaonline.alkarandombox.untill;

import com.meowj.langutils.lang.LanguageHelper;
import net.alkaonline.alkarandombox.boxmanager.Box;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class SlotTimer implements Runnable {

    private final Object key = new Object();

    private final Box box;
    private final Player player;
    private final Inventory inv;
    private BukkitTask task;
    private final int reward;
    private int count = 0;
    private final int max;
    private int roulette = 0;
    private final ItemStack cloneItem;

    private final Random random = new Random();

    public void setTask(BukkitTask task) {
        this.task = task;
    }

    private static <E> E getWeightedRandom(Map<E, Double> weights, Random random) {
        E result = null;
        double bestValue = Double.MAX_VALUE;

        for (E element : weights.keySet()) {
            double value = -Math.log(random.nextDouble()) / weights.get(element);
            if (value < bestValue) {
                bestValue = value;
                result = element;
            }
        }
        return result;
    }

    public SlotTimer(Player player, Inventory inv, Box box) {
        this.player = player;
        this.inv = inv;
        this.box = box;
        List<Double> probability = box.getRating();
        Map<Integer, Double> list = new HashMap<>();
        for (int i = 0; i < probability.size(); i++) {
            list.put(i, probability.get(i));
        }
        Random random = new Random(Calendar.getInstance().getTimeInMillis());
        reward = getWeightedRandom(list, random);
        max = box.getRewards().size() * 2;
        cloneItem = box.getRewards().get(reward).clone();
    }

    @Override
    public void run() {
        synchronized (key) {
            for (int i = 0; i < 9 * 3; i++) {
                if (i == 13)
                    continue;
                ItemStack item = new ItemStack(Material.LEGACY_STAINED_GLASS_PANE);
                item.setDurability((short) random.nextInt(16));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(" ");
                item.setItemMeta(meta);
                inv.setItem(i, item);
            }
            if (count == max) {
                if (box.getRandom().get(reward)) {
                    Random random = new Random(Calendar.getInstance().getTimeInMillis());
                    cloneItem.setAmount(random.nextInt(cloneItem.getAmount()) + 1);
                }
                inv.setItem(13, cloneItem);
                ItemStack item = cloneItem;
                player.sendMessage(Constants.Info_Format + ChatColor.LIGHT_PURPLE + LanguageHelper.getItemName(item, player) + " " + item.getAmount()
                        + ChatColor.GREEN + "개가 나왔습니다!");
                if (box.getEpicSet().get(reward)) {
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        online.sendMessage(Constants.Info_Format
                                + box.getEpic().replaceAll("<item>", LanguageHelper.getItemName(item, online) + " " + item.getAmount())
                                .replace("<player>", player.getDisplayName()).replace("<box>", box.getDisplayName()));
                    }
                }
            } else if (count == max + 3) {
                player.playSound(player.getLocation(), box.getSounds().get(reward), 50, 20);
                if (box.getRandom().get(reward)) {
                    player.getInventory().addItem(cloneItem);
                } else {
                    player.getInventory().addItem(cloneItem);
                }
                player.closeInventory();
                task.cancel();
            }
            if (count <= max) {
                if (roulette < box.getRewards().size()) {
                    ItemStack item = box.getRewards().get(roulette).clone();
                    if (item.getAmount() > 64) {
                        ArrayList<String> lore = new ArrayList<>();
                        ItemMeta meta = item.getItemMeta();
                        if (box.getRandom().get(reward)) {
                            lore.add("?개");
                        } else {
                            lore.add(item.getAmount() + "개");
                        }
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        item.setAmount(64);
                    }
                    inv.setItem(13, item);
                } else {
                    roulette = 0;
                }
            }
            player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 50, 20);
            roulette++;
            count++;
        }
    }

}
