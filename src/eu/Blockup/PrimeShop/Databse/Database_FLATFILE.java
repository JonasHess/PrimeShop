package eu.Blockup.PrimeShop.Databse;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.PricingEngine.DataHandling.Shop_Item;
import eu.Blockup.PrimeShop.PricingEngine.DataHandling.Variabler_typ;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.ReturnObjects.ReturnBoolean;

public class Database_FLATFILE extends DatabaseIntersection {

	public Database_FLATFILE(PrimeShop plugin, DatabseTyp databseTyp) {
		super(plugin, databseTyp);
		// TODO Auto-generated constructor stub
	}

	private File file;
	private FileConfiguration cfg;
	private boolean file_is_loaded;
	private String economyName = Cofiguration_Handler.default_Economy_Name + ".";

	@Override
	public boolean load_Database() {
		// TODO Auto-generated method stub

		if (!file_is_loaded) {
			try {
				file = new File("plugins/PrimeShop/", "items.yml"); // TODO

				cfg = YamlConfiguration.loadConfiguration(file);

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				PrimeShop.plugin.getLogger().log(Level.SEVERE, "PrimeShop: error reading Flatfile");
				PrimeShop.plugin.getLogger().log(Level.SEVERE, Message_Handler.resolve_to_message(4));
				e1.printStackTrace();
				file_is_loaded = false;
				return false;
			}
			file_is_loaded = true;
		}
		return true;
	}

	@Override
	public void close_Database() {
		file = null;
		cfg = null;
		file_is_loaded = false;
	}

	@Override
	public ReturnBoolean link_with_Databse_if_not_allready_linked(
			Shop_Item sql_Item) {

		ReturnBoolean result = new ReturnBoolean();
		if (sql_Item.Object_is_linked_with_Database.getValue()) {
			return result;
		} else {
			return get_Item_from_Databse(sql_Item);
		}
	}

