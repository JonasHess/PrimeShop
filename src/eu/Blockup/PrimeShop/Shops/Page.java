package eu.Blockup.PrimeShop.Shops;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Page {

    
    public List<ItemStack> listOfItems;
    static int amount_of_items_fitting__in_one_page = 26;
    
    
    Page(List<ItemStack> listOfItems, int index_of_this_page, int max_count_of_pages) {
        super();
        this.listOfItems = listOfItems;
    }
}
