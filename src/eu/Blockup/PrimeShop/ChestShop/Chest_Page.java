package eu.Blockup.PrimeShop.ChestShop;

import java.util.List;

public class Chest_Page { // NO_UCD (use default)
    public List<Item_Supply> listOfItems;
    static int amount_of_items_fitting__in_one_page = 26;

    Chest_Page(List<Item_Supply> listOfItems, int index_of_this_page,
            int max_count_of_pages) {
        super();
        this.listOfItems = listOfItems;
    }
}
