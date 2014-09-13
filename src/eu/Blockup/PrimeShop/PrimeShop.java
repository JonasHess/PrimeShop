package eu.Blockup.PrimeShop;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;
import net.minecraft.util.org.apache.commons.io.FileUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import eu.Blockup.PrimeShop.ChestShop.ChestShop;
import eu.Blockup.PrimeShop.Commands.Command_Registrer;
import eu.Blockup.PrimeShop.Databse.DatabaseIntersection;
import eu.Blockup.PrimeShop.Databse.Database_FLATFILE;
import eu.Blockup.PrimeShop.Databse.Item_Saver;
import eu.Blockup.PrimeShop.Databse.PreRender_all_Items_in_Shops;
import eu.Blockup.PrimeShop.Databse.DatabaseIntersection.DatabseTyp;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Listeners.SectionListener;
import eu.Blockup.PrimeShop.Metrics.Metrics;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.CooldownManager;
import eu.Blockup.PrimeShop.Other.Item_Comparer;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.Other.Shop_Configuration_Handler;
import eu.Blockup.PrimeShop.PricingEngine.Item_Trader;
import eu.Blockup.PrimeShop.PricingEngine.DataHandling.Custom_Price_Links;
import eu.Blockup.PrimeShop.PricingEngine.DataHandling.EvaluatedRecipe;
import eu.Blockup.PrimeShop.PricingEngine.DataHandling.Shop_Item;
import eu.Blockup.PrimeShop.PricingEngine.DataHandling.Shop_Item_Stack;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.Item_Node_of_ItemBloodline;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.Recepie_Examiner;
import eu.Blockup.PrimeShop.Shops.Shop;

public class PrimeShop extends JavaPlugin {
	
	public static PrimeShop plugin;
	public static DatabaseIntersection databaseHandler;
	public static HashMap<String, Shop_Item> hashMap_SQL_Item;
	public static HashMap<String, List<EvaluatedRecipe>> hashMap_EvaluatedRecipe;
	public static HashMap<String, Item_Node_of_ItemBloodline> hashMap_Item_Node_of_ItemBloodline;
	
	public static HashMap<String, ChestShop> hashMap_Chest_Shops;
	private Recepie_Examiner recepie_Examiner;
	public Item_Trader itemTrader;
	public static Economy economy = null;
	public Cofiguration_Handler cofiguration_Handler;
	public static HashMap<String, Shop> hashMap_Shops;
	public static List<String> list_of_deaktivated_PriceLinks;
	public static HashMap<Integer, List<Shop>> hashMap_CitizensNPCs;
	
	public static int permissionGroupMaximum = 100;
	public static int permissionGroupMinimum = 1;

	public static Map<String, InventoryInterface> hashMap_InventoryInterfaces;
	public static Map<String, Inventory> hashMap_InventorySessions;
	static Map<String, ItemStack> hashMap_Inventory_handSave;
//	private NPC_Click_Listener citizensClickListener;
//	private Sign_Click_Listener signClickListener;
	public static Shop_Configuration_Handler shopConfigHandler;
	public CooldownManager cooldownManager; 
	
	public static boolean citezens_is_enabled;
	


