package eu.Blockup.PrimeShop.InventoryInterfaces.Buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;

public class Button_close_Interface extends Button {

	public Button_close_Interface() {
		super(Cofiguration_Handler.closeButton_ItemStack, Message_Handler.resolve_to_message(34), Message_Handler.resolve_to_message(35));
	}
	
	@Override
	public void onClick(InventoryInterface inventoryInterface, Player player, ItemStack cursor, ItemStack current, ClickType type) {
		PrimeShop.close_InventoyInterface(player);
	}

}
