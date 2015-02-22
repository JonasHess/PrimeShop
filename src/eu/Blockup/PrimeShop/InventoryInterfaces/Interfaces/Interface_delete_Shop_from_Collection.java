package eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_with_no_task;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.Shops.Shop;

class Interface_delete_Shop_from_Collection extends InventoryInterface {

    public Interface_delete_Shop_from_Collection(
            final List<InventoryInterface> link_Back_Stack, Player player,
            final Shop shop, final int collectionID, final boolean npc) {
        super(Message_Handler.resolve_to_message(70), 4, link_Back_Stack);
        this.setCloseable(false);

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
                new Button_with_no_task(shop.displayIcon, Message_Handler
                        .resolve_to_message(72), Message_Handler
                        .resolve_to_message(45)));

        // Yes

        this.addOption(3, 2, new Button(Material.WOOL, (short) 5,
                Message_Handler.resolve_to_message(73)) {

            @Override
            public void onClick(InventoryInterface inventoryInterface,
                    Player player, ItemStack cursor, ItemStack current,
                    ClickType type) {

                if (npc) {
                    if (PrimeShop.plugin.remove_Shop_from_NPC(shop,
                            collectionID)) {
                        player.sendMessage(Message_Handler
                                .resolve_to_message(71));
                        PrimeShop.shopConfigHandler.write_shops_to_Harddisk();
                        PrimeShop.close_InventoyInterface(player);
                    }
                }

            }
        });

        // No
        this.addOption(5, 2, new Button(Material.WOOL, (short) 14,
                Message_Handler.resolve_to_message(74), "") {

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
    }
}
