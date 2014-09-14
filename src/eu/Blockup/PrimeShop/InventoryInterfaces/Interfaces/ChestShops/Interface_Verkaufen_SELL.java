package eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.ChestShops;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.ChestShop.ChestShop;
import eu.Blockup.PrimeShop.ChestShop.Item_Supply;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickHandler;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_Amount;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_close_Interface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_with_no_task;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.PricingEngine.Item_Trader;
import eu.Blockup.PrimeShop.PricingEngine.Pool_of_Item_Traders;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.ReturnObjects.ReturnPrice;


public class Interface_Verkaufen_SELL extends InventoryInterface {

    public Item_Supply itemStack_to_be_bought;
    public final ChestShop chestShop;
    public boolean deleteable;
    public int amount;
    public String permissionToBUY;
    public String permissionToSELL;

    public Interface_Verkaufen_SELL(final List<InventoryInterface> link_Back_Stack, Player player,
            ChestShop chestShop, final Item_Supply itemStack_to_be_bought,
            final int amount) {
        super(Message_Handler.resolve_to_message(63), 4, link_Back_Stack);
        this.chestShop = chestShop;
        this.setCloseable(false);
        this.amount = amount;
        this.itemStack_to_be_bought = itemStack_to_be_bought;
    
        // TODO  PERMISSIONS
            this.permissionToBUY = "PrimeShop.VIP.canBuySellAllItemsRegardlessIfTheyWereAddedToAShop";
            this.permissionToSELL = "PrimeShop.VIP.canBuySellAllItemsRegardlessIfTheyWereAddedToAShop";

        

        this.setClickHandler(new ClickHandler() {
            @Override
            public boolean onClick(Player player, ItemStack cursor,
                    ItemStack current, ClickType type) {
//              player.sendMessage("Du hast gedrückt");
                return false;

            }
        });

        // Empty Option
        for (int a = 0; a < this.getWidth(); a++) {
            for (int b = 0; b < this.getHeight(); b++) {
                this.addOption(a, b, new Button_with_no_task(Cofiguration_Handler.background_ItemStack, " "));
            }
        }

//        // Kaufen
//
//        
//        Button_Buy_Sell_Item buy_Button = new Button_Buy_Sell_Item(permissionToBUY, true, 55, itemStack_to_be_bought, "");
//        this.addOption(3, 2, buy_Button);
//        buy_Button.setDisplayIcon(Cofiguration_Handler.buyButton_ItemStack);
//        buy_Button.refresh_price(this.amount);
        

        // Verkaufen

        Button_Buy_Sell_Item_in_ChestShop sell_Button = new Button_Buy_Sell_Item_in_ChestShop(itemStack_to_be_bought, permissionToSELL, false, this.amount, itemStack_to_be_bought.getItemStack(),"");
        this.addOption(4, 2, sell_Button);
        sell_Button.setDisplayIcon(Cofiguration_Handler.sellButton_ItemStack);
        sell_Button.refresh_price(this.amount);
        
        
        
        if (PrimeShop.has_player_Permission_for_this_Command(player, "PrimeShop.admin.changePrices")) { 
            
        
       

        // Display Icon

        
        if (itemStack_to_be_bought.getItemStack().getMaxStackSize() > 1) {
        Button_Amount button_Amount = new Button_Amount(itemStack_to_be_bought.getItemStack(), "", 
                Message_Handler.resolve_to_message(56), Message_Handler.resolve_to_message(57), Message_Handler.resolve_to_message(58), Message_Handler.resolve_to_message(59));
        button_Amount.setAmount(this.amount);
        this.addOption(4, 0, button_Amount);
        } else {
            this.addOption(4, 0, new Button_with_no_task(itemStack_to_be_bought.getItemStack(), "", Message_Handler.resolve_to_message(56, PrimeShop.convert_IemStack_to_DisplayName(itemStack_to_be_bought.getItemStack())), Message_Handler.resolve_to_message(60)));
        }
        
        
        
        // Close Option
        this.addOption(8, 0, new Button_close_Interface());

        // Go Back Option
            if (position_in_Stack > 0) {
    
                this.addOption(0, 0, new Button(new ItemStack(Material.CHEST),
                        Message_Handler.resolve_to_message(61), Message_Handler.resolve_to_message(62)) {
    
                    @Override
                    public void onClick(InventoryInterface inventoryInterface, Player player, ItemStack cursor,
                            ItemStack current, ClickType type) {
                        PrimeShop.close_InventoyInterface(player);
                        PrimeShop.open_InventoyInterface(player,
                                inventoryInterface.branch_back_Stack.get(position_in_Stack - 1));
                    }
                });
    
            }
        }

    }
        
    public static String get_Price2(ItemStack itemStack, boolean kaufen,
            int amount) {
        Item_Trader itemTrader = Pool_of_Item_Traders.get_ItemTrader();
        ReturnPrice ReturnSELL = itemTrader.get_Price_of_Itemstack(itemStack,
                amount, kaufen);
        Pool_of_Item_Traders.return_Item_Trader(itemTrader);
        itemTrader = null;

        if (ReturnSELL.succesful) {
            return Message_Handler.resolve_to_message(40, PrimeShop.economy.format(ReturnSELL.price));
        } else {
            return Message_Handler.resolve_to_message(13);
        }
    }
}

