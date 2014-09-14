package eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces;


import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickHandler;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_close_Interface;
import eu.Blockup.PrimeShop.InventoryInterfaces.Buttons.Button_with_no_task;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Item_Comparer;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.Shops.Shop;

class Interface_List_of_Subitems extends InventoryInterface {

	final private Interface_List_of_Subitems current_Menu;
	
//	@SuppressWarnings("deprecation")
	public Interface_List_of_Subitems(final List<InventoryInterface> link_Back_Stack, final ItemStack resultItem, final Shop shop,Player player) {
		super(Message_Handler.resolve_to_message(80), 6, link_Back_Stack);
//		this.setCloseable(false);
		this.current_Menu = this;

		this.setClickHandler(new ClickHandler() {
			@Override
			public boolean onClick(Player player, ItemStack cursor,
					ItemStack current, ClickType type) {
				
//				Blockup_Economy.closeMenu(player);
//				player.sendMessage("Du hast gedrückt");
				
//				Blockup_Economy.openMenu(player, new Shop_add_Items(player, current_Menu, cursor, shop));
				
				return false;

			}
		});
		
		final List<ItemStack> list_of_SubItems = PrimeShop.get_all_SubItems_of_ItemStack(resultItem);

		// Add Items to Menu
		int amount_of_items = list_of_SubItems.size();
		int itemsAddedItems = 0;

		for (int a = 0; a < this.getWidth(); a++) {
			for (int b = 0; b < this.getHeight(); b++) {
				this.addOption(a, b, new Button_with_no_task(Cofiguration_Handler.background_ItemStack, " "));
			}
		}

		
		this.addOption(4, 0, new Button_with_no_task(resultItem,
				Message_Handler.resolve_to_message(81), Message_Handler.resolve_to_message(82) , ""));

		for (int y = 2; y < 5; y++) {
			for (int x = 0; x < 9; x++) {

				this.removeOption(x, y);
				if (itemsAddedItems < amount_of_items) {

					ItemStack currentItem;
					try {
						currentItem = list_of_SubItems.get(itemsAddedItems);
					} catch (Exception e) {
						PrimeShop.plugin.getLogger().log(Level.SEVERE, "Internal Error finding Item in list with subitems");

						e.printStackTrace();
						return;
					}

					this.addOption(x, y, new Button(currentItem, Message_Handler.resolve_to_message(83), Message_Handler.resolve_to_message(84)) {

						@Override
						public void onClick(InventoryInterface inventoryInterface, Player player,
								ItemStack cursor, ItemStack current,
								ClickType type) {
							if (current != null) {
								if (!current.getType().equals(Material.AIR)){
									for (ItemStack compared_Item : list_of_SubItems) {
										if (Item_Comparer.do_Items_match(compared_Item, current, true, false, true, false, true)) {
											PrimeShop.close_InventoyInterface(player);
											PrimeShop.open_InventoyInterface(player, new Interface_Buy_Sell_Item(link_Back_Stack, player, shop, compared_Item, 1, false, false));
											break;
										}
									}
								}
							}

						}
					});
					itemsAddedItems++;

				}
			}
		}

		// Disable PriceLinking
		this.addOption(8, 5, new Button(Material.TRAP_DOOR, Message_Handler.resolve_to_message(85), Message_Handler.resolve_to_message(86)) {
			
			@Override
			public void onClick(InventoryInterface inventoryInterface, Player player, ItemStack cursor,
				ItemStack current, ClickType type) {
				PrimeShop.plugin.disable_PriceLinking_for_Item(resultItem);
				PrimeShop.close_InventoyInterface(player);
				PrimeShop.open_InventoyInterface(player, inventoryInterface.branch_back_Stack.get(position_in_Stack -2)); // TODO go back in Stack
				
			}
		});

		// Close Option

		this.addOption(8, 0, new Button_close_Interface());

		// Go Back Option
		if (position_in_Stack > 0) {
			this.addOption(0, 0, new Button(resultItem,
					Message_Handler.resolve_to_message(61), Message_Handler.resolve_to_message(62)) {

				@Override
				public void onClick(InventoryInterface inventoryInterface, Player player, ItemStack cursor,
						ItemStack current, ClickType type) {
					PrimeShop.close_InventoyInterface(player);
					
					InventoryInterface getBack_Interface;
					if (inventoryInterface.parentMenu instanceof Interface_Buy_Sell_Item) {
						Interface_Buy_Sell_Item parent_Buy_Sell = (Interface_Buy_Sell_Item) inventoryInterface.parentMenu;
						getBack_Interface = (InventoryInterface) new  Interface_Buy_Sell_Item(parent_Buy_Sell.get_brnach_back_list_of_parentMenu(), player, parent_Buy_Sell.shop, parent_Buy_Sell.itemStack_to_be_bought, 1, parent_Buy_Sell.deleteable, false);
					}else {
						getBack_Interface = inventoryInterface.parentMenu;
					}
					PrimeShop.open_InventoyInterface(player, getBack_Interface);
				}
			});

		}
//		// Go Back Option
//		if (position_in_Stack > 0) {
//			this.addOption(0, 0, new Button(resultItem,
//					"Back", "Go Back") {
//				
//				@Override
//				public void onClick(InventoryInterface inventoryInterface, Player player, ItemStack cursor,
//						ItemStack current, ClickType type) {
//					PrimeShop.closeMenu(player);
//					PrimeShop.openMenu(player, inventoryInterface.branch_back_Stack.get(position_in_Stack -1));
//				}
//			});
//			
//		}
		
		


				
				

	}
}