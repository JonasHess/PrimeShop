package eu.Blockup.PrimeShop.InventoryInterfaces;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;

public class InventoryInterface implements Cloneable {
    private static final int MAX_HEIGHT = 6;
    private String title;
    private Button[][] buttons;
    private int width = 9;
    private int height;
    private boolean closeable = true;
    public final List<InventoryInterface> branch_back_Stack;
    public final int position_in_Stack;
    public InventoryInterface parentMenu;
    private ClickHandler clickHandler;

    private List<InventoryInterface> copy_Stack_list(final List<InventoryInterface> originallist) {
        List<InventoryInterface> result = new ArrayList<InventoryInterface>();

        for (InventoryInterface m : originallist) {
            result.add(m);
        }
        result.add(this);
        return result;
    }

    public List<InventoryInterface> get_brnach_back_list_of_parentMenu() {
        if (parentMenu == null) {
            return new ArrayList<InventoryInterface>();
        } else {
            return parentMenu.branch_back_Stack;
        }
    }
   
     public InventoryInterface(String title, final List<InventoryInterface> link_Back_Stack) {
         this(title, 3, link_Back_Stack);
     }

    public InventoryInterface(String title, int lines, final List<InventoryInterface> Stack_List) {
        List<InventoryInterface> branch_List;
        if (Stack_List == null) {
            branch_List = new  ArrayList<InventoryInterface>();
        } else {
            branch_List = Stack_List;
        }
        
        if (branch_List.size() >0) { parentMenu = branch_List.get(branch_List.size()-1);
        } else { parentMenu = null; }
        
        position_in_Stack = branch_List.size();
        this.branch_back_Stack = copy_Stack_list(branch_List);
        this.setTitle(title);
        if (lines > InventoryInterface.MAX_HEIGHT) {
            lines = InventoryInterface.MAX_HEIGHT;
        }
        this.buttons = new Button[9][lines];
        this.height = lines;
    }

    public void return_to_predecessor(int i, Player p) {
        if (i >= 0 && i <= position_in_Stack) {
            this.close(p);
            PrimeShop.open_InventoyInterface(p, branch_back_Stack.get(i));
        } else {
            p.sendMessage(""+i);
        }
        return;
    }

    private void close(Player player) {
        PrimeShop.close_InventoyInterface(player);
    }

    private InventoryInterface setTitle(String title) {
        if (title.length() > 32) {
            title = title.substring(0, 32);
        }
        this.title = title;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public InventoryInterface addOption(int x, int y, Button button) {
        this.buttons[x][y] = button;
        return this;
    }

    public InventoryInterface removeOption(int x, int y) {
        return this.addOption(x, y, null);
    }

    public Button getOption(int x, int y) {
        return this.buttons[x][y];
    }

    public Button getOption(int slot) {
        if (slot < 0 || slot > this.getSlots()) {
            return null;
        }
        return this.getOption(slot % 9, slot / 9);
    }

    public Button[][] getOptionGrid() {
        return this.buttons;
    }

    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<Button>();
        for (int x = 0; x < this.buttons.length; x++) {
            for (int y = 0; y < this.buttons[x].length; y++) {
                Button button = this.getOption(x, y);
                if (button != null) {
                    buttons.add(button);
                }
            }
        }
        return buttons;
    }

    public final int getWidth() {
        return this.width;
    }

    public final int getHeight() {
        return this.height;
    }

    public final int getSlots() {
        return this.getWidth() * this.getHeight();
    }

    public InventoryInterface setCloseable(boolean closeable) {
        this.closeable = closeable;
        return this;
    }

    public boolean isCloseable() {
        return this.closeable;
    }

    public InventoryInterface setClickHandler(ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
        return this;
    }

    public ClickHandler getClickHandler() {
        return this.clickHandler;
    }

    public boolean hasClickHandler() {
        return this.clickHandler != null;
    }

    public final void refresh(Player player) {
        Inventory inv = null;
        String name = player.getName();
        if (PrimeShop.hashMap_InventorySessions.containsKey(name)) {
            inv = PrimeShop.hashMap_InventorySessions.get(name);
        }
        boolean newName = inv == null ? false : !inv.getTitle().equals(
                this.getTitle());
        if (inv == null || newName) {
            if (newName) {
                PrimeShop.open_InventoyInterface(player, this);
                return;
            }
            inv = Bukkit
                    .createInventory(null, this.getSlots(), this.getTitle());
        }
        for (int x = 0; x < this.buttons.length; x++) {
            for (int y = 0; y < this.buttons[x].length; y++) {
                Button button = this.getOption(x, y);
                ItemStack item = null;
                if (button != null) {
                    item = button.toItemStack();
                }
                inv.setItem(x + y * 9, item);
            }
        }
        if (inv != PrimeShop.hashMap_InventorySessions.get(name)) {
            player.openInventory(inv);
        } else {
            player.updateInventory();
        }
        PrimeShop.hashMap_InventorySessions.put(name, inv);
        PrimeShop.hashMap_InventoryInterfaces.put(name, this);
    }

    @Override
    public InventoryInterface clone() {
        // System.out.println("Starte Clonen");
        return this; //this.clone(this.getTitle());
    }
}
