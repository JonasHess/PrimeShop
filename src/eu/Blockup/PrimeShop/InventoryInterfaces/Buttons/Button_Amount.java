package eu.Blockup.PrimeShop.InventoryInterfaces.Buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;
import eu.Blockup.PrimeShop.Other.Message_Handler;

public class Button_Amount extends Button {

	private boolean checkMaxStackSize = false;

	private int minCount = 1;
	private int maxCount = 64;

	public Button_Amount(Material type) {
		super(type);
	}

	public Button_Amount(Material type, String name, String... description) {
		super(type, name, description);
	}

	public Button_Amount(Material type, short damage, String name, String... description) {
		super(type, damage, name, description);
	}
	
	public Button_Amount(ItemStack itemStack , String name, String... description) {
		super(itemStack, name, description);
	}

	public final Button_Amount setCheckMaxStackSize(boolean check) {
		this.checkMaxStackSize = check;
		return this;
	}

	public final boolean getCheckMaxStackSize() {
		return this.checkMaxStackSize;
	}

	public final Button_Amount setMinCount(int count) {
		this.minCount = count < 1 ? 1 : count;
		return this;
	}

	public final int getMinCount() {
		return this.minCount;
	}

	public final Button_Amount setMaxCount(int count) {
		this.maxCount = count > 64 ? count : 64;
		return this;
	}

	public final int getMaxCount() {
		return this.maxCount;
	}

	@Override
	public void onClick(InventoryInterface inventoryInterface, Player player, ItemStack cursor, ItemStack current, ClickType type) {
		int amount = this.getAmount();
		if (type == ClickType.LEFT) {
			amount = amount + 1;
		} else if (type == ClickType.SHIFT_LEFT) {
			amount = this.getMaxCount();
		} else if (type == ClickType.RIGHT) {
			amount = amount - 1;
		} else if (type == ClickType.SHIFT_RIGHT) {
			amount = this.getMinCount();
		}
		if (this.getCheckMaxStackSize()) {
			int maxAmount = current.getType().getMaxStackSize();
			if (amount > maxAmount) {
				amount = maxAmount;
			}
		}
		if (amount <= 0) {
			amount = 1;
		}
		this.setAmount(amount);

		// //////////////////////

		for (Button button : inventoryInterface.getButtons()) {

			if (button instanceof Button_Buy_Sell_Item) {
				try {
					((Button_Buy_Sell_Item) button).refresh_price(amount);
				} catch (Exception e) {
					player.sendMessage(Message_Handler.resolve_to_message(1));
				}
				// } finally {
				// menu.refresh(player);
				// }
			}
		}

		// ///////////////////////
		inventoryInterface.refresh(player);
	}

}
