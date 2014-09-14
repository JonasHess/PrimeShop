package eu.Blockup.PrimeShop.PricingEngine.DataHandling;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.ReturnObjects.ReturnBoolean;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.ReturnObjects.ReturnPrice;

public class Shop_Item {

    public boolean changes_since_last_save = false;

    private Variabler_typ<Boolean> itemWasCrafted;
    public Variabler_typ<Double> lastPriceItemWasTradedWith;
    public Variabler_typ<Boolean> Object_is_linked_with_Database;
    private int stockFloorId;
    public Variabler_typ<Long> sqlId;
    public Variabler_typ<String> itemDisplayname;
    public String mcItemid;
    public Variabler_typ<Double> rate_of_price_change;
    public Variabler_typ<Double> initial_price;
    public Variabler_typ<Double> timesItemWasBought;
    public Variabler_typ<Double> timesItemWasSold;
    public Variabler_typ<Integer> permissionGroup;



    // Konstructor

    public Shop_Item(ItemStack itemstack, int stockFloor) {
        this.setMcItemID(itemstack);
        initialize_after_konstruction();
        this.itemDisplayname.setValue(PrimeShop
                .convert_IemStack_to_DisplayName(itemstack));

        this.stockFloorId = stockFloor;
    }

    private Shop_Item(String mcItemID, int stockFloor) {
        this.setMcItemID(mcItemID);
        initialize_after_konstruction();
        this.initial_price
                .setValue(Cofiguration_Handler.default_initial_price);
        this.itemDisplayname.setValue(PrimeShop
                .convert_IemStack_to_DisplayName(mcItemID));

        this.stockFloorId = stockFloor;
    }

    private void initialize_after_konstruction() {
        this.itemWasCrafted = new Variabler_typ<Boolean>(this, false);
        // this.itemWasCrafted.setValue(false);

        this.lastPriceItemWasTradedWith = new Variabler_typ<Double>(this, 0.0);
        // this.lastPriceItemWasTradedWith.setValue(0.0);

        this.Object_is_linked_with_Database = new Variabler_typ<Boolean>(this,
                false);
        // this.Object_is_linked_with_Database.setValue(false);

        this.sqlId = new Variabler_typ<Long>(this, 0L);
        // this.sqlId.setValue(0L);

        this.itemDisplayname = new Variabler_typ<String>(this,
                "Default Item Name");
        // this.itemDisplayname.setValue("Default Item Name");

        // this.mcItemid = new Variabler_typ<String>();
        this.rate_of_price_change = new Variabler_typ<Double>(this,
                Cofiguration_Handler.default_rate_of_price_change);
        // this.Changing_Rate.setValue(Cofiguration_Handler.priceChangePerTrade);

        this.initial_price = new Variabler_typ<Double>(this,
                Cofiguration_Handler.default_initial_price);
        // this.defaultPrice.setValue(100.0);

        this.timesItemWasBought = new Variabler_typ<Double>(this, 0.0);
        // this.timesItemWasBought.setValue(0.0);

        this.timesItemWasSold = new Variabler_typ<Double>(this, 0.0);
        // this.timesItemWasSold.setValue(0.0);

        this.permissionGroup = new Variabler_typ<Integer>(this, 1);
        // this.permissionGroup.setValue(1);
    }

    public void setMcItemID(ItemStack itemstack) {
        this.mcItemid = PrimeShop.convertItemStacktoToIdString(itemstack);
    }

    public void setMcItemID(String mcItemID) {
        this.mcItemid = mcItemID;
    }


    public synchronized Shop_Item clone() {
        Shop_Item result = new Shop_Item(this.mcItemid, this.stockFloorId);

        result.itemWasCrafted = this.itemWasCrafted.clone(result);
        result.lastPriceItemWasTradedWith = this.lastPriceItemWasTradedWith
                .clone(result);
        result.Object_is_linked_with_Database = this.Object_is_linked_with_Database
                .clone(result);
        result.stockFloorId = this.stockFloorId;
        result.sqlId = this.sqlId.clone(result);
        result.itemDisplayname = this.itemDisplayname.clone(result);
        result.mcItemid = this.mcItemid;
        result.rate_of_price_change = this.rate_of_price_change.clone(result);
        result.initial_price = this.initial_price.clone(result);
        result.timesItemWasBought = this.timesItemWasBought.clone(result);
        result.timesItemWasSold = this.timesItemWasSold.clone(result);
        notify();
        return result;

    }
    private synchronized double get_defaultPrice() {
        if (this.initial_price.getValue() < Cofiguration_Handler.smallestValueItemCanHave)
            return Cofiguration_Handler.smallestValueItemCanHave;

        if (this.initial_price.is_value_eq_defaultValue())
            return Cofiguration_Handler.default_initial_price;
        return this.initial_price.getValue();
    }

    // Kaufen / Verkaufen

    private synchronized double get_Changing_Rate() {
        if (this.rate_of_price_change.is_value_eq_defaultValue())
            return Cofiguration_Handler.default_rate_of_price_change;
        return this.rate_of_price_change.getValue();
    }

    private double calculate_amount_of_times_sold_for_break_point() {
        if (get_defaultPrice() <= 0 || rate_of_price_change.getValue() <= 0)
            return timesItemWasSold.getValue();
        return (-1
                * (this.get_Changing_Rate() * Cofiguration_Handler.smallestValueItemCanHave) / get_defaultPrice())
                + this.get_Changing_Rate() + this.timesItemWasBought.getValue();
    }

