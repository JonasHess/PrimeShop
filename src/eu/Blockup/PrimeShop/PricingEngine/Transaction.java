package eu.Blockup.PrimeShop.PricingEngine;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.PricingEngine.Enchantments.EnchantmentHandler;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.Item_Node_of_ItemBloodline;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.ReturnObjects.ReturnPrice;

public class Transaction extends Thread{
    // TODO add Player
    private double amount;
    public double priceTotal;
    private boolean Kaufen;
    public boolean transactionWasSuccessful;
    public boolean transactionisCompleted = false;
    public String errorMessage;
    private boolean save_priceChanges_to_Backend;
    private Item_Node_of_ItemBloodline tree;
    private CountDownLatch latch;
    private ItemStack itemstack;



    public Transaction(ItemStack itemstack, CountDownLatch latch, double amount, boolean kaufen, boolean save_priceChanges_to_Backend, Item_Node_of_ItemBloodline tree) {
	super();
	this.itemstack = itemstack;
	this.latch =latch; 
	this.amount = amount;
	Kaufen = kaufen;
	this.save_priceChanges_to_Backend = save_priceChanges_to_Backend;
	this.tree = tree;
    }



    
    private double get_price_for_enchantments() {
        //is this supposed to get the total price of _all_ enchantments?
    	double result = 0;
    	
    	Map<Enchantment, Integer> map = itemstack.getEnchantments();
    	
		for (Enchantment enchantment : map.keySet()) {
			
//			result += EnchantmentHandler.getPrice(enchantment.getId(), map.get(enchantment));
			result += EnchantmentHandler.getPrice(enchantment.getName() , map.get(enchantment));
		}
    	
    	return result;
    }
    
    
    private void startTransactione() {
    	
	ReturnPrice result = this.tree.buy_this_item(this.Kaufen, this.amount, this.save_priceChanges_to_Backend);
	if (result != null) {
	    this.priceTotal = result.price;
	    this.priceTotal += get_price_for_enchantments();
	    if (this.Kaufen) {
	    	this.priceTotal = this.priceTotal * Cofiguration_Handler.value_every_item_bought_gets_multiplied_with;	    	
	    }else {
	    	this.priceTotal = this.priceTotal * Cofiguration_Handler.value_every_item_sold_gets_multiplied_with;	    	
	    }
	    this.priceTotal = this.priceTotal * Cofiguration_Handler.globalRateOfInflation;
	    this.transactionWasSuccessful = result.succesful;
	    this.errorMessage = result.errorMessage;
	    this.transactionisCompleted = true;
	} else {
	    this.errorMessage = Message_Handler.resolve_to_message(13);
	    this.transactionWasSuccessful = false;
	    this.transactionisCompleted = true;  
	}
	this.latch.countDown();
	
    }

    @Override
    public void run() {
	this.startTransactione();
    }
    

}
