package eu.Blockup.PrimeShop.ChestShop;

import java.util.ArrayList;
import java.util.List;



import org.bukkit.entity.Player;
//import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


public class ChestShop {

    //these are all used (20140914)
    private String UUID;
    private double money_deposite;
    List<Item_Supply> list_Verkauf;
    List<Item_Supply> list_Ankauf;
    List<Item_Supply> list_Mailbox;
//    private List<Location> list_Locations;
    
    public ChestShop(String uUID, double money_deposite) {
        super();
        UUID = uUID;
        this.money_deposite = money_deposite;
        this.list_Verkauf = new ArrayList<Item_Supply>();
        this.list_Ankauf = new ArrayList<Item_Supply>();
        this.list_Mailbox = new ArrayList<Item_Supply>();
//        this.list_Locations = new ArrayList<Location>();
    }
 
    public String get_UUID () {
        return this.UUID;
    }
    
    public double get_Balance () {
        return this.money_deposite;
    }
    
    public void add_money (double value) {
        this.money_deposite += value;
    }
    
    public boolean has_money (double value) {
        if (this.money_deposite >= value) {
            return true;
        }
        return false;
    }
    
    public boolean withdraw_money (double value) {
        if (has_money(value)) {
            this.money_deposite += value;
            return true;
        }
        return false;
    }
    
//
//    
//    public boolean has_location (Location location) {
//        for (Location l : this.list_Locations) {   // TODO   equals?
//            if (l.equals(location)) return true;
//            continue;
//        }
//        return false;
//    }
//    
//    public void add_location (Location location) {
//        this.list_Locations.add(location);
//    }
    

    // GET ITEM SUPPLY
    private Item_Supply get_Item_Supply_out_of_List (List<Item_Supply> list, ItemStack itemStack) {
        
        for (Item_Supply iS : list) {
            if (itemStack.equals(iS.getItemStack())) {   // TODO Mit allgeminer Lösung ersetzen
                return iS;
            }
        }
        return null;
        
    }
    
     public Item_Supply get_Supply_of_Verkaufen (ItemStack itemStack) {
         return get_Item_Supply_out_of_List(list_Verkauf, itemStack);
     }
    
     public Item_Supply get_Supply_of_Ankauf (ItemStack itemStack) {
         return get_Item_Supply_out_of_List(list_Ankauf, itemStack);
     }
    
     public Item_Supply get_Supply_of_Mailbox (ItemStack itemStack) {
         return get_Item_Supply_out_of_List(list_Mailbox, itemStack);
     }
    
    
    // ADD ITEM
    private void add_Item_to_Supply_List (List<Item_Supply> list, ItemStack itemStack, int amount) {
        
        Item_Supply iS = get_Item_Supply_out_of_List (list,  itemStack);
        
        if (iS == null) {
            list.add(new Item_Supply(itemStack , amount));
            
        } else {
            iS.add_amount(amount);
        }
        
    }
    
    
    public void add_Item_to_Verkaufen (ItemStack itemStack, int amount) {
        add_Item_to_Supply_List (list_Verkauf, itemStack, amount);
    }
    public void add_Item_to_Ankauf (ItemStack itemStack, int amount) {
        add_Item_to_Supply_List (list_Ankauf, itemStack, amount);
    }
    public void add_Item_to_Mailbox (ItemStack itemStack, int amount) {
        add_Item_to_Supply_List (list_Mailbox, itemStack, amount);
    }
    
    
    
//     public boolean remove_Item_from_Ankauf (ItemStack itemStack, int amount) {
//         return remove_Item_from_Supply_List (list_Ankauf, itemStack, amount);
//     }
//    
//     public boolean remove_Item_from_Mailbox (ItemStack itemStack, int amount) {
//         return remove_Item_from_Supply_List (list_Mailbox, itemStack, amount);
//     }
        
    // Pages
    
    //Count
    private int get_PageCount_of_List(List<Item_Supply> list) {
        int listsize = list.size();
        if (listsize > 0) {
            double exact_number_of_pages = (double) listsize / Chest_Page.amount_of_items_fitting__in_one_page;
            return this.roundup (exact_number_of_pages);
        } else {
            return 0;
        }
    }

    public int get_PageCount_of_Verkaufen() {
        return get_PageCount_of_List(list_Verkauf);
    }
    
     public int get_PageCount_of_Ankaufen() {
         return get_PageCount_of_List(list_Ankauf);
     }
     
     public int get_PageCount_of_Mailbox() {
         return get_PageCount_of_List(list_Verkauf);
     }
    
    
    // Get Page
    private Chest_Page get_Page(List<Item_Supply> list, int pagenumber) {

        int this_list_of_Items_Size = list.size();
        int start_index = (int) ((pagenumber -1) * Chest_Page.amount_of_items_fitting__in_one_page);
        
        int added_items = 0;
        int current_position = start_index;
        List<Item_Supply> listOfItems = new ArrayList<Item_Supply>();;
        while ((added_items <= Chest_Page.amount_of_items_fitting__in_one_page) && (current_position <= this_list_of_Items_Size -1)) {
            listOfItems.add(list.get(current_position));
            added_items++;
            current_position++;
        }
        return new Chest_Page(listOfItems, pagenumber, get_PageCount_of_List(list));
    }
    
    public Chest_Page get_Page_X_of_Verkaufen(int pagenumber) {
        return get_Page(list_Verkauf, pagenumber);
    }
    
    
     public Chest_Page get_Page_X_of_Ankaufen(int pagenumber) {
         return get_Page(list_Ankauf, pagenumber);
     }
     
     public Chest_Page get_Page_X_of_Mailbox(int pagenumber) {
         return get_Page(list_Mailbox, pagenumber);
     }
    
    // Calculate Page Roundup
    private int roundup (double givennumber) {
        int result;
        int zwischensumme;
        zwischensumme = (int) givennumber;
        if ((givennumber - zwischensumme) == 0) {
            result = zwischensumme;
        } else {
            result =  zwischensumme + 1;
        }
        return result;
    }
    
    
    public boolean is_this_player_the_owner_of_the_ChestShop (Player player) {
        if (player.getUniqueId().toString().equalsIgnoreCase(this.UUID)) {
            return true;
        }
        return false;
    }
    
    public static void give_Items_back_to_Player (Player player , Item_Supply item_Supply) {
        
        PlayerInventory playerInventory = player.getInventory();
        item_Supply.getItemStack().setAmount(1);
        
        while (!(playerInventory.firstEmpty() == -1) && item_Supply.getAmount() > 0) {
            playerInventory.addItem(item_Supply.getItemStack().clone());
            item_Supply.remove_amount_of(1);
        }
        if (item_Supply.getAmount() > 0) {
            player.sendMessage("Your Inventory is full.");
        }
    }
    
    private void remove_empty_supply_slots_from_List (List<Item_Supply> list) {
        
        List<Item_Supply> hitList = new ArrayList<Item_Supply>();
        
        for (Item_Supply a : list) {
            if (!a.has_amount_of(1)) {
                hitList.add(a);
            }
        }
        
        for (Item_Supply hit : hitList)  {
            list.remove(hit);
            System.out.println("REMOVED ITEM FROM CHESTSHOP");  // TODO remove
        }
        
    }
    
    public void remove_empty_supply_slots () {
        remove_empty_supply_slots_from_List(list_Ankauf);
        remove_empty_supply_slots_from_List(list_Mailbox);
        remove_empty_supply_slots_from_List(list_Verkauf);
    }
    
}
