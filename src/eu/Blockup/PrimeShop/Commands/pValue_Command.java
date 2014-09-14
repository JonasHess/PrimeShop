package eu.Blockup.PrimeShop.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.PricingEngine.Item_Trader;
import eu.Blockup.PrimeShop.PricingEngine.Pool_of_Item_Traders;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.ReturnObjects.ReturnPrice;

class pValue_Command implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {

		// Value | Price

		if (!PrimeShop.has_player_Permission_for_this_Command(cs,
				"PrimeShop.Defaults.canUseCommandToGetPriceOfItem")) {
			cs.sendMessage(Message_Handler.resolve_to_message(109));
			return true;
		}

		if (args.length < 1) {
			cs.sendMessage(Message_Handler.resolve_to_message(105));
			cs.sendMessage("/value <itemname> <amount>");
			return true;
		}

		int amount = 1;
		if (args.length >= 2) {
			String stringAmount = args[1];
			if (PrimeShop.isThisStringNumeric(stringAmount)) {
				amount = (int) Integer.valueOf(stringAmount);
				if (amount > 200)
					amount = 200;
				if (amount < 1)
					amount = 1;
			}
		}

		ItemStack item_to_be_added = PrimeShop
				.convert_random_String_to_ItemStack(args[0], cs);

		if (item_to_be_added != null) {
			Item_Trader itemTrader = Pool_of_Item_Traders.get_ItemTrader();

			ReturnPrice resultPrice = itemTrader.get_Price_of_Itemstack(
					item_to_be_added, amount, true);
			Pool_of_Item_Traders.return_Item_Trader(itemTrader);

			if (resultPrice.succesful) {
				String itemName = PrimeShop
						.convert_IemStack_to_DisplayName(item_to_be_added);
				String priceString = PrimeShop.economy
						.format(resultPrice.price);
				cs.sendMessage(Message_Handler.resolve_to_message(111,
						(String.valueOf(amount)), itemName, priceString));
				return true;
			} else {
				cs.sendMessage("Internal Error occurt. Please contact a moderator");
			}
		}

		return true;
	}

}
