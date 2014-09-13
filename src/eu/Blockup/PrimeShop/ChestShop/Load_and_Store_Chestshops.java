package eu.Blockup.PrimeShop.ChestShop;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.Other.Message_Handler;

public class Load_and_Store_Chestshops {

	File file;
	FileConfiguration cfg;

	public synchronized boolean read_shop_file() {


		PrimeShop.hashMap_Shops.clear();

		try {
			file = new File("plugins/PrimeShop/", "chestShops.yml"); // TODO
																		// Name
																		// ändern

			cfg = YamlConfiguration.loadConfiguration(file);
			
			
			
			
			// Für alle ChestShops
			for (String cS_ID : cfg.getConfigurationSection("ChestShops").getKeys(false)) {
				String uUID = cfg.getString("ChestShops." + cS_ID + ".UUID");
				Double balance = cfg.getDouble("ChestShops." + cS_ID + ".Balance");
				
				ChestShop cs = new ChestShop(uUID, balance);
				
				
				
				
				ConfigurationSection itemSection;
				
				
				// Für Verkauf
				
				itemSection = cfg.getConfigurationSection("ChestShops." + cS_ID + ".Disposals");

				for (String aID : itemSection.getKeys(false)) {
					int amount = cfg.getInt("ChestShops." + cS_ID + ".Disposals." + aID + ".Amount");
					ItemStack item = cfg.getItemStack("ChestShops." + cS_ID + ".Disposals." + aID + ".Item");
					cs.add_Item_to_Verkaufen(item, amount);
				}
				
				// Für Ankauf
				
				itemSection = cfg.getConfigurationSection("ChestShops." + cS_ID + ".Acquisitions");

				for (String aID : itemSection.getKeys(false)) {
					int amount = cfg.getInt("ChestShops." + cS_ID + ".Acquisitions." + aID + ".Amount");
					ItemStack item = cfg.getItemStack("ChestShops." + cS_ID + ".Acquisitions." + aID + ".Item");
					cs.add_Item_to_Ankauf(item, amount);
				}
				
				// Für Mailbox
				
				itemSection = cfg.getConfigurationSection("ChestShops." + cS_ID + ".Mailbox");

				for (String aID : itemSection.getKeys(false)) {
					int amount = cfg.getInt("ChestShops." + cS_ID + ".Mailbox." + aID + ".Amount");
					ItemStack item = cfg.getItemStack("ChestShops." + cS_ID + ".Mailbox." + aID + ".Item");
					cs.add_Item_to_Mailbox(item, amount);
				}
				
				
				PrimeShop.hashMap_Chest_Shops.put(uUID, cs);
			}
			
			
			
			
			
//			
//			
//
//			for (String shopname : cfg.getConfigurationSection("").getKeys(
//					false)) {
//
//				if (shopname.equalsIgnoreCase("Version")) continue;
//				
//				String displayiconid = cfg.getString(shopname
//						+ ".DisplayIconID");
//
//				ItemStack displayIcon = PrimeShop.convertItemIdStringToItemstack(displayiconid, 1);
//
//				Shop shop = new Shop(shopname, displayIcon);
//
//				if (cfg.contains(shopname + ".Items")) {
//
//					ConfigurationSection itemSection = cfg
//							.getConfigurationSection(shopname + ".Items");
//
//					for (String itemID : itemSection.getKeys(false)) {
////						System.out.println("Item Added: " + itemID);
//						ItemStack loadedItemSack = cfg.getItemStack(shopname
//								+ ".Items." + itemID);
////						System.out.println(loadedItemSack.toString());
//						shop.add_ItemStack(loadedItemSack);
//					}
//				}
//				List<Integer> list_with_shopkeeperIDs = cfg.getIntegerList(shopname + ".NPC_Shopkeeper_IDs");
//				
//				for (int shopkeeperID : list_with_shopkeeperIDs) {
//					PrimeShop.plugin.add_Shop_to_NPC(shop, shopkeeperID);
//				}
//				
////				System.out.println("Adde Shop to Map : " + shopname);
//				shop.shopname = shopname;
//				PrimeShop.hashMap_Shops.put(shopname, shop);
//			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			PrimeShop.plugin.getLogger().log(Level.SEVERE, ChatColor.RED + "error reading chestShops.yml");
			PrimeShop.plugin.getLogger().log(Level.SEVERE, ChatColor.RED + (Message_Handler.resolve_to_message(1)));
			e1.printStackTrace();
			return false;
		}

		return true; // Todo Error handling
	}

	public synchronized boolean write_shops_to_Harddisk() {

		file = new File("plugins/PrimeShop/", "chestShops.yml"); // TODO Name
																	// ändern
//		file.delete();
		
		cfg = YamlConfiguration.loadConfiguration(file);
		
		
		
//		// Delete all Shops from File
//		ConfigurationSection itemSection = cfg.getConfigurationSection("");
		
		
		cfg.options().copyDefaults(true);
		
		int cS_ID = 0;
		for (ChestShop chestShop : PrimeShop.hashMap_Chest_Shops.values()) {
			cS_ID ++;
			
			// UUID
			cfg.set("ChestShops." + cS_ID + ".UUID", chestShop.get_UUID());
			
			// Balance
			cfg.set("ChestShops." + cS_ID + ".Balance", chestShop.get_Balance());
			
			// Verkauf
			int verkauf_ID = 0;
			for (Item_Supply item_Verkauf : chestShop.list_Verkauf) {
				verkauf_ID++;
				
				cfg.set("ChestShops." + cS_ID + ".Disposals." + verkauf_ID + ".Item", item_Verkauf.getItemStack());
				cfg.set("ChestShops." + cS_ID + ".Disposals." + verkauf_ID + ".Amount", item_Verkauf.getAmount());
				
			}
			
			// Ankauf purchase
			int ankauf_ID = 0;
			for (Item_Supply item_Ankauf : chestShop.list_Ankauf) {
				ankauf_ID++;
				cfg.set("ChestShops." + cS_ID + ".Acquisitions." + ankauf_ID + ".Item", item_Ankauf.getItemStack());
				cfg.set("ChestShops." + cS_ID + ".Acquisitions." + ankauf_ID + ".Amount", item_Ankauf.getAmount());
				
			}
			
			// Mailbox
			int mailbox_ID = 0;
			for (Item_Supply item_Mailbox : chestShop.list_Mailbox) {
				mailbox_ID++;
				
				cfg.set("ChestShops." + cS_ID + ".Mailbox." + mailbox_ID + ".Item", item_Mailbox.getItemStack());
				cfg.set("ChestShops." + cS_ID + ".Mailbox." + mailbox_ID + ".Amount", item_Mailbox.getAmount());
				
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