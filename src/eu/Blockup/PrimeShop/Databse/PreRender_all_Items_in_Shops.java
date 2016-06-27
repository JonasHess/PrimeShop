package eu.Blockup.PrimeShop.Databse;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.Shops.Shop;

public class PreRender_all_Items_in_Shops extends Thread {

    public void run() {

        for (Shop shop : PrimeShop.hashMap_Shops.values()) {
            for (ItemStack item : shop.listOfItems) {
                List<ItemStack> list_of_related_items = PrimeShop
                        .get_all_SubItems_of_ItemStack(item);
                for (ItemStack related_item : list_of_related_items) {
                    List<ItemStack> resultlist = PrimeShop
                            .get_all_SubItems_of_ItemStack(related_item);
                    if (resultlist.isEmpty()) {
                        PrimeShop.databaseHandler
                                .link_with_Databse_if_not_allready_linked(PrimeShop
                                        .get_Shop_Item_of_Itemstack(related_item));
                    }
                }
            }
        }
        // System.out.println("Finished loading PrimeShop");
    }
}
