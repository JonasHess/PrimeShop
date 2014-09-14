package eu.Blockup.PrimeShop.InventoryInterfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public abstract class Button {
    
    private Material type;
    private short damage = (short) 0;
    private Map<Enchantment, Integer> enchantments;
    private int amount = 1;

    private String name = "Default name - please change";
    private String[] description;
    
    private String clickMessage = null;
    private String permission = null;
    private String permissionMessage = null;
    
    public Button(Material type) {
        this.setType(type);
    }
    
    public Button(Material type, String name, String... description) {
        this(type);
        this.setName(name);
        this.setDescription(description);
    }
    
    public Button(Material type, short damage, String name, String... description) {
        this(type, name, description);
        this.setDamage(damage);
    }
    
    public Button(Material type, short damage, int amount, String name, String... description) {
        this(type, damage, name, description);
        this.setAmount(amount);
    }
    
    
    public Button(Material type, short damage, Map<Enchantment, Integer> enchantments, int amount, String name, String... description) {
        this(type, damage, name, description);
        this.setAmount(amount);
        this.enchantments = enchantments;
    }
    
    
    @SuppressWarnings("deprecation")
    public Button(ItemStack displayItem, String name, String... description) {
        this(displayItem.getType(), (short) displayItem.getData().getData(), name, description);
        this.setAmount(displayItem.getAmount());
        this.enchantments = displayItem.getEnchantments();
    }
    
    
    
    @SuppressWarnings("deprecation")
    public Button setDisplayIcon(ItemStack displayItem) {
        this.setType(displayItem.getType());
        this.setDamage(displayItem.getData().getData());
        this.setAmount(displayItem.getAmount());
        return this;
    }
    
    private Button setType(Material type) {
        this.type = type;
        return this;
    }
    
    public Material getType() {
        return this.type;
    }
    
    public Button setDamage(short damage) {
        this.damage = damage;
        return this;
    }
    
    public short getDamage() {
        return this.damage;
    }
    
    public Button setAmount(int amount) {
        this.amount = amount;
        return this;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public Button setName(String name) {
        this.name = name;
        return this;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Button setDescription(List<String> desc) {
        this.setDescription(desc.toArray(new String[desc.size()]));
        return this;
    }
    
    public Button setDescription(String... desc) {
        this.description = desc;
        return this;
    }
    
    public String[] getDescription() {
        return this.description;
    }
    
    private boolean hasDescription() {
        return this.getDescription() != null;
    }
    
    public Button setClickMessage(String message) {
        this.clickMessage = message;
        return this;
    }
    
    public String getClickMessage() {
        return this.clickMessage;
    }
    
    private boolean hasClickMessage() {
        return this.getClickMessage() != null;
    }
    
    public Button setPermission(String permission) {
        this.permission = permission;
        return this;
    }
    
    public String getPermission() {
        return this.permission;
    }
    
    private boolean hasPermission() {
        return this.getPermission() != null;
    }
    
    public Button setPermissionMessage(String message) {
        this.permissionMessage = message;
        return this;
    }
    
    public String getPermissionMessage() {
        return this.permissionMessage;
    }
    
    private boolean hasPermissionMessage() {
        return this.getPermissionMessage() != null;
    }
    
    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(this.getType(), this.getAmount(), this.getDamage());
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(item.getType());
        meta.setDisplayName(this.getName());
        List<String> lore = new ArrayList<String>();
        if (this.hasDescription()) {
            for (String line : this.getDescription()) {
                lore.add(line);
            }
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        if (enchantments != null) {
            item.addUnsafeEnchantments(enchantments);
        }
        return item;
    }
    
    public final void performClick(InventoryInterface inventoryInterface, Player player, ItemStack cursor, ItemStack current, ClickType type) {
        if (!this.hasPermission() || player.hasPermission(this.getPermission())) {
            if (this.hasClickMessage()) {
                player.sendMessage(this.getClickMessage());
            }
            this.onClick(inventoryInterface, player, cursor, current, type);
        } else {
            if (this.hasPermissionMessage()) {
                player.sendMessage(this.getPermissionMessage());
            }
        }
    }
    
    public abstract void onClick(InventoryInterface inventoryInterface, Player player, ItemStack cursor, ItemStack current, ClickType type);
}
