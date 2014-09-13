package eu.Blockup.PrimeShop.Databse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.Other.Cofiguration_Handler;
import eu.Blockup.PrimeShop.Other.Message_Handler;
import eu.Blockup.PrimeShop.PricingEngine.DataHandling.Shop_Item;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.ReturnObjects.ReturnBoolean;

public class Database_MySql extends DatabaseIntersection {

	private MySql[] MySQL_Pool;
	private int MySQL_Pointer;

	public Database_MySql(PrimeShop plugin, DatabseTyp databseTyp) {
		super(plugin, databseTyp);
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
		if (!sql_Item.Object_is_linked_with_Database.getValue()) {

			result.succesful = result.succesful && this.find_and_set_SQL_ID_of_this_Item(sql_Item);

			if (!result.succesful) {

				if (!this.write_NEW_Item_to_SQL(sql_Item).succesful) {
					result.succesful = false;
					result.errorMessage = "Konnte kein neues Objekt in die Datenbank schreiben!";

				} else {
					result.succesful = result.succesful && this.find_and_set_SQL_ID_of_this_Item(sql_Item);

					if (result.succesful) {
						this.save_Item_to_Databse(sql_Item);
					}
				}
			}
		}
		if (result.succesful) {
			MySql sql = this.getMySql();
			Connection conn = sql.getConnection();
			ResultSet rs = null;
			PreparedStatement st = null;
			try {
				st = conn.prepareStatement("SELECT * FROM Economy_Items WHERE mcItemid=?"); // AND
																							// Stockfloor
																							// is
																							// ep
				st.setString(1, sql_Item.mcItemid);
				rs = st.executeQuery();
				rs.last();
				if (rs.getRow() != 0) {
					rs.first();

					sql_Item.itemDisplayname.setValue(rs.getString("itemDisplayname"));
					sql_Item.itemDisplayname.set_change_value_to(false);

					sql_Item.rate_of_price_change.setValue(rs.getDouble("changingRate"));
					sql_Item.rate_of_price_change.set_change_value_to(false);

					// sql_Item.itemWasCrafted.setValue(rs.getBoolean("itemWasCrafted"));
					// sql_Item.itemWasCrafted.set_change_value_to(false);

					sql_Item.initial_price.setValue(rs.getDouble("defaultPrice"));
					sql_Item.initial_price.set_change_value_to(false);

					sql_Item.timesItemWasBought.setValue(rs.getDouble("timesItemWasBought"));
					sql_Item.timesItemWasBought.set_change_value_to(false);

					sql_Item.timesItemWasSold.setValue(rs.getDouble("timesItemWasSold"));
					sql_Item.timesItemWasSold.set_change_value_to(false);

					sql_Item.lastPriceItemWasTradedWith.setValue(rs.getDouble("lastPriceItemWasTradedWith"));
					sql_Item.lastPriceItemWasTradedWith.set_change_value_to(false);

				} else {
					result.errorMessage = "Select-Befehl erzielte keine Resultate!";
					result.succesful = false;
				}

			} catch (SQLException e) {
				e.printStackTrace();
				result.succesful = false;
				result.errorMessage = "Fehlerhafter Select Befehl!";
			} finally {
				sql.closeRessources(conn, rs, st);
			}
		}

//		notifyAll();
		return result;
	}



	
	@Override
	public synchronized ReturnBoolean save_Item_to_Databse(Shop_Item sql_Item) {
		ReturnBoolean result = new ReturnBoolean();

		if (sql_Item.itemDisplayname.has_changed()) {
			result.succesful = result.succesful && this.write_SQL_itemDisplayname(sql_Item).succesful;
		}
		if (sql_Item.rate_of_price_change.has_changed()) {
			result.succesful = result.succesful && this.write_SQL_Changin_Rate(sql_Item).succesful;
		}
		// if (sql_Item.itemWasCrafted.has_changed()) {
		// result.succesful = result.succesful &&
		// this.write_SQL_itemWasCrafted(sql_Item).succesful;
		// }
		if (sql_Item.initial_price.has_changed()) {
			result.succesful = result.succesful && this.write_SQL_defaultPrice(sql_Item).succesful;
		}
		if (sql_Item.timesItemWasBought.has_changed()) {
			result.succesful = result.succesful && this.write_SQL_timesItemWasBought(sql_Item).succesful;
		}
		if (sql_Item.timesItemWasSold.has_changed()) {
			result.succesful = result.succesful && this.write_SQL_timesItemWasSold(sql_Item).succesful;
		}
		if (!result.succesful) {
			result.errorMessage = "Konnte Objekt nicht in die Datenbank speichern!";
		}
		notifyAll();
		return result;
	}
	
	

