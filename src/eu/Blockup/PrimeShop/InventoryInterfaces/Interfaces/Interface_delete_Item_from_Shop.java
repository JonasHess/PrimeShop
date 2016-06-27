package eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces;

import java.util.List;

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
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_delete_Item;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.Shops.Shop;

class Interface_delete_Item_from_Shop extends InventoryInterface {
    public Interface_delete_Item_from_Shop(
            final List<InventoryInterface> link_Back_Stack, Player player,
            final ItemStack itemstack_to_be_added, final Shop shop) {
        super(Message_Handler.resolve_to_message(67), 4, link_Back_Stack); // TODO
                                                                           // Correct
                                                                           // ItemName

        this.setCloseable(false);

        this.setClickHandler(new ClickHandler() {
            @Override
            public boolean onClick(Player player, ItemStack cursor,
                    ItemStack current, ClickType type) {
                return false;
            }
        });

        // Empty Options

        for (int a = 0; a < this.getWidth(); a++) {
            for (int b = 0; b < this.getHeight(); b++) {
                this.addOption(a, b, new Button_with_no_task(
                        Cofiguration_Handler.background_ItemStack, " "));
            }
        }

        // Icon

        this.addOption(
                4,
                0,
                new Button_with_no_task(itemstack_to_be_added, Message_Handler
                        .resolve_to_message(54), Message_Handler
                        .resolve_to_message(45)));

        // Yes

        this.addOption(
                3,
                2,
                new Button_delete_Item(Material.WOOL, (short) 5,
                        itemstack_to_be_added, shop, Message_Handler
                                .resolve_to_message(68)));

        // No
        this.addOption(5, 2, new Button(Material.WOOL, (short) 14,
                Message_Handler.resolve_to_message(69), "") {

            @Override
            public void onClick(InventoryInterface inventoryInterface,
                    Player player, ItemStack cursor, ItemStack current,
                    ClickType type) {
                PrimeShop.close_InventoyInterface(player);

                inventoryInterface.branch_back_Stack.get(position_in_Stack - 1)
                        .refresh(player);
                PrimeShop.open_InventoyInterface(player,
                        inventoryInterface.branch_back_Stack
                                .get(position_in_Stack - 1));
            }
        });

        // Close Option
        this.addOption(8, 0, new Button_close_Interface());
    }
}
