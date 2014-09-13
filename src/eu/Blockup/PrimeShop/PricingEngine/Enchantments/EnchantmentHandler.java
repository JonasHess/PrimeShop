package eu.Blockup.PrimeShop.PricingEngine.Enchantments;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentHandler {

	private static List<EnchantmentData> list_with_Enchantment_Prices = new ArrayList<EnchantmentData>();
	
	
	public EnchantmentHandler () {
		list_with_Enchantment_Prices = new ArrayList<EnchantmentData>();
	}
	
	public static void add_Enchantment(EnchantmentData enchantmentData) {
		list_with_Enchantment_Prices.add(enchantmentData);
	}
	
//	public static double getPrice(int id, int level) {
//	    //id is deprecated!
//		for (EnchantmentData d : list_with_Enchantment_Prices){
//			if ((d.getEnchantmentID() == id) && (d.getEnchantmentLevel() == level)) {
//				return d.price;
//				
//			}
//		}
//		return 0;	
//	}
	
    public static double getPrice(String name, Integer level) {
        for (EnchantmentData d : list_with_Enchantment_Prices){
//            Logger.getLogger("Minecraft").info
//                ("DEBUG: getPrice checks "+d.getName()+" with level "+d.getEnchantmentLevel());
            
            if (d.getName().equalsIgnoreCase(name) && (d.getEnchantmentLevel() == level)) {
                return d.price;
                
            }
        }
//        Logger.getLogger("Minecraft").info
//            ("DEBUG: getPrice did not find enchantment "+name+" with level "+level);
        return 0;   
    }
	
	public static void clear_List () {
		list_with_Enchantment_Prices.clear();
	}
}
