package eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickHandler;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_modify_Price;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_close_Interface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_Amount;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_with_no_task;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_pricetag_Price;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_Buy_Sell_Item;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.PricingEngine.Item_Trader;
import eu.Blockup.PrimeShop.PricingEngine.Pool_of_Item_Traders;
import eu.Blockup.PrimeShop.PricingEngine.DataHandling.Shop_Item;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.ReturnObjects.ReturnPrice;
import eu.Blockup.PrimeShop.Shops.Page;
import eu.Blockup.PrimeShop.Shops.Shop;

public class Interface_Change_Price extends InventoryInterface {

	private Shop_Item sqlItem;
	
	@SuppressWarnings("deprecation")
	public Interface_Change_Price(final List<InventoryInterface> link_Back_Stack, Player player, final ItemStack itemStack_to_be_bought) {
		super(Message_Handler.resolve_to_message(64), 4, link_Back_Stack); // TODO Correct ItemName

//		for (int i = 0; i<position_in_Stack; i++) {
//			InventoryInterface  m = this.branch_back_Stack.get(i);
//			if (m instanceof Interface_Change_Price) {
//			}
//		}
//		System.out.println("Such nach sich Selbst abgeschlossen!");
		
		this.setCloseable(false);

		this.setClickHandler(new ClickHandler() {
			@Override
			public boolean onClick(Player player, ItemStack cursor, ItemStack current, ClickType type) {
//				player.sendMessage("Du hast gedrückt");
				return false;

			}
		});

		// Background Icons

		for (int a = 0; a < this.getWidth(); a++) {
			for (int b = 0; b < this.getHeight(); b++) {
				this.addOption(a, b, new Button_with_no_task(Cofiguration_Handler.background_ItemStack, " "));
			}
		}

		// Current Price

		this.sqlItem = PrimeShop.get_Shop_Item_of_Itemstack(itemStack_to_be_bought);

		this.addOption(4, 0, new Button_pricetag_Price(sqlItem, Material.getMaterial(421)));

		// Changing Rate

		// +1000
		this.addOption(1, 2, new Button_modify_Price(sqlItem, 1000.0, Material.getMaterial(388), "+" + PrimeShop.economy.format(1000), ""));

		// -1000
		this.addOption(1, 3, new Button_modify_Price(sqlItem, -1000.0, Material.getMaterial(388), PrimeShop.economy.format(-1000), ""));

		// +100
		this.addOption(2, 2, new Button_modify_Price(sqlItem, 100.0, Material.getMaterial(264), "+" + PrimeShop.economy.format(100), ""));

		// -100
		this.addOption(2, 3, new Button_modify_Price(sqlItem, -100.0, Material.getMaterial(264), PrimeShop.economy.format(-100), ""));

		// +10
		this.addOption(3, 2, new Button_modify_Price(sqlItem, 10.0, Material.getMaterial(266), "+" + PrimeShop.economy.format(10), ""));

		// -10
		this.addOption(3, 3, new Button_modify_Price(sqlItem, -10.0, Material.getMaterial(266), PrimeShop.economy.format(-10), ""));

		// +1
		this.addOption(4, 2, new Button_modify_Price(sqlItem, 1.0, Material.getMaterial(265), "+" + PrimeShop.economy.format(1), ""));
		// -1
		this.addOption(4, 3, new Button_modify_Price(sqlItem, -1.0, Material.getMaterial(265), PrimeShop.economy.format(-1), ""));

		// +0,1
		this.addOption(5, 2, new Button_modify_Price(sqlItem, 0.1, Material.getMaterial(331), "+" + PrimeShop.economy.format(0.1), ""));

		// -0,1
		this.addOption(5, 3, new Button_modify_Price(sqlItem, -0.1, Material.getMaterial(331), PrimeShop.economy.format(-0.1), ""));

		// +0,01
		this.addOption(6, 2, new Button_modify_Price(sqlItem, 0.01, Material.getMaterial(263), "+" + PrimeShop.economy.format(0.01), ""));

		// -0,01
		this.addOption(6, 3, new Button_modify_Price(sqlItem, -0.01, Material.getMaterial(263), PrimeShop.economy.format(-0.01), ""));

		
		
		// Close Option
		this.addOption(8, 0, new Button_close_Interface());

		// Go Back Option
		if (position_in_Stack > 0) {

			this.addOption(0, 0, new Button(itemStack_to_be_bought, Message_Handler.resolve_to_message(61), Message_Handler.resolve_to_message(62)) {

				@Override
				public void onClick(InventoryInterface inventoryInterface, Player player, ItemStack cursor, ItemStack current, ClickType type) {
					PrimeShop.close_InventoyInterface(player);

					Interface_Buy_Sell_Item parentShop = (Interface_Buy_Sell_Item) inventoryInterface.branch_back_Stack.get(position_in_Stack - 1);
					for (Button button : parentShop.getButtons()) {

						if (button instanceof Button_Buy_Sell_Item) {
							try {
								((Button_Buy_Sell_Item) button).refresh_price();
							} catch (Exception e) {
								player.sendMessage(Message_Handler.resolve_to_message(13));
							}
						}
					}
					parentShop.refresh(player);
					PrimeShop.open_InventoyInterface(player, parentShop);
				}
			});

		}

	}

//	public static String get_Price2(ItemStack itemStack, boolean kaufen, int amount) {
//		Item_Trader itemTrader = Pool_of_Item_Traders.get_ItemTrader();
//		ReturnPrice ReturnSELL = itemTrader.get_Price_of_Itemstack(itemStack, amount, kaufen);
//		Pool_of_Item_Traders.return_Item_Trader(itemTrader);
//		itemTrader = null;
//
//		if (ReturnSELL.succesful) {
//			return "Price: " + PrimeShop.economy.format(ReturnSELL.price);
//		} else {
//			return "Error: You Can not buy this Item";
//		}
//	}
}
