package eu.Blockup.PrimeShop.InventoryInterfaces.Buttons;

import java.text.DecimalFormat;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.PricingEngine.DataHandling.Shop_Item;


	public class Button_pricetag_Rate extends Button {
		
		private Shop_Item sqlItem;
		
		public Button_pricetag_Rate(Shop_Item sqlItem, Material type) {
			super(type, "");
			this.sqlItem = sqlItem;
			refresh_Price();
		}
		

		
		public Button_pricetag_Rate(Shop_Item sqlItem, Material type, short damage) {
			this(sqlItem, type);
			super.setDamage(damage);
		}
		
		
		@Override
		public void onClick(InventoryInterface inventoryInterface, Player player, ItemStack cursor, ItemStack current, ClickType type) {
			;
		}
		
		void refresh_Price() {

		    DecimalFormat df = new DecimalFormat("#.00");
		    String rate_of_price_change = df.format(sqlItem.rate_of_price_change.getValue());
//			rate_of_price_change += " Items";
			String currentPrice = PrimeShop.economy.format(sqlItem.calculate_price(sqlItem.timesItemWasBought.getValue(), sqlItem.timesItemWasSold.getValue()));
			this.setName(Message_Handler.resolve_to_message(43));
			this.setDescription(Message_Handler.resolve_to_message(23, rate_of_price_change), Message_Handler.resolve_to_message(21,currentPrice));
			
			
		}

	}
