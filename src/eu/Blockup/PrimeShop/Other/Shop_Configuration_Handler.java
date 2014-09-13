package eu.Blockup.PrimeShop.Other;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.Shops.Shop;

public class Shop_Configuration_Handler {
	File file;
	FileConfiguration cfg;

	public synchronized boolean read_shop_file() {


		PrimeShop.hashMap_Shops.clear();

		try {
			file = new File("plugins/PrimeShop/", "shops.yml"); // TODO
																		// Name
																		// ändern

			cfg = YamlConfiguration.loadConfiguration(file);

			for (String shopname : cfg.getConfigurationSection("").getKeys(
					false)) {

				if (shopname.equalsIgnoreCase("Version")) continue;
				
				String displayiconid = cfg.getString(shopname
						+ ".DisplayIconID");

				ItemStack displayIcon = PrimeShop.convertItemIdStringToItemstack(displayiconid, 1);

				Shop shop = new Shop(shopname, displayIcon);

				if (cfg.contains(shopname + ".Items")) {

					ConfigurationSection itemSection = cfg
							.getConfigurationSection(shopname + ".Items");

					for (String itemID : itemSection.getKeys(false)) {
//						System.out.println("Item Added: " + itemID);
						ItemStack loadedItemSack = cfg.getItemStack(shopname
								+ ".Items." + itemID);
//						System.out.println(loadedItemSack.toString());
						shop.add_ItemStack(loadedItemSack);
					}
				}
				List<Integer> list_with_shopkeeperIDs = cfg.getIntegerList(shopname + ".NPC_Shopkeeper_IDs");
				
				for (int shopkeeperID : list_with_shopkeeperIDs) {
					PrimeShop.plugin.add_Shop_to_NPC(shop, shopkeeperID);
				}
				
//				System.out.println("Adde Shop to Map : " + shopname);
				shop.shopname = shopname;
				PrimeShop.hashMap_Shops.put(shopname, shop);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			PrimeShop.plugin.getLogger().log(Level.SEVERE, ChatColor.RED + "error reading shops.yml");
			PrimeShop.plugin.getLogger().log(Level.SEVERE, ChatColor.RED + (Message_Handler.resolve_to_message(1)));
			e1.printStackTrace();
			return false;
		}

		return true; // Todo Error handling
	}

	public synchronized boolean write_shops_to_Harddisk() {

		file = new File("plugins/PrimeShop/", "shops.yml"); // TODO Name
																	// ändern
//		file.delete();
		
		cfg = YamlConfiguration.loadConfiguration(file);
		
		
		
		// Delete all Shops from File
		ConfigurationSection itemSection = cfg.getConfigurationSection("");
		for (String shopname : itemSection.getKeys(false)) {
			
			cfg.set(shopname, null);
		}
		
		cfg.addDefault("Version", (Double)1.0);
		cfg.options().copyDefaults(true);
		
		// cfg.createSection("Shops");
		for (String key : PrimeShop.hashMap_Shops.keySet()) {

			Shop shop = PrimeShop.hashMap_Shops.get(key);

			// cfg.createPath(cfg.getConfigurationSection("Shops"),
			// shop.shopname);
			cfg.set(shop.shopname + ".DisplayIconID", PrimeShop
					.convertItemStacktoToIdString(shop.displayIcon));
			
			// Shopkeeper IDs
			List<Integer> list_with_shopkeepers= new ArrayList<Integer>();
			
			
			Map<Integer, List<Shop>> map = PrimeShop.hashMap_CitizensNPCs;

			for (Integer shopkeeperID : map.keySet()) {
				List<Shop> list_with_shops= map.get(shopkeeperID);
				
				for (Shop s : list_with_shops) {
					if (s.shopname.matches(shop.shopname)) {
		        		list_with_shopkeepers.add(shopkeeperID);
		        	}
				}
			}
		    
		    cfg.set(shop.shopname + ".NPC_Shopkeeper_IDs", list_with_shopkeepers);
				
		    
			
		    // Items
		    
			for (Iterator<ItemStack> iter = shop.listOfItems.iterator(); iter
					.hasNext();) {
				ItemStack item = iter.next();
				boolean itemWasPlaced = false;
				int id = 0;
				while (!itemWasPlaced) {
					id++;
					if (!(cfg.contains(shop.shopname + ".Items." + id))) {
						itemWasPlaced = true;
						cfg.set(shop.shopname + ".Items." + id, item);
					}
				}
			}

		}

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			PrimeShop.plugin.getLogger().log(Level.SEVERE, ChatColor.RED + (Message_Handler.resolve_to_message(1)));
			return false;
		}
		return true;
	}

}