	@Override
	public void onEnable() {

		PrimeShop.plugin = this;
		this.getDescription();

		
		// Copy Configfiles to /Plugin/PrimeShop  
		copy_default_config_Files_to_Plugin_dir("custom_price_links.yml");
		copy_default_config_Files_to_Plugin_dir("enchantments.yml");
		copy_default_config_Files_to_Plugin_dir("items.yml");
		copy_default_config_Files_to_Plugin_dir("config.yml");
		
	
		
		// Lade Citizens
		if(getServer().getPluginManager().getPlugin("Citizens") == null || getServer().getPluginManager().getPlugin("Citizens").isEnabled() == false) {
			getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled.  NPCs will not be supported");
			citezens_is_enabled = false;
//			getServer().getPluginManager().disablePlugin(this);	
//			return;
		} else {
			// Citizens ClickListener
			citezens_is_enabled = true;
//			citizensClickListener = new NPC_Click_Listener();
		}
		
		
		
		// Sign Listener
//		signClickListener = new Sign_Click_Listener();
		

		// Initialisiere die SQL Items und Rezept Datenbanken
		list_of_deaktivated_PriceLinks = new ArrayList<String>();
		hashMap_SQL_Item = new HashMap<String, Shop_Item>();
		hashMap_EvaluatedRecipe = new HashMap<String, List<EvaluatedRecipe>>();
		hashMap_CitizensNPCs = new HashMap<Integer, List<Shop>>();
		hashMap_Item_Node_of_ItemBloodline = new HashMap<String, Item_Node_of_ItemBloodline>();
		hashMap_InventoryInterfaces = new HashMap<String, InventoryInterface>();
		hashMap_InventorySessions = new HashMap<String, Inventory>();
		hashMap_Inventory_handSave = new HashMap<String, ItemStack>();
		hashMap_Chest_Shops = new HashMap<String, ChestShop>();
		
		recepie_Examiner = new Recepie_Examiner(this);
		itemTrader = new Item_Trader(this);

		// Setup Custom Price Links
		Custom_Price_Links.load_Custom_Price_Links();

		// Lese die Language File in den RAM
		hashMap_Shops = new HashMap<String, Shop>();
		if (!Message_Handler.initialize_language_support()) {
			return;
		}

		// Lese die Shops in den RAM;
		shopConfigHandler = new Shop_Configuration_Handler();
		// shopConfigHandler.write_shops_to_Harddisk();
		shopConfigHandler.read_shop_file();

		// Lade Vault
		if (!this.setupEconomy()) {
			// TODO Disable plugin
			getLogger().log(Level.SEVERE, Message_Handler.resolve_to_message(2));
			this.getPluginLoader().disablePlugin(this);
			return;
		}

		// Lade Configuration
		this.cofiguration_Handler = new Cofiguration_Handler();
		if (!cofiguration_Handler.load_Configuration()) {
			// TODO Disable plugin
			//getServer().getPluginManager().disablePlugin((JavaPlugin) this);
			getLogger().log(Level.SEVERE, Message_Handler.resolve_to_message(3));
			PrimeShop.plugin.getPluginLoader().disablePlugin(PrimeShop.plugin);
			return;
		}

		// CooldownManager
		cooldownManager =  new CooldownManager(Cofiguration_Handler.spamProtection_in_milliseconds); 
		
		// Lade Datenbank
//		databaseHandler = new Database_MySql(this, DatabseTyp.MYSQL);
		databaseHandler = new Database_FLATFILE(this, DatabseTyp.MYSQL);
		if (!databaseHandler.load_Database()) {
			getLogger().log(Level.SEVERE, Message_Handler.resolve_to_message(1));
			this.getPluginLoader().disablePlugin(this);
			return;
		}
		
		
		// Register Saving Scheduler
		Bukkit.getScheduler().scheduleSyncRepeatingTask(PrimeShop.plugin,(Runnable) new Item_Saver(), 2 * 20L, Cofiguration_Handler.save_Changes_every_X_Seconds * 20L);
		
		// registen Inventory Section Listener
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new SectionListener(), this);
		
		// Registriere die Command Listener
		Command_Registrer.register_Command_Listeners(this);
		
		// Pre render all Items in Shops
		PreRender_all_Items_in_Shops a = new PreRender_all_Items_in_Shops();
		a.start();
		
		// Metrics
		
