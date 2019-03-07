package net.alkaonline.alkarandombox.listener;

import net.alkaonline.alkarandombox.boxmanager.BoxManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    private final BoxManager boxManager;

    public InventoryClickListener(BoxManager boxManager) {
        this.boxManager = boxManager;
    }

    @EventHandler
    public void onTrigger(InventoryClickEvent event) {// 발동중이면 취소
        int select = event.getRawSlot();
        if (select <= 0)
            return;
        String inv = event.getInventory().getName();
        if (boxManager.getBoxByDisplayName(inv) != null) {
            event.setCancelled(true);
        }
    }

}
