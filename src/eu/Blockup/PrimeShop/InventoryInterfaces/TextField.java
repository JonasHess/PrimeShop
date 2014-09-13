package eu.Blockup.PrimeShop.InventoryInterfaces;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public abstract class TextField {

	private String name;
	private List<String> pages = new ArrayList<String>();
	
	private String startMessage = null;
	
	public TextField(String name) {
		this.name = name;
	}
	
	public TextField(String name, String... pages) {
		this(name);
		this.setPages(pages);
	}
	
	public TextField setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getName() {
		return this.name;
	}
	
	public TextField clearPages() {
		this.pages.clear();
		return this;
	}
	
	public TextField setPages(String... pages) {
		this.clearPages();
		for (String page : pages) {
			this.pages.add(page);
		}
		return this;
	}
	
	public TextField setPage(int index, String page) {
		while (this.pages.size() <= index) {
			this.addPage("");
		}
		
		return this;
	}
	
	public TextField addPage(String page) {
		this.pages.add(page);
		return this;
	}
	
	public String[] getPages() {
		return this.pages.toArray(new String[this.pages.size()]);
	}
	
	public TextField setStartMessage(String startMessage) {
		this.startMessage = startMessage;
		return this;
	}
	
	public String getStartMessage() {
		return this.startMessage;
	}
	
	public boolean hasStartMessage() {
		return this.getStartMessage() != null;
	}
	
	public ItemStack toItemStack() {
		ItemStack item = new ItemStack(Material.BOOK_AND_QUILL);
		BookMeta meta = (BookMeta) item.getItemMeta();
		meta.setDisplayName(this.getName());
		if (this.pages.isEmpty()) {
			meta.setPages("");
		} else {
			meta.setPages(this.pages);
		}
		item.setItemMeta(meta);
		return item;
	}
	
	public abstract void onSend(Player player, String title, String[] content);
}
