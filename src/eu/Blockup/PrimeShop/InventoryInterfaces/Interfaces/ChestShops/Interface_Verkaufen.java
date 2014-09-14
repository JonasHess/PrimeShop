package eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.ChestShops;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.ChestShop.ChestShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_close_Interface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_with_no_task;
import eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.Interface_Collection_of_Shops;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;

public class Interface_Verkaufen extends InventoryInterface {
    private ChestShop chestShop;
    private int pagenumber;
    private int maxPages;
    
    public Interface_Verkaufen(final List<InventoryInterface> link_Back_Stack,Player player, final ChestShop chestShop, int pagenumber) {
        super("VERKAUFEN TITLE", 6, link_Back_Stack);   // TODO
        
        this.setCloseable(false);
        this.chestShop = chestShop;
        
        
        this.pagenumber = pagenumber;
        this.maxPages = chestShop.get_PageCount_of_Verkaufen();
        
        
        // Close Option
        this.addOption(8, 0, new Button_close_Interface());
        reprint_items(player);
    }

    private void reprint_items(Player player) {
        // Add Items to Menu
        int amount_of_items = chestShop.get_Page_X_of_Verkaufen(pagenumber).listOfItems.size();
        int itemsAddedItems = 0;

        for (int a = 0; a < this.getWidth(); a++) {
            for (int b = 0; b < this.getHeight(); b++) {
                this.addOption(a, b, new Button_with_no_task(Cofiguration_Handler.background_ItemStack, " "));
            }
        }

        for (int y = 2; y < 5; y++) {
            for (int x = 0; x < 9; x++) {

                this.removeOption(x, y);
                if (itemsAddedItems < amount_of_items) {

                    ItemStack currentItem;
                    try {
                        currentItem = chestShop.get_Page_X_of_Verkaufen(pagenumber).listOfItems.get(itemsAddedItems).getItemStack();
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

                            player.sendMessage("You have Clicked");
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
                                new Interface_Verkaufen(inventoryInterface
                                        .get_brnach_back_list_of_parentMenu(),
                                        player, chestShop, pagenumber + 1));

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
                                new Interface_Verkaufen(inventoryInterface
                                        .get_brnach_back_list_of_parentMenu(),
                                        player, chestShop, maxPages));

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
                                new Interface_Verkaufen(inventoryInterface
                                        .get_brnach_back_list_of_parentMenu(),
                                        player, chestShop, pagenumber - 1));

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
                                                new Interface_Verkaufen(
                                                        inventoryInterface
                                                                .get_brnach_back_list_of_parentMenu(),
                                                        player, chestShop, 1));

                            }
                        });
            }
        }
        
        // Go Back Option
        boolean goBack = true;
        if (parentMenu != null) {

            if (parentMenu instanceof Interface_Collection_of_Shops) {
                if (((Interface_Collection_of_Shops) parentMenu).getList_of_Shops()
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