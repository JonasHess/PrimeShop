package eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.ChestShops;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.ChestShop.ChestShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickHandler;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_close_Interface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_with_no_task;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;

public class Interface_ChestShop_Page extends InventoryInterface {


    private ChestShop chestShop;
    private int pagenumber;
    private int maxPages;
    private final boolean kaufen;
    private final Stage stage;
    public enum Stage {
        Verkaufen,
        Ankaufen,
        Mailbox
    } 
    
//  @SuppressWarnings("deprecation")
    public Interface_ChestShop_Page(final List<InventoryInterface> link_Back_Stack,Player player, final ChestShop chestShop, final Stage stage, int pagenumber) {
        super("VERKAUFEN TITLE", 6, link_Back_Stack);   // TODO
        
        this.setCloseable(false);
        this.chestShop = chestShop;
        
        this.stage = stage;
        if (stage == Stage.Verkaufen) {
            kaufen = true;
        } else {
            kaufen = false;
        }
        
        
        this.pagenumber = pagenumber;
        

        
        
        // Close Option
        this.addOption(8, 0, new Button_close_Interface());
        
    
    
        this.setClickHandler(new ClickHandler() {
            @Override
            public boolean onClick(Player player, ItemStack cursor,
                    ItemStack current, ClickType type) {

                if (stage != Stage.Verkaufen)return false; 
                if (player == null) return false;
                if (current == null) return false;
                if (current.getType().equals(Material.AIR)) return false;
                if (!chestShop.is_this_player_the_owner_of_the_ChestShop(player)) return false;
                
                
                if (type == ClickType.SHIFT_LEFT) {

                  // TODO Permission needed? 
                    int amount = current.getAmount();
                    ItemStack item = current.clone();
                    item.setAmount(1);
                    
                        
                    // ADD item to Shop
                    chestShop.add_Item_to_Verkaufen(item, amount);
                    
                    // Remove item from Inventory
                    player.getInventory().remove(current);
                    
                    // Send MEssage to Player
                    player.sendMessage(ChatColor.GREEN + Message_Handler.resolve_to_message(36));

                    
                    reprint_items(player);
                            
                }
            return false;

            }
        });
        
        reprint_items(player);
        



    }

