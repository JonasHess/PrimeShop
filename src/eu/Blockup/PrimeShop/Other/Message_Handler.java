package eu.Blockup.PrimeShop.Other;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import eu.Blockup.PrimeShop.PrimeShop;

public class Message_Handler {

    private static HashMap<Integer, String> hashMap_Messages;

    private static void fill_hashmap() {

        /*
         * Message_Handler.resolve_to_message(70)
         */

        hashMap_Messages = new HashMap<Integer, String>();
        hashMap_Messages.put(1, ChatColor.RED + "ERROR: Can not access Shop.yml");
        hashMap_Messages.put(2, ChatColor.RED + "Error: Can not find Economy_Plugin compatible with Vault");
        hashMap_Messages.put(3, ChatColor.RED + "Failed Reading Configfile!");
        hashMap_Messages.put(4, ChatColor.RED + "ERROR: Can not open items.yml nor write to it");
        hashMap_Messages.put(5, ChatColor.WHITE + "PrimeShop is terminating");
        hashMap_Messages.put(6, ChatColor.WHITE + " *** Create new MySQL-Connection *** $1/$2");
        hashMap_Messages.put(7, ChatColor.WHITE + "Cronboy awaked");
        hashMap_Messages.put(8, ChatColor.GOLD + "Bought for " + ChatColor.RED + "$1$2");
        hashMap_Messages.put(9, ChatColor.RED + "Not enough money to buy!");
        hashMap_Messages.put(10, ChatColor.RED + "Error in price calculation");
        hashMap_Messages.put(11, ChatColor.RED + "Inventory is full");
        hashMap_Messages.put(12, ChatColor.RED + "You have to choose an item first!");
        hashMap_Messages.put(13, ChatColor.RED + "Error in price calculation, please inform an Moderator");
        hashMap_Messages.put(14, ChatColor.RED + "You don't have enough of this Item");
        hashMap_Messages.put(15, ChatColor.GOLD + "Sold for: " + ChatColor.GREEN + "$1");
        hashMap_Messages.put(16, ChatColor.RED + "There are no shops!");
        hashMap_Messages.put(17, ChatColor.RED + "Price can't be less than zero!");
        hashMap_Messages.put(18, ChatColor.RED + "Rate of price-change can't be less than 0%");
        hashMap_Messages.put(19, ChatColor.AQUA + "Price: ");
        hashMap_Messages.put(20, ChatColor.GOLD + "initialPrice: $1");
        hashMap_Messages.put(21, ChatColor.BLUE + "currentPrice: $1");
        hashMap_Messages.put(22, ChatColor.GOLD + "Change default price");
        hashMap_Messages.put(23, ChatColor.GOLD + "Price doubles every $1 purchases");
        hashMap_Messages.put(24, ChatColor.GOLD + "Modify rate of price change");
        hashMap_Messages.put(25, ChatColor.RED + "Make price be constant!");
        hashMap_Messages.put(26, ChatColor.GREEN + "Set to default value");
        hashMap_Messages.put(27, ChatColor.RED + "You can not access this command from terminal");
        hashMap_Messages.put(28, ChatColor.RED + "You have no item in your hand!");
        hashMap_Messages.put(29, ChatColor.RED + "Could not recognize this itemID");
        hashMap_Messages.put(30, ChatColor.RED + "You do not have permission to buy this Item");
        hashMap_Messages.put(31, ChatColor.RED + "This item was not recognized");
        hashMap_Messages.put(32, ChatColor.RED + "You do not have the permission to sell this item");
        hashMap_Messages.put(33, ChatColor.RED + "Error: PrimeShop was not able to access the file config.yml");
        hashMap_Messages.put(34, ChatColor.AQUA + "Close");
        hashMap_Messages.put(35, ChatColor.GRAY + "Go back to game");
        hashMap_Messages.put(36, ChatColor.GREEN + "Item was successfully added to shop");
        hashMap_Messages.put(37, ChatColor.RED + "were not able to delete this item for unknown reason");
        hashMap_Messages.put(38, ChatColor.GREEN + "Item was successfully removed from shop");
        hashMap_Messages.put(39, ChatColor.GREEN + "Changed price to: $1");
        hashMap_Messages.put(40, ChatColor.WHITE + "Price: $1");
        hashMap_Messages.put(41, ChatColor.RED + "Error: This item is currently not available");
        hashMap_Messages.put(42, ChatColor.GOLD + "Rate of price change: $1");
        hashMap_Messages.put(43, ChatColor.GOLD + "Rate of price change:");
        hashMap_Messages.put(44, ChatColor.RED + "Item to be added to shop $1");
        hashMap_Messages.put(45, ChatColor.RED + "Do you confirm?");
        hashMap_Messages.put(46, ChatColor.RED + "YES, add item to shop");
        hashMap_Messages.put(47, ChatColor.AQUA + "NO, don't add item");
        hashMap_Messages.put(48, ChatColor.GREEN + "Adding item to shop");
        hashMap_Messages.put(49, ChatColor.GOLD + "Click, to list ingredients of this item");
        hashMap_Messages.put(50, ChatColor.DARK_GRAY + "You can not edit the price of this item since pricelinking is enabled for it.");
        hashMap_Messages.put(51, ChatColor.DARK_GRAY + "To change the price, select an ingredient which has no ingredients itself");
        hashMap_Messages.put(52, ChatColor.GREEN + "Enable pricelinking for this item");
        hashMap_Messages.put(53, ChatColor.GREEN + "Enabling pricechanges will make the price be compound by the prices of its ingredients");
        hashMap_Messages.put(54, ChatColor.RED + "Delete Item from Shop");
        hashMap_Messages.put(55, ChatColor.DARK_GRAY + "");
        hashMap_Messages.put(56, ChatColor.GOLD + "Info:");
        hashMap_Messages.put(57, ChatColor.DARK_GRAY + "  ---> Click HERE <---");
        hashMap_Messages.put(58, ChatColor.DARK_GRAY + " to manipulate the quantity.");
        hashMap_Messages.put(59, ChatColor.DARK_GRAY + "Use the SHIFT operator to stack.");
        hashMap_Messages.put(60, ChatColor.DARK_GRAY + "This item is not stackable");
        hashMap_Messages.put(61, ChatColor.AQUA + "Back");
        hashMap_Messages.put(62, ChatColor.GRAY + "Go Back to previous window");
        hashMap_Messages.put(63, ChatColor.GRAY + "Buy & Sell");
        hashMap_Messages.put(64, ChatColor.GOLD + "Change initial price");
        hashMap_Messages.put(65, ChatColor.GOLD + "Rate of price change");
        hashMap_Messages.put(66, ChatColor.AQUA + "Click to open this shop");
        hashMap_Messages.put(67, ChatColor.RED + "Delete item from shop?");
        hashMap_Messages.put(68, ChatColor.RED + "YES, delete Item to Shop");
        hashMap_Messages.put(69, ChatColor.GREEN + "NO, don't delete Item");
        hashMap_Messages.put(70, ChatColor.RED + "Delete shop from NPC?");
        hashMap_Messages.put(71, ChatColor.GREEN + "Shop was removed fom NPC");
        hashMap_Messages.put(72, ChatColor.RED + "Delete shop from NPC");
        hashMap_Messages.put(73, ChatColor.RED + "YES, delete shop from NPC");
        hashMap_Messages.put(74, ChatColor.GREEN + "NO, don't delete shop from NPC");
        hashMap_Messages.put(75, ChatColor.RED + "Delete this shop?");
        hashMap_Messages.put(76, ChatColor.GREEN + "Shop was deleted!");
        hashMap_Messages.put(77, ChatColor.GREEN + "NO, don't delete this shop");
        hashMap_Messages.put(78, ChatColor.RED + "Delete this shop!");
        hashMap_Messages.put(79, ChatColor.RED + "YES, delete shop");
        hashMap_Messages.put(80, ChatColor.GRAY + "List of ingredients");
        hashMap_Messages.put(81, ChatColor.WHITE + "You can not edit the price of this item since pricelinking is enabled for it.");
        hashMap_Messages.put(82, ChatColor.WHITE + "To change the price, select an ingredient which has no ingredients itself");
        hashMap_Messages.put(83, ChatColor.WHITE + "");
        hashMap_Messages.put(84, ChatColor.WHITE + "Click to observe this ingredient");
        hashMap_Messages.put(85, ChatColor.RED + "Disable pricelinking for this item ");
        hashMap_Messages.put(86, ChatColor.GRAY + "Price will no longer be calculated in reference to its ingredients");
        hashMap_Messages.put(87, ChatColor.RED + "You do not have permission to open this shop!");
        hashMap_Messages.put(88, "");
        hashMap_Messages.put(89, ChatColor.DARK_GRAY + "Click to have a closer look.");
        
        
        /*Not enough arguments
         * Message_Handler.resolve_to_message(104)
         */
        
        
        hashMap_Messages.put(90, ChatColor.AQUA + "Next Page");
        hashMap_Messages.put(91, ChatColor.AQUA + "Last Page");
        hashMap_Messages.put(92, ChatColor.AQUA + "Previous Page");
        hashMap_Messages.put(93, ChatColor.AQUA + "First Page");
        hashMap_Messages.put(94, ChatColor.RED + "You do not have permission to create new shops");
        hashMap_Messages.put(95, ChatColor.GREEN + "Shop was created");
        hashMap_Messages.put(96, ChatColor.RED + "Could not recognize this itemID");
        hashMap_Messages.put(97, ChatColor.RED + "You do not have permission to open shops by command");
        hashMap_Messages.put(98, ChatColor.RED + "This shop could not be found");
        hashMap_Messages.put(99, ChatColor.AQUA + "Use the command: '/PrimeShop list' to find a list with all available shops");
        hashMap_Messages.put(100, ChatColor.GRAY + "Available Shops:");
        hashMap_Messages.put(101, ChatColor.RED + "Shops can only be removed ingame");
        hashMap_Messages.put(102, ChatColor.RED + "You do not have permission to delete shops");
        hashMap_Messages.put(103, ChatColor.RED + "Shop $1 could not be found");
        hashMap_Messages.put(104, ChatColor.RED + "You do not have the permission to use this command");
        hashMap_Messages.put(105, ChatColor.RED + "Not enough arguments");
        hashMap_Messages.put(106, ChatColor.RED + "You rank on this server is not high enough to buy this item.");
        hashMap_Messages.put(107, ChatColor.GOLD + "This item was not added to a shop yet. You are only able to buy it because of your special permissions.");
        hashMap_Messages.put(108, ChatColor.RED + "This item was not added to a shop you have access to.");
        hashMap_Messages.put(109, ChatColor.RED + "You have not the permission to use the lookup price information");
        hashMap_Messages.put(110, ChatColor.WHITE + "The value of 64 'Grass' is: 100.44€");
        hashMap_Messages.put(111, ChatColor.BLUE + "The value of $1 \"$2\" is: $3");
        hashMap_Messages.put(112, ChatColor.RED + "You do not have permissions to rename shops");
        hashMap_Messages.put(113, ChatColor.GREEN + "Shop was renamed");
        hashMap_Messages.put(114, ChatColor.RED + "You do not have permission to add items to this shop");
        hashMap_Messages.put(115, ChatColor.RED + "This will delete all your items and transfer them to the shop instead");
        hashMap_Messages.put(116, ChatColor.GREEN + "All your inventory items have been moved to the shop");
        hashMap_Messages.put(117, ChatColor.RED + "You do not have permission to link this shop with a NPC");
        hashMap_Messages.put(118, ChatColor.RED + "This NPC already knows this shop");
        hashMap_Messages.put(119, ChatColor.GREEN + "NPC: $1 now contains shop $2");
        hashMap_Messages.put(120, ChatColor.GOLD + "Buy");
        hashMap_Messages.put(121, ChatColor.GOLD + "Sell");
        hashMap_Messages.put(122, ChatColor.DARK_GRAY + "Amount: $1");
        hashMap_Messages.put(123, ChatColor.WHITE + "");
        hashMap_Messages.put(124, ChatColor.GRAY + "How many purchases are need to double the price?");
        hashMap_Messages.put(125, ChatColor.GRAY + "How much is this item worth, if demand and supply are perfectly balanced?");
        
        
        hashMap_Messages.put(126, ChatColor.RED + "There already is a shop with similar or related name!");
        hashMap_Messages.put(127, ChatColor.RED + "You have not the permission to create or edit Shop-Signs");
        hashMap_Messages.put(128, ChatColor.RED + "To destroy this sign, hit it with an arrow.");
        hashMap_Messages.put(129, ChatColor.RED + "You do not have permission to trade with this NPC");
        hashMap_Messages.put(130, ChatColor.RED + "You do not have permission to interact with this signs");
        hashMap_Messages.put(131, ChatColor.RED + "This sign is broken. Please inform the server administration");
        hashMap_Messages.put(132, ChatColor.WHITE + "$1.)" + ChatColor.AQUA + " $2 " + ChatColor.WHITE + "is worth " + ChatColor.GOLD + "$3");
        hashMap_Messages.put(133, ChatColor.RED + "Don't spam! Preferably change the stack size.");
        hashMap_Messages.put(135, ChatColor.RED + "WARNING: This command will sell all your inventory-items!");
        hashMap_Messages.put(136, ChatColor.RED + "Confirm with: $1");
        hashMap_Messages.put(137, ChatColor.AQUA + "Sold all Items for : " + ChatColor.AQUA + "$1");
        hashMap_Messages.put(138, ChatColor.AQUA + "to add some items to this shop,");
        hashMap_Messages.put(139, ChatColor.AQUA + "simply make a SHIFT LeftClick on the item");
        hashMap_Messages.put(140, ChatColor.AQUA + "you want to be added");
        hashMap_Messages.put(141, ChatColor.GOLD + "Add items to shop");
        hashMap_Messages.put(142, ChatColor.GRAY + "Most bought first:");
        hashMap_Messages.put(143, ChatColor.GRAY + "Most sold first:");
        hashMap_Messages.put(144, ChatColor.GRAY + "                      Prices TOP");
        hashMap_Messages.put(145, ChatColor.GRAY + "                      Prices FLOOR");
        hashMap_Messages.put(146, ChatColor.GRAY + "Update available");
    }

