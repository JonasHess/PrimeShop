package eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickHandler;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_Confrim_add_Item;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_close_Interface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_with_no_task;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.Shops.Shop;

public class Interface_add_Item_to_Shop extends InventoryInterface {

    public InventoryInterface parentShop;

    // @SuppressWarnings("deprecation")
    public Interface_add_Item_to_Shop(
            final List<InventoryInterface> link_Back_Stack, Player player,
            final ItemStack itemstack_to_be_added, Shop shop) {
        super(Message_Handler.resolve_to_message(48), 4, link_Back_Stack); // TODO
                                                                           // Correct
                                                                           // ItemName

        this.setCloseable(false);

        this.setClickHandler(new ClickHandler() {
            @Override
            public boolean onClick(Player player, ItemStack cursor,
                    ItemStack current, ClickType type) {
                // player.sendMessage("Du hast gedr�ckt");
                return false;

            }
        });

        // Empty Options

        for (int a = 0; a < this.getWidth(); a++) {
            for (int b = 0; b < this.getHeight(); b++) {
                this.addOption(a, b, new Button_with_no_task(
                        Cofiguration_Handler.background_ItemStack, " "));
            }
        }

        this.addOption(4, 0, new Button_with_no_task(itemstack_to_be_added,
                Message_Handler.resolve_to_message(44, shop.shopname),
                Message_Handler.resolve_to_message(45)));

        // Hinzuf�gen

        this.addOption(3, 2, new Button_Confrim_add_Item(shop,
                itemstack_to_be_added, Material.WOOL, (short) 5,
                Message_Handler.resolve_to_message(46), ""));

        // Verkaufen
        // this.addOption(5, 2, new SellOption(false, 1,
        // itemstack_to_be_added.getType()).setType(Material.getMaterial(51)));

        this.addOption(5, 2, new Button(Material.WOOL, (short) 14,
                Message_Handler.resolve_to_message(47), "") {

            @Override
            public void onClick(InventoryInterface inventoryInterface,
                    Player player, ItemStack cursor, ItemStack current,
                    ClickType type) {
                PrimeShop.close_InventoyInterface(player);

                parentShop.refresh(player);
                PrimeShop.open_InventoyInterface(player, parentShop);
            }
        });

        // Close Option
        this.addOption(8, 0, new Button_close_Interface());

        // Go Back Option
        // if (parentShop != null) {
        // ItemStack displayIcon = parentShop.itemStack_to_be_bought;
        // @SuppressWarnings("deprecation")
        // short damageid2 = displayIcon.getData().getData();
        //
        // this.addOption(0, 0, new Option(displayIcon.getType(), damageid2,
        // "Back", "Go back to item") {
        //
        // @Override
        // public void onClick(Menu menu, Player player, ItemStack cursor,
        // ItemStack current, ClickType type) {
        // Blockup_Economy.closeMenu(player);
        //
        // for (Option option : parentShop.getOptions()) {
        //
        // if (option instanceof SellOption) {
        // try {
        // ((SellOption) option).refresh_price();
        // } catch (Exception e) {
        // player.sendMessage("DID NOT WORK");
        // }
        // }
        // }
        // parentShop.refresh(player);
        // Blockup_Economy.openMenu(player, parentShop);
        // }
        // });
        //
        // }

    }

    // @SuppressWarnings("deprecation")
    // public void refresh_Price () {
    // // SQL_Item sqlItem =
    // Blockup_Economy.get_SQL_Item_of_Itemstack(itemStack_to_be_bought);
    // String defualtPrice =
    // Blockup_Economy.economy.format(sqlItem.defaultPrice.getValue());
    // String currentPrice =
    // Blockup_Economy.economy.format(sqlItem.calculate_price(sqlItem.timesItemWasBought.getValue(),
    // sqlItem.timesItemWasSold.getValue()));
    //
    // this.addOption(4, 0,new
    // EmptyOption(Material.getMaterial(421),Message_Handler.resolve_to_message(19),
    // Message_Handler.resolve_to_message(20,defualtPrice),
    // Message_Handler.resolve_to_message(21,currentPrice)));
    //
    //
    // }

    // public static String get_Price2(ItemStack itemStack, boolean kaufen, int
    // amount) {
    // Item_Trader itemTrader = Pool_of_Item_Traders.get_ItemTrader();
    // ReturnPrice ReturnSELL = itemTrader.get_Price_of_Itemstack(itemStack,
    // amount, kaufen);
    // Pool_of_Item_Traders.return_Item_Trader(itemTrader);
    // itemTrader = null;
    //
    // if (ReturnSELL.succesful) {
    // return "Price: " + Blockup_Economy.economy.format(ReturnSELL.price);
    // } else {
    // return "Error: You Can not buy this Item";
    // }
    // }
}
