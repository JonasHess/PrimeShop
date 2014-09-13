package eu.Blockup.PrimeShop.Databse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



import eu.Blockup.PrimeShop.PrimeShop;
import eu.Blockup.PrimeShop.PricingEngine.Item_Analysis.ReturnObjects.ReturnBoolean;

public class MySql {
	private String host;
	private int port;
	private String user;
	private String password;
	private String database;
	private Connection conn;
	private PrimeShop plugin;

	public MySql(PrimeShop plugin) throws Exception {
		this.plugin = plugin;

		host = this.plugin.cofiguration_Handler.host;
		port = this.plugin.cofiguration_Handler.port;
		user = this.plugin.cofiguration_Handler.user;
		password = this.plugin.cofiguration_Handler.password;
		database = this.plugin.cofiguration_Handler.database;
		this.conn = openConnection();
	}

	public Connection openConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.user, this.password);
		this.conn = conn;
		return conn;
	}

	public synchronized Connection getConnection() {

		return this.conn;

	}

	public synchronized boolean hasConnection(Connection conn) {
		boolean result = false;
		if (conn == null) {
			result = false;
		} else {
			try {
				result = conn.isValid(1);
			} catch (SQLException e) {
				return false;
			}
		}
		notifyAll();
		return result;
	}

	public synchronized ReturnBoolean queryUpdate(String query) {
		ReturnBoolean result = new ReturnBoolean();
		Connection conn = this.getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);
			st.executeUpdate();
		} catch (SQLException e) {
			result.errorMessage = "Failed to send update '" + query + "'.";
			System.err.println(result.errorMessage);
			result.succesful = false;
		} finally {
			this.closeRessources(conn, null, st);
		}
		notifyAll();
		return result;
	}

	public synchronized boolean closeRessources(Connection conn, ResultSet rs, PreparedStatement st) {
		boolean result = true;
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				result = false;
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				result = false;
			}
		}
		notifyAll();
		return result;
	}

	public void closeConnection() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.conn = null;
		}
	}
	
	public boolean createTabels() {
		return this.queryUpdate("CREATE TABLE IF NOT EXISTS `Economy_Items` (`sqlId` bigint(20) NOT NULL AUTO_INCREMENT,`mcItemid` text,`itemDisplayname` text,`itemWasCrafted` tinyint(1) DEFAULT NULL,`changingRate` double DEFAULT NULL,`defaultPrice` double DEFAULT NULL,`defaultPriceWasSetBySystem` double DEFAULT NULL,`neededPermissionToBuy` text,`neededPermissionToSell` text,`timesItemWasBought` double DEFAULT NULL,`timesItemWasSold` double DEFAULT NULL,`lastPriceItemWasTradedWith` double DEFAULT NULL, PRIMARY KEY (`sqlId`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;").succesful;
	}
//	public boolean createTabels() {
//		return this.queryUpdate("CREATE TABLE IF NOT EXISTS `Economy_Items` (`sqlId` bigint(20) NOT NULL AUTO_INCREMENT,`mcItemid` text,`itemDisplayname` text,`itemWasCrafted` tinyint(1) DEFAULT NULL,`defaultPrice` double DEFAULT NULL,`defaultPriceWasSetBySystem` double DEFAULT NULL,`neededPermissionToBuy` text,`neededPermissionToSell` text,`timesItemWasBought` double DEFAULT NULL,`timesItemWasSold` double DEFAULT NULL,`lastPriceItemWasTradedWith` double DEFAULT NULL, PRIMARY KEY (`sqlId`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;").succesful;
//	}

}
