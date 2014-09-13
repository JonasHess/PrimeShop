package eu.Blockup.PrimeShop.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.Interface_Buy_Sell_Item;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.PricingEngine.Item_Trader;
import eu.Blockup.PrimeShop.PricingEngine.Pool_of_Item_Traders;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.ReturnObjects.ReturnPrice;

public class sellall_Command implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {

		// Sell / Buy / changePrice
		
		if (!(cs instanceof Player)) {
			cs.sendMessage(ChatColor.RED
					+ Message_Handler.resolve_to_message(27));
			return true;
		}

		Player p = (Player) cs;

		
		if (!PrimeShop.has_player_Permission_for_this_Command(p,"PrimeShop.VIP.sellEntireIventory")) {			
			p.sendMessage(Message_Handler.resolve_to_message(104));
			return true;
		}

		if (args.length < 1) {
			p.sendMessage(Message_Handler.resolve_to_message(135));
			p.sendMessage(Message_Handler.resolve_to_message(136, " /pSellAll confirm"));
			return true;
		}
		
		if (!args[0].equalsIgnoreCase("confirm")) {
			p.sendMessage(Message_Handler.resolve_to_message(135));
			p.sendMessage(Message_Handler.resolve_to_message(136, " /pSellAll confirm"));
			return true;
		}
		
		double totalPrice = 0;
		for (ItemStack item_to_be_added : p.getInventory()) {  // doppelter zugriff?
			
			if (item_to_be_added != null) {
				Item_Trader itemTrader = Pool_of_Item_Traders.get_ItemTrader();

				ReturnPrice resultPrice = itemTrader.sell_ItemStack(item_to_be_added, item_to_be_added.getAmount(), p, false);
				
				Pool_of_Item_Traders.return_Item_Trader(itemTrader);

				if (resultPrice.succesful) {
//					String itemName = PrimeShop
//							.convert_IemStack_to_DisplayName(item_to_be_added);
//					String priceString = PrimeShop.economy
//							.format(resultPrice.price);
//					cs.sendMessage(Message_Handler.resolve_to_message(111,
//							(String.valueOf(amount)), itemName, priceString));
//					return true;
					totalPrice += resultPrice.price;
				} else {
					cs.sendMessage("Internal Error occurt. Please contact a moderator");
				}
			}
			
		}
		p.sendMessage(Message_Handler.resolve_to_message(137, PrimeShop.economy.format(totalPrice)));
		
		
//		ItemStack item_to_be_added = PrimeShop
//				.convert_random_String_to_ItemStack(args[0], cs);
//
//		if (item_to_be_added != null) {
//
//			boolean has_permission = PrimeShop.plugin
//					.has_Player_Permission_for_this_Item(p, item_to_be_added);
//
//			if (!has_permission) {
//				p.sendMessage(Message_Handler.resolve_to_message(106));
//				return true;
//			}
//			has_permission = false;
//			Shop shop = null;
//			Shop lastshop_with_access_to = null;
//			for (Shop value : PrimeShop.hashMap_Shops.values()) {
//				if (PrimeShop.has_player_Permission_for_this_Command(p,
//						"PrimeShop.Defaults.openShop." + value.shopname)) {
//					lastshop_with_access_to = value;
//					for (ItemStack item : value.listOfItems) {
//						if (item.isSimilar(item_to_be_added)) {
//							has_permission = true;
//							shop = value;
//						}
//					}
//				}
//			}
//			if ((PrimeShop.has_player_Permission_for_this_Command(p,"PrimeShop.VIP.canBuySellAllItemsRegardlessIfTheyWereAddedToAShop") && (!has_permission))
//					|| (PrimeShop
//							.has_player_Permission_for_this_Command(p,
//									"PrimeShop.admin.changePrices"))) {
//
//				// if
//				// (p.hasPermission("PrimeShop.VIP.canBuySellAllItemsRegardlessIfTheyWereAddedToAShop")
//				// && (!has_permission)) {
//				p.sendMessage(Message_Handler.resolve_to_message(107));
//				has_permission = true;
//				shop = lastshop_with_access_to;
//			}
//
//			if (!has_permission) {
//				p.sendMessage(Message_Handler.resolve_to_message(108));
//			} else {
//				
//				int amount = 1;
//				if (args.length >= 2) {
//					if (PrimeShop.isThisStringNumeric(args[1])) {
//						amount = Integer.valueOf(args[1]);
//						if (amount < 1) {
//							amount = 1;
//						}
//						if (amount > item_to_be_added.getMaxStackSize()) {
//							amount = item_to_be_added.getMaxStackSize();
//						}
//					}
//				} 
//				PrimeShop.open_InventoyInterface(p,
//						new Interface_Buy_Sell_Item(null, p, shop,
//								item_to_be_added, amount, false)); // TODO
//																// amount
//			}
//		}

		return true;
	}

}