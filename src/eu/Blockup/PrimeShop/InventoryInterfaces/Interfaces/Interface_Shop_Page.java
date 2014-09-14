package eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickHandler;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_close_Interface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_with_no_task;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.Shops.Page;
import eu.Blockup.PrimeShop.Shops.Shop;

public class Interface_Shop_Page extends InventoryInterface {

    private Shop shop;
    final private Interface_Shop_Page current_Menu;
    public int pagenumber;
    private final int maxPages;
//    @SuppressWarnings("deprecation")
    public Interface_Shop_Page(final List<InventoryInterface> link_Back_Stack,
            Player player, final Shop shop, final int pagenumber) {
        super(shop.shopname, 6, link_Back_Stack);
        this.shop = shop;
        this.setCloseable(false);
        this.current_Menu = this;
        this.pagenumber = pagenumber;
        this.maxPages = this.shop.number_of_pages;

        // Close Option
        this.addOption(8, 0, new Button_close_Interface());

        if (!PrimeShop.has_player_Permission_for_this_Command(player,
                "PrimeShop.Defaults.openShop." + shop.shopname)) {
            player.sendMessage(Message_Handler.resolve_to_message(87));
            boolean go_back_to_parent = true;
            if (parentMenu != null) {
                if (parentMenu instanceof Interface_Collection_of_Shops) {
                    if (((Interface_Collection_of_Shops) parentMenu).list_of_Shops
                            .size() == 1) {
                        go_back_to_parent = false;
                    }
                }
                if (go_back_to_parent) {
                    PrimeShop.close_InventoyInterface(player);
                    PrimeShop.open_InventoyInterface(player, parentMenu);
                    return;
                }
            }
            PrimeShop.close_InventoyInterface(player);
            return;
        }

        // /////////////////////////////////////////////////#
        this.setClickHandler(new ClickHandler() {
            @Override
            public boolean onClick(Player player, ItemStack cursor,
                    ItemStack current, ClickType type) {

                if ((type == ClickType.SHIFT_LEFT) && (current != null)) {

                    if (PrimeShop.has_player_Permission_for_this_Command(
                            player, "PrimeShop.admin.addItemsToShop."
                                    + shop.shopname)) {

                        if (!current.getType().equals(Material.AIR)) {
                            current.setAmount(1);
                            shop.add_ItemStack(current);
                            shop.refresh_pageCount();
                            PrimeShop.shopConfigHandler.write_shops_to_Harddisk();
                            reprint_items(player);
                            player.getInventory().remove(current);
                            player.sendMessage(ChatColor.GREEN + Message_Handler.resolve_to_message(36));
                            
                        }
                    }
                }
            return false;

            }
        });

        reprint_items(player);
    }

