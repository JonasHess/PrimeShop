package eu.Blockup.PrimeShop.InventoryInterfaces.Buttons;

import java.text.DecimalFormat;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.PricingEngine.DataHandling.Shop_Item;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.ReturnObjects.ReturnBoolean;

public class Button_modify_Rate extends Button {

    private double offset;
    // private ItemStack itemStack;
    private Shop_Item sqlItem;

    // TODO Remove unused code found by UCDetector
    // public Button_modify_Rate(Shop_Item sqlItem, double offset, Material
    // display_Material) {
    // super(display_Material);
    // this.offset = offset;
    // this.sqlItem = sqlItem;
    // // this.setName(offset_to_String(this.offset));
    // // this.itemStack = new ItemStack(display_Material);
    // }

    public Button_modify_Rate(Shop_Item sqlItem, double offset,
            Material display_Material, String name, String... description) {
        super(display_Material, name, description);
        this.offset = offset;
        this.sqlItem = sqlItem;
        // this.setName(offset_to_String(this.offset));
        // this.itemStack = new ItemStack(display_Material);
    }

    public Button_modify_Rate(Shop_Item sqlItem, double offset,
            Material display_Material, short damage, String name,
            String... description) {
        super(display_Material, damage, name, description);
        this.offset = offset;
        this.sqlItem = sqlItem;
        // this.setName(offset_to_String(this.offset));
        // this.itemStack = new ItemStack(display_Material, 1, damage);
    }

    @Override
    public void onClick(InventoryInterface inventoryInterface, Player player,
            ItemStack cursor, ItemStack current, ClickType type) {
        // if (type == ClickType.LEFT) {
        ReturnBoolean result = new ReturnBoolean();

        if (offset == 2) {
            result.succesful = true;
            sqlItem.rate_of_price_change.setValue(0.0);

        } else {
            if (offset == 3) {
                result.succesful = true;
                sqlItem.rate_of_price_change.setValue(300.0);
                // sqlItem.rate_of_price_change.setValue(Cofiguration_Handler.default_rate_of_price_change);

            } else {
                result = sqlItem.change_Changin_Rate(offset);
            }

        }

        if (!result.succesful) {
            player.sendMessage(result.errorMessage);
        }

        for (Button button : inventoryInterface.getButtons()) {

            if (button instanceof Button_pricetag_Rate) {
                try {
                    ((Button_pricetag_Rate) button).refresh_Price();
                } catch (Exception e) {
                    PrimeShop.plugin.getLogger().log(Level.SEVERE,
                            "Error casting Interface to Button_pricetag_Rate");
                }
            }
        }
        PrimeShop.databaseHandler.save_Item_to_Databse(sqlItem);
        inventoryInterface.refresh(player);

        DecimalFormat df = new DecimalFormat("#.00");
        String rate_of_price_change = df.format(sqlItem.rate_of_price_change
                .getValue());
        rate_of_price_change += " Items";
        player.sendMessage(Message_Handler.resolve_to_message(23,
                rate_of_price_change));

        return;
    }
    // }
    // public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack
    // current, ClickType type) {
    // if (type == ClickType.RIGHT) {
    // this.setName(this.get_Price(this.itemStack, this.kaufen, this.menge));
    // menu.refresh(player);
    // return;
    // }
    //
    //
    // ReturnPrice result;
    // Item_Trader itemTrader = Pool_of_Item_Traders.get_ItemTrader();
    // if (kaufen) {
    // result = itemTrader.buy_ItemStack(this.itemStack, this.menge, player);
    // } else {
    // result = itemTrader.sell_ItemStack(this.itemStack, this.menge, player);
    // }
    // Pool_of_Item_Traders.return_Item_Trader(itemTrader);
    // itemTrader = null;
    // if (!result.succesful) {
    // player.sendMessage(result.errorMessage);
    // }
    //
    // for (Option option : menu.getOptions()) {
    //
    // if (option instanceof SellOption) {
    // try {
    // ((SellOption) option).refresh_price();
    // } catch (Exception e) {
    // player.sendMessage("DID NOT WORK");
    // }
    // // } finally {
    // // menu.refresh(player);
    // // }
    // }
    // }
    // menu.refresh(player);
    //
    // }

    // TODO Remove unused code found by UCDetector
    // public synchronized String get_Price(ItemStack itemStack, boolean kaufen,
    // int amount) {
    // Item_Trader itemTrader = Pool_of_Item_Traders.get_ItemTrader();
    // ReturnPrice ReturnSELL = itemTrader.get_Price_of_Itemstack(itemStack,
    // amount, kaufen);
    // Pool_of_Item_Traders.return_Item_Trader(itemTrader);
    // itemTrader = null;
    //
    // if (ReturnSELL.succesful) {
    // return Message_Handler.resolve_to_message(21,
    // PrimeShop.economy.format(ReturnSELL.price));
    // } else {
    // return Message_Handler.resolve_to_message(41);
    // }
    // }

    // private String offset_to_String(double offset) {
    // String result;
    // if (offset > 0) {
    // result = "+ ";
    // } else {
    // result = "";
    // }
    // result += PrimeShop.economy.format(offset);
    // return result;
    // }

    // public void refresh_price() {
    // refresh_price(this.getAmount());
    // }
    // public void refresh_price(int amount) {
    // this.menge = amount;
    // this.setAmount(amount);
    // this.setName(this.get_Price(this.itemStack, this.kaufen, amount));
    // }
}
