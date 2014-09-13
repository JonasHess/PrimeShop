package eu.Blockup.PrimeShop.Commands;


import java.util.ArrayList;
import java.util.List;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.Listeners.NPC_Click_Listener;

public class Command_Registrer {

	
	public static void register_Command_Listeners (PrimeShop plugin) {
		
		
		plugin.getCommand("primeshop").setExecutor(new PrimeShop_Command(plugin));
		
		
		pBuy_Command pbuy = new pBuy_Command();
		
		
		plugin.getCommand("pBuy").setExecutor(pbuy);
		plugin.getCommand("pSell").setExecutor(pbuy);
		plugin.getCommand("value").setExecutor(new pValue_Command());
		plugin.getCommand("pSellAll").setExecutor(new sellall_Command());
		plugin.getCommand("prices").setExecutor(new prices_command());
		plugin.getCommand("PlayerShop").setExecutor(new PlayerShop_Commands());
	
		
	}
}
