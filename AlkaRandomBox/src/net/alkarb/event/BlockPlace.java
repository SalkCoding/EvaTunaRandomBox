package net.alkarb.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.alkarb.boxmanager.BoxManager;
import net.alkarb.untill.Constants;
import net.md_5.bungee.api.ChatColor;

public class BlockPlace implements Listener {

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if(item.getType() == Material.AIR || item == null)
			return;
		ItemMeta meta = item.getItemMeta();
		if (BoxManager.getBox(meta.getDisplayName()) != null) {
			event.setCancelled(true);
			player.sendMessage(Constants.Format + ChatColor.LIGHT_PURPLE + meta.getDisplayName() + ChatColor.RED
					+ "은 설치할 수 없습니다.");
		}
	}

}
