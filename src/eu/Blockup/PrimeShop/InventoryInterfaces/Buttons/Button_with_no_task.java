package eu.Blockup.PrimeShop.InventoryInterfaces.Buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;

public class Button_with_no_task extends Button {
	
	public Button_with_no_task(Material type, String message) {
		super(type, message);
	}
	
	public Button_with_no_task(Material type,String name, String... description) {
		super(type, name, description);
	}
	
	public Button_with_no_task(Material type, short damage, String message) {
		this(type, message);
		super.setDamage(damage);
	}
	
	public Button_with_no_task(Material type, short damage, int amount, String message) {
		this(type, damage, message);
		super.setAmount(amount);
	}
	
	public Button_with_no_task(ItemStack displayItem, String name, String... description) {
		super(displayItem, name, description);
	}
	
	@Override
	public void onClick(InventoryInterface inventoryInterface, Player player, ItemStack cursor, ItemStack current, ClickType type) {
		;
	}

}
