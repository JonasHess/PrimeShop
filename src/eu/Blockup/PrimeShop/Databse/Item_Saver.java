package eu.Blockup.PrimeShop.Databse;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.PricingEngine.DataHandling.Shop_Item;

public class Item_Saver implements Runnable {

    @Override
    public void run() {

        save_all_Items();
    }

    public void save_all_Items() {

        for (Shop_Item value : PrimeShop.hashMap_SQL_Item.values()) {
            if (value.Object_is_linked_with_Database.getValue()
                    && value.changes_since_last_save) {
                PrimeShop.databaseHandler.save_Item_to_Databse(value);
            }

        }
        // PrimeShop.plugin.getServer().broadcastMessage("Saving all Items!");

    }
}