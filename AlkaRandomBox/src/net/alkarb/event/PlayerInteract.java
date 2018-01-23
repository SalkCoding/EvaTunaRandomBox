package net.alkarb.event;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.alkarb.boxmanager.Box;
import net.alkarb.boxmanager.BoxManager;
import net.alkarb.untill.Constants;
import net.alkarb.untill.RandomBox;
import net.md_5.bungee.api.ChatColor;

public class PlayerInteract implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
			return;
		if(item.getType() == Material.AIR)
			return;
		ItemMeta meta = item.getItemMeta();
		Box box = BoxManager.getBox(meta.getDisplayName());
		if (box != null) {
			List<String> lore = meta.getLore();
			List<String> boxlore = box.getLore();
			for(int i = 0;i<lore.size();i++) {
				if(!(boxlore.get(i).equals(lore.get(i)))){
					return;
				}
			}
			if (player.getGameMode() != GameMode.SURVIVAL) {
				player.sendMessage(Constants.Format + ChatColor.YELLOW + "서바이벌 모드에서만 사용 가능합니다.");
				return;
			}
			RandomBox.RandomSlot((Player) player, box);
			if (item.getAmount() == 1) {
				item.setAmount(0);
			} else {
				item.setAmount(item.getAmount() - 1);
			}
		}
	}

}
