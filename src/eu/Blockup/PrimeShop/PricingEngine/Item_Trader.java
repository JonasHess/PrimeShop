package eu.Blockup.PrimeShop.PricingEngine;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.milkbowl.vault.economy.Economy;
import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.Item_Node_of_ItemBloodline;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.ReturnObjects.ReturnPrice;

import org.bukkit.inventory.PlayerInventory;

public class Item_Trader {

	private PrimeShop plugin;
	private Economy economy;

	public Item_Trader(PrimeShop plugin) {
		this.plugin = plugin;
		this.economy = PrimeShop.getEconomy();
	}

	private String convert_price_to_String(double price) {
		return economy.format(price);
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
				if (this.economy.has(player.getName(), futureprice.price)) {
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
					if (!transaction.transactionisCompleted) {
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
			this.economy.withdrawPlayer(player.getName(), finalPrice);
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
			int amount, Player player, boolean output_enabled) {

		ReturnPrice result = new ReturnPrice();
		
		if (!PrimeShop.plugin.has_Player_Permission_for_this_Item(player, itemStack)) {
			result.succesful = false;
			result.errorMessage = Message_Handler.resolve_to_message(32);
			return result;
		}
		
		result.succesful = false;
		ItemStack players_Item = null;
		PlayerInventory playerInventory = player.getInventory();

		if (itemStack == null) {
			result.errorMessage = Message_Handler.resolve_to_message(12);
			return result;
		}
		itemStack.setAmount(amount);
		if (itemStack.getData().getItemTypeId() <= 0) {
			result.errorMessage = Message_Handler.resolve_to_message(12);
			return result;
		}
		// if (playerInventory.contains(itemStack)) {
		if (hasPlayerThisITem(player, itemStack, amount)) {
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
			if (!transaction.transactionisCompleted) {
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
				this.economy.depositPlayer(player.getName(), result.price);
				if (output_enabled) {
					player.sendMessage(Message_Handler.resolve_to_message(15,
							this.convert_price_to_String(result.price)));
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
		Item_Node_of_ItemBloodline tree = this.plugin.get_Tree_of_Itemstack(
				itemStack).clone(null);
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
		if (!transaction.transactionisCompleted) {
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



	public boolean hasPlayerThisITem(Player p, ItemStack i, int amount) {

		if (p.getInventory().containsAtLeast(i, amount)) {
			return true;
		} else {
			return false;
		}
		// int item_count = 0;
		// ItemStack[] inv = p.getInventory().getContents();
		// for (ItemStack item : inv) {
		// if (item != null) {
		//
		// if (Item_Comparer.do_Items_match_typ_data_enchantments_meta(
		// item, i)) {
		//
		// // if (item.getType().equals(i.getType())) {
		// // if (item.getData().getData() == i.getData().getData()){
		// // // if (item.getEnchantments().containsKey(e)) {
		// // // if (item.getEnchantmentLevel() == enchantmentLevel) {
		// item_count = item_count + item.getAmount();
		//
		// }
		// // }
		//
		// }
		//
		// }
		//
		// if (item_count < amount) {
		// return false;
		// } else {
		// return true;
		// }

	}

}
