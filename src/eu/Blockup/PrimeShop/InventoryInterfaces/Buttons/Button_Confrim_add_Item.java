package eu.Blockup.PrimeShop.InventoryInterfaces.Buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.Interface_add_Item_to_Shop;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.Shops.Shop;

public class Button_Confrim_add_Item extends Button {

    private ItemStack item_to_be_added;
    private Shop shop;

    public Button_Confrim_add_Item(Shop shop, ItemStack item_to_be_added,
            Material type_of_Icon, short data_of_Icon, String name,
            String... description) {
        super(type_of_Icon, data_of_Icon, item_to_be_added.getEnchantments(),
                item_to_be_added.getAmount(), name, description);
        this.shop = shop;
        this.item_to_be_added = item_to_be_added;

    }

    @Override
    public void onClick(InventoryInterface inventoryInterface, Player player,
            ItemStack cursor, ItemStack current, ClickType type) {
        shop.add_ItemStack(item_to_be_added);
        PrimeShop.shopConfigHandler.write_shops_to_Harddisk();
        player.sendMessage(Message_Handler.resolve_to_message(36));
        PrimeShop.close_InventoyInterface(player);

        if (inventoryInterface instanceof Interface_add_Item_to_Shop) {

            Interface_add_Item_to_Shop parentmenu = (Interface_add_Item_to_Shop) inventoryInterface;
            if (parentmenu.parentShop != null) {
                parentmenu.parentShop.refresh(player);
                PrimeShop.open_InventoyInterface(player, parentmenu);
            }
        }
    }
}