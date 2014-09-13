package eu.Blockup.PrimeShop.PricingEngine.Item_Analysis;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Recepie_Node_of_ItemBloodline {
    public Item_Node_of_ItemBloodline parentNode;
    public int parentRecepieItemAmount;
    public List<Item_Node_of_ItemBloodline> listOfTreeNode;
    private boolean evilRecepie;

    public boolean write_Objects_to_SQL() {
	boolean result = true;
	for (Item_Node_of_ItemBloodline temp_node : this.listOfTreeNode) {
	    result = result && temp_node.write_Objects_to_SQL();
	}

	return result;
    }

    public Recepie_Node_of_ItemBloodline clone(Item_Node_of_ItemBloodline parent) {

	Recepie_Node_of_ItemBloodline result;
	result = new Recepie_Node_of_ItemBloodline(parent);

	for (Item_Node_of_ItemBloodline node : this.listOfTreeNode) {
	    result.listOfTreeNode.add(node.clone(result));
	}
	if (this.evilRecepie) {
	    result.setEvilRecepie(true);
	}
	result.parentRecepieItemAmount = this.parentRecepieItemAmount;
	return result;
    }

    public Recepie_Node_of_ItemBloodline(Item_Node_of_ItemBloodline parentNode) {
	this.parentNode = parentNode;
	this.listOfTreeNode = new ArrayList<Item_Node_of_ItemBloodline>();
	this.evilRecepie = false;
    }

    public boolean getEvilRecepie() {
	return evilRecepie;
    }

    public void setEvilRecepie(boolean evilRecepie) {
	this.evilRecepie = evilRecepie;
    }
    
	public List<ItemStack> collect_all_involved_items() {
    	
    	
    	List<ItemStack> result = new ArrayList<ItemStack>();
    	
    	
    	for (Item_Node_of_ItemBloodline itemNode : this.listOfTreeNode) {
    		result.addAll(itemNode.collect_all_involved_items());	
    	}
    	
    	return result;
    	
    }
 
// 

}
