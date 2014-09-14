package eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.ChestShops;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.ChestShop.ChestShop;
import eu.Blockup.PrimeShop.ChestShop.Item_Supply;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.PricingEngine.Item_Trader;
import eu.Blockup.PrimeShop.PricingEngine.Pool_of_Item_Traders;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.ReturnObjects.ReturnPrice;

public class Button_Buy_Sell_Item_in_ChestShop extends Button {

    private int menge;
    private ItemStack itemStack;
    private boolean kaufen;
    private static Material material = Material.DIAMOND_ORE;
    private String permission;
    private Item_Supply item_Supply;
    private ChestShop chestShop;

//    public SellOption(boolean kaufen, int amount, Material type) {
//        super(material);
//        this.kaufen = kaufen;
//        this.menge = amount;
//        this.itemStack = new ItemStack(type);
//        this.itemStack.setAmount(amount);
//        this.setAmount(amount);
//        this.refresh_price();
//    }

//    public SellOption(boolean kaufen, int amount, ItemStack itemstack, String name, String... description) {
//        super(material, name, description);
//        this.kaufen = kaufen;
//        this.menge = amount;
//        this.itemStack = new ItemStack(type);
//        this.itemStack.setAmount(amount);
//        this.setAmount(amount);
//        this.refresh_price();
//    }
//    public SellOption(boolean kaufen, int amount, Material type, String name, String... description) {
//        super(material, name, description);
//        this.kaufen = kaufen;
//        this.menge = amount;
//        this.itemStack = new ItemStack(type);
//        this.itemStack.setAmount(amount);
//        this.setAmount(amount);
//        this.refresh_price();
//    }

    public Button_Buy_Sell_Item_in_ChestShop(ChestShop chestShop, Item_Supply item_Supply, String permission, boolean kaufen, int amount, ItemStack itemstack, String name, String... description) {
        super(material, name, description);
        this.kaufen = kaufen;
        this.menge = amount;
        this.item_Supply = item_Supply;
        this.chestShop = chestShop;
        this.itemStack = itemstack;
        this.itemStack.setAmount(amount);
        this.setAmount(amount);
        this.refresh_price();
        this.permission = permission;
    }
//    public SellOption(boolean kaufen, int amount, Material type, short damage, String name, String... description) {
//        super(material, damage, name, description);
//        this.kaufen = kaufen;
//        this.menge = amount;
//        this.itemStack = new ItemStack(type, amount, damage);
//        this.itemStack.setAmount(amount);
//        this.setAmount(amount);
//        this.refresh_price();
//    }

    @Override
    public void onClick(InventoryInterface inventoryInterface, Player player, ItemStack cursor, ItemStack current, ClickType type) {
        
        
//        player.sendMessage("Time left:" + PrimeShop.plugin.cooldownManager.timeLeft(player, Time.SECONDS));
        if(PrimeShop.plugin.cooldownManager.is_player_Spamming(player)) {
            player.sendMessage(Message_Handler.resolve_to_message(133));
            return;
        }
        PrimeShop.plugin.cooldownManager.player_Clicked(player);
        
//        if (type == ClickType.RIGHT) {
////            this.setName(this.get_Price(this.itemStack, this.kaufen, this.menge));
//            
////            String buttonTitle;
//            if (kaufen) {
//                this.setName(Message_Handler.resolve_to_message(120));
//            } else {
//                this.setName(Message_Handler.resolve_to_message(121));
//            }
//            this.setDescription(Message_Handler.resolve_to_message(122, String.valueOf(getAmount())), this.get_Price(this.itemStack, this.kaufen, this.menge));
//            
//            inventoryInterface.refresh(player);
//            return;
//        }
        
        ReturnPrice result = new ReturnPrice();
        if (!PrimeShop.has_player_Permission_for_this_Command(player, permission)) {
            result.succesful = false;
            if (kaufen) {
                result.errorMessage = Message_Handler.resolve_to_message(30);
                player.sendMessage(result.errorMessage);
                return;
            }else {
                result.errorMessage = Message_Handler.resolve_to_message(32);
                player.sendMessage(result.errorMessage);
                return;
            }
        }
        
        // TODO prüfen, ob ChestShop genug items hat

        
        
        if (item_Supply.has_amount_of(menge)) {
            
            Item_Trader itemTrader = Pool_of_Item_Traders.get_ItemTrader();
            
            
            
            if (kaufen) {
                result = itemTrader.buy_ItemStack(this.itemStack, this.menge, player);
            } else {
                result = itemTrader.sell_ItemStack(this.itemStack, this.menge, player, true, true, chestShop);
            }
            Pool_of_Item_Traders.return_Item_Trader(itemTrader);
            itemTrader = null;
            if (!result.succesful) {
                player.sendMessage(result.errorMessage);
            }else {
                item_Supply.remove_amount_of(menge);
                
                if (kaufen) {
                    this.chestShop.add_money(result.price);
                }
            }
            
        } else {
            player.sendMessage("This Chest shop does not have enough of this item");
        }
        
        
        
        
        for (Button button : inventoryInterface.getButtons()) {

            if (button instanceof Button_Buy_Sell_Item_in_ChestShop) {
                try {
                    ((Button_Buy_Sell_Item_in_ChestShop) button).refresh_price();
                } catch (Exception e) {
                    player.sendMessage(Message_Handler.resolve_to_message(13));
                }
            }
        }
        inventoryInterface.refresh(player);

    }

    private synchronized String get_Price (ItemStack itemStack, boolean kaufen, int amount) {
        Item_Trader itemTrader = Pool_of_Item_Traders.get_ItemTrader();
        
        
        ReturnPrice ReturnSELL = itemTrader.get_Price_of_Itemstack(itemStack, amount, kaufen);
        Pool_of_Item_Traders.return_Item_Trader(itemTrader);
        itemTrader = null;
        
        if (ReturnSELL.succesful) {
             return Message_Handler.resolve_to_message(40, PrimeShop.economy.format(ReturnSELL.price));
        } else {
            return Message_Handler.resolve_to_message(13);
        }
    }
    
    
    public void refresh_price() {    
        refresh_price(this.getAmount());        
    }
    public void refresh_price(int amount) {    
        this.menge = amount;
        this.setAmount(amount);
        int amount_Left = item_Supply.getAmount();
//        this.setName(this.get_Price(this.itemStack, this.kaufen, amount));
        if (kaufen) {
            this.setName(Message_Handler.resolve_to_message(120));
        } else {
            this.setName(Message_Handler.resolve_to_message(121));
        }
        this.setDescription("Amount left: " + amount_Left + " " + Message_Handler.resolve_to_message(122, String.valueOf(getAmount())), this.get_Price(this.itemStack, this.kaufen, this.menge));
        
    }
}