	    try {
	        Metrics metrics = new Metrics(this);
	        metrics.start();
	    } catch (IOException e) {
	        // Failed to submit the stats :-(
	    }

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDisable() {
	
		Item_Saver itemSaver =new Item_Saver();
		itemSaver.save_all_Items();
		

		// Close all InventoryInterfaces
		for (Player player : Bukkit.getOnlinePlayers()) {
			PrimeShop.close_InventoyInterface(player);
		}

		list_of_deaktivated_PriceLinks = null;
		hashMap_SQL_Item = null;
		hashMap_EvaluatedRecipe = null;
		hashMap_CitizensNPCs = null;
		hashMap_Item_Node_of_ItemBloodline = null;
		hashMap_InventoryInterfaces = null;
		hashMap_InventorySessions = null;
		hashMap_Inventory_handSave = null;
		recepie_Examiner = null;
		itemTrader = null;
		PrimeShop.economy = null;

		PrimeShop.plugin = null;
	}


	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			setEconomy(economyProvider.getProvider());
		}
		return (getEconomy() != null);
	}
	

	public static ItemStack convert_random_String_to_ItemStack(String input,
			int amount) {
		ItemStack result = null;

		try {
			result = PrimeShop.convertItemIdStringToItemstack(input, 1);
		} catch (Exception e) {
			return null;
		}

		return result;

	}

	@SuppressWarnings("deprecation")
	public static ItemStack convert_random_String_to_ItemStack(String input,
			CommandSender cs) {
		ItemStack result = null;

		if (input == null) {
			return null;
		}

		// Material.Name
		Material mat = Material.getMaterial(input.toUpperCase());
		if (mat == null) {
			result = null;
		} else {
			result = new ItemStack(mat);
		}

		// hand

		if (cs != null) {
			if (input.equalsIgnoreCase("hand")) {
				if (!(cs instanceof Player)) {
					cs.sendMessage(Message_Handler.resolve_to_message(27));
					return null;
				}
				if (((Player) cs).getItemInHand() == null) {
					cs.sendMessage(Message_Handler.resolve_to_message(28));
					return null;
				}
				if (((Player) cs).getItemInHand().getType() == Material.AIR) {
					cs.sendMessage(Message_Handler.resolve_to_message(28));
					return null;
				}

				return ((Player) cs).getItemInHand().clone();
			}
		}
		// id:data

		if (input.contains(":")) {
			result = PrimeShop.convert_random_String_to_ItemStack(input,
					0);
		}

		// id
		if (isThisStringNumeric(input)) {

			try {
				result = new ItemStack(Integer.parseInt(input));
			} catch (NumberFormatException e) {
				result = null;
			}

		}

		if (result != null) {
			if (result.getType().equals(Material.AIR)) {
				if (cs != null) {
					cs.sendMessage(ChatColor.RED
							+ Message_Handler.resolve_to_message(29));
				}
				return null;
			}
		} else {
			if (cs != null) {
				cs.sendMessage(ChatColor.RED
						+ Message_Handler.resolve_to_message(29));
			}
		}
		return result;
	}

	public static boolean isThisStringNumeric(final String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static String convertItemIdToString(int itemID, byte itemDATA) {

		String prefix = String.valueOf(itemID);
		String suffix = String.valueOf(Integer.valueOf(itemDATA));

		if (suffix.contains("-1")) {
			suffix = "0";
		}
		if (prefix.matches("5")) {
			suffix = "0";
		}
		if (prefix.matches("17")) {
			suffix = "0";
		}
		if (prefix.matches("125")) {
			suffix = "0";
		}
		if (prefix.matches("126")) {
			suffix = "0";
		}
		if (prefix.matches("134")) {
			suffix = "0";
		}
		if (prefix.matches("162")) {
			suffix = "0";
		}
		return prefix + ":" + suffix;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack convertItemIdStringToItemstack(String itemString,
			int amount) {
		String[] a = itemString.split(":");
		int id = Integer.valueOf(a[0]);
		int damage = 0;
		if (a.length == 2) {
			damage = Integer.valueOf(a[1]);
			ItemStack result = new ItemStack(id, amount, (short) 0,
					(byte) damage);
			return result;
		} else {
			return null;
		}
	}

	public static String convertItemStacktoToIdString(ItemStack itemstack) {
		@SuppressWarnings("deprecation")
		byte data = itemstack.getData().getData();
		@SuppressWarnings("deprecation")
		int id = itemstack.getType().getId();

		return PrimeShop.convertItemIdToString(id, data);
	}

	public List<EvaluatedRecipe> get_Recipes_of_Item(ItemStack itemstack) {
		if (Cofiguration_Handler.price_Linking_for_all_Items_DISABLED)
			return get_empty_Recipe_List(itemstack);

		String mc_Item_ID = PrimeShop
				.convertItemStacktoToIdString(itemstack);
		if (is_PriceLinking_disabled_for_Item(itemstack)) {
			return get_empty_Recipe_List(itemstack);
		}
		List<EvaluatedRecipe> result;
		if (hashMap_EvaluatedRecipe.containsKey(mc_Item_ID)) {
			result = hashMap_EvaluatedRecipe.get(mc_Item_ID);
		} else {
			result = recepie_Examiner.findRecipesOfItem(itemstack);
			hashMap_EvaluatedRecipe.put(mc_Item_ID, result);
		}
		return result;
	}

	public boolean is_PriceLinking_disabled_for_Item(String mc_Item_ID) {
		
		List<String> asd = new ArrayList<String>();
		// Firework
		asd.add("401:0");
		asd.add("401:1");
		asd.add("402:1");
		asd.add("402:0");
//		// Leather Helemet
//		asd.add("298:0");
//		asd.add("298:1");
//		asd.add("298:-1");
//		asd.add("298:-1");
		// Inc Sack
		asd.add("351:-1");
		asd.add("351:0");
		asd.add("351:1");
		// Enchanted Book
		asd.add("403:-1");
		asd.add("403:0");
		asd.add("403:1");
		// Map
		asd.add("395:0");
		asd.add("395:-1");
		asd.add("395:1");
		asd.add("358:0");
		asd.add("358:-1");
		asd.add("358:1");
		
		
		if (asd.contains(mc_Item_ID)) return true;
		
		
		
		
		
		for (String s : list_of_deaktivated_PriceLinks) {
			if (s.equals(mc_Item_ID)) {
				return true;
			}
		}
		return false;
	}

	public boolean is_PriceLinking_disabled_for_Item(ItemStack istemStack) {
		String mc_Item_ID = PrimeShop
				.convertItemStacktoToIdString(istemStack);
		return is_PriceLinking_disabled_for_Item(mc_Item_ID);
	}

	public void enable_PriceLinking_for_Item(ItemStack itemStack) {
		String mcID = convertItemStacktoToIdString(itemStack);
		for (int i = 0; i < list_of_deaktivated_PriceLinks.size(); i++) {
			String s = list_of_deaktivated_PriceLinks.get(i);
			if (s.equals(mcID)) {
				list_of_deaktivated_PriceLinks.remove(i);
				break;
			}
		}
		Cofiguration_Handler.save_list_of_disabled_PriceLinks_to_Disk();
	}
	
	
	public void disable_PriceLinking_for_Item(ItemStack itemStack) {
		String mcID = convertItemStacktoToIdString(itemStack);
		list_of_deaktivated_PriceLinks.add(mcID);
		Cofiguration_Handler.save_list_of_disabled_PriceLinks_to_Disk();
		
		databaseHandler.get_Item_from_Databse(get_Shop_Item_of_Itemstack(itemStack));
	}

	private List<EvaluatedRecipe> get_empty_Recipe_List(ItemStack itemStack) {
		List<EvaluatedRecipe> result = new ArrayList<EvaluatedRecipe>();
		return result;
	}

	public static Shop_Item get_Shop_Item_of_Itemstack(ItemStack itemstack) {
		Shop_Item result;
		String mc_Item_ID = PrimeShop
				.convertItemStacktoToIdString(itemstack);
		if (hashMap_SQL_Item.containsKey(mc_Item_ID)) {
			result = hashMap_SQL_Item.get(mc_Item_ID);
		} else {
			result = new Shop_Item(itemstack, 1); // TODO Stockfloor
			hashMap_SQL_Item.put(mc_Item_ID, result);
		}

		return result;
	}

	public static Item_Node_of_ItemBloodline get_Tree_of_Itemstack(
			ItemStack itemstack) {

		Item_Node_of_ItemBloodline result;
		if (PrimeShop.plugin.is_PriceLinking_disabled_for_Item(itemstack)) {
			result = new Item_Node_of_ItemBloodline(null, new Shop_Item_Stack(
					PrimeShop.get_Shop_Item_of_Itemstack(itemstack), 1,
					true), PrimeShop.plugin);
			result.grow_this_tree();
		} else {
			String mc_Item_ID = PrimeShop
					.convertItemStacktoToIdString(itemstack);
			if (hashMap_Item_Node_of_ItemBloodline.containsKey(mc_Item_ID)) {
				result = hashMap_Item_Node_of_ItemBloodline.get(mc_Item_ID);
			} else {
				result = new Item_Node_of_ItemBloodline(
						null,
						new Shop_Item_Stack(PrimeShop
								.get_Shop_Item_of_Itemstack(itemstack), 1, true),
						PrimeShop.plugin);
				result.grow_this_tree();
				hashMap_Item_Node_of_ItemBloodline.put(mc_Item_ID, result);
			}
		}
		return result;
	}

	public static String convert_IemStack_to_DisplayName(ItemStack itemStack) {
		String result = itemStack.getType().toString().toLowerCase(Locale.ENGLISH);
		return result;
	}

	public static String convert_IemStack_to_DisplayName(String mcItemID) {
		return convert_IemStack_to_DisplayName(convertItemIdStringToItemstack(
				mcItemID, 1));
	}

	public static Economy getEconomy() {
		return economy;
	}

	public static void setEconomy(Economy economy) {
		PrimeShop.economy = economy;
	}

	public static List<ItemStack> get_all_SubItems_of_ItemStack(
			ItemStack resultitem) {

		// Get list with SubItems
		List<ItemStack> list_containing_duplicates = new ArrayList<ItemStack>();

		Item_Node_of_ItemBloodline tree = PrimeShop
				.get_Tree_of_Itemstack(resultitem);
		List<ItemStack> list = tree.collect_all_involved_items();

		// Filter the Parent Item from List
		for (ItemStack itemstack : list) {
			itemstack.setAmount(1);
			
			if (!Item_Comparer.do_Items_match(itemstack, resultitem, true, false, true, true, true)) {

				list_containing_duplicates.add(itemstack);
			}
		}

		// Delete duplicates and pass them to result list

		List<ItemStack> result = new ArrayList<ItemStack>();
		for (ItemStack itemstack : list_containing_duplicates) {
			boolean duplicationFound = false;

			for (ItemStack itemstack2 : result) {
				if (Item_Comparer.do_Items_match(itemstack, itemstack2, true, false, true, true, true)) {
					duplicationFound = true;
					break;
				}
			}

			if (!duplicationFound) {
				result.add(itemstack);
			}
		}
		return result;
	}
	
	// Get Permission Group for Item
	public int get_Players_PermissionGroup (Player player) {
		for (int i = PrimeShop.permissionGroupMaximum; i > PrimeShop.permissionGroupMinimum; i--) {
			if (PrimeShop.has_player_Permission_for_this_Command(player, "PrimeShop.VIP.permission_Group." + i)) {
				return i;
			}
		}
		return PrimeShop.permissionGroupMinimum;
	}
	
	public boolean has_Player_Permission_for_this_Item (Player player, ItemStack itemStack) {
		List<ItemStack> a = get_all_SubItems_of_ItemStack(itemStack);
		int max = -100;
		int tmp;
		if (a.isEmpty()) {
			max = get_Shop_Item_of_Itemstack(itemStack).permissionGroup.getValue();
		} else {
			for (ItemStack i : a) {
				tmp = get_Shop_Item_of_Itemstack(i).permissionGroup.getValue();
				if (tmp > max) {
					max = tmp;
				}
			}
		}
		int playersPermission = get_Players_PermissionGroup(player);
		
		if(playersPermission >= max) return true;
		return false;
		
	}


	public List<Shop> get_List_of_Shops_from_NPC (int npcID) {
		if (hashMap_CitizensNPCs.containsKey(npcID)) {
			return hashMap_CitizensNPCs.get(npcID);
		} else {
			return null;
		}
	}
	
	public boolean add_Shop_to_NPC (Shop shop, int npcID) {
		if (hashMap_CitizensNPCs.containsKey(npcID)) {
			if (hashMap_CitizensNPCs.get(npcID).contains(shop)) {
				return false;
			}
			hashMap_CitizensNPCs.get(npcID).add(shop);
		} else {
			List<Shop> list = new ArrayList<Shop>();
			list.add(shop);
			hashMap_CitizensNPCs.put(npcID, list);
		}
		return true;
	}
	
	public boolean remove_Shop_from_NPC(Shop shop, int npcID) {
		if (hashMap_CitizensNPCs.containsKey(npcID)) {
			if (hashMap_CitizensNPCs.get(npcID).contains(shop)) {				
				hashMap_CitizensNPCs.get(npcID).remove(shop);
				return true;
			}
		}
			return false;
	}
	
	


	public static void open_InventoyInterface(final Player player, InventoryInterface inventoryInterface) {
		PrimeShop.close_InventoyInterface(player);
		final InventoryInterface menu_ = inventoryInterface.clone();
		Bukkit.getScheduler().scheduleSyncDelayedTask(PrimeShop.plugin,
				new Runnable() {
					@Override
					public void run() {
						menu_.refresh(player);
					}
				});
	}

	public static boolean close_InventoyInterface(Player player) {
		if (!PrimeShop.has_Player_InventoyInterface(player)) {
			return false;
		}
		PrimeShop.hashMap_InventoryInterfaces.remove(player.getName());
		PrimeShop.hashMap_InventorySessions.remove(player.getName());
		player.closeInventory();
		return true;
	}

	public static InventoryInterface get_Players_InventoyInterface(Player player) {
		return PrimeShop.hashMap_InventoryInterfaces.get(player.getName());
	}

	public static boolean has_Player_InventoyInterface(Player player) {
		return PrimeShop.get_Players_InventoyInterface(player) != null;
	}

	public static boolean has_player_Permission_for_this_Command (CommandSender cs, final String permission) {
		if (!(cs instanceof Player)) return true;
		Player player = (Player) cs;
		if (player.hasPermission(permission)) return true;
		String permissionString = permission;
		while (permissionString.contains(".")) {
			permissionString = permissionString.substring(0, permissionString.length()-1);
			if (permissionString.charAt(permissionString.length() -1) == '.') 
			if(player.hasPermission(permissionString + "*")) return true;
		}
		
		return false;
	}	
	
	
	private void copy_default_config_Files_to_Plugin_dir (String filename) {
	     URL inputUrl = getClass().getResource("/eu/Blockup/PrimeShop/" + filename);
	        File dest = new File("plugins/PrimeShop/" + filename);
	       
	        if(!(dest.exists() && !dest.isDirectory())) {
    	        try {
    	            FileUtils.copyURLToFile(inputUrl, dest);
    	        } catch (IOException e1) {
    	            // TODO Auto-generated catch block
    	            e1.printStackTrace();
    	        }
	        }
	}
}
