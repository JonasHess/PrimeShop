package eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_close_Interface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_with_no_task;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.Shops.Shop;


public class Interface_Collection_of_Shops extends InventoryInterface{

	final public int collectionID;
	final public boolean npc;
	final public List<Shop> list_of_Shops;
	
	public Interface_Collection_of_Shops(Player player, String title, final List<Shop> list_of_shops, final List<InventoryInterface> Stack_list, final int collectionID, boolean npc) {
		super(title, 3, Stack_list);
		this.setCloseable(false);
		this.collectionID = collectionID;
		this.npc = npc;
		this.list_of_Shops = list_of_shops;

		for (int a = 0; a < this.getWidth(); a++) {
			for (int b = 0; b < this.getHeight(); b++) {
				this.addOption(a, b, new Button_with_no_task(Cofiguration_Handler.background_ItemStack, " "));
			}
		}
		
		int itemsAddedItems = 0;
		int amount_of_items = list_of_shops.size();
		
//		this.addOption(4, 0, new EmptyOption(list_of_shops.get(itemsAddedItems).displayIcon,
//				shop.shopname));

		for (int y = 1; y < 2; y++) {
			for (int x = 0; x < 9; x++) {

				
				if (itemsAddedItems < amount_of_items) {


					Shop shop = list_of_shops.get(itemsAddedItems);
					this.addOption(x, y, new Button(shop.displayIcon, shop.shopname, Message_Handler.resolve_to_message(66)) {

						@SuppressWarnings("deprecation")
						@Override
						public void onClick(InventoryInterface inventoryInterface, Player player,
								ItemStack cursor, ItemStack current,
								ClickType type) {
							
							
							ItemStack itemA = current;
							ItemStack itemB = null;
							boolean match = false;
							
							Shop shop_to_be_opened = null;
							for (Shop s : list_of_shops) {
								itemB = s.displayIcon;
							
								if (itemA.getType() == itemB.getType()) {
									if (itemA.getData().getData() == itemB.getData().getData()) {
										match = true;
										Map<Enchantment, Integer> map = itemA.getEnchantments();

										for (Enchantment key : map.keySet()) {
										    if (!itemB.containsEnchantment(key)) {
										    	match = false;
										    	break;
										    }
										}
										if (match) {
											shop_to_be_opened = s;
										}
									}
								}
							}
							
							if (match) {
								// shop gefunden
								PrimeShop.close_InventoyInterface(player);
								PrimeShop.open_InventoyInterface(player, new Interface_Shop_Page(inventoryInterface.branch_back_Stack, player, shop_to_be_opened, 1));
							} else {
								PrimeShop.plugin.getLogger().log(Level.SEVERE, "Fatal Error finding correct Shop");
								player.sendMessage("Error: 568 ; Please report this special error to the developer of PrimeShop");
							}
						}
					});
					itemsAddedItems++;

				}
			}
		}
		
		// Close Option
		this.addOption(8, 0, new Button_close_Interface());
				
		

				
				
	}

	
}
