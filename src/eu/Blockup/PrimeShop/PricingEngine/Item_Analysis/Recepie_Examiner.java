package eu.Blockup.PrimeShop.PricingEngine.Item_Analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.Other.Item_Comparer;
import eu.Blockup.PrimeShop.PricingEngine.DataHandling.EvaluatedRecipe;

public class Recepie_Examiner {

    private PrimeShop plugin;

    public Recepie_Examiner(PrimeShop plugin) {
	this.plugin = plugin;
    }

    // TODO Globaler iterator für alle Bukkit Rezept.. Welher verkleinert wird,
    // wann immer ein rezept ausgewertet wurden.

    public List<EvaluatedRecipe> findRecipesOfItem(ItemStack materialData) {

    	List<Recipe> recipies = PrimeShop.plugin.getServer().getRecipesFor(materialData);
    	
	List<EvaluatedRecipe> result = new ArrayList<EvaluatedRecipe>();
	EvaluatedRecipe tempEvaluatedRecepie;
	
	for ( Recipe recipe : recipies) {
						
	    if (Item_Comparer.do_Items_match(recipe.getResult(), materialData, true, false, true, true, true)) {

		// ShapedRecipe
		if (recipe instanceof ShapedRecipe) {

		    tempEvaluatedRecepie = (new EvaluatedRecipe("ShapedRecipe", recipe.getResult()));

		    Map<Character, ItemStack> itemMap = ((ShapedRecipe) recipe).getIngredientMap();
		    for (Character key : itemMap.keySet()) {
			if (itemMap.get(key) != null) {
			    tempEvaluatedRecepie.addImputItem(itemMap.get(key));
			}
		    }
		    itemMap.clear();
		    result.add(tempEvaluatedRecepie);
		    tempEvaluatedRecepie = null;
		}

		// FurnaceRecipe
		if (recipe instanceof FurnaceRecipe) {
		    tempEvaluatedRecepie = (new EvaluatedRecipe("FurnaceRecipe", recipe.getResult()));
		    tempEvaluatedRecepie.addImputItem(((FurnaceRecipe) recipe).getInput());
		    result.add(tempEvaluatedRecepie);
		    tempEvaluatedRecepie = null;
		}

		// ShapelessRecipe
		if (recipe instanceof ShapelessRecipe) {
		    tempEvaluatedRecepie = (new EvaluatedRecipe("FurnaceRecipe", recipe.getResult()));
		    List<ItemStack> itemlist = ((ShapelessRecipe) recipe).getIngredientList();
		    for (ItemStack listelement : itemlist) {
			if (listelement.clone() != null) {
			    tempEvaluatedRecepie.addImputItem(listelement.clone());
			}
		    }
		    result.add(tempEvaluatedRecepie);
		    tempEvaluatedRecepie = null;
		}
	    }
	}
	return result;
    }

}
