package eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.ChestShops;

import java.util.List;

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
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;

public class Interface_ChestShop_Balance extends InventoryInterface {

    private final ChestShop chestShop;
    
    public Interface_ChestShop_Balance(final List<InventoryInterface> link_Back_Stack, Player player, final ChestShop chestShop) {
        super(Message_Handler.resolve_to_message(64), 4, link_Back_Stack); // TODO Correct ItemName
        
        this.chestShop = chestShop;
        this.setCloseable(false);

        reprint_items(player);
        
    }
    
    @SuppressWarnings("deprecation")
    private void print_buttons () {
        // Changing Rate

        // +1000
        this.addOption(1, 2, new Button_ChestShop_Add_and_Withdraw_Money(chestShop, 1000.0, Material.getMaterial(388), "+" + PrimeShop.economy.format(1000), ""));

        // -1000
        this.addOption(1, 3, new Button_ChestShop_Add_and_Withdraw_Money(chestShop, -1000.0, Material.getMaterial(388), PrimeShop.economy.format(-1000), ""));

        // +100
        this.addOption(2, 2, new Button_ChestShop_Add_and_Withdraw_Money(chestShop, 100.0, Material.getMaterial(264), "+" + PrimeShop.economy.format(100), ""));

        // -100
        this.addOption(2, 3, new Button_ChestShop_Add_and_Withdraw_Money(chestShop, -100.0, Material.getMaterial(264), PrimeShop.economy.format(-100), ""));

        // +10
        this.addOption(3, 2, new Button_ChestShop_Add_and_Withdraw_Money(chestShop, 10.0, Material.getMaterial(266), "+" + PrimeShop.economy.format(10), ""));

        // -10
        this.addOption(3, 3, new Button_ChestShop_Add_and_Withdraw_Money(chestShop, -10.0, Material.getMaterial(266), PrimeShop.economy.format(-10), ""));

        // +1
        this.addOption(4, 2, new Button_ChestShop_Add_and_Withdraw_Money(chestShop, 1.0, Material.getMaterial(265), "+" + PrimeShop.economy.format(1), ""));
        // -1
        this.addOption(4, 3, new Button_ChestShop_Add_and_Withdraw_Money(chestShop, -1.0, Material.getMaterial(265), PrimeShop.economy.format(-1), ""));

        // +0,1
        this.addOption(5, 2, new Button_ChestShop_Add_and_Withdraw_Money(chestShop, 0.1, Material.getMaterial(331), "+" + PrimeShop.economy.format(0.1), ""));

        // -0,1
        this.addOption(5, 3, new Button_ChestShop_Add_and_Withdraw_Money(chestShop, -0.1, Material.getMaterial(331), PrimeShop.economy.format(-0.1), ""));

        // +0,01
        this.addOption(6, 2, new Button_ChestShop_Add_and_Withdraw_Money(chestShop, 0.01, Material.getMaterial(263), "+" + PrimeShop.economy.format(0.01), ""));

        // -0,01
        this.addOption(6, 3, new Button_ChestShop_Add_and_Withdraw_Money(chestShop, -0.01, Material.getMaterial(263), PrimeShop.economy.format(-0.01), ""));

    }
    
        @SuppressWarnings("deprecation")
        public void reprint_items(Player player) {
            // Background
            for (int a = 0; a < this.getWidth(); a++) {
                for (int b = 0; b < this.getHeight(); b++) {
                    this.addOption(a, b, new Button_with_no_task(Cofiguration_Handler.background_ItemStack, " "));
                }
            }
            
            // Display Item
            this.addOption(4, 0, new Button_ChestShop_Balance_Info(chestShop, Material.getMaterial(421)));
            
            
            
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

            

            print_buttons();
            
            this.refresh(player);
        }

}
