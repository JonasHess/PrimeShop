package eu.Blockup.PrimeShop.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.PricingEngine.Price_Sorter;

class prices_command implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		
		
		if (args.length > 0) {
			// TOP
			
			if (args[0].equalsIgnoreCase("top") || args[0].equalsIgnoreCase("most") || args[0].equalsIgnoreCase("highest") || args[0].equalsIgnoreCase("high")) {
				
				if (!PrimeShop.has_player_Permission_for_this_Command(cs, "PrimeShop.VIP.seeListOfMostExpensiveItems")) {
				  cs.sendMessage(Message_Handler.resolve_to_message(104));
				  return true;
			  	}
				for (int i = 0; i < 3; i++) {
					cs.sendMessage("");
				}
				cs.sendMessage(ChatColor.GRAY + Message_Handler.resolve_to_message(144));
				cs.sendMessage(ChatColor.GRAY + Message_Handler.resolve_to_message(142));
				for (String message : Price_Sorter.get_Sortet_List(10, false)) {
					cs.sendMessage(message);
				}
				return true;
			}
			
			// buttom
			if (args[0].equalsIgnoreCase("bottom") || args[0].equalsIgnoreCase("cheapest") || args[0].equalsIgnoreCase("ground") || args[0].equalsIgnoreCase("floor") || args[0].equalsIgnoreCase("soil") || args[0].equalsIgnoreCase("least") || args[0].equalsIgnoreCase("lowest") || args[0].equalsIgnoreCase("low") ) {
			  
				if (!PrimeShop.has_player_Permission_for_this_Command(cs, "PrimeShop.VIP.seeListOfCeapestItems")) {
	              cs.sendMessage(Message_Handler.resolve_to_message(104));
	              return true;
	            }
				for (int i = 0; i < 3; i++) {
					cs.sendMessage("");
				}
				cs.sendMessage(ChatColor.GRAY + Message_Handler.resolve_to_message(145));
				cs.sendMessage(ChatColor.GRAY + Message_Handler.resolve_to_message(143));
				for (String message : Price_Sorter.get_Sortet_List(10, true)) {
					cs.sendMessage(message);
				}
				return true;
			}
		}
		for (int i = 0; i < 3; i++) {
			cs.sendMessage("");
		}
		cs.sendMessage(ChatColor.GRAY + "                         Prices");
		cs.sendMessage(" - " + ChatColor.GOLD + "top");
		cs.sendMessage(" - " + ChatColor.GOLD + "floor");
		return true;
	}
	
	
}
