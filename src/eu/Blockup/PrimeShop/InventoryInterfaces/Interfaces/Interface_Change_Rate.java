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
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_modify_Rate;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_close_Interface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_with_no_task;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_pricetag_Rate;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.PricingEngine.DataHandling.Shop_Item;

public class Interface_Change_Rate extends InventoryInterface {

	private Shop_Item sqlItem;
	
	@SuppressWarnings("deprecation")
	public Interface_Change_Rate(final List<InventoryInterface> link_Back_Stack, Player player, final ItemStack itemStack_to_modify) {
		super(Message_Handler.resolve_to_message(65), 4, link_Back_Stack);
		this.setCloseable(false);
		
		// Setzte den Vorgänger in die Variable parentShop	
		final Interface_Buy_Sell_Item parentShop = (Interface_Buy_Sell_Item) this.branch_back_Stack.get(position_in_Stack - 1);
		
		
		
		// Click Handler nicht benötigt..
		
		this.setClickHandler(new ClickHandler() {
			@Override
			public boolean onClick(Player player, ItemStack cursor, ItemStack current, ClickType type) {
//				player.sendMessage("Du hast gedrückt");
				return false;

			}
		});

		
		
		// Hintergrund Icons
		// TODO  in der Config verankern
		for (int a = 0; a < this.getWidth(); a++) {
			for (int b = 0; b < this.getHeight(); b++) {
				this.addOption(a, b, new Button_with_no_task(Cofiguration_Handler.background_ItemStack, " "));
			}
		}

		

		//  Price Tag -->  hat eigene Button Klasse
		this.sqlItem = PrimeShop.get_Shop_Item_of_Itemstack(itemStack_to_modify);
		this.addOption(4, 0, new Button_pricetag_Rate(sqlItem, Material.getMaterial(421)));
		

		// +1000
		this.addOption(0, 2, new Button_modify_Rate(sqlItem, 1000.0, Material.getMaterial(388), "+1000", ""));

		// -1000
		this.addOption(0, 3, new Button_modify_Rate(sqlItem, -1000.0, Material.getMaterial(388), "-1000", ""));

		// +100
		this.addOption(1, 2, new Button_modify_Rate(sqlItem, 100.0, Material.getMaterial(264), "+100", ""));

		// -100
		this.addOption(1, 3, new Button_modify_Rate(sqlItem, -100.0, Material.getMaterial(264), "-100", ""));

		// +10
		this.addOption(2, 2, new Button_modify_Rate(sqlItem, 10.0, Material.getMaterial(266), "+10", ""));

		// -10
		this.addOption(2, 3, new Button_modify_Rate(sqlItem, -10.0, Material.getMaterial(266), "-10", ""));

		// +1
		this.addOption(3, 2, new Button_modify_Rate(sqlItem, 1.0, Material.getMaterial(265), "+1", ""));
		// -1
		this.addOption(3, 3, new Button_modify_Rate(sqlItem, -1.0, Material.getMaterial(265), "-1", ""));

		// +0,1
		this.addOption(4, 2, new Button_modify_Rate(sqlItem, 0.1, Material.getMaterial(331), "+0.1", ""));

		// -0,1
		this.addOption(4, 3, new Button_modify_Rate(sqlItem, -0.1, Material.getMaterial(331), "-0.1", ""));

		// +0,01
		this.addOption(5, 2, new Button_modify_Rate(sqlItem, 0.01, Material.getMaterial(263), "+0.01", ""));

		// -0,01
		this.addOption(5, 3, new Button_modify_Rate(sqlItem, -0.01, Material.getMaterial(263), "-0.01", ""));

		
		
		// Set to default rate
		this.addOption(8, 2, new Button_modify_Rate(sqlItem, 3, Material.getMaterial(35), (short) 5,Message_Handler.resolve_to_message(26), ""));



		// Set to 0
		 this.addOption(8, 3, new Button_modify_Rate(sqlItem, 2 , Material.getMaterial(35), (short) 14, Message_Handler.resolve_to_message(25), ""));



		// Close Option
		this.addOption(8, 0, new Button_close_Interface());
		

		// Go Back Option
		if (position_in_Stack > 0) {
			ItemStack displayIcon = parentShop.itemStack_to_be_bought;

			this.addOption(0, 0, new Button(displayIcon, Message_Handler.resolve_to_message(61), Message_Handler.resolve_to_message(62)) {  // TODO

				@Override
				public void onClick(InventoryInterface inventoryInterface, Player player, ItemStack cursor, ItemStack current, ClickType type) {
					PrimeShop.close_InventoyInterface(player);
					PrimeShop.open_InventoyInterface(player, new Interface_Buy_Sell_Item(inventoryInterface.branch_back_Stack.get(position_in_Stack -2).branch_back_Stack, player, parentShop.shop, parentShop.itemStack_to_be_bought, parentShop.amount , parentShop.deleteable, false));
				}
			});

		}
//		// Go Back Option
//		if (position_in_Stack > 0) {
//			ItemStack displayIcon = parentShop.itemStack_to_be_bought;
//			
//			this.addOption(0, 0, new Option(displayIcon, "Back", "Go back to item") {  // TODO
//				
//				@Override
//				public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type) {
//					Blockup_Economy.closeMenu(player);
//					
//					for (Option option : parentShop.getOptions()) {
//						
//						if (option instanceof SellOption) {
//							try {
//								((SellOption) option).refresh_price();
//							} catch (Exception e) {
//								player.sendMessage("DID NOT WORK"); 
//							}
//						}
//					}
//					parentShop.refresh(player);
//					Blockup_Economy.openMenu(player, parentShop);
//				}
//			});
//			
//		}

	}

	
	//  
//	public static String get_Price2(ItemStack itemStack, boolean kaufen, int amount) {
//		Item_Trader itemTrader = Pool_of_Item_Traders.get_ItemTrader();
//		ReturnPrice ReturnSELL = itemTrader.get_Price_of_Itemstack(itemStack, amount, kaufen);
//		Pool_of_Item_Traders.return_Item_Trader(itemTrader);
//
//		if (ReturnSELL.succesful) {
//			return "Price: " + PrimeShop.economy.format(ReturnSELL.price);
//		} else {
//			return "Error: You Can not buy this Item";  // TODO
//		}
//	}
}