    public synchronized ReturnPrice buyItem(boolean buy, double amount,
            boolean save_after_buy) {
        ReturnPrice result = new ReturnPrice();
        if (!this.Object_is_linked_with_Database.getValue()) {
            ReturnBoolean temp_result = PrimeShop.databaseHandler
                    .get_Item_from_Databse(this);
            if (!temp_result.succesful) {
                result.succesful = false;
                result.errorMessage = temp_result.errorMessage;
                PrimeShop.plugin.getLogger().log(Level.SEVERE, ChatColor.RED + Message_Handler.resolve_to_message(1));
                return result;
            }
        }
        if (!buy) {
            // verkaufe --> preis wird kleiner
            double aktueller_preis = this.calculate_price(
                    this.timesItemWasBought.getValue(),
                    this.timesItemWasSold.getValue());
            double future_preis = this.calculate_price(
                    this.timesItemWasBought.getValue(),
                    this.timesItemWasSold.getValue() + amount);

            if (aktueller_preis <= Cofiguration_Handler.smallestValueItemCanHave) {
                // aktueller Preis ist kleiner als Minimum!
                result.price = Cofiguration_Handler.smallestValueItemCanHave;
                this.timesItemWasSold
                        .setValue(calculate_amount_of_times_sold_for_break_point());
            } else {
                // aktueller Preis ist NICHT kleiner als Minimum!

                if (future_preis <= Cofiguration_Handler.smallestValueItemCanHave) {
                    // zukünftiger Preis ist kleiner als Minimum!
                    result.price = aktueller_preis
                            - Cofiguration_Handler.smallestValueItemCanHave; // TODO
                                                                                // Dead
                                                                                // code

                    this.timesItemWasSold
                            .setValue(calculate_amount_of_times_sold_for_break_point());
                } else {
                    // zukünftiger Preis ist NICHT kleiner als Minimum!
                    // standart Fall!
                    result.price = this.calculate_price(
                            this.timesItemWasBought.getValue(),
                            this.timesItemWasSold.getValue());
                    this.timesItemWasSold.setValue(this.timesItemWasSold
                            .getValue() + amount);

                    // result.price = future_preis;
                }
            }
        } else {
            // kaufen --> Preis wird größer!
            this.timesItemWasBought.setValue(this.timesItemWasBought.getValue()
                    + amount);
            result.price = this.calculate_price(
                    this.timesItemWasBought.getValue(),
                    this.timesItemWasSold.getValue());

        }

        result.succesful = true;
        result.price *= amount;
        try {
            this.lastPriceItemWasTradedWith.setValue(result.price / amount);
        } catch (Exception e) {
            PrimeShop.plugin.getLogger().log(Level.SEVERE, ChatColor.RED + "Division by 0 in price calculation");
            this.lastPriceItemWasTradedWith.setValue(0.0);
        }

        if (save_after_buy) {  //TODO remove
            // this.write_Object_to_SQL();
        }
        notifyAll();
        return result;
    }

    public ReturnBoolean change_default_Price(double offset) {
        ReturnBoolean result = new ReturnBoolean();

        if ((this.initial_price.getValue() + offset) < 0.00) {
            result.errorMessage = Message_Handler.resolve_to_message(17);
            result.succesful = false;
            return result;
        }

        // Wenn default geändert werden soll dann deaktiviere default
        if ((offset != 0) && (initial_price.is_value_eq_defaultValue())) {
            this.initial_price.set_value_is_defaultValue(false);
        }

        // Wenn der neuer Wer eq dem default Wert dann setze auf default
        if ((this.initial_price.getValue() + offset) == initial_price
                .get_defaultValue()) {
            this.initial_price.setValue(initial_price.getValue() + offset);
            this.initial_price.set_to_defaultValue();
            return result;
        }

        this.initial_price.setValue(this.initial_price.getValue() + offset);
        result.succesful = true;
        return result;
    }

    public ReturnBoolean change_Changin_Rate(double offset) {
        ReturnBoolean result = new ReturnBoolean();

        if ((this.rate_of_price_change.getValue() + offset) < 0.00) {
            result.errorMessage = Message_Handler.resolve_to_message(18);
            result.succesful = false;
            return result;

        }

        // Wenn default geändert werden soll dann deaktiviere default
        if ((offset != 0) && (rate_of_price_change.is_value_eq_defaultValue())) {
            this.rate_of_price_change.set_value_is_defaultValue(false);
        }

        // Wenn der neuer Wer eq dem default Wert dann setze auf default
        if ((this.rate_of_price_change.getValue() + offset) == rate_of_price_change
                .get_defaultValue()) {
            this.rate_of_price_change.setValue(rate_of_price_change.getValue() + offset);
            this.rate_of_price_change.set_to_defaultValue();
            return result;
        }

        this.rate_of_price_change.setValue(this.rate_of_price_change.getValue() + offset);
        result.succesful = true;

        return result;
    }

    public double calculate_price(double timesItemWasBought,
            double timesItemWasSold) {
        if (get_Changing_Rate() <= 0)
            return get_defaultPrice();
        return get_defaultPrice() + (1 / this.get_Changing_Rate())
                * get_defaultPrice() * (timesItemWasBought - timesItemWasSold);
    }

    public ItemStack getMcItemId_as_ItemStack(int amount) {
        return PrimeShop.convertItemIdStringToItemstack(this.mcItemid, amount);
    }

    public void setMcItemId_from_ItemStack(ItemStack itemstack) {
        this.mcItemid = PrimeShop.convertItemStacktoToIdString(itemstack);
    }

    public void setMcItemId_from_ItemStack(String mcid_string) {
        this.mcItemid = mcid_string;
    }

    // Getter
    public String getMcItemId_as_String() {
        return this.mcItemid;
    }
}
