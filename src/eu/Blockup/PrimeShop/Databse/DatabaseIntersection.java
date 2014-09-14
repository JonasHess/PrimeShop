package eu.Blockup.PrimeShop.Databse;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.PricingEngine.DataHandling.Shop_Item;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.ReturnObjects.ReturnBoolean;

public abstract class DatabaseIntersection {

    protected PrimeShop plugin;
    
    
    public abstract boolean load_Database () ;
    
    public abstract void close_Database () ;
    
    
    DatabaseIntersection(PrimeShop plugin, DatabseTyp databseTyp) {
        this.plugin = plugin;
    }

    public abstract ReturnBoolean link_with_Databse_if_not_allready_linked(Shop_Item sql_Item);
    
    public abstract ReturnBoolean get_Item_from_Databse(Shop_Item sql_Item);

    //public abstract SQL_Item get_Item_from_Databse(ItemStack itemStack, int stockFloorID);

    //public abstract boolean save_Item_to_Databse_2(SQL_Item sqlItem);

    // public abstract void save_Item_to_Databse_(SQL_Item sqlItem);
    public abstract ReturnBoolean save_Item_to_Databse(Shop_Item sql_Item);
    
    public abstract  ReturnBoolean write_NEW_Item_to_SQL (Shop_Item sql_Item);

    public static enum DatabseTyp {
        MYSQL
// TODO Remove unused code found by UCDetector
//         SQLITE, MYSQL, FLATFILE;
    }
    
    protected abstract void read_entire_database_to_memory ();
    

}
