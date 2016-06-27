package eu.Blockup.PrimeShop.PricingEngine.DataHandling;

public class Variabler_typ<Typ> { // NO_UCD (use default)

    private Typ value;
    private Typ defaultValue;
    private boolean has_changed;
    private Shop_Item parent;
    private boolean value_is_defaultValue;

    Variabler_typ(Shop_Item parent, Typ defaultValue) {
        this.has_changed = false;
        this.parent = parent;
        this.defaultValue = defaultValue;
        this.value_is_defaultValue = true;
    }

    public synchronized Typ getValue() {
        if (value_is_defaultValue) {
            return defaultValue;
        }
        return value;
    }

    public synchronized void setValue(Typ value) {
        this.value = value;
        this.has_changed = true;
        this.parent.changes_since_last_save = true;
        this.value_is_defaultValue = false;
    }

    public synchronized boolean is_value_eq_defaultValue() {
        return this.value_is_defaultValue;
    }

    synchronized void set_to_defaultValue() {
        this.value_is_defaultValue = true;
    }

    public synchronized void set_new_defaultValue(Typ newDefaultValue) {
        this.defaultValue = newDefaultValue;
    }

    synchronized Typ get_defaultValue() {
        return this.defaultValue;
    }

    public synchronized boolean has_changed() {
        return has_changed;
    }

    public synchronized void set_change_value_to(boolean has_changed) {
        this.has_changed = has_changed;
    }

    public synchronized void set_value_is_defaultValue(
            boolean value_is_defaultValue) {
        this.value_is_defaultValue = value_is_defaultValue;
    }

    Variabler_typ<Typ> clone(Shop_Item parent) { // TODO testen!

        Variabler_typ<Typ> result = new Variabler_typ<Typ>(parent, defaultValue);
        result.setValue(this.value);
        result.set_change_value_to(this.has_changed);
        result.set_value_is_defaultValue(this.value_is_defaultValue);
        return result;
    }

}