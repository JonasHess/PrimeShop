package eu.Blockup.PrimeShop.InventoryInterfaces.Interfaces.ChestShops;

import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.ChestShop.ChestShop;
import eu.Blockup.PrimeShop.InventoryInterfaces.Button;
import eu.Blockup.PrimeShop.InventoryInterfaces.ClickType;
import eu.Blockup.PrimeShop.InventoryInterfaces.InventoryInterface;

public class Button_ChestShop_Add_and_Withdraw_Money extends Button {

    private double offset;
    private ChestShop chestShop;


    public Button_ChestShop_Add_and_Withdraw_Money(ChestShop chestShop, double offset, Material display_Material, String name, String... description) {
        super(display_Material, name, description);
        this.offset = offset;
        this.chestShop = chestShop;
        // this.itemStack = new ItemStack(display_Material);
    }



    @Override
        public void onClick(InventoryInterface inventoryInterface, Player player, ItemStack cursor, ItemStack current, ClickType type) {
            if (type == ClickType.LEFT) {
                this.setName(offset_to_String(this.offset));
                
                double balance = chestShop.get_Balance();
                
                if (balance + offset < 0) {
                    player.sendMessage("There is not enought money in this Shop");
                    return;
                }
                
                chestShop.add_money(offset);
                
                
                for (Button button : inventoryInterface.getButtons()) {
                    
                    if (button instanceof Button_ChestShop_Balance_Info) {
                        try {
                            ((Button_ChestShop_Balance_Info) button).refresh_Appearance();
                        } catch (Exception e) {
                            PrimeShop.plugin.getLogger().log(Level.SEVERE, "Error casting Interface_Button to Pricetag");
                        }
                    }
                }
                // TODO Save Shop!
                inventoryInterface.refresh(player);
                return;
            }
        }




    private String offset_to_String(double offset) {
        String result;
        if (offset > 0) {
            result = "+ ";
        } else {
            result = "";
        }
        result += PrimeShop.economy.format(offset);
        return result;
    }


}