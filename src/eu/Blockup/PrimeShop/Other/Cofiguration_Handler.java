package eu.Blockup.PrimeShop.Other;

import java.io.File;
import java.io.IOException;
import java.util.List;




import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.PricingEngine.Enchantments.EnchantmentData;
import eu.Blockup.PrimeShop.PricingEngine.Enchantments.EnchantmentHandler;

public class Cofiguration_Handler {

    public static String host;
    public static int port;
    public static String user;
    public static String password;
    public static String database;

    public static long save_Changes_every_X_Seconds = 60;
    public static long spamProtection_in_milliseconds = 60L;
    public static double default_rate_of_price_change = 0.0001;
    public static double default_initial_price = 1.0;
    public static double globalRateOfInflation = 1.0;
// TODO Remove unused code found by UCDetector
//     public static boolean sellingItemsisEnabled = true;
// TODO Remove unused code found by UCDetector
//     public static boolean buyingItemsisEnabled = true;
    public static double smallestValueItemCanHave = 0.00;
    public static double taxValueItemIncreasesWhenItWasCrafted = 0.05;
    public static int amount_of_simultanious_opened_MySQL_Connection = 2;
    public static boolean price_Linking_for_all_Items_DISABLED = false;
    public static double value_every_item_bought_gets_multiplied_with = 1.0;
    public static double value_every_item_sold_gets_multiplied_with = 0.5;
    public static String default_Economy_Name = "PrimeShop_Economy";
    public static String Sign_Shop_Headline = "PrimeShop";
    
    public static ItemStack closeButton_ItemStack = PrimeShop.convertItemIdStringToItemstack("160:0", 1);
    public static ItemStack buyButton_ItemStack = PrimeShop.convertItemIdStringToItemstack("119:0", 1);
    public static ItemStack sellButton_ItemStack = PrimeShop.convertItemIdStringToItemstack("119:0", 1);
    public static ItemStack backToCollectionButton_ItemStack = PrimeShop.convertItemIdStringToItemstack("389:0", 1);
    public static ItemStack background_ItemStack = PrimeShop.convertItemIdStringToItemstack("160:15", 1);

    @SuppressWarnings("deprecation")
    public boolean load_Configuration() {
        
        // Lade File
        File file = new File("plugins/PrimeShop/", "config.yml");
        // Creates new file if doesn't exist
        try {
        if (!file.exists())
                file.createNewFile();
            } catch (IOException e2) {
                e2.printStackTrace();
                PrimeShop.plugin.getLogger().log(Level.SEVERE, Message_Handler.resolve_to_message(33));
                return false;
            }
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.addDefault("Version", (Double)1.0);
        
        // Saving periode
        cfg.addDefault("Save_Changes_every_X_Seconds", save_Changes_every_X_Seconds);

        cfg.addDefault("SpamProtection_in_milliseconds", spamProtection_in_milliseconds);
        
        // Price Linking
        cfg.addDefault("Price_Linking.for_all_Items_DISABLED", price_Linking_for_all_Items_DISABLED);
        cfg.getStringList("Price_Linking.list_of_disabled_Items");

        // Price Calculation
        cfg.addDefault("Price_Calculation.globalRateOfInflation", 1.0);
        cfg.addDefault("Price_Calculation.value_every_item_bought_gets_multiplied_with", 1.0);
        cfg.addDefault("Price_Calculation.value_every_item_sold_gets_multiplied_with", 1.0);
        cfg.addDefault("Price_Calculation.taxValueItemIncreasesWhenItWasCrafted", 0.05);
        
        // Appearance
        cfg.addDefault("Appearance.Buy_Button", "119:0");
        cfg.addDefault("Appearance.Sell_Button", "119:0");
        cfg.addDefault("Appearance.Close_Button", "160:0");
        cfg.addDefault("Appearance.NPC_Button", "389:0");
        cfg.addDefault("Appearance.background_Item", "160:15");
        cfg.addDefault("Appearance.Sign_Shop_Headline", "PrimeShop");
        
        // Save Defaults
        cfg.options().copyDefaults(true);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            PrimeShop.plugin.getLogger().log(Level.SEVERE, Message_Handler.resolve_to_message(33));
            return false;
        }

