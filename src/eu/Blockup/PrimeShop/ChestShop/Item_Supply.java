package eu.Blockup.PrimeShop.ChestShop;

import org.bukkit.inventory.ItemStack;

public class Item_Supply { // NO_UCD (use default)

    private ItemStack itemStack;
    private int amount;

    Item_Supply(ItemStack itemStack, int amount) {

        super();
        this.itemStack = itemStack;
        this.amount = amount;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public int getAmount() {
        return amount;
    }

    public boolean has_amount_of(int i) {
        if (amount >= i)
            return true;
        return false;

    }

    public boolean remove_amount_of(int x) {
        if (!has_amount_of(x))
            return false;

        this.amount = amount - x;
        return true;
    }

    public void add_amount(int x) {
        this.amount = this.amount + x;
    }

}
