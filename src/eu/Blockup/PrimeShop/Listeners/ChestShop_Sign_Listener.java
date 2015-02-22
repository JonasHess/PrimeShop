package eu.Blockup.PrimeShop.Listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.ChestShop.ChestShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.Interface_Shop_Page;
import eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.ChestShops.Interface_ChestShop_MainMenu;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.Shops.Shop;

public class ChestShop_Sign_Listener implements Listener {
    public ChestShop_Sign_Listener() {
        PrimeShop.plugin.getServer().getPluginManager()
                .registerEvents(this, PrimeShop.plugin);
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        String name = "ChestShop";
        // String name = Cofiguration_Handler.Sign_Shop_Headline;

        if (event.getPlayer() != null && event.getClickedBlock() != null
                && event.getClickedBlock().getState() instanceof Sign) {
            Sign sign = (Sign) event.getClickedBlock().getState();

            Player player = event.getPlayer();

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                // Use an Arrow...
                if (PrimeShop.has_player_Permission_for_this_Command(player,
                        "PrimeShop.admin.createSigns")) {
                    player.sendMessage(Message_Handler.resolve_to_message(128));
                    event.setCancelled(true);
                    return;
                }
            }

            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {

                String[] lines = sign.getLines();
                if (lines[0].toUpperCase().contains(name.toUpperCase())) {
                    // PrimeShop

                    // Permission Sign interact
                    if (!PrimeShop.has_player_Permission_for_this_Command(
                            player, "PrimeShop.Defaults.interactWithSigns")) {
                        player.sendMessage(Message_Handler
                                .resolve_to_message(130));
                        event.setCancelled(true);
                        return;
                    }

                    // Arrow Destory
                    if (PrimeShop.has_player_Permission_for_this_Command(
                            player, "PrimeShop.admin.createSigns")) {
                        if (player.getInventory().getItemInHand().getData()
                                .getItemTypeId() == 262) {
                            event.setCancelled(false);
                            return;
                        }
                    }

                    // Shopname
                    // String shopname = lines[1];
                    // boolean found = false;
                    // Shop shop = null;
                    //
                    // for (Map.Entry<String, Shop> entry :
                    // PrimeShop.hashMap_Shops.entrySet()) {
                    // if
                    // (shopname.toUpperCase().contains(entry.getKey().toUpperCase()))
                    // {
                    // found = true;
                    // shop = entry.getValue();
                    // }
                    // }
                    //
                    // if (! (found && shop != null)) {
                    // player.sendMessage("");
                    // player.sendMessage("");
                    // if
                    // (PrimeShop.has_player_Permission_for_this_Command(player,
                    // "PrimeShop.admin.createShops")) {
                    // // shop not found
                    // player.sendMessage(Message_Handler.resolve_to_message(98));
                    // player.sendMessage(Message_Handler.resolve_to_message(99));
                    // } else {
                    // // inform administrator
                    // player.sendMessage(Message_Handler.resolve_to_message(131));
                    // }
                    //
                    // event.setCancelled(true);
                    // return;
                    // } else {
                    // PrimeShop.open_InventoyInterface(player, new
                    // Interface_Shop_Page(
                    // new ArrayList<InventoryInterface>(), player, shop, 1
                    // )
                    // );
                    // event.setCancelled(true);
                    //
                    //
                    // return;
                    // }

                    ChestShop cS = new ChestShop("asdf79as7df987sdf", 99.99D);

                    for (int i = 1; i < 100; i++) {
                        @SuppressWarnings("deprecation")
                        ItemStack item = new ItemStack(i);
                        cS.add_Item_to_Ankauf(item, 100);
                        cS.add_Item_to_Verkaufen(item, 100);
                        cS.add_Item_to_Mailbox(item, 100);
                    }

                    PrimeShop.open_InventoyInterface(player,
                            new Interface_ChestShop_MainMenu(player, cS));
                    event.setCancelled(true);
                    return;

                } else {
                    return;

                }

            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSignChange(SignChangeEvent event) {
        // clicked on a sign and signs enabled?
        if (event.getPlayer() != null) {
            Player player = event.getPlayer();
            String[] lines = event.getLines();

            String name = "ChestShop";
            // String name = Cofiguration_Handler.Sign_Shop_Headline;
            if (lines[0].toUpperCase().contains(name.toUpperCase())) {
                if (PrimeShop.has_player_Permission_for_this_Command(player,
                        "PrimeShop.admin.createSigns")) {

                    String shopname = lines[1];
                    // boolean found = false;
                    // Shop shop = null;
                    //
                    // for (Map.Entry<String, Shop> entry :
                    // PrimeShop.hashMap_Shops
                    // .entrySet()) {
                    // if
                    // (entry.getKey().toUpperCase().contains(shopname.toUpperCase()))
                    // {
                    // found = true;
                    // shop = entry.getValue();
                    // }
                    // }
                    //
                    // if (! (found && shop != null)) {
                    // player.sendMessage(Message_Handler.resolve_to_message(98));
                    // player.sendMessage(Message_Handler.resolve_to_message(99));
                    // event.setCancelled(true);
                    // return;
                    // }

                    event.setLine(0, ChatColor.BLUE + "[" + name + "]");
                    event.setLine(1, ChatColor.GOLD + player.getDisplayName());

                } else { // No Permission
                    player.sendMessage(Message_Handler.resolve_to_message(127));
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    // @EventHandler(priority = EventPriority.HIGHEST)
    // public void onBlockBreak(BlockBreakEvent event) {
    // //This event doesn't get executed?!
    //
    // Block eventBlock = event.getBlock();
    //
    // // check surrounding blocks for a Sign
    // List<Block> blockList = new ArrayList<Block>();
    // blockList.add(eventBlock);
    // blockList.add(eventBlock.getRelative(BlockFace.UP, 1));
    // blockList.add(eventBlock.getRelative(BlockFace.EAST, 1));
    // blockList.add(eventBlock.getRelative(BlockFace.WEST, 1));
    // blockList.add(eventBlock.getRelative(BlockFace.SOUTH, 1));
    // blockList.add(eventBlock.getRelative(BlockFace.NORTH, 1));
    //
    // for (Block b : blockList) {
    // if (b != null && b.getState() instanceof Sign) {
    // Sign sign = (Sign) b.getState();
    // Player player = event.getPlayer();
    // String[] lines = sign.getLines();
    // if (lines[0].toUpperCase().contains(
    // Cofiguration_Handler.Sign_Shop_Headline.toUpperCase())) {
    // if (player != null) {
    // if ((eventBlock.equals(b) && player.getItemInHand()
    // .getType().name().equalsIgnoreCase("arrow"))) {
    // continue;
    // }
    // if (PrimeShop.has_player_Permission_for_this_Command(
    // player, "PrimeShop.admin.createSigns")
    // && !eventBlock.equals(b)) {
    // player.sendMessage(ChatColor.RED
    // + "You have to break the sign first!");
    // }
    // }
    // event.setCancelled(true);
    // return;
    // }
    // }
    // }
    // }
}