    public void reprint_items(Player player) {
        
        
        
        // Add Items to Menu
        int amount_of_items = chestShop.get_Page_X_of_Verkaufen(pagenumber).listOfItems.size();
        int itemsAddedItems = 0;

        for (int a = 0; a < this.getWidth(); a++) {
            for (int b = 0; b < this.getHeight(); b++) {
                this.addOption(a, b, new Button_with_no_task(Cofiguration_Handler.background_ItemStack, " "));
            }
        }

//      // DISPLAY//
//      this.addOption(4, 0,
//              new Button_with_no_task(this.shop.displayIcon.getType(),
//                      this.shop.shopname));
        
       chestShop.remove_empty_supply_slots();

        for (int y = 2; y < 5; y++) {
            for (int x = 0; x < 9; x++) {

                this.removeOption(x, y);
                if (itemsAddedItems < amount_of_items) {

                    ItemStack currentItem;
                    try {
                        currentItem = null;
                        if (stage == Stage.Mailbox) currentItem = chestShop.get_Page_X_of_Mailbox(pagenumber).listOfItems.get(itemsAddedItems).getItemStack();
                        if (stage == Stage.Verkaufen) currentItem = chestShop.get_Page_X_of_Verkaufen(pagenumber).listOfItems.get(itemsAddedItems).getItemStack();
                        if (stage == Stage.Ankaufen) currentItem = chestShop.get_Page_X_of_Ankaufen(pagenumber).listOfItems.get(itemsAddedItems).getItemStack();
                        
                    } catch (Exception e) {
                        PrimeShop.plugin.getLogger().log(Level.SEVERE, "Internal Error finding Item in list of SellingPage");
                        e.printStackTrace();
                        return;
                    }

                    this.addOption(x, y, new Button(currentItem, Message_Handler.resolve_to_message(88),
                            Message_Handler.resolve_to_message(89)) {

                        @Override
                        public void onClick(
                                InventoryInterface inventoryInterface,
                                Player player, ItemStack cursor,
                                ItemStack current, ClickType type) {

                            player.sendMessage("You have Clicked ");  //TODO remove
                            
                            int slot_position = -1;
                            for (int y = 2; y < 5; y++) {
                                for (int x = 0; x < 9; x++) {
                                    slot_position++;
                                    if (inventoryInterface.getOption(x, y).equals(this)) {
                                        
                                        
                                        if (stage == Stage.Verkaufen || stage == Stage.Ankaufen) {
                                            PrimeShop.close_InventoyInterface(player);
                                            PrimeShop.open_InventoyInterface(
                                                    player,
                                                    new Interface_ChestShop_Sell_Buy(
                                                            inventoryInterface.branch_back_Stack,
                                                            player, chestShop, chestShop.get_Page_X_of_Verkaufen(pagenumber).listOfItems.get(slot_position), kaufen, 1
                                                            ));
                                            return;
                                        }
                                        
                                        if (stage == Stage.Mailbox) {
                                            ChestShop.give_Items_back_to_Player(player, chestShop.get_Page_X_of_Mailbox(pagenumber).listOfItems.get(slot_position));
                                            reprint_items(player);  // TODO WARNING  This can result in Player seeing empty pages
                                            return;
                                        }
                                    }
                                }
                            }
                            
                        }
                    });
                    itemsAddedItems++;

                }
            }
        }
        
        if (maxPages > 1) {
            int y_Row_Page_Iterator = this.getHeight() - 1;
            
            
            // Compass
            this.addOption(4, y_Row_Page_Iterator, new Button_with_no_task(
                    Material.COMPASS, String.valueOf(pagenumber)));
            
            
            // Next Page

            // for (int counteri = 2; counteri <= 6; counteri++) {
            // this.removeOption(counteri, y_Row_Page_Iterator);
            // }

            if ((maxPages > pagenumber)) {
                this.addOption(5, y_Row_Page_Iterator, new Button(
                        Material.PAPER, (short) 0, (pagenumber + 1) % 64,
                        Message_Handler.resolve_to_message(90), "", "") {

                    @Override
                    public void onClick(InventoryInterface inventoryInterface,
                            Player player, ItemStack cursor, ItemStack current,
                            ClickType type) {
                        PrimeShop.close_InventoyInterface(player);
                        PrimeShop.open_InventoyInterface(
                                player,
                                new Interface_ChestShop_Page(inventoryInterface
                                        .get_brnach_back_list_of_parentMenu(),
                                        player, chestShop, stage, pagenumber + 1));

                    }
                });

            }

            // Last Page
            if (maxPages > pagenumber + 1) {
                this.addOption(6, y_Row_Page_Iterator, new Button(
                        Material.PAPER, (short) 0, maxPages % 64,
                        Message_Handler.resolve_to_message(91), "", "") {

                    @Override
                    public void onClick(InventoryInterface inventoryInterface,
                            Player player, ItemStack cursor, ItemStack current,
                            ClickType type) {
                        PrimeShop.close_InventoyInterface(player);
                        PrimeShop.open_InventoyInterface(
                                player,
                                new Interface_ChestShop_Page(inventoryInterface
                                        .get_brnach_back_list_of_parentMenu(),
                                        player, chestShop, stage, maxPages));

                    }
                });
            }

            // previous Page

            if (pagenumber > 1) {
                this.addOption(3, y_Row_Page_Iterator, new Button(
                        Material.PAPER, (short) 0, (pagenumber - 1) % 64,
                        Message_Handler.resolve_to_message(92), "", "") {

                    @Override
                    public void onClick(InventoryInterface inventoryInterface,
                            Player player, ItemStack cursor, ItemStack current,
                            ClickType type) {
                        PrimeShop.close_InventoyInterface(player);
                        PrimeShop.open_InventoyInterface(
                                player,
                                new Interface_ChestShop_Page(inventoryInterface
                                        .get_brnach_back_list_of_parentMenu(),
                                        player, chestShop, stage, pagenumber - 1));

                    }
                });

            }
            // First Page
            if (pagenumber > 2) {
                this.addOption(2, y_Row_Page_Iterator,
                        new Button(Material.PAPER, (short) 0, 1,
                                Message_Handler.resolve_to_message(93), "", "") {

                            @Override
                            public void onClick(
                                    InventoryInterface inventoryInterface,
                                    Player player, ItemStack cursor,
                                    ItemStack current, ClickType type) {
                                PrimeShop.close_InventoyInterface(player);
                                PrimeShop
                                        .open_InventoyInterface(
                                                player,
                                                new Interface_ChestShop_Page(
                                                        inventoryInterface
                                                                .get_brnach_back_list_of_parentMenu(),
                                                        player, chestShop, stage, 1));

                            }
                        });
            }
        }
        
        // Go Back Option
//        boolean goBack = true;
        if (parentMenu != null) {

//            if (parentMenu instanceof Interface_Collection_of_Shops) {
//                if (((Interface_Collection_of_Shops) parentMenu).list_of_Shops
//                        .size() == 1) {
//                    goBack = false;
//                }
//
//            }

                this.addOption(0, 0, new Button(Cofiguration_Handler.backToCollectionButton_ItemStack, Message_Handler.resolve_to_message(61), Message_Handler.resolve_to_message(62)) {

                    @Override
                    public void onClick(InventoryInterface inventoryInterface,
                            Player player, ItemStack cursor, ItemStack current,
                            ClickType type) {
                        inventoryInterface.return_to_predecessor(
                                position_in_Stack - 1, player);
                    }
                });

        }

        

        // Close Option
        this.addOption(8, 0, new Button_close_Interface());
        
        this.refresh(player);
    }
    
    private int get_max_PageCount () {
        if (stage == Stage.Mailbox)   return chestShop.get_PageCount_of_Mailbox();
        if (stage == Stage.Verkaufen) return chestShop.get_PageCount_of_Verkaufen(); 
        if (stage == Stage.Ankaufen)  return chestShop.get_PageCount_of_Ankaufen();
        return 64;
    }
    private void refresh_Page_After_deletion () {
        chestShop.remove_empty_supply_slots();   // This might be opened twice
        
    }
    
}