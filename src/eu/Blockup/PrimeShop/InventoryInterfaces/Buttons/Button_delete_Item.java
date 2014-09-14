package eu.Blockup.PrimeShop.InventoryInterfaces.Buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.Interface_Shop_Page;
import eu.Blockup.PrimeShop.Other.Item_Comparer;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.Shops.Shop;

public class Button_delete_Item extends Button {

    private ItemStack item_to_be_removed;
    private Shop shop;

    private Button_delete_Item(Material type, String message) {
        super(type, message);
    }

// TODO Remove unused code found by UCDetector
//     public Button_delete_Item(Material type, String name,
//             String... description) {
//         super(type, name, description);
//     }

    public Button_delete_Item(Material type,
            short damage, ItemStack item_to_be_removed, Shop shop,
            String message) {
        this(type, message);
        super.setDamage(damage);
        this.shop = shop;
        this.item_to_be_removed = item_to_be_removed;
    }

    @Override
    public void onClick(InventoryInterface inventoryInterface, Player player, ItemStack cursor,
            ItemStack current, ClickType type) {
        // shop.add_ItemStack(item_to_be_removed);

        ItemStack matchedItem = null;
        boolean match = false;
        for (ItemStack itemstack : shop.listOfItems) {

            if (Item_Comparer.do_Items_match(itemstack, item_to_be_removed, true, true, true, true, true)) {
                match = true;
                matchedItem = itemstack;
                break;
            }
        }

        if (!match) {
            player.sendMessage(Message_Handler.resolve_to_message(37));
        } else {
            shop.remove_ItemStack(matchedItem);
            player.sendMessage(Message_Handler.resolve_to_message(38));
            PrimeShop.shopConfigHandler.write_shops_to_Harddisk();
        }
        
        if (inventoryInterface.parentMenu != null) {
            if (inventoryInterface.parentMenu.parentMenu  != null) {
                if (inventoryInterface.parentMenu.parentMenu instanceof Interface_Shop_Page) {
                    Interface_Shop_Page shopPage = (Interface_Shop_Page) inventoryInterface.parentMenu.parentMenu ;
                    int pagenumber;
                    if (shopPage.pagenumber <= shop.number_of_pages) {
                        pagenumber = shopPage.pagenumber;                
                    }else {
                        pagenumber = 1;
                    }
                        
                    PrimeShop.open_InventoyInterface(player, new Interface_Shop_Page(shopPage.get_brnach_back_list_of_parentMenu(), player, shop, pagenumber));
                    return;
                }
            }    
        }
        PrimeShop.close_InventoyInterface(player);
    }
}
