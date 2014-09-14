package eu.Blockup.PrimeShop.InventoryInterfaces.Buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.PricingEngine.DataHandling.Shop_Item;


	public class Button_pricetag_Price extends Button {
		
		private Shop_Item sqlItem;
		
		public Button_pricetag_Price(Shop_Item sqlItem, Material type) {
			super(type, "");
			this.sqlItem = sqlItem;
			refresh_Price();
		}
		

		
// TODO Remove unused code found by UCDetector
// 		public Button_pricetag_Price(Shop_Item sqlItem, Material type, short damage) {
// 			this(sqlItem, type);
// 			super.setDamage(damage);
// 		}
		
		
		@Override
		public void onClick(InventoryInterface inventoryInterface, Player player, ItemStack cursor, ItemStack current, ClickType type) {
			;
		}
		
		void refresh_Price() {
			String defualtPrice = PrimeShop.economy.format(sqlItem.initial_price.getValue());
			String currentPrice = PrimeShop.economy.format(sqlItem.calculate_price(sqlItem.timesItemWasBought.getValue(), sqlItem.timesItemWasSold.getValue()));
			this.setName(Message_Handler.resolve_to_message(19));
			this.setDescription(Message_Handler.resolve_to_message(20,defualtPrice), Message_Handler.resolve_to_message(21,currentPrice));
			
			
			
		}

	}