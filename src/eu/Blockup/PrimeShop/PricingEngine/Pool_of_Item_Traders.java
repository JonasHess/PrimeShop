package eu.Blockup.PrimeShop.PricingEngine;

import eu.Blockup.PrimeShop.PrimeShop;

public class Pool_of_Item_Traders {

    // TODO ADD Pool behaviour for more performance

    public static Item_Trader get_ItemTrader() {

        return new Item_Trader(PrimeShop.plugin);
    }

    public static void return_Item_Trader(Item_Trader itemTrader) {
        return;
    }
}
