package net.alkaonline.alkarandombox.listener;

import net.alkaonline.alkarandombox.AlkaRandomBox;
import net.alkaonline.alkarandombox.boxmanager.Box;
import net.alkaonline.alkarandombox.untill.Constants;
import net.alkaonline.alkarandombox.untill.SlotTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

public class PlayerInteractListener implements Listener {

    private final AlkaRandomBox plugin;

    public PlayerInteractListener(AlkaRandomBox plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) return;

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName()) return;

        Box box = plugin.getBoxManager().getBoxByDisplayName(meta.getDisplayName());
        if (box != null) {
            List<String> lore = meta.getLore();
            List<String> boxLore = box.getLore();
            if (!lore.get(0).contains("박스")) {
                for (int i = 0; i < Math.min(lore.size(), boxLore.size()); i++) {
                    if (!(boxLore.get(i).equals(lore.get(i)))) {
                        return;
                    }
                }
            }
            if (player.getGameMode() != GameMode.SURVIVAL) {
                player.sendMessage(Constants.Error_Format + ChatColor.YELLOW + "서바이벌 모드에서만 사용 가능합니다.");
                return;
            }
            randomSlot(player, box);
            if (item.getAmount() == 1) {
                player.getInventory().setItemInMainHand(null);
            } else {
                item.setAmount(item.getAmount() - 1);
            }
        }
    }

    private void randomSlot(Player player, Box box) {
        Inventory inv = Bukkit.createInventory(null, 9 * 3, box.getDisplayName());
        for (int i = 0; i < 9 * 3; i++) {
            if (i == 13) continue;
            ItemStack item = new ItemStack(Material.LEGACY_STAINED_GLASS_PANE);
            item.setDurability((short) new Random().nextInt(16));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("");
            item.setItemMeta(meta);
            inv.setItem(i, item);
        }
        player.openInventory(inv);
        SlotTimer slot = new SlotTimer(player, inv, box);
        slot.setTask(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, slot, 10, 8));
    }

}
