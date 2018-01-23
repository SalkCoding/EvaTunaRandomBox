package net.alkarb.untill;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import net.alkarb.boxmanager.Box;
import net.alkarb.main.Main;
import net.md_5.bungee.api.ChatColor;

public class RandomBox {

	public static void giveBox(Player player, Box box,int amount) {
		ItemStack item = new ItemStack(Material.CHEST);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(box.getDisplayName());
		meta.setLore(box.getLore());
		item.setItemMeta(meta);
		item.setAmount(amount);
		player.getInventory().addItem(item);
	}

	public static void RandomSlot(Player player, Box box) {
		Inventory inv = Bukkit.createInventory(null, 9 * 3, box.getDisplayName());
		for (int i = 0; i < 9 * 3; i++) {
			if (i == 13)
				continue;
			ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE);
			item.setDurability((short) (Math.random() * 15));
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("");
			item.setItemMeta(meta);
			inv.setItem(i, item);
		}
		player.openInventory(inv);
		SlotTimer slot = new SlotTimer(player, inv, box);
		slot.setTask(Bukkit.getScheduler().runTaskTimer(Main.getInstance(), slot, 10, 8));
	}

}

class SlotTimer implements Runnable {

	Box box;
	Player player;
	Inventory inv;
	BukkitTask task;
	int reward = -1;
	int count = 0;
	int max = 0;
	int roulette = 0;
	ItemStack cloneitem;
	public void setTask(BukkitTask task) {
		this.task = task;
	}

	public static <E> E getWeightedRandom(Map<E, Double> weights, Random random) {
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
		HashMap<Integer, Double> list = new HashMap<Integer, Double>();
		for (int i = 0; i < probability.size(); i++) {
			list.put(i, probability.get(i));
		}
		Random random = new Random(Calendar.getInstance().getTimeInMillis());
		reward = getWeightedRandom(list, random);
		max = box.getRewards().size() * 2;
		cloneitem = box.getRewards().get(reward).clone();
	}

	@Override
	public void run() {

		for (int i = 0; i < 9 * 3; i++) {
			if (i == 13)
				continue;
			ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE);
			item.setDurability((short) (Math.random() * 15));
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(" ");
			item.setItemMeta(meta);
			inv.setItem(i, item);
		}
		if (count == max) {
			if(box.getRandom().get(reward)) {
				Random random = new Random(Calendar.getInstance().getTimeInMillis());
				cloneitem.setAmount(random.nextInt(cloneitem.getAmount()) + 1);
			}
			inv.setItem(13, cloneitem);
			ItemStack item = cloneitem;
			player.sendMessage(Constants.Format + ChatColor.LIGHT_PURPLE + item.getType() + " " + item.getAmount()
					+ ChatColor.GREEN + "개가 나왔습니다!");
			if (box.getEpicSet().get(reward) == true) {
				for (Player online : Bukkit.getOnlinePlayers()) {
					online.sendMessage(Constants.Format
							+ box.getEpic().replaceAll("<item>", item.getType().toString() + " " + item.getAmount())
									.replace("<player>", player.getDisplayName()).replace("<box>", box.getDisplayName()));
				}
			}
		} else if (count == max + 3) {
			player.playSound(player.getLocation(), box.getSounds().get(reward), 50, 20);
			if (box.getRandom().get(reward)) {
				player.getInventory().addItem(cloneitem);
			} else {
				player.getInventory().addItem(cloneitem);
			}
			player.closeInventory();
			task.cancel();
		}
		if (count <= max) {
			if (roulette < box.getRewards().size()) {
				ItemStack item = box.getRewards().get(roulette).clone();
				if (item.getAmount() > 64) {
					ArrayList<String> lore = new ArrayList<String>();
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