        // Load user configs
        try {
            // load saving periode
            Cofiguration_Handler.save_Changes_every_X_Seconds = cfg.getLong("Save_Changes_every_X_Seconds");
            if (Cofiguration_Handler.save_Changes_every_X_Seconds < 5) Cofiguration_Handler.save_Changes_every_X_Seconds = 5;
            
            // load spamProtection_in_milliseconds
            Cofiguration_Handler.spamProtection_in_milliseconds = cfg.getLong("SpamProtection_in_milliseconds");
            if (Cofiguration_Handler.spamProtection_in_milliseconds < 0) Cofiguration_Handler.spamProtection_in_milliseconds = 0;
            
            // load price linking stuff
            Cofiguration_Handler.price_Linking_for_all_Items_DISABLED = cfg.getBoolean("Price_Linking.for_all_Items_DISABLED");

            add_Item_to_List_of_disabled_PriceLinks(cfg.getStringList("Price_Linking.list_of_disabled_Items"));
            
            // inflation Rates
            Cofiguration_Handler.globalRateOfInflation = cfg.getDouble("Price_Calculation.globalRateOfInflation");
            Cofiguration_Handler.value_every_item_bought_gets_multiplied_with = cfg.getDouble("Price_Calculation.value_every_item_bought_gets_multiplied_with");
            Cofiguration_Handler.value_every_item_sold_gets_multiplied_with = cfg.getDouble("Price_Calculation.value_every_item_sold_gets_multiplied_with");
            Cofiguration_Handler.taxValueItemIncreasesWhenItWasCrafted = cfg.getDouble("Price_Calculation.taxValueItemIncreasesWhenItWasCrafted");

        
            
            
            ItemStack tmp = null;
            
            tmp = PrimeShop.convert_random_String_to_ItemStack(cfg.getString("Appearance.Buy_Button"), null);
            if (tmp != null) Cofiguration_Handler.buyButton_ItemStack = tmp;
            
            tmp = PrimeShop.convert_random_String_to_ItemStack(cfg.getString("Appearance.Sell_Button"), null);
            if (tmp != null) Cofiguration_Handler.sellButton_ItemStack = tmp;
            
            tmp = PrimeShop.convert_random_String_to_ItemStack(cfg.getString("Appearance.Close_Button"), null);
            if (tmp != null) Cofiguration_Handler.closeButton_ItemStack = tmp;
            
            tmp = PrimeShop.convert_random_String_to_ItemStack(cfg.getString("Appearance.NPC_Button"), null);
            if (tmp != null) Cofiguration_Handler.backToCollectionButton_ItemStack = tmp;
            
            tmp = PrimeShop.convert_random_String_to_ItemStack(cfg.getString("Appearance.background_Item"), null);
            if (tmp != null) Cofiguration_Handler.background_ItemStack = tmp;
            
            String shop_Headline = cfg.getString("Appearance.Sign_Shop_Headline");
            if (shop_Headline != null) if (shop_Headline.length() > 2) Cofiguration_Handler.Sign_Shop_Headline = shop_Headline;
            
            
        } catch (Exception e) {
            e.printStackTrace();
            PrimeShop.plugin.getLogger().log(Level.SEVERE, Message_Handler.resolve_to_message(1));
            return false;
        }
        
        // Load enchantments.yml
        EnchantmentHandler.clear_List();

        try {
            file = new File("plugins/PrimeShop/", "enchantments.yml");   // TODO
            cfg = YamlConfiguration.loadConfiguration(file);
            cfg.addDefault("Version", (Double)1.0);
            cfg.options().copyDefaults(true);
            
            for (String name : cfg.getConfigurationSection("").getKeys(false)) {

                int id                          = cfg.getInt(name + ".enchantmentID");      //deprecated
                int level                       = cfg.getInt(name + ".enchantmentLevel");
                int price                       = cfg.getInt(name + ".enchantmentPrice");
                String minecraftEnchantmentName = cfg.getString(name + ".name");
                
                //backward compatibility
                if (minecraftEnchantmentName == null) {
                    minecraftEnchantmentName = Enchantment.getById(id).getName();
                    cfg.set(name + ".name" , minecraftEnchantmentName);
                }

//                PrimeShop.plugin.getLogger().log(Level.SEVERE, name + " " + id + " " + level + " " + price);
//                EnchantmentHandler.add_Enchantment(new EnchantmentData(id, level, price));
                EnchantmentHandler.add_Enchantment(new EnchantmentData(id, level, price, minecraftEnchantmentName));

            }
            cfg.save(file);
        } catch (Exception e1) {
            PrimeShop.plugin.getLogger().log(Level.SEVERE, "Error reading enchantments.yml");    // TODO
            e1.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean save_list_of_disabled_PriceLinks_to_Disk() {
        File file = new File("plugins/PrimeShop/", "config.yml"); // TODO
                                                                        
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        
        try {
            cfg.set("Price_Linking.list_of_disabled_Items", PrimeShop.list_of_deaktivated_PriceLinks); // TODO enable this again

        } catch (Exception e) {
            e.printStackTrace();
            PrimeShop.plugin.getLogger().log(Level.SEVERE, Message_Handler.resolve_to_message(1));
            return false;
        }
        
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            PrimeShop.plugin.getLogger().log(Level.SEVERE, Message_Handler.resolve_to_message(1));
            return false;
        }
        
        return true;
    }

    private static void add_Item_to_List_of_disabled_PriceLinks(
            List<String> list) {

        for (String s : list) {
            ItemStack i = PrimeShop.convert_random_String_to_ItemStack(s,
                    null);

            if (i == null) {
                PrimeShop.plugin.getLogger().log(Level.SEVERE, ChatColor.RED
                                + "String \"" + s
                                + "\" could not be recognized as valide Item! Please check config.yml"); //TODO
            } else {
                String str = PrimeShop.convertItemStacktoToIdString(i);
                PrimeShop.list_of_deaktivated_PriceLinks.add(str);
            }
        }
    }
}
