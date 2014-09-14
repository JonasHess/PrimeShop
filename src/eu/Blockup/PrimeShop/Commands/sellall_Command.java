package eu.Blockup.PrimeShop.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.PricingEngine.Item_Trader;
import eu.Blockup.PrimeShop.PricingEngine.Pool_of_Item_Traders;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.ReturnObjects.ReturnPrice;

class sellall_Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
            String[] args) {

        // Sell / Buy / changePrice
        
        if (!(cs instanceof Player)) {
            cs.sendMessage(ChatColor.RED
                    + Message_Handler.resolve_to_message(27));
            return true;
        }

        Player p = (Player) cs;

        
        if (!PrimeShop.has_player_Permission_for_this_Command(p,"PrimeShop.VIP.sellEntireIventory")) {            
            p.sendMessage(Message_Handler.resolve_to_message(104));
            return true;
        }

        if (args.length < 1) {
            p.sendMessage(Message_Handler.resolve_to_message(135));
            p.sendMessage(Message_Handler.resolve_to_message(136, " /pSellAll confirm"));
            return true;
        }
        
        if (!args[0].equalsIgnoreCase("confirm")) {
            p.sendMessage(Message_Handler.resolve_to_message(135));
            p.sendMessage(Message_Handler.resolve_to_message(136, " /pSellAll confirm"));
            return true;
        }
        
        double totalPrice = 0;
        for (ItemStack item_to_be_added : p.getInventory()) {  // doppelter zugriff?
            
            if (item_to_be_added != null) {
                Item_Trader itemTrader = Pool_of_Item_Traders.get_ItemTrader();

                ReturnPrice resultPrice = itemTrader.sell_ItemStack(item_to_be_added, item_to_be_added.getAmount(), p, false);
                
                Pool_of_Item_Traders.return_Item_Trader(itemTrader);

                if (resultPrice.succesful) {
                    totalPrice += resultPrice.price;
                } else {
                    cs.sendMessage("Internal Error occurt. Please contact a moderator");
                }
            }
            
        }
        p.sendMessage(Message_Handler.resolve_to_message(137, PrimeShop.economy.format(totalPrice)));
        
        return true;
    }

}