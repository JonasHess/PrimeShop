package eu.Blockup.PrimeShop.PricingEngine.DataHandling;

import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.ReturnObjects.ReturnBoolean;

public class Shop_Item_Stack {
	private Shop_Item sql_Item;
	private double amount;
	private boolean itemWasCrafted; // TODO entfernen... nicht benötigt

	public Shop_Item_Stack clone() {
		Shop_Item_Stack result = new Shop_Item_Stack((this.sql_Item.clone()), this.getAmount(), (true));
		return result;
	}

	public Shop_Item_Stack(Shop_Item sql_Item, double amount, boolean itemWasCrafted) {
		this.sql_Item = sql_Item;
		// TODO remove item was crafted varible. It is not needed
		this.amount = amount;
		this.itemWasCrafted = itemWasCrafted;
	}

	public double getAmount() {
		return amount;
	}

	// Setter & Getter;
	public Shop_Item getSql_Item() {
		return sql_Item;
	}

	public boolean isItemWasCrafted() {
		return itemWasCrafted;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setItemWasCrafted(boolean itemWasCrafted) {
		this.itemWasCrafted = itemWasCrafted;
	}

	public void setSql_Item(Shop_Item sql_Item) {
		this.sql_Item = sql_Item;
	}

}
