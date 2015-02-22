package eu.Blockup.PrimeShop.PricingEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.PricingEngine.DataHandling.Shop_Item;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.ReturnObjects.ReturnPrice;

class Price_Comparator implements Comparator<Shop_Item> {

    @Override
    public int compare(Shop_Item s1, Shop_Item s2) {

        double price1 = s1.timesItemWasBought.getValue()
                - s1.timesItemWasSold.getValue();
        double price2 = s2.timesItemWasBought.getValue()
                - s2.timesItemWasSold.getValue();

        if (price1 > price2) {
            return 1;
        } else if (price1 < price2) {
            return -1;
        }
        return 0;
    }

}

public class Price_Sorter {

    public static List<String> get_Sortet_List(int maxLines, boolean ascending) {

        List<Shop_Item> Item_List = new ArrayList<Shop_Item>();

        for (Shop_Item value : PrimeShop.hashMap_SQL_Item.values()) {
            if (value.Object_is_linked_with_Database.getValue())
                Item_List.add(value);
        }

        Collections.sort(Item_List, new Price_Comparator());

        List<String> resultList = new ArrayList<String>();

        for (int i = 0; i < Item_List.size(); i++) {

            int j;

            if (ascending) {
                j = i;
            } else {
                j = Item_List.size() - i - 1;
            }

            if (i < maxLines) {
                Shop_Item item = Item_List.get(j);
                String itemname = item.itemDisplayname.getValue() + " ("
                        + item.mcItemid + ")";
                String itemPrice = "UNKNOWN";

                Item_Trader itemTrader = Pool_of_Item_Traders.get_ItemTrader();

                ReturnPrice resultPrice = itemTrader.get_Price_of_Itemstack(
                        item.getMcItemId_as_ItemStack(1), 1, ascending);
                Pool_of_Item_Traders.return_Item_Trader(itemTrader);

                if (resultPrice.succesful) {
                    itemPrice = PrimeShop.economy.format(resultPrice.price);
                }

                // String itemPrice =
                // PrimeShop.economy.format(item.calculate_price(item.timesItemWasBought.getValue(),
                // item.timesItemWasSold.getValue()));
                // String itemPrice =
                // PrimeShop.economy.format(item.clone().buyItem(true, 1,
                // false).price);
                resultList.add(Message_Handler.resolve_to_message(132,
                        String.valueOf(i + 1), itemname.toUpperCase(),
                        itemPrice));

            } else {
                break;
            }
        }

        return resultList;

    }

}