    public void reprint_items(Player player) {
        // Add Items to Menu
        final Page page = shop.get_Page(pagenumber);
        int amount_of_items = page.listOfItems.size();
        int itemsAddedItems = 0;

        for (int a = 0; a < this.getWidth(); a++) {
            for (int b = 0; b < this.getHeight(); b++) {
                this.addOption(a, b, new Button_with_no_task(Cofiguration_Handler.background_ItemStack, " "));
            }
        }

        this.addOption(4, 0,
                new Button_with_no_task(this.shop.displayIcon.getType(),
                        this.shop.shopname));

        for (int y = 2; y < 5; y++) {
            for (int x = 0; x < 9; x++) {

                this.removeOption(x, y);
                if (itemsAddedItems < amount_of_items) {

                    ItemStack currentItem;
                    try {
                        currentItem = page.listOfItems.get(itemsAddedItems);
                    } catch (Exception e) {
                        PrimeShop.plugin.getLogger().log(Level.SEVERE, "Internal Error finding Item in list of ShopPages");
                        e.printStackTrace();
                        return;
                    }

                    this.addOption(x, y, new Button(currentItem, Message_Handler.resolve_to_message(88),
                            Message_Handler.resolve_to_message(89)) {

                        @Override
                        public void onClick(InventoryInterface inventoryInterface, Player player, 
                                ItemStack cursor, ItemStack current, ClickType type) {
                            
                            int slot_position = -1;
                            for (int y = 2; y < 5; y++) {
                                for (int x = 0; x < 9; x++) {
                                    slot_position++;
                                    if (inventoryInterface.getOption(x, y).equals(this)) {
                                        PrimeShop.close_InventoyInterface(player);
                                        PrimeShop.open_InventoyInterface(
                                            player,
                                            new Interface_Buy_Sell_Item(
                                                inventoryInterface.branch_back_Stack,
                                                player, shop, page.listOfItems.get(slot_position), 1,
                                                true, false
                                            ));
                                        return;
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
                                new Interface_Shop_Page(inventoryInterface
                                        .get_brnach_back_list_of_parentMenu(),
                                        player, shop, pagenumber + 1));

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
                                new Interface_Shop_Page(inventoryInterface
                                        .get_brnach_back_list_of_parentMenu(),
                                        player, shop, maxPages));

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
                                new Interface_Shop_Page(inventoryInterface
                                        .get_brnach_back_list_of_parentMenu(),
                                        player, shop, pagenumber - 1));

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
                                                new Interface_Shop_Page(
                                                        inventoryInterface
                                                                .get_brnach_back_list_of_parentMenu(),
                                                        player, shop, 1));

                            }
                        });
            }
        }
        
        

        // Delete Shop from Collection
        if (PrimeShop.has_player_Permission_for_this_Command(player,
                "PrimeShop.admin.addShopsToNPCs." + shop.shopname)) {
            if (parentMenu != null) {
                if (parentMenu instanceof Interface_Collection_of_Shops) {
                    final Interface_Collection_of_Shops shop_collection = (Interface_Collection_of_Shops) parentMenu;

                    this.addOption(
                            7,
                            5,
                            new Button(
                                    PrimeShop.convertItemIdStringToItemstack(
                                            "160:6", 1), Message_Handler.resolve_to_message(72)) {

                                @Override
                                public void onClick(
                                        InventoryInterface inventoryInterface,
                                        Player player, ItemStack cursor,
                                        ItemStack current, ClickType type) {

                                    PrimeShop.close_InventoyInterface(player);
                                    PrimeShop
                                            .open_InventoyInterface(
                                                    player,
                                                    new Interface_delete_Shop_from_Collection(
                                                            inventoryInterface.branch_back_Stack,
                                                            player,
                                                            shop,
                                                            shop_collection.collectionID,
                                                            shop_collection.npc));
                                }

                            });
                }
            }
        }

        // delete entire Shop
        if (PrimeShop.has_player_Permission_for_this_Command(player,
                "PrimeShop.admin.deleteShops")) {
            this.addOption(
                    8,
                    5,
                    new Button(PrimeShop.convertItemIdStringToItemstack(
                            "160:14", 1), Message_Handler.resolve_to_message(78)) {

                        @Override
                        public void onClick(
                                InventoryInterface inventoryInterface,
                                Player player, ItemStack cursor,
                                ItemStack current, ClickType type) {
                            PrimeShop.close_InventoyInterface(player);
                            PrimeShop
                                    .open_InventoyInterface(
                                            player,
                                            new Interface_delete_Shop(
                                                    inventoryInterface.branch_back_Stack,
                                                    player, shop));

                        }
                    });
        }
        
        
        // Add Item Information Block
        
        if (PrimeShop.has_player_Permission_for_this_Command(
                player, "PrimeShop.admin.addItemsToShop."
                        + shop.shopname))
        this.addOption(0, 5, new Button_with_no_task(PrimeShop.convertItemIdStringToItemstack("160:13", 1), Message_Handler.resolve_to_message(141), Message_Handler.resolve_to_message(138), Message_Handler.resolve_to_message(139), Message_Handler.resolve_to_message(140)));

        
        
        
        // Go Back Option
        boolean goBack = true;
        if (parentMenu != null) {

            if (parentMenu instanceof Interface_Collection_of_Shops) {
                if (((Interface_Collection_of_Shops) parentMenu).list_of_Shops
                        .size() == 1) {
                    goBack = false;
                }

            }

            if (goBack) {
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

        }

        

        // Close Option
        this.addOption(8, 0, new Button_close_Interface());
        
        this.refresh(player);
    }
}