	public synchronized MySql getMySql() {
		this.MySQL_Pointer++;
		if (this.MySQL_Pointer > Cofiguration_Handler.amount_of_simultanious_opened_MySQL_Connection - 1) {
			this.MySQL_Pointer = 0;
		}
		notifyAll();
		return this.MySQL_Pool[this.MySQL_Pointer];

	}

	private ReturnBoolean create_MySql_Backend_connections() {
		int max = Cofiguration_Handler.amount_of_simultanious_opened_MySQL_Connection;
		ReturnBoolean result = new ReturnBoolean();
		result.succesful = true;

		this.MySQL_Pool = new MySql[max];
		for (int i = 0; i < max; i++) {

			try {

				System.out.println(Message_Handler.resolve_to_message(6, String.valueOf(i), String.valueOf(max)));
				try {
					this.MySQL_Pool[i] = new MySql(this.plugin);
				} catch (Exception e) {
					System.err.println(Message_Handler.resolve_to_message(4));
					result.succesful = false;
					e.printStackTrace();
					break;
				}
			} catch (Exception e) {
				System.err.println(Message_Handler.resolve_to_message(4) + "  (" + e.getMessage() + ").");
				result.succesful = false;
				break;
			}

		}
		if (result.succesful) {
			result.succesful = result.succesful && this.MySQL_Pool[0].createTabels();
		}
		return result;
	}

	private void close_all_MySQL_Connections() {
		if (this.MySQL_Pool != null) {
			int max = Cofiguration_Handler.amount_of_simultanious_opened_MySQL_Connection;
			for (int i = 0; i < max; i++) {
				this.MySQL_Pool[i].closeConnection();
			}
		}

	}

	@Override
	public boolean load_Database() {
		if (this.create_MySql_Backend_connections().succesful) {
			return true;
		}
		return false;

	}

	@Override
	public void close_Database() {
		this.close_all_MySQL_Connections();

	}

