package eu.Blockup.PrimeShop.PricingEngine.DataHandling;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;

public class Custom_Price_Links {

	
	

	public static boolean load_Custom_Price_Links() {
		File file;
		FileConfiguration cfg;
		
		try {
			file = new File("plugins/PrimeShop/", "custom_price_links.yml"); // TODO
																		// Name
																		// ändern
			cfg = YamlConfiguration.loadConfiguration(file);
			ItemStack ingrediant;
			ItemStack result;
			List<EvaluatedRecipe> list = new ArrayList<EvaluatedRecipe>();
			EvaluatedRecipe recepie;


			for (String resultID : cfg.getConfigurationSection("").getKeys(false)) {
				
				
				result = PrimeShop.convert_random_String_to_ItemStack(resultID, null);
				
				int resultAmount = cfg.getInt(resultID + ".Amount");
				
				result.setAmount(resultAmount);
				recepie = new EvaluatedRecipe("ShapedRecipe", result);

				for (String  ingredientID: cfg.getConfigurationSection(resultID + ".Ingredients").getKeys(false)) {
					int ingredientAmount = cfg.getInt(resultID + "." + ingredientID + ".Amount");
					
					if (ingredientAmount < 1) ingredientAmount = 1;
					ingrediant = PrimeShop.convert_random_String_to_ItemStack(ingredientID, null);
					ingrediant.setAmount(ingredientAmount);
					recepie.addImputItem(ingrediant);
				}
				
				list.add(recepie);
				
				PrimeShop.hashMap_EvaluatedRecipe.put(PrimeShop.convertItemStacktoToIdString(result), list);
				
				list = new ArrayList<EvaluatedRecipe>();
			}	
		} catch (Exception e1) {
			PrimeShop.plugin.getLogger().log(Level.SEVERE, ChatColor.RED + "Error reading Custom_Price_Links.yml"); // TODO
			e1.printStackTrace();
			return false;
		}

		return true; // Todo Error handling
	}
	
	
	
	
	
	
//	@SuppressWarnings("deprecation")
//	public static void addCustomPriceLinks () {
//		
//		ItemStack ingrediant;
//		ItemStack result;
//		List<EvaluatedRecipe> list = new ArrayList<EvaluatedRecipe>();
//		
//	// Wasser Eimer
//		result =  new ItemStack(326);		
//		EvaluatedRecipe recepie = new EvaluatedRecipe("Shapeless", result);
//	
//		ingrediant = new ItemStack(Material.POTION, 1);
//		recepie.addImputItem(ingrediant);
//		list.add(recepie);	
//	
//		Blockup_Economy.hashMap_EvaluatedRecipe.put(Blockup_Economy.convertItemStacktoToIdString(result), list);
//	}
	
	
	
	

	
	
	
}
