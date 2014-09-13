package eu.Blockup.PrimeShop.PricingEngine.Enchantments;

public class EnchantmentData {
	public int enchantmentID;
	public int enchantmentLevel;
	public double price;

	public EnchantmentData(int enchantmentID, int enchantmentLevel,
			double enchantmentPrice) {
		this.enchantmentID = enchantmentID;
		this.enchantmentLevel = enchantmentLevel;
		this.price = enchantmentPrice;
	}

	public int getEnchantmentID() {
		return enchantmentID;
	}

	public int getEnchantmentLevel() {
		return enchantmentLevel;
	}

	public double getPrice() {
		return price;
	}

}
