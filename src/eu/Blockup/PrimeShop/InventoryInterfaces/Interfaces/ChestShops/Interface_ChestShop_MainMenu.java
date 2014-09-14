package eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.ChestShops;


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
import eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.ChestShops.Interface_ChestShop_Page.Stage;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;

public class Interface_ChestShop_MainMenu extends InventoryInterface {


    private ChestShop chestShop;
    
//  @SuppressWarnings("deprecation")
    public Interface_ChestShop_MainMenu(Player player, final ChestShop chestShop) {
        super("ChestShop", 3, null);   // TODO
        
        this.setCloseable(false);
        this.chestShop = chestShop;
        

        // Close Option
        this.addOption(8, 0, new Button_close_Interface());

        
        reprint_items(player);

    }

    public void reprint_items(Player player) {
        // Background
        for (int a = 0; a < this.getWidth(); a++) {
            for (int b = 0; b < this.getHeight(); b++) {
                this.addOption(a, b, new Button_with_no_task(Cofiguration_Handler.background_ItemStack, " "));
            }
        }
        
        
        // Menu Items
        
        // Verkaufen
        this.addOption(2, 2, new Button(new ItemStack(Material.DIAMOND), "Buy Items from Shop", "") {
            
            @Override
            public void onClick(InventoryInterface inventoryInterface, Player player,
                    ItemStack cursor, ItemStack current, ClickType type) {
                    PrimeShop.close_InventoyInterface(player);
                    PrimeShop.open_InventoyInterface(player, new Interface_ChestShop_Page(branch_back_Stack, player, chestShop, Stage.Verkaufen, 1));
            }
        });
        
        
        // Ankaufen
        this.addOption(3, 2, new Button(new ItemStack(Material.GOLD_INGOT), "Sell Items from Shop", "") {
            
            @Override
            public void onClick(InventoryInterface inventoryInterface, Player player,
                    ItemStack cursor, ItemStack current, ClickType type) {
                PrimeShop.close_InventoyInterface(player);
                PrimeShop.open_InventoyInterface(player, new Interface_ChestShop_Page(branch_back_Stack, player, chestShop, Stage.Ankaufen, 1));
            }
        });
        
        
        // Mailbox
        this.addOption(4, 2, new Button(new ItemStack(Material.BOOKSHELF), "Mailbox", "") {
            
            @Override
            public void onClick(InventoryInterface inventoryInterface, Player player,
                    ItemStack cursor, ItemStack current, ClickType type) {
                PrimeShop.close_InventoyInterface(player);
                PrimeShop.open_InventoyInterface(player, new Interface_ChestShop_Page(branch_back_Stack, player, chestShop, Stage.Mailbox, 1));
            }
        });
        
        
        // Money Balance
        this.addOption(5, 2, new Button(new ItemStack(Material.GOLD_NUGGET), "Shop Balance", "") {
            
            @Override
            public void onClick(InventoryInterface inventoryInterface, Player player,
                    ItemStack cursor, ItemStack current, ClickType type) {
                PrimeShop.close_InventoyInterface(player);
                PrimeShop.open_InventoyInterface(player, new Interface_ChestShop_Balance(branch_back_Stack, player, chestShop));
            }
        });
        
        
        
    
        // Display Icon
        this.addOption(4, 0,new Button_with_no_task(new ItemStack(Material.SKULL_ITEM),"Shop of " + "PLAYER"));  // TODO
    
    
         // Close Option
         this.addOption(8, 0, new Button_close_Interface());
            
          // Go Back Option
          
        if (parentMenu != null) {
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

        

        
        this.refresh(player);
    }
}