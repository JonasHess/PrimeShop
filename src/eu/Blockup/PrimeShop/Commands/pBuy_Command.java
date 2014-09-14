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
import eu.Blockup.PrimeShop.Shops.Shop;

class pBuy_Command implements CommandExecutor {

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

		if (!p.hasPermission("PrimeShop.VIP.useTheBuy_SellCommand")) {
			p.sendMessage(Message_Handler.resolve_to_message(104));
			return true;
		}

		if (args.length < 1) {
			p.sendMessage(Message_Handler.resolve_to_message(105));
			p.sendMessage("pbuy <itemname>");
			return true;
		}

		ItemStack item_to_be_added = PrimeShop
				.convert_random_String_to_ItemStack(args[0], cs);

		if (item_to_be_added != null) {

			boolean has_permission = PrimeShop.plugin
					.has_Player_Permission_for_this_Item(p, item_to_be_added);

			if (!has_permission) {
				p.sendMessage(Message_Handler.resolve_to_message(106));
				return true;
			}
			has_permission = false;
			Shop shop = null;
			Shop lastshop_with_access_to = null;
			for (Shop value : PrimeShop.hashMap_Shops.values()) {
				if (PrimeShop.has_player_Permission_for_this_Command(p,
						"PrimeShop.Defaults.openShop." + value.shopname)) {
					lastshop_with_access_to = value;
					for (ItemStack item : value.listOfItems) {
						if (item.isSimilar(item_to_be_added)) {
							has_permission = true;
							shop = value;
						}
					}
				}
			}
			if ((PrimeShop
					.has_player_Permission_for_this_Command(p,
							"PrimeShop.VIP.canBuySellAllItemsRegardlessIfTheyWereAddedToAShop") && (!has_permission))
					|| (PrimeShop
							.has_player_Permission_for_this_Command(p,
									"PrimeShop.admin.changePrices"))) {

				// if
				// (p.hasPermission("PrimeShop.VIP.canBuySellAllItemsRegardlessIfTheyWereAddedToAShop")
				// && (!has_permission)) {
				p.sendMessage(Message_Handler.resolve_to_message(107));
				has_permission = true;
				shop = lastshop_with_access_to;
			}

			
			if (!has_permission) {
				p.sendMessage(Message_Handler.resolve_to_message(108));
			} else {
				
				int amount = 1;
				if (args.length >= 2) {
					if (PrimeShop.isThisStringNumeric(args[1])) {
						amount = Integer.valueOf(args[1]);
						if (amount < 1) {
							amount = 1;
						}
						if (amount > item_to_be_added.getMaxStackSize()) {
							amount = item_to_be_added.getMaxStackSize();
						}
					}
				} 
				
				boolean shop_is_null = false;
				if (shop == null) shop_is_null =  true;
				
				PrimeShop.open_InventoyInterface(p,
						new Interface_Buy_Sell_Item(null, p, shop,
								item_to_be_added, amount, false, shop_is_null)); // TODO
																// amount
			}
		}

		return true;
	}

}
