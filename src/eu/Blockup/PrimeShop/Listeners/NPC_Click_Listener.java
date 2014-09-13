package eu.Blockup.PrimeShop.Listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.Interface_Collection_of_Shops;
import eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.Interface_Shop_Page;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.Shops.Shop;
import net.citizensnpcs.api.event.NPCLeftClickEvent;



public class NPC_Click_Listener  implements Listener {
	
	public NPC_Click_Listener () {
	PrimeShop.plugin.getServer().getPluginManager().registerEvents(this, PrimeShop.plugin);
	}
	
	@EventHandler
	public void sdf(NPCLeftClickEvent event) {
		List<Shop> list_of_shops = PrimeShop.plugin.get_List_of_Shops_from_NPC(event.getNPC().getId());
		
		if (list_of_shops == null) {
			return;
		} 
		if (list_of_shops.size() != 0) {
		if (!PrimeShop.has_player_Permission_for_this_Command(event.getClicker(), "PrimeShop.Defaults.interactWithNPCs")) {	
			event.getClicker().sendMessage(Message_Handler.resolve_to_message(129));
			return;
		}
		
		if (list_of_shops.size() == 1) {
			Shop shop = list_of_shops.get(0);
			List<InventoryInterface> list= new ArrayList<InventoryInterface>();
			list.add(new Interface_Collection_of_Shops(event.getClicker(), event.getNPC().getFullName(), list_of_shops, null, event.getNPC().getId(), true));		
			PrimeShop.open_InventoyInterface(event.getClicker(), new Interface_Shop_Page(list, event.getClicker(), shop, 1));
		} else {
			PrimeShop.open_InventoyInterface(event.getClicker(), new Interface_Collection_of_Shops(event.getClicker(), event.getNPC().getFullName(), list_of_shops, null, event.getNPC().getId(), true));
		}
		}
	}
}