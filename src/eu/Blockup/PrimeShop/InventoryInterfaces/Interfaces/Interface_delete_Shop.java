package eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_with_no_task;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.Shops.Shop;

public class Interface_delete_Shop extends InventoryInterface{

	@SuppressWarnings("deprecation")
	public Interface_delete_Shop(final List<InventoryInterface> link_Back_Stack, Player player, final Shop shop) {
		super(Message_Handler.resolve_to_message(75), 4, link_Back_Stack); // TODO Correct ItemName

		this.setCloseable(false);


		// Empty Options

		for (int a = 0; a < this.getWidth(); a++) {
			for (int b = 0; b < this.getHeight(); b++) {
				this.addOption(a, b,
						new Button_with_no_task(Cofiguration_Handler.background_ItemStack, " "));
			}
		}

		// Icon

		this.addOption(4, 0, new Button_with_no_task(shop.displayIcon,
				Message_Handler.resolve_to_message(78), Message_Handler.resolve_to_message(45)));

		// Yes
		this.addOption(3, 2, new Button(Material.WOOL, (short) 5, Message_Handler.resolve_to_message(79)) {
			
			@Override
			public void onClick(InventoryInterface inventoryInterface, Player player,
					ItemStack cursor, ItemStack current, ClickType type) {
				
				for (List<Shop> value :PrimeShop.hashMap_CitizensNPCs.values()) {
				    value.remove(shop);
				}
				PrimeShop.hashMap_Shops.remove(shop.shopname);
				player.sendMessage(ChatColor.GREEN
						+ Message_Handler.resolve_to_message(76));
				PrimeShop.shopConfigHandler.write_shops_to_Harddisk();
				PrimeShop.close_InventoyInterface(player);
			}
		});

		// No
		this.addOption(5, 2, new Button(Material.WOOL, (short) 14,
				Message_Handler.resolve_to_message(77), "") {

			@Override
			public void onClick(InventoryInterface inventoryInterface,
					Player player, ItemStack cursor, ItemStack current,
					ClickType type) {
				PrimeShop.close_InventoyInterface(player);
				if (inventoryInterface.parentMenu != null) {
					PrimeShop.open_InventoyInterface(player, parentMenu);
				}
			}
		});
	}
}