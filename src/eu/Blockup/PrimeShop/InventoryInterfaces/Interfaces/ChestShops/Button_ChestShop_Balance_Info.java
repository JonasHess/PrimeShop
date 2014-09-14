package eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.ChestShops;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.ChestShop.ChestShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;

public class Button_ChestShop_Balance_Info extends Button {
    
    private ChestShop chestShop;
    
    public Button_ChestShop_Balance_Info(ChestShop chestShop, Material type) {
        super(type, "");
        this.chestShop = chestShop;
        refresh_Appearance();
    }
    

    
    
    @Override
    public void onClick(InventoryInterface inventoryInterface, Player player, ItemStack cursor, ItemStack current, ClickType type) {
        ;
    }
    
    void refresh_Appearance() {
        String currentBalance = PrimeShop.economy.format(chestShop.get_Balance());
        this.setName("This CestShop contains: " + currentBalance);
//        this.setDescription();
    }

}