package eu.Blockup.PrimeShop.Commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.Interface_Shop_Page;
import eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.Interface_add_Item_to_Shop;
import eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.Interface_delete_Shop;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.Shops.Shop;

class PrimeShop_Command implements CommandExecutor {

    private final PrimeShop plugin;

    public PrimeShop_Command(PrimeShop plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
            String[] args) {

        // cs.sendMessage("Befehl PrimeShop wird jetzt ausgefuehrt");

        Player p = null;

        // if (!PrimeShop.has_player_Permission_for_this_Command(cs,
        // "PrimeShop.admin.basics")) {
        // cs.sendMessage(Message_Handler.resolve_to_message(104));
        // return true;
        // }

        // Es gibt Parameter
        if (!(args.length == 0)) {
            // if (args[0].equalsIgnoreCase("Shop") ||
            // (args[0].equalsIgnoreCase("s"))) {

            // Open

            if (args[0].equalsIgnoreCase("open")) {

                if (!PrimeShop.has_player_Permission_for_this_Command(cs,
                        "PrimeShop.VIP.useTheOpenShopCommand")) {
                    cs.sendMessage(Message_Handler.resolve_to_message(97));
                    return true;
                }

                if (!(args.length == 2)) {
                    cs.sendMessage("");
                    cs.sendMessage(ChatColor.LIGHT_PURPLE
                            + "/PrimeShop open <shopname>");
                    return true;
                }

                if (!(cs instanceof Player)) {
                    cs.sendMessage(ChatColor.RED
                            + Message_Handler.resolve_to_message(27));
                    return true;
                }

                p = (Player) cs;
                String shopname = args[1];
                boolean found = false;
                Shop shop = null;

                for (Map.Entry<String, Shop> entry : PrimeShop.hashMap_Shops
                        .entrySet()) {
                    if (entry.getKey().toUpperCase()
                            .contains(shopname.toUpperCase())) {
                        // if (shopname.contains(entry.getKey())) {
                        found = true;
                        shop = entry.getValue();
                    }
                }

                if (found && shop != null) {

                    PrimeShop.open_InventoyInterface(p,
                            new Interface_Shop_Page(
                                    new ArrayList<InventoryInterface>(), p,
                                    shop, 1));
                    return true;
                } else {
                    cs.sendMessage("");
                    p.sendMessage(Message_Handler.resolve_to_message(98));
                    p.sendMessage(Message_Handler.resolve_to_message(99));
                    return true;
                }
            }

            // List
            if (args[0].equalsIgnoreCase("list")) {
                if (!PrimeShop.has_player_Permission_for_this_Command(cs,
                        "PrimeShop.VIP.canSeeAListOfAllShops")) {
                    cs.sendMessage(Message_Handler.resolve_to_message(104));
                    return true;
                }

                cs.sendMessage("");
                if (PrimeShop.hashMap_Shops.size() == 0) {
                    cs.sendMessage(Message_Handler.resolve_to_message(16));
                    return true;
                }
                cs.sendMessage(Message_Handler.resolve_to_message(100));
                for (String key : PrimeShop.hashMap_Shops.keySet()) {
                    Shop shop = PrimeShop.hashMap_Shops.get(key);
                    cs.sendMessage(" - " + ChatColor.AQUA + shop.shopname);
                }
                return true;
            }

            // ADMIN BASIC PERMISSION
            if (!PrimeShop.has_player_Permission_for_this_Command(cs,
                    "PrimeShop.admin.basics")) {
                cs.sendMessage(Message_Handler.resolve_to_message(104));
                return true;
            }

            // Create

            if (args[0].equalsIgnoreCase("create")
                    || args[0].equalsIgnoreCase("craete")
                    || args[0].equalsIgnoreCase("creat")
                    || args[0].equalsIgnoreCase("c")) {

                if (!PrimeShop.has_player_Permission_for_this_Command(cs,
                        "PrimeShop.admin.createShops")) {

                    cs.sendMessage(Message_Handler.resolve_to_message(94));
                    return true;
                }

                if (!(args.length == 3)) {
                    cs.sendMessage("");
                    cs.sendMessage(ChatColor.LIGHT_PURPLE
                            + "/PrimeShop create <shopname> <displayItem>");
                    cs.sendMessage(ChatColor.GRAY
                            + "Example:   /PrimeShop create Wool_Shop 35:5");
                    return true;
                }

                ItemStack displayitem = PrimeShop
                        .convert_random_String_to_ItemStack(args[2], cs);

                if (displayitem == null) {
                    cs.sendMessage("");
                    // cs.sendMessage(ChatColor.RED
                    // + Message_Handler.resolve_to_message(96));
                    return true;

                }

                for (Shop a : PrimeShop.hashMap_Shops.values()) {
                    if (a.shopname.toUpperCase()
                            .contains(args[1].toUpperCase())
                            || args[1].toUpperCase().contains(
                                    a.shopname.toUpperCase())) {
                        cs.sendMessage(Message_Handler.resolve_to_message(126));
                        return true;
                    }
                }

                // duplicated DisplayIcon
                ItemStack itemA = displayitem;
                boolean match = false;
                for (Shop s : PrimeShop.hashMap_Shops.values()) {
                    ItemStack itemB = s.displayIcon;

                    if (itemA.getType() == itemB.getType()) {
                        if (itemA.getData().getData() == itemB.getData()
                                .getData()) {
                            match = true;
                            Map<Enchantment, Integer> map = itemA
                                    .getEnchantments();

                            for (Enchantment key : map.keySet()) {
                                if (!itemB.containsEnchantment(key)) {
                                    match = false;
                                    continue;
                                }
                            }
                        }
                        if (match) {
                            cs.sendMessage(ChatColor.RED
                                    + "This display icon is already used by another shop. Please choose a different one and try again");
                            return true;
                        }
                    }
                }

                Shop shop = new Shop(args[1], displayitem);
                PrimeShop.hashMap_Shops.put(args[1], shop);
                cs.sendMessage("");
                cs.sendMessage(ChatColor.GREEN
                        + Message_Handler.resolve_to_message(95));
                PrimeShop.shopConfigHandler.write_shops_to_Harddisk();
                return true;

            }

            // Delete
            if (args[0].equalsIgnoreCase("delete")) {

                if (!(cs instanceof Player)) {
                    cs.sendMessage("");
                    cs.sendMessage(ChatColor.RED
                            + Message_Handler.resolve_to_message(101));
                    return true;
                }

                p = (Player) cs;
                if (!PrimeShop.has_player_Permission_for_this_Command(cs,
                        "PrimeShop.admin.deleteShops")) {
                    cs.sendMessage(Message_Handler.resolve_to_message(102));
                    return true;
                }

                if (!(args.length == 2)) {
                    cs.sendMessage("");
                    cs.sendMessage(ChatColor.LIGHT_PURPLE
                            + "/PrimeShop delete <shopname>");
                    return true;
                }

                String shopname = args[1];
                boolean found = false;
                Shop shop = null;

                for (Map.Entry<String, Shop> entry : PrimeShop.hashMap_Shops
                        .entrySet()) {
                    if (entry.getKey().toUpperCase()
                            .contains(shopname.toUpperCase())) {
                        found = true;
                        entry.getKey();
                        shop = entry.getValue();
                    }
                }
                if (!found) {
                    cs.sendMessage(ChatColor.RED
                            + Message_Handler.resolve_to_message(103, shopname));
                    return true;
                } else {
                    PrimeShop.open_InventoyInterface(p,
                            new Interface_delete_Shop(null, p, shop));
                    return true;
                }
            }

            // Rename
            if (args[0].equalsIgnoreCase("rename")) {

                if (!PrimeShop.has_player_Permission_for_this_Command(cs,
                        "PrimeShop.admin.createShops")) {

                    cs.sendMessage(Message_Handler.resolve_to_message(112));
                    return true;
                }

                if (!(args.length == 3)) {
                    cs.sendMessage("");
                    cs.sendMessage(ChatColor.LIGHT_PURPLE
                            + "/PrimeShop rename <originalName> <newName>");
                    return true;
                }

                String originalShopname = args[1];
                String newName = args[2];
                boolean found = false;

                for (Map.Entry<String, Shop> entry : PrimeShop.hashMap_Shops
                        .entrySet()) {
                    if (entry.getKey().toUpperCase()
                            .contains(originalShopname.toUpperCase())) {
                        // if (originalShopname.contains(entry.getKey())) {
                        found = true;
                        originalShopname = entry.getKey();
                    }
                }
                if (!found) {
                    cs.sendMessage(Message_Handler.resolve_to_message(98));
                    return true;
                } else {
                    Shop shop = PrimeShop.hashMap_Shops
                            .remove(originalShopname);
                    shop.shopname = newName;
                    PrimeShop.hashMap_Shops.put(newName, shop);
                    PrimeShop.shopConfigHandler.write_shops_to_Harddisk();
                    cs.sendMessage("");
                    cs.sendMessage(ChatColor.GREEN
                            + Message_Handler.resolve_to_message(113));
                    return true;
                }
            }

            // AddItem

            if (args[0].equalsIgnoreCase("add")
                    || args[0].equalsIgnoreCase("additem")) {
                if (!(args.length == 3)) {
                    cs.sendMessage("");
                    cs.sendMessage(ChatColor.LIGHT_PURPLE
                            + "/PrimeShop additem <shopname> <itemID>");
                    return true;
                }

                String originalShopname = args[1];
                boolean found = false;

                for (Map.Entry<String, Shop> entry : PrimeShop.hashMap_Shops
                        .entrySet()) {
                    if (entry.getKey().toUpperCase()
                            .contains(originalShopname.toUpperCase())) {
                        // if
                        // (originalShopname.equalsIgnoreCase(entry.getKey())) {
                        found = true;
                        originalShopname = entry.getKey();
                    }
                }
                if (!found) {
                    cs.sendMessage("");
                    cs.sendMessage(ChatColor.RED
                            + Message_Handler.resolve_to_message(98));
                    return true;
                } else {
                    Shop shop = PrimeShop.hashMap_Shops.get(originalShopname);

                    if (!PrimeShop.has_player_Permission_for_this_Command(cs,
                            "PrimeShop.admin.addItemsToShop." + shop.shopname)) {
                        cs.sendMessage(Message_Handler.resolve_to_message(114));
                        return true;
                    }

                    ItemStack item_to_be_added = PrimeShop
                            .convert_random_String_to_ItemStack(args[2], cs);

                    if (item_to_be_added != null) {

                        if (!(cs instanceof Player)) {
                            cs.sendMessage("");
                            cs.sendMessage(ChatColor.RED
                                    + Message_Handler.resolve_to_message(27));
                            return true;
                        }

                        p = (Player) cs;

                        if (item_to_be_added.getTypeId() == 401) {
                            // TODO potions warning
                            p.sendMessage(ChatColor.RED
                                    + " Firework, Potions, Colores Leather Armor, Maps and some Books are not supported in this version of PrimeShop ");
                            return true;
                        }

                        PrimeShop.open_InventoyInterface(p,
                                new Interface_add_Item_to_Shop(
                                        new ArrayList<InventoryInterface>(), p,
                                        item_to_be_added, shop));

                        return true;
                    }
                }
                return true;
            }

            // AddInventory

            if (args[0].equalsIgnoreCase("addinventory")) {

                if (!(cs instanceof Player)) {
                    cs.sendMessage(Message_Handler.resolve_to_message(27));
                    return true;
                }

                p = (Player) cs;

                if (!(args.length == 3)) {
                    cs.sendMessage("");
                    cs.sendMessage(Message_Handler.resolve_to_message(115));
                    cs.sendMessage(ChatColor.LIGHT_PURPLE
                            + "/PrimeShop addinventory <shopname> confirm");
                    return true;
                }

                if (!args[2].equalsIgnoreCase("confirm")) {
                    cs.sendMessage("");
                    p.sendMessage(ChatColor.RED
                            + "WARNING: All your Items will be deleted!");
                    p.sendMessage(ChatColor.RED
                            + "You have to add \"confirm\" at the end of this command");
                    return true;
                }

                String shopname = args[1];
                boolean match = false;
                for (Map.Entry<String, Shop> entry : PrimeShop.hashMap_Shops
                        .entrySet()) {
                    if (entry.getKey().toUpperCase()
                            .contains(shopname.toUpperCase())) {
                        match = true;
                        shopname = entry.getKey();
                    }
                }
                if (!match) {
                    cs.sendMessage(ChatColor.RED
                            + Message_Handler.resolve_to_message(98));
                    return true;
                } else {
                    Shop shop = PrimeShop.hashMap_Shops.get(shopname);
                    if (!PrimeShop.has_player_Permission_for_this_Command(cs,
                            "PrimeShop.admin.addItemsToShop." + shop.shopname)) {
                        cs.sendMessage(Message_Handler.resolve_to_message(114));
                        return true;
                    }

                    for (ItemStack item : p.getInventory()) {
                        if (item != null) {
                            if (!item.getType().equals(Material.AIR)) {
                                shop.add_ItemStack(item);
                            }
                        }
                    }
                    p.getInventory().clear();
                    cs.sendMessage(Message_Handler.resolve_to_message(116));
                    PrimeShop.shopConfigHandler.write_shops_to_Harddisk();
                    PrimeShop.open_InventoyInterface(p,
                            new Interface_Shop_Page(null, p, shop, 1));
                    return true;
                }
            }

            // AddALL

            // ////////////////////////

            // if (args[0].equalsIgnoreCase("addall")) {
            // if (!(args.length == 2)) {
            // cs.sendMessage(ChatColor.LIGHT_PURPLE
            // + "/PrimeShop addall <shopname>");
            // return true;
            // }
            //
            // String originalShopname = args[1];
            // boolean found = false;
            //
            // for (Map.Entry<String, Shop> entry : PrimeShop.hashMap_Shops
            // .entrySet()) {
            // if (entry.getKey().toUpperCase()
            // .contains(originalShopname.toUpperCase())) {
            // found = true;
            // originalShopname = entry.getKey();
            // }
            // }
            // if (!found) {
            // cs.sendMessage(ChatColor.RED
            // + Message_Handler.resolve_to_message(98));
            // return true;
            // } else {
            // Shop shop = PrimeShop.hashMap_Shops.get(originalShopname);
            //
            // Iterator<Recipe> iter = this.plugin.getServer()
            // .recipeIterator();
            // while (iter.hasNext()) {
            // Recipe recipe = iter.next();
            // shop.add_ItemStack(recipe.getResult());
            // }
            // shop.refresh_pageCount();
            // PrimeShop.shopConfigHandler.write_shops_to_Harddisk();
            // return true;
            // }
            // }

            // Get ID

            if (args[0].equalsIgnoreCase("id")) {

                if (!(cs instanceof Player)) {
                    cs.sendMessage("Du kannst diesen Befehl nicht in der Console �ffnen!");
                    return true;
                }

                p = (Player) cs;

                p.sendMessage(PrimeShop.convertItemStacktoToIdString(p
                        .getItemInHand()));
                p.sendMessage(p.getItemInHand().getItemMeta().toString());

                return true;
            }

            // NPC
            if (args[0].equalsIgnoreCase("npc")) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(ChatColor.RED
                            + Message_Handler.resolve_to_message(27));
                    return true;
                }

                p = (Player) cs;

                if (!PrimeShop.citezens_is_enabled) {
                    p.sendMessage(ChatColor.RED
                            + "The Citizens plugin seems not to be installed :(");
                    return true;
                }

                // Add Shop to NPC

                if (args.length >= 2) {

                    if (args[1].equalsIgnoreCase("addshop")
                            || args[1].equalsIgnoreCase("add")
                            || args[1].equalsIgnoreCase("link")
                            || args[1].equalsIgnoreCase("give")) {

                        if (!(args.length == 3)) {
                            cs.sendMessage(ChatColor.LIGHT_PURPLE
                                    + "/PrimeShop NPC addshop <shopname>");
                            return true;
                        }

                        String originalShopname = args[2];
                        boolean found = false;

                        for (Map.Entry<String, Shop> entry : PrimeShop.hashMap_Shops
                                .entrySet()) {
                            if (entry.getKey().toUpperCase()
                                    .contains(originalShopname.toUpperCase())) {
                                found = true;
                                originalShopname = entry.getKey();
                            }
                        }
                        if (!found) {
                            cs.sendMessage(ChatColor.RED
                                    + Message_Handler.resolve_to_message(98));
                            return true;
                        } else {
                            Shop shop = PrimeShop.hashMap_Shops
                                    .get(originalShopname);
                            if (!PrimeShop
                                    .has_player_Permission_for_this_Command(cs,
                                            "PrimeShop.admin.addShopsToNPCs."
                                                    + shop.shopname)) {
                                cs.sendMessage(ChatColor.RED
                                        + Message_Handler
                                                .resolve_to_message(117));
                                return true;
                            }

                            BlockIterator iterator = new BlockIterator(
                                    p.getWorld(), p.getLocation().toVector(), p
                                            .getEyeLocation().getDirection(),
                                    0, 100);
                            while (iterator.hasNext()) {
                                Block item = iterator.next();
                                for (Entity entity : p.getNearbyEntities(100,
                                        100, 100)) {
                                    if (CitizensAPI.getNPCRegistry().isNPC(
                                            entity)) {
                                        int acc = 2;
                                        for (int x = -acc; x < acc; x++) {
                                            for (int z = -acc; z < acc; z++) {
                                                for (int y = -acc; y < acc; y++) {
                                                    if (entity
                                                            .getLocation()
                                                            .getBlock()
                                                            .getRelative(x, y,
                                                                    z)
                                                            .equals(item)) {
                                                        CitizensAPI
                                                                .getNPCRegistry()
                                                                .isNPC(entity);
                                                        net.citizensnpcs.api.npc.NPC a = CitizensAPI
                                                                .getNPCRegistry()
                                                                .getNPC(entity);
                                                        a.getEntity()
                                                                .setVelocity(
                                                                        new Vector(
                                                                                0,
                                                                                1,
                                                                                0));
                                                        // entity.getLocation().getWorld().strikeLightning(entity.getLocation());
                                                        if (PrimeShop.plugin
                                                                .add_Shop_to_NPC(
                                                                        shop,
                                                                        a.getId())) {
                                                            PrimeShop.shopConfigHandler
                                                                    .write_shops_to_Harddisk();
                                                            p.sendMessage(Message_Handler
                                                                    .resolve_to_message(
                                                                            119,
                                                                            a.getFullName(),
                                                                            originalShopname));
                                                        } else {
                                                            p.sendMessage(Message_Handler
                                                                    .resolve_to_message(118));
                                                        }
                                                        return true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("create")) {

                        p.sendMessage("");
                        p.sendMessage("");
                        p.sendMessage("");
                        cs.sendMessage("------------------>");
                        p.sendMessage("To create NPCs you have to use the Citizens standart command:");
                        p.sendMessage(" /NPC create <name> --type Villager");
                        cs.sendMessage("------------------>");

                        return true;
                    }
                }

                for (int i = 0; i < 2; i++) {
                    cs.sendMessage("");
                }
                cs.sendMessage("------------------>");
                cs.sendMessage(" - " + ChatColor.LIGHT_PURPLE
                        + "/PrimeShop NPC addshop <shopname>");
                cs.sendMessage(" - " + ChatColor.LIGHT_PURPLE
                        + "/PrimeShop NPC create");
                cs.sendMessage("------------------>");

                return true;
            }
            // NPC ENDE
            // Done
        }

        while (true) {
            int i = 5;
            break;
        }

        // Hauptmenu
        for (int i = 0; i < 3; i++) {
            cs.sendMessage("");
        }
        cs.sendMessage(ChatColor.GRAY + "                         PrimeShop");
        cs.sendMessage(" - " + ChatColor.GOLD + "open " + ChatColor.AQUA
                + "<shopname>");
        cs.sendMessage(" - " + ChatColor.GOLD + "list");

        // ADMIN BASIC PERMISSION
        if (!PrimeShop.has_player_Permission_for_this_Command(cs,
                "PrimeShop.admin.basics")) {
            return true;
        }

        cs.sendMessage(" - " + ChatColor.GOLD + "create " + ChatColor.AQUA
                + "<shopname> <itemID | itemName | hand>");
        cs.sendMessage(" - " + ChatColor.GOLD + "rename " + ChatColor.AQUA
                + "<shopname> <new-name>");
        cs.sendMessage(" - " + ChatColor.GOLD + "delete " + ChatColor.AQUA
                + "<shopname>");
        cs.sendMessage(" - " + ChatColor.GOLD + "additem " + ChatColor.AQUA
                + "<shopname> <itemID>");
        cs.sendMessage(" - " + ChatColor.GOLD + "addinventory "
                + ChatColor.AQUA + "<shopname>");
        cs.sendMessage(" - " + ChatColor.GOLD + "NPC addshop " + ChatColor.AQUA
                + "<shopname>");
        cs.sendMessage(" - " + ChatColor.GOLD + "NPC create");
        return true;

    }

}