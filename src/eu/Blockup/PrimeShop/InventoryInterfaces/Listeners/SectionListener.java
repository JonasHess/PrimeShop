package eu.Blockup.PrimeShop.InventoryInterfaces.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;

public class SectionListener implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent event) { // NO_UCD (unused code)
        
        if (event.isCancelled()) return;
        
        HumanEntity he = event.getWhoClicked();
        if (he instanceof Player) {
            Player player = (Player) he;
            
            if (!PrimeShop.has_Player_InventoyInterface(player)) {
                return;
            }
            
            
            
            // Prevent Player from loosing Items
            if (event.getAction() == InventoryAction.SWAP_WITH_CURSOR ||
//                    event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY ||
                    event.getAction() == InventoryAction.NOTHING) {
                event.setCancelled(true);
                return;
            }
            
            
            
            ClickType type = null;
            boolean left = event.isLeftClick();
            boolean shift = event.isShiftClick();
            if (left && shift) {
                type = ClickType.SHIFT_LEFT;
            } else if (left && !shift) {
                type = ClickType.LEFT;
            } else if (!left && shift) {
                type = ClickType.SHIFT_RIGHT;
            } else if (!left && !shift) {
                type = ClickType.RIGHT;
            } else {
                type = ClickType.UNKNOWN;
            }
            
            ItemStack cursor = event.getCursor();
            ItemStack current = event.getCurrentItem();
            
            InventoryInterface inventoryInterface = PrimeShop.get_Players_InventoyInterface(player);
            
            int slot = event.getSlot();
            int slot_raw = event.getRawSlot();
            if (slot != slot_raw || slot >= inventoryInterface.getSlots()) {
                // Inv slots are allowed
                if (event.isShiftClick()) {
                    this.cancel(event);
                    // But shift clicks are not
                }
                if (inventoryInterface.hasClickHandler()) {
                    if (inventoryInterface.getClickHandler().onClick(player, cursor, current, type)) {
                        this.cancel(event);
                    }
                }
                return;
            }
            
            this.cancel(event);
            Button button = inventoryInterface.getOption(slot_raw);
            
            if (button == null) {
                return;
            }
            
            button.performClick(inventoryInterface, player, cursor, current, type);                    
        }
    }
    
    @EventHandler
    public void onClose(InventoryCloseEvent event) { // NO_UCD (unused code)
        HumanEntity he = event.getPlayer();
        if (he instanceof Player) {
            Player player = (Player) he;
            if (PrimeShop.has_Player_InventoyInterface(player)) {
                InventoryInterface inventoryInterface = PrimeShop.get_Players_InventoyInterface(player);
                PrimeShop.close_InventoyInterface(player);
                if (!inventoryInterface.isCloseable()) {
                    PrimeShop.open_InventoyInterface(player, inventoryInterface);
                }
            }
        }
    }
    
    private void cancel(InventoryClickEvent event) {
        event.setCancelled(true);
        ((Player) event.getWhoClicked()).updateInventory();
    }
}
