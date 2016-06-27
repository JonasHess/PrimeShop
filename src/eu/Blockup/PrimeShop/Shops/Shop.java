package eu.Blockup.PrimeShop.Shops;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Shop {
    public String shopname;
    public ItemStack displayIcon;
    public List<ItemStack> listOfItems;
    public int number_of_pages = 0;

    public Shop(String shopname, ItemStack displayIcon) {
        super();
        this.shopname = shopname;
        this.displayIcon = displayIcon;
        this.listOfItems = new ArrayList<ItemStack>();
        ;
    }

    public void add_ItemStack(ItemStack itemStack) {
        itemStack.setAmount(1);
        this.listOfItems.add(itemStack);
        refresh_pageCount();
    }

    public void remove_ItemStack(ItemStack itemStack) {
        this.listOfItems.remove(itemStack);
        refresh_pageCount();
    }

    public void refresh_pageCount() {
        int listsize = this.listOfItems.size();
        if (listsize > 0) {
            double exact_number_of_pages = (double) listsize
                    / Page.amount_of_items_fitting__in_one_page;
            this.number_of_pages = this.roundup(exact_number_of_pages);
        } else {
            this.number_of_pages = 0;
        }
    }

    public Page get_Page(int pagenumber) {

        int this_list_of_Items_Size = this.listOfItems.size();
        int start_index = (int) ((pagenumber - 1) * Page.amount_of_items_fitting__in_one_page);

        int added_items = 0;
        int current_position = start_index;
        List<ItemStack> listOfItems = new ArrayList<ItemStack>();
        ;
        while ((added_items <= Page.amount_of_items_fitting__in_one_page)
                && (current_position <= this_list_of_Items_Size - 1)) {
            listOfItems.add(this.listOfItems.get(current_position));
            added_items++;
            current_position++;
        }
        return new Page(listOfItems, pagenumber, this.number_of_pages);
    }

    private int roundup(double givennumber) {
        int result;
        int zwischensumme;
        zwischensumme = (int) givennumber;
        if ((givennumber - zwischensumme) == 0) {
            result = zwischensumme;
        } else {
            result = zwischensumme + 1;
        }
        return result;
    }
}
