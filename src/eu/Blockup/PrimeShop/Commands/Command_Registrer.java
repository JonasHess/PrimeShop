package eu.Blockup.PrimeShop.Commands;

import eu.Blockup.PrimeShop.PrimeShop;

public class Command_Registrer {

    public static void register_Command_Listeners(PrimeShop plugin) {

        plugin.getCommand("primeshop").setExecutor(
                new PrimeShop_Command(plugin));

        pBuy_Command pbuy = new pBuy_Command();

        plugin.getCommand("pBuy").setExecutor(pbuy);
        plugin.getCommand("pSell").setExecutor(pbuy);
        plugin.getCommand("value").setExecutor(new pValue_Command());
        plugin.getCommand("pSellAll").setExecutor(new sellall_Command());
        plugin.getCommand("prices").setExecutor(new prices_command());
        plugin.getCommand("PlayerShop").setExecutor(new ChestShop_Commands());

    }
}
