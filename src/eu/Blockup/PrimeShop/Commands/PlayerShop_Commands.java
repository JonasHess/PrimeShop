package eu.Blockup.PrimeShop.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.ChestShop.ChestShop;
import eu.Blockup.PrimeShop.ChestShop.Load_and_Store_Chestshops;
import eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.ChestShops.Interface_Verkaufen;

class PlayerShop_Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
            String[] args) {

        if (args.length > 0) {

            Player p;
            if (cs instanceof Player) {
                p = (Player) cs;
                // Verkaufen
                if (args[0].equalsIgnoreCase("verkaufen")) {

                    ChestShop cS = new ChestShop("asdf79as7df987sdf", 99.99D);

                    for (int i = 1; i < 100; i++) {
                        @SuppressWarnings("deprecation")
                        ItemStack item = new ItemStack(i);
                        cS.add_Item_to_Ankauf(item, 100);
                        cS.add_Item_to_Verkaufen(item, 100);
                        cS.add_Item_to_Mailbox(item, 100);
                    }

                    PrimeShop.open_InventoyInterface(p,
                            new Interface_Verkaufen(null, p, cS, 1));
                }
            }

            if (args[0].equalsIgnoreCase("füllen")) {
                ChestShop cS = new ChestShop("asdf79as7df987sdf", 99.99D);
                ItemStack item = new ItemStack(Material.DIAMOND_AXE);
                cS.add_Item_to_Ankauf(item, 100);
                cS.add_Item_to_Verkaufen(item, 100);
                cS.add_Item_to_Mailbox(item, 100);
                PrimeShop.hashMap_Chest_Shops.put("dfgsdfgdfg789dfg9", cS);

            }

            if (args[0].equalsIgnoreCase("speichern")) {
                Load_and_Store_Chestshops bla = new Load_and_Store_Chestshops();

                bla.write_shops_to_Harddisk();

            }

            if (args[0].equalsIgnoreCase("laden")) {
                Load_and_Store_Chestshops bla = new Load_and_Store_Chestshops();

                bla.read_shop_file();

            }
        }
        return true;
    }
}