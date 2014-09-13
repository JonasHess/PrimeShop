package eu.Blockup.PrimeShop.PricingEngine.Enchantments;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import eu.Blockup.PrimeShop.PrimeShop;

public class LoadEnchantments {
	File file;
	FileConfiguration cfg;

	public boolean read_enchantments() {

		PrimeShop.hashMap_Shops.clear();

		try {
			file = new File("plugins/Blockup_Econnomy/", "enchantments.yml");

			cfg = YamlConfiguration.loadConfiguration(file);


			for (String name : cfg.getConfigurationSection("").getKeys(
					false)) {

				int id = cfg.getInt(name+ ".ID");
				int level = cfg.getInt(name+ ".ID");
				int price = cfg.getInt(name+ ".ID");
				
				EnchantmentHandler.add_Enchantment(new EnchantmentData(id, level, price));
				
			}
			
		} catch (Exception e1) {
			PrimeShop.plugin.getLogger().log(Level.SEVERE, ChatColor.RED + "Error reading enchantments.yml");
			e1.printStackTrace();
		}

		return true; 
	}
}
