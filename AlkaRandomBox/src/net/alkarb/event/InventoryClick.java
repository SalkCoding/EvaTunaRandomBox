package net.alkarb.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import net.alkarb.boxmanager.BoxManager;

public class InventoryClick implements Listener {

	@EventHandler
	public void onTriger(InventoryClickEvent event) {// 발동중이면 취소
		int select = event.getRawSlot();
		if (select <= 0)
			return;
		String inv = event.getClickedInventory().getName();
		if (BoxManager.getBox(inv) != null) {
			event.setCancelled(true);
		}
	}

}