    public static synchronized boolean initialize_language_support() {
        boolean result = true;
        Message_Handler.fill_hashmap();
        result = result && Message_Handler.read_language_file();
        return true;
    }

    private static synchronized boolean read_language_file() {
        File file = new File("plugins/PrimeShop/", "messages.yml"); // TODO
                                                                            // plugin
                                                                            // name
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.addDefault("Version", (Double)1.0);
        
        int mapsize = hashMap_Messages.size();
        for (int i = 1; i <= mapsize; i++) {
            cfg.addDefault("message." + i, hashMap_Messages.get(i));
        }
        cfg.options().copyDefaults(true);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            PrimeShop.plugin.getLogger().log(Level.SEVERE, ChatColor.RED + "Error: PrimeShop can not acces messages.yml");
            return false;
        }

        try {
            for (int i = 1; i <= mapsize + 1; i++) {   // TODO FIX
                hashMap_Messages.put(i, cfg.getString("message." + i));
            }

            // /////////////////////////////////

            // cfg.getCurrentPath();
            // ConfigurationSection egal =
            // cfg.getConfigurationSection("Main");//.getKeys(false);

            // Alte Load Shopy.yml procedur
            // ConfigurationSection list_of_shops =
            // cfg.getConfigurationSection("Shops");
            //
            // for (String shopname : list_of_shops.getKeys(false)) {
            // System.out.println("ShopName:" + shopname);
            // String displayiconid = cfg.getString("Shops." + shopname +
            // ".displayitem");
            // System.out.println("DisplayIconID: " + displayiconid);
            // ItemStack displayIcon =
            // Blockup_Economy.convertItemIdStringToItemstack(displayiconid, 1);
            // Shop shop = new Shop(shopname, displayIcon);
            // List<String> itemnamelist = cfg.getStringList("Shops." + shopname
            // + ".Items");
            // for (String itemname : itemnamelist) {
            // ItemStack shopItem =
            // Blockup_Economy.convertItemIdStringToItemstack(itemname, 1);
            // shop.add_ItemStack(shopItem);
            // }
            // Blockup_Economy.hashMap_Shops.put(shopname, shop);
            // }

            // for ( bla :egal) {
            // System.out.println("Group-name:" + bla);
            // // List<String> egal2 = cfg.getStringList("test." + bla);
            // // System.out.println(egal2.toString());
            // }

            // ///////////////////////////////////////////////

        } catch (Exception e) {
            e.printStackTrace();
            PrimeShop.plugin.getLogger().log(Level.SEVERE, (Message_Handler.resolve_to_message(1)));
            return false;
        }
        return true;
    }

    public static String resolve_to_message(int id) {
        return Message_Handler.resolve_to_message(id, "");
    }

    public static String resolve_to_message(int id, String arg0) {
        return Message_Handler.resolve_to_message(id, arg0, "");
    }

    public static String resolve_to_message(int id, String arg0, String arg1) {
        return Message_Handler.resolve_to_message(id, arg0, arg1, "");
    }

    public static String resolve_to_message(int id, String arg0, String arg1, String arg2) {
        return Message_Handler.resolve_to_message(id, arg0, arg1, arg2, "");
    }

    private static String resolve_to_message(int id, String arg0, String arg1, String arg2, String arg3) {

        String result = hashMap_Messages.get(id);
        result = result.replaceAll(Pattern.quote("$1"), Matcher.quoteReplacement(arg0));
        result = result.replaceAll(Pattern.quote("$2"), Matcher.quoteReplacement(arg1));
        result = result.replaceAll(Pattern.quote("$3"), Matcher.quoteReplacement(arg2));
        result = result.replaceAll(Pattern.quote("$4"), Matcher.quoteReplacement(arg3));

        return result;
    }

    public static void clear_hashmap() {
        hashMap_Messages.clear();
    }

}
