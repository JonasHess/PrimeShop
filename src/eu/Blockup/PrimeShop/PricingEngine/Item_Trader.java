package eu.Blockup.PrimeShop.PricingEngine;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.ChestShop.ChestShop;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.Item_Node_of_ItemBloodline;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.ReturnObjects.ReturnPrice;

import org.bukkit.inventory.PlayerInventory;

public class Item_Trader {

//    private PrimeShop plugin;

    Item_Trader(PrimeShop plugin) {
    }

    private String convert_price_to_String(double price) {
        return PrimeShop.economy.format(price);
    }

    @SuppressWarnings("deprecation")
    public synchronized ReturnPrice buy_ItemStack(ItemStack itemStack,
            double amount, Player player) {

        ReturnPrice result = new ReturnPrice();
        double finalPrice = 0.0;
        if (!PrimeShop.plugin.has_Player_Permission_for_this_Item(player, itemStack)) {
            result.succesful = false;
            result.errorMessage = Message_Handler.resolve_to_message(30);
            return result;
        }
        
        
        result.succesful = false;
        PlayerInventory playerInventory = player.getInventory();

        if (itemStack == null) {
            result.errorMessage = Message_Handler.resolve_to_message(31);
            return result;
        }
        if (itemStack.getData().getItemTypeId() <= 0) {
            result.errorMessage = Message_Handler.resolve_to_message(31);
            return result;
        }
        if (!(playerInventory.firstEmpty() == -1)) { 
            ReturnPrice futureprice = this.get_Price_of_Itemstack(itemStack,
                    (int) amount, true);
            if (futureprice.succesful) {
                if (PrimeShop.has_Player_more_Money_than(player, futureprice.price)) {
                    finalPrice = futureprice.price;
                    CountDownLatch latch = new CountDownLatch(1);
                    Item_Node_of_ItemBloodline tree = PrimeShop
                            .get_Tree_of_Itemstack(itemStack);
                    Transaction transaction = new Transaction(itemStack, latch, amount,
                            true, false, tree);
                    transaction.start();

                    try {
                        latch.await(3, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        transaction.transactionWasSuccessful = false;
                        transaction.errorMessage = Message_Handler.resolve_to_message(10);
                        e.printStackTrace();
                    }
                    if (!transaction.transactionIsCompleted) {
                        transaction.stop();
                        result.succesful = false;
                        result.errorMessage = Message_Handler.resolve_to_message(13);
                        player.sendMessage(Message_Handler.resolve_to_message(13));
                        return result;
                    }
                    if (transaction.transactionWasSuccessful) {
                        result.succesful = true;
                        result.price = transaction.priceTotal;
                    } else {
                        result.succesful = false;
                        result.errorMessage = transaction.errorMessage;
                    }

                } else {
                    // player not enough money
                    result.errorMessage = Message_Handler.resolve_to_message(9);
                }
            } else {
                // error calculation future price
                result.errorMessage = Message_Handler.resolve_to_message(10);
            }

        } else {
            // Player hat nicht genügent Platz
            result.succesful = false;
            result.errorMessage = Message_Handler.resolve_to_message(11);
        }

        if (result.succesful) {
            PrimeShop.withdraw_money_from_Players_Account(player, finalPrice);
            player.sendMessage(Message_Handler.resolve_to_message(8,
                    convert_price_to_String(result.price)));
            playerInventory.addItem(itemStack); // TODO hat schon einmal nicht funktioniert!

        } else {
            player.sendMessage(result.errorMessage);
        }
        notifyAll();
        return result;

    }

    @SuppressWarnings("deprecation")
    public synchronized ReturnPrice sell_ItemStack(ItemStack itemStack,
            int amount, Player player, boolean output_enabled, boolean sell_to_an_Chestshop, ChestShop chestShop) {

        ReturnPrice result = new ReturnPrice();
        
        // Permission check
        if (!PrimeShop.plugin.has_Player_Permission_for_this_Item(player, itemStack)) {
            result.succesful = false;
            result.errorMessage = Message_Handler.resolve_to_message(32);
            return result;
        }
        
        // Initialization
        result.succesful = false;
        ItemStack players_Item = null;
        PlayerInventory playerInventory = player.getInventory();
        ReturnPrice futurePrice = null;

        if (itemStack == null) {
            result.errorMessage = Message_Handler.resolve_to_message(12);
            return result;
        }
        itemStack.setAmount(amount);
        if (itemStack.getData().getItemTypeId() <= 0) {
            result.errorMessage = Message_Handler.resolve_to_message(12);
            return result;
        }
        
        // Player has Item he is going to sell?
        
        if (hasPlayerThisITem(player, itemStack, amount)) {
            
            
            if (sell_to_an_Chestshop) {
                
                // check, if ChestShop has enough money to pay the User
                futurePrice = this.get_Price_of_Itemstack(itemStack, amount, false);
                if (futurePrice.succesful) {
                    if (!chestShop.has_money(futurePrice.price)) {
                        result.succesful = false;
                        result.errorMessage = "Shop has not enough money"; // TODO 
                        return result;
                    }
                }else {
                    return futurePrice;
                }
            }
            
            
            playerInventory.removeItem(itemStack);
            // players_Item = itemStack.clone(); // TODO test if this is valide
            players_Item = itemStack;

            CountDownLatch latch = new CountDownLatch(1);
            Item_Node_of_ItemBloodline tree = PrimeShop
                    .get_Tree_of_Itemstack(itemStack);
            Transaction transaction = new Transaction(itemStack, latch, amount, false,
                    false, tree);
            transaction.start();

            try {
                latch.await(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                transaction.transactionWasSuccessful = false;
                transaction.errorMessage = Message_Handler
                        .resolve_to_message(13);
                e.printStackTrace();
            }
            if (!transaction.transactionIsCompleted) {
                transaction.stop();
                result.succesful = false;
                result.errorMessage = Message_Handler.resolve_to_message(13);
                player.sendMessage(Message_Handler.resolve_to_message(13));
                return result;
            }
            if (transaction.transactionWasSuccessful) {
                result.succesful = true;
                result.price = transaction.priceTotal;
            } else {
                result.succesful = false;
                result.errorMessage = transaction.errorMessage;

            }
            if (result.succesful) {
                double sell_price = result.price;
                if (sell_to_an_Chestshop) {
                    sell_price = futurePrice.price;
                    chestShop.withdraw_money(sell_price);
                    chestShop.add_Item_to_Mailbox(itemStack, amount); 
                }                    
                PrimeShop.add_Money_to_Players_Account(player, sell_price);
                if (output_enabled) {
                    player.sendMessage(Message_Handler.resolve_to_message(15,
                            this.convert_price_to_String(sell_price)));
                }
            } else {
                player.getInventory().addItem(players_Item);
                player.sendMessage(result.errorMessage);
            }

        } else {
            // Player hat die Items nicht, die er verkaufen möchte
            result.errorMessage = Message_Handler.resolve_to_message(14);
        }
        notifyAll();
        return result;

    }

    @SuppressWarnings("deprecation")
    public synchronized ReturnPrice get_Price_of_Itemstack(ItemStack itemStack,
            int amount, boolean kaufen) {

        ReturnPrice result = new ReturnPrice();
        if (itemStack == null) {
            return result;
        }
        itemStack.setAmount(amount);
        if (itemStack.getData().getItemTypeId() <= 0) {
            result.errorMessage = "Unknown item";
            return result;
        }
        CountDownLatch latch = new CountDownLatch(1);
//        Item_Node_of_ItemBloodline tree = this.plugin.get_Tree_of_Itemstack(itemStack).clone(null);
        Item_Node_of_ItemBloodline tree = PrimeShop.get_Tree_of_Itemstack(itemStack).clone(null);
        Transaction transaction = new Transaction(itemStack, latch, amount, kaufen, false,
                tree);
        transaction.start();
        try {
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            transaction.transactionWasSuccessful = false;
            transaction.errorMessage = Message_Handler.resolve_to_message(13);
            e.printStackTrace();
        }
        if (!transaction.transactionIsCompleted) {
            transaction.stop();
            result.succesful = false;
            result.errorMessage = Message_Handler.resolve_to_message(13);
            return result;
        }
        if (transaction.transactionWasSuccessful) {
            result.succesful = true;
            result.price = transaction.priceTotal;
        } else {
            result.succesful = false;
            result.errorMessage = transaction.errorMessage;
        }
        notifyAll();
        return result;

    }



    private boolean hasPlayerThisITem(Player p, ItemStack i, int amount) {

        if (p.getInventory().containsAtLeast(i, amount)) {
            return true;
        } else {
            return false;
        }
    }

}
