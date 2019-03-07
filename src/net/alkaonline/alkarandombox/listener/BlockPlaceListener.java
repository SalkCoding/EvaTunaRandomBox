package net.alkaonline.alkarandombox.listener;

import net.alkaonline.alkarandombox.boxmanager.BoxManager;
import net.alkaonline.alkarandombox.untill.Constants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockPlaceListener implements Listener {

    private final BoxManager boxManager;

    public BlockPlaceListener(BoxManager boxManager) {
        this.boxManager = boxManager;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName()) {
            return;
        }

        if (boxManager.getBoxByDisplayName(meta.getDisplayName()) != null) {
            event.setCancelled(true);
            player.sendMessage(Constants.Error_Format + ChatColor.LIGHT_PURPLE + meta.getDisplayName() + ChatColor.RED
                    + "은/는 설치할 수 없습니다.");
        }
    }

}