	@Override
	protected void read_entire_database_to_memory() {
		// TODO Auto-generated method stub

	}

	
	
	
	public synchronized ReturnBoolean queryUpdate(String query) {
		ReturnBoolean result = new ReturnBoolean();
		Connection conn = this.getMySql().getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.executeUpdate();
		} catch (SQLException e) {
			result.errorMessage = "Failed to send SQL update '" + query + "'.";
			System.err.println(result.errorMessage);
			result.succesful = false;
			e.printStackTrace();
		} finally {
			this.getMySql().closeRessources(conn, null, st);
		}
		notifyAll();
		return result;
	}

	public synchronized ReturnBoolean write_NEW_Item_to_SQL(Shop_Item sql_Item) {
		ReturnBoolean result;
		this.queryUpdate("INSERT INTO `Economy_Items` (`mcItemid`, `itemDisplayname`, `changingRate`, `defaultPrice`, `timesItemWasBought`, `timesItemWasSold`, `lastPriceItemWasTradedWith`) VALUES ('" + sql_Item.mcItemid + "', '" + sql_Item.itemDisplayname.getValue() + "', '" + sql_Item.rate_of_price_change.getValue() + "', '" + sql_Item.initial_price.getValue() + "', '" + sql_Item.timesItemWasBought.getValue() + "' ,'"+ sql_Item.timesItemWasSold.getValue() + "', '" + sql_Item.lastPriceItemWasTradedWith.getValue() + "')");
		
		
		
		
//		this.queryUpdate("INSERT INTO Economy_Items (mcItemid) VALUES ('" + sql_Item.mcItemid + "')");
		/*
		 
		 INSERT INTO `Economy_Items` (`sqlId`, `mcItemid`, `itemDisplayname`, `itemWasCrafted`, `changinRate`, `defaultPrice`, `defaultPriceWasSetBySystem`, `neededPermissionToBuy`, `neededPermissionToSell`, `timesItemWasBought`, `timesItemWasSold`, `lastPriceItemWasTradedWith`) VALUES
(1, '3:0', 'ItemStack{DIRT x 1}', NULL, NULL, 100, NULL, NULL, NULL, 0, 0, NULL);
		 */
		
		if (this.find_and_set_SQL_ID_of_this_Item(sql_Item)) {
			result = this.save_Item_to_Databse(sql_Item);
		} else {
			result = new ReturnBoolean();
			result.succesful = false;
			result.errorMessage = "Error:   Can not write to backend!";
			System.out.println(result.errorMessage);
			
		}
		notifyAll();
		return result;

	}

	public synchronized boolean find_and_set_SQL_ID_of_this_Item(Shop_Item sql_Item) {
		boolean result = true;
		MySql sql = this.getMySql();
		Connection conn = sql.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("SELECT * FROM Economy_Items WHERE mcItemid=?"); // TODO
																						// AND
																						// Stackfloor
																						// is
																						// eq
			st.setString(1, sql_Item.mcItemid);
			rs = st.executeQuery();
			rs.last();
			if (rs.getRow() != 0) {
				rs.first();

				sql_Item.sqlId.setValue(rs.getLong("sqlId"));
			} else {
				result = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		} finally {
			result = result && sql.closeRessources(conn, rs, st);
		}
		if (result) {
			sql_Item.Object_is_linked_with_Database.setValue(true);
		} else {
			sql_Item.Object_is_linked_with_Database.setValue(false);
		}
		notifyAll();
		return result;
	}



	public synchronized ReturnBoolean write_SQL_itemDisplayname(Shop_Item sql_Item) {
		ReturnBoolean result;

		result = this.queryUpdate("UPDATE Economy_Items SET itemDisplayname='" + sql_Item.itemDisplayname.getValue() + "' WHERE sqlId='" + sql_Item.sqlId.getValue() + "'");

		if (result.succesful) {
			sql_Item.itemDisplayname.set_change_value_to(false);
		}
		return result;
	}

	public synchronized ReturnBoolean write_SQL_Changin_Rate(Shop_Item sql_Item) {
		ReturnBoolean result;
		result = this.queryUpdate("UPDATE Economy_Items SET changinRate='" + sql_Item.rate_of_price_change.getValue() + "' WHERE sqlId='" + sql_Item.sqlId.getValue() + "'");
		if (result.succesful) {
			sql_Item.rate_of_price_change.set_change_value_to(false);
		}
		return result;

	}

	public synchronized ReturnBoolean write_SQL_defaultPrice(Shop_Item sql_Item) {
		ReturnBoolean result;
		result = this.queryUpdate("UPDATE Economy_Items SET defaultPrice='" + sql_Item.initial_price.getValue() + "' WHERE sqlId='" + sql_Item.sqlId.getValue() + "'");
		if (result.succesful) {
			sql_Item.initial_price.set_change_value_to(false);
		}
		return result;

	}

	public synchronized ReturnBoolean write_SQL_timesItemWasBought(Shop_Item sql_Item) {
		ReturnBoolean result;
		result = this.queryUpdate("UPDATE Economy_Items SET timesItemWasBought='" + sql_Item.timesItemWasBought.getValue() + "' WHERE sqlId='" + sql_Item.sqlId.getValue() + "'");

		if (result.succesful) {
			sql_Item.timesItemWasBought.set_change_value_to(false);
		}
		return result;

	}

	public synchronized ReturnBoolean write_SQL_timesItemWasSold(Shop_Item sql_Item) {
		ReturnBoolean result;
		result = this.queryUpdate("UPDATE Economy_Items SET timesItemWasSold='" + sql_Item.timesItemWasSold.getValue() + "' WHERE sqlId='" + sql_Item.sqlId.getValue() + "'");

		if (result.succesful) {
			sql_Item.timesItemWasSold.set_change_value_to(false);
		}
		return result;

	}




}