	@Override
	public ReturnBoolean get_Item_from_Databse(Shop_Item sql_Item) {

		ReturnBoolean result = new ReturnBoolean();

		if (!this.file_is_loaded) {
			if (!this.load_Database()) {
				result.succesful = false;
				result.errorMessage = "Error loading FLATFILE!";
				return result;
			}
		}

		/*
		 * 
		 * this.lastPriceItemWasTradedWith.setValue(0.0);
		 * 
		 * this.Object_is_linked_with_Database.setValue(false);
		 * 
		 * this.sqlId.setValue(0L);
		 * 
		 * this.itemDisplayname.setValue("Default Item Name");
		 * 
		 * 
		 * mcItemid;
		 * 
		 * this.rate_of_price_change.setValue();
		 * 
		 * this.initial_price.setValue(100.0);
		 * 
		 * this.timesItemWasBought.setValue(0.0);
		 * 
		 * this.timesItemWasSold.setValue(0.0);
		 * 
		 * this.permissionGroup.setValue(1);
		 */
		
		

		try {
			
			if (!cfg.contains(economyName + sql_Item.mcItemid)) {
				return write_NEW_Item_to_SQL(sql_Item);
			} else {
			sql_Item.lastPriceItemWasTradedWith.setValue(cfg
					.getDouble(economyName + sql_Item.mcItemid
							+ ".lastPriceItemWasTradedWith"));
			sql_Item.lastPriceItemWasTradedWith.set_change_value_to(false);

			sql_Item.itemDisplayname.setValue(cfg.getString(economyName + sql_Item.mcItemid
					+ ".itemDisplayname"));
			sql_Item.itemDisplayname.set_change_value_to(false);

			sql_Item.rate_of_price_change.setValue(cfg.getDouble(economyName + sql_Item.mcItemid
					+ ".rate_of_price_change.price_doubles_every"));
			
			sql_Item.rate_of_price_change.set_value_is_defaultValue(cfg.getBoolean(economyName + sql_Item.mcItemid
					+ ".rate_of_price_change.uses_the_default_rate"));
			sql_Item.rate_of_price_change.set_change_value_to(false);

			// sql_Item.itemWasCrafted.setValue(rs.getBoolean("itemWasCrafted"));
			// sql_Item.itemWasCrafted.set_change_value_to(false);

			sql_Item.initial_price.setValue(cfg.getDouble(economyName + sql_Item.mcItemid
					+ ".initial_price.price"));
			
			sql_Item.initial_price.set_value_is_defaultValue(cfg.getBoolean(economyName + sql_Item.mcItemid
					+ ".initial_price.uses_the_default_price_instead"));
			
			sql_Item.initial_price.set_change_value_to(false);

			sql_Item.timesItemWasBought.setValue(cfg
					.getDouble(economyName + sql_Item.mcItemid + ".timesItemWasBought"));
			sql_Item.timesItemWasBought.set_change_value_to(false);

			sql_Item.timesItemWasSold.setValue(cfg.getDouble(economyName + sql_Item.mcItemid
					+ ".timesItemWasSold"));
			sql_Item.timesItemWasSold.set_change_value_to(false);

			sql_Item.permissionGroup.setValue(cfg.getInt(economyName + sql_Item.mcItemid
					+ ".permissionGroup"));
			sql_Item.permissionGroup.set_change_value_to(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			result.errorMessage = "Error reading Item from Flatfile";
			result.succesful = false;
			return result;
		}

		sql_Item.Object_is_linked_with_Database.setValue(true);
		sql_Item.Object_is_linked_with_Database.set_change_value_to(false);
		return result;

	}

	@Override
	protected void read_entire_database_to_memory() {
		// TODO Auto-generated method stub

	}

	@Override
	public ReturnBoolean save_Item_to_Databse(Shop_Item sql_Item) {

		ReturnBoolean result = new ReturnBoolean();

		if (!this.file_is_loaded) {
			if (!this.load_Database()) {
				result.succesful = false;
				result.errorMessage = "Error loading FLATFILE!";
				return result;
			}
		}

		if (sql_Item.itemDisplayname.has_changed()) {
			result.succesful = result.succesful
					&& this.write_SQL_itemDisplayname(sql_Item).succesful;
		}
		if (sql_Item.rate_of_price_change.has_changed()) {
			result.succesful = result.succesful
					&& this.write_SQL_Changin_Rate(sql_Item).succesful;
		}
		// if (sql_Item.itemWasCrafted.has_changed()) {
		// result.succesful = result.succesful &&
		// this.write_SQL_itemWasCrafted(sql_Item).succesful;
		// }
		if (sql_Item.initial_price.has_changed()) {
			result.succesful = result.succesful
					&& this.write_SQL_initial_price(sql_Item).succesful;
		}
		if (sql_Item.timesItemWasBought.has_changed()) {
			result.succesful = result.succesful
					&& this.write_SQL_timesItemWasBought(sql_Item).succesful;
		}
		if (sql_Item.timesItemWasSold.has_changed()) {
			result.succesful = result.succesful
					&& this.write_SQL_timesItemWasSold(sql_Item).succesful;
		}

		if (sql_Item.permissionGroup.has_changed()) {
			result.succesful = result.succesful
					&& this.write_SQL_permissionGroup(sql_Item).succesful;
		}

		if (sql_Item.lastPriceItemWasTradedWith.has_changed()) {
			result.succesful = result.succesful
					&& this.write_SQL_lastPriceItemWasTradedWith(sql_Item).succesful;
		}

		result.succesful = result.succesful
				&& this.save_CFG_to_File().succesful;

		if (!result.succesful) {
			result.errorMessage = "Konnte Objekt nicht in die Datenbank speichern!"; // TODO
		}else {
			sql_Item.changes_since_last_save = false;
		}

		// notifyAll();
		return result;
	}

	private ReturnBoolean write_SQL_timesItemWasSold(Shop_Item sql_Item) {
		ReturnBoolean result = new ReturnBoolean();

		try {
			cfg.set(economyName + sql_Item.mcItemid + ".timesItemWasSold",
					sql_Item.timesItemWasSold.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			result.succesful = false;
		}
		return result;
	}

	private ReturnBoolean write_SQL_timesItemWasBought(Shop_Item sql_Item) {
		ReturnBoolean result = new ReturnBoolean();

		try {
			cfg.set(economyName + sql_Item.mcItemid + ".timesItemWasBought",
					sql_Item.timesItemWasBought.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			result.succesful = false;
		}
		return result;
	}

	private ReturnBoolean write_SQL_initial_price(Shop_Item sql_Item) {
		ReturnBoolean result = new ReturnBoolean();

		try {
			cfg.set(economyName + sql_Item.mcItemid + ".initial_price.price",
					sql_Item.initial_price.getValue());
			cfg.set(economyName + sql_Item.mcItemid + ".initial_price.uses_the_default_price_instead",
					sql_Item.initial_price.is_value_eq_defaultValue());
		} catch (Exception e) {
			e.printStackTrace();
			result.succesful = false;
		}
		return result;
	}

	private ReturnBoolean write_SQL_Changin_Rate(Shop_Item sql_Item) {
		ReturnBoolean result = new ReturnBoolean();

		try {
			cfg.set(economyName + sql_Item.mcItemid + ".rate_of_price_change.price_doubles_every",
					sql_Item.rate_of_price_change.getValue());
			cfg.set(economyName + sql_Item.mcItemid + ".rate_of_price_change.uses_the_default_rate",
					sql_Item.rate_of_price_change.is_value_eq_defaultValue());
		} catch (Exception e) {
			e.printStackTrace();
			result.succesful = false;
		}
		return result;

	}

	private ReturnBoolean write_SQL_permissionGroup(Shop_Item sql_Item) {
		ReturnBoolean result = new ReturnBoolean();

		try {
			cfg.set(economyName + sql_Item.mcItemid + ".permissionGroup",
					sql_Item.permissionGroup.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			result.succesful = false;
		}
		return result;

	}

	private ReturnBoolean write_SQL_lastPriceItemWasTradedWith(Shop_Item sql_Item) {
		ReturnBoolean result = new ReturnBoolean();

		try {
			cfg.set(economyName + sql_Item.mcItemid + ".lastPriceItemWasTradedWith",
					sql_Item.lastPriceItemWasTradedWith.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			result.succesful = false;
		}
		return result;

	}

	private ReturnBoolean write_SQL_itemDisplayname(Shop_Item sql_Item) {
		ReturnBoolean result = new ReturnBoolean();

		try {
			cfg.set(economyName + sql_Item.mcItemid + ".itemDisplayname",
					sql_Item.itemDisplayname.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			result.succesful = false;
		}
		return result;
	}

	private ReturnBoolean save_CFG_to_File() {
		ReturnBoolean result = new ReturnBoolean();
		try {
			cfg.addDefault("Version", (Double)1.0);
			cfg.options().copyDefaults(true);
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			PrimeShop.plugin.getLogger().log(Level.SEVERE, (Message_Handler.resolve_to_message(4)));
			result.succesful = false;
			result.errorMessage = "Could not write to Flatfile!"; // TODO
		}
		return result;
	}

	@Override
	public ReturnBoolean write_NEW_Item_to_SQL(Shop_Item sql_Item) {
		ReturnBoolean result = new ReturnBoolean();

		if (!this.file_is_loaded) {
			if (!this.load_Database()) {
				result.succesful = false;
				result.errorMessage = "Error loading FLATFILE!";
				return result;
			}
		}

		result.succesful = result.succesful
				&& this.write_SQL_itemDisplayname(sql_Item).succesful;
		result.succesful = result.succesful
				&& this.write_SQL_Changin_Rate(sql_Item).succesful;
		result.succesful = result.succesful
				&& this.write_SQL_initial_price(sql_Item).succesful;
		result.succesful = result.succesful
				&& this.write_SQL_timesItemWasBought(sql_Item).succesful;
		result.succesful = result.succesful
				&& this.write_SQL_timesItemWasSold(sql_Item).succesful;
		result.succesful = result.succesful
				&& this.write_SQL_permissionGroup(sql_Item).succesful;
		result.succesful = result.succesful
				&& this.write_SQL_lastPriceItemWasTradedWith(sql_Item).succesful;

		result.succesful = result.succesful
				&& this.save_CFG_to_File().succesful;

		if (!result.succesful) {
			result.errorMessage = "Konnte Objekt nicht in die Datenbank speichern!"; // TODO
		}

		// notifyAll();
		return result;
	}

}
