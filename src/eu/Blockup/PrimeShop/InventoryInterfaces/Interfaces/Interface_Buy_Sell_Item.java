package eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickHandler;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_close_Interface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_Amount;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_with_no_task;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_Buy_Sell_Item;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.Shops.Shop;

public class Interface_Buy_Sell_Item extends InventoryInterface {

    ItemStack itemStack_to_be_bought;
    final Shop shop;
    boolean deleteable;
    int amount;
    private String permissionToBUY;
    private String permissionToSELL;

    @SuppressWarnings("deprecation")
    public Interface_Buy_Sell_Item(
            final List<InventoryInterface> link_Back_Stack, Player player,
            Shop shop_, final ItemStack itemStack_to_be_bought,
            final int amount, boolean deleteable,
            final boolean item_is_bought_without_shop) {
        super(Message_Handler.resolve_to_message(63), 4, link_Back_Stack);
        this.shop = shop_;
        this.setCloseable(false);
        this.amount = amount;
        this.deleteable = deleteable;
        this.itemStack_to_be_bought = itemStack_to_be_bought;

        try {
            this.permissionToBUY = "PrimeShop.Defaults.buyfromShop."
                    + shop.shopname;
            this.permissionToSELL = "PrimeShop.Defaults.sellfromShop."
                    + shop.shopname;
        } catch (Exception e) {
            if (!item_is_bought_without_shop)
                e.printStackTrace();
            this.permissionToBUY = "PrimeShop.VIP.canBuySellAllItemsRegardlessIfTheyWereAddedToAShop";
            this.permissionToSELL = "PrimeShop.VIP.canBuySellAllItemsRegardlessIfTheyWereAddedToAShop";
        }

        this.setClickHandler(new ClickHandler() {
            @Override
            public boolean onClick(Player player, ItemStack cursor,
                    ItemStack current, ClickType type) {
                // player.sendMessage("Du hast gedr�ckt");
                return false;

            }
        });

        // Empty Option
        for (int a = 0; a < this.getWidth(); a++) {
            for (int b = 0; b < this.getHeight(); b++) {
                this.addOption(a, b, new Button_with_no_task(
                        Cofiguration_Handler.background_ItemStack, " "));
            }
        }

        // Kaufen

        Button_Buy_Sell_Item buy_Button = new Button_Buy_Sell_Item(
                permissionToBUY, true, 55, itemStack_to_be_bought, "");
        this.addOption(3, 2, buy_Button);
        buy_Button.setDisplayIcon(Cofiguration_Handler.buyButton_ItemStack);
        buy_Button.refresh_price(this.amount);

        // Verkaufen

        Button_Buy_Sell_Item sell_Button = new Button_Buy_Sell_Item(
                permissionToSELL, false, this.amount, itemStack_to_be_bought,
                "");
        this.addOption(5, 2, sell_Button);
        sell_Button.setDisplayIcon(Cofiguration_Handler.sellButton_ItemStack);
        sell_Button.refresh_price(this.amount);

        if (PrimeShop.has_player_Permission_for_this_Command(player,
                "PrimeShop.admin.changePrices")) {

            if (this.shop != null) {

                if (PrimeShop.get_all_SubItems_of_ItemStack(
                        itemStack_to_be_bought).isEmpty()
                        || PrimeShop.plugin
                                .is_PriceLinking_disabled_for_Item(itemStack_to_be_bought)) {

                    // Change Price

                    ItemStack currentItem = new ItemStack(
                            Material.getMaterial(266));
                    this.addOption(
                            8,
                            2,
                            new Button(currentItem.getType(), currentItem
                                    .getData().getData(), Message_Handler
                                    .resolve_to_message(22), Message_Handler
                                    .resolve_to_message(125)) {

                                @Override
                                public void onClick(
                                        InventoryInterface inventoryInterface,
                                        Player player, ItemStack cursor,
                                        ItemStack current, ClickType type) {
                                    PrimeShop.close_InventoyInterface(player);
                                    PrimeShop.databaseHandler
                                            .link_with_Databse_if_not_allready_linked(PrimeShop
                                                    .get_Shop_Item_of_Itemstack(itemStack_to_be_bought));
                                    PrimeShop
                                            .open_InventoyInterface(
                                                    player,
                                                    new Interface_Change_Price(
                                                            inventoryInterface.branch_back_Stack,
                                                            player,
                                                            itemStack_to_be_bought
                                                                    .clone()));

                                }
                            });

                    // Changing Rate

                    currentItem = new ItemStack(Material.getMaterial(66));
                    this.addOption(
                            8,
                            1,
                            new Button(currentItem.getType(), currentItem
                                    .getData().getData(), Message_Handler
                                    .resolve_to_message(24), Message_Handler
                                    .resolve_to_message(124)) {

                                @Override
                                public void onClick(
                                        InventoryInterface inventoryInterface,
                                        Player player, ItemStack cursor,
                                        ItemStack current, ClickType type) {
                                    if (Cofiguration_Handler.dynamic_pricing_for_all_Items_DISABLED) {
                                        player.sendMessage(ChatColor.RED
                                                + "Dynamic pricing is disabled in the config.yml!");
                                        return;
                                    }
                                    PrimeShop.close_InventoyInterface(player);
                                    PrimeShop.databaseHandler
                                            .link_with_Databse_if_not_allready_linked(PrimeShop
                                                    .get_Shop_Item_of_Itemstack(itemStack_to_be_bought));
                                    PrimeShop
                                            .open_InventoyInterface(
                                                    player,
                                                    new Interface_Change_Rate(
                                                            inventoryInterface.branch_back_Stack,
                                                            player,
                                                            itemStack_to_be_bought
                                                                    .clone()));

                                }
                            });
                } else {

                    // List all SubItems

                    this.addOption(8, 2, new Button(Material.getMaterial(14),
                            (short) 1, Message_Handler.resolve_to_message(49),
                            Message_Handler.resolve_to_message(50),
                            Message_Handler.resolve_to_message(51)) {

                        @Override
                        public void onClick(
                                InventoryInterface inventoryInterface,
                                Player player, ItemStack cursor,
                                ItemStack current, ClickType type) {

                            PrimeShop.close_InventoyInterface(player);
                            PrimeShop
                                    .open_InventoyInterface(
                                            player,
                                            new Interface_List_of_Subitems(
                                                    inventoryInterface.branch_back_Stack,
                                                    itemStack_to_be_bought,
                                                    shop, player));
                        }
                    });
                }
                // Enable PriceLinking

                if (PrimeShop.plugin
                        .is_PriceLinking_disabled_for_Item(itemStack_to_be_bought)) {

                    addOption(
                            7,
                            3,
                            new Button(Material.ANVIL, Message_Handler
                                    .resolve_to_message(52), Message_Handler
                                    .resolve_to_message(53)) {

                                @Override
                                public void onClick(
                                        InventoryInterface inventoryInterface,
                                        Player player, ItemStack cursor,
                                        ItemStack current, ClickType type) {

                                    if (PrimeShop.plugin
                                            .is_PriceLinking_disabled_for_Item(itemStack_to_be_bought)) {

                                        PrimeShop.plugin
                                                .enable_PriceLinking_for_Item(itemStack_to_be_bought);
                                        PrimeShop
                                                .close_InventoyInterface(player);
                                        PrimeShop
                                                .open_InventoyInterface(
                                                        player,
                                                        inventoryInterface.branch_back_Stack
                                                                .get(inventoryInterface.position_in_Stack - 1));

                                    }

                                }
                            });

                }

            }
            if (shop_ != null) {
                if (PrimeShop.has_player_Permission_for_this_Command(player,
                        "PrimeShop.admin.addItemsToShop." + shop_.shopname)) {

                    // Remove Item from Shop

                    if (deleteable) {
                        this.addOption(8, 3,
                                new Button(Material.WOOL, (short) 14,
                                        Message_Handler.resolve_to_message(54),
                                        Message_Handler.resolve_to_message(55),
                                        "") {

                                    @Override
                                    public void onClick(
                                            InventoryInterface inventoryInterface,
                                            Player player, ItemStack cursor,
                                            ItemStack current, ClickType type) {

                                        PrimeShop
                                                .close_InventoyInterface(player);
                                        PrimeShop
                                                .open_InventoyInterface(
                                                        player,
                                                        new Interface_delete_Item_from_Shop(
                                                                inventoryInterface.branch_back_Stack,
                                                                player,
                                                                itemStack_to_be_bought,
                                                                shop));
                                    }
                                });
                    }
                }

            }
        }

        // Display Icon
        if (itemStack_to_be_bought.getMaxStackSize() > 1) {
            Button_Amount button_Amount = new Button_Amount(
                    itemStack_to_be_bought, "",
                    Message_Handler.resolve_to_message(56),
                    Message_Handler.resolve_to_message(57),
                    Message_Handler.resolve_to_message(58),
                    Message_Handler.resolve_to_message(59));
            button_Amount.setAmount(this.amount);
            this.addOption(4, 0, button_Amount);
        } else {
            this.addOption(
                    4,
                    0,
                    new Button_with_no_task(
                            itemStack_to_be_bought,
                            "",
                            Message_Handler.resolve_to_message(
                                    56,
                                    PrimeShop
                                            .convert_IemStack_to_DisplayName(itemStack_to_be_bought)),
                            Message_Handler.resolve_to_message(60)));
        }

        // Close Option
        this.addOption(8, 0, new Button_close_Interface());

        // Go Back Option
        if (!item_is_bought_without_shop) {
            if (position_in_Stack > 0) {

                this.addOption(
                        0,
                        0,
                        new Button(shop.displayIcon, Message_Handler
                                .resolve_to_message(61), Message_Handler
                                .resolve_to_message(62)) {

                            @Override
                            public void onClick(
                                    InventoryInterface inventoryInterface,
                                    Player player, ItemStack cursor,
                                    ItemStack current, ClickType type) {
                                PrimeShop.close_InventoyInterface(player);
                                PrimeShop.open_InventoyInterface(player,
                                        inventoryInterface.branch_back_Stack
                                                .get(position_in_Stack - 1));
                            }
                        });

            }
        }

    }
}
