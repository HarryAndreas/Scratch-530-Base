package org.rs2.mysql;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class DatabaseManager {
	
	/**
	 * Database manager instance
	 */
	private static DatabaseManager databaseManager;
	
	/**
	 * A map of connections
	 */
	private final Map<String, DatabaseConnection> databaseConnections = new HashMap<String, DatabaseConnection>();
	
	/**
	 * The logger instance
	 */
	private static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());
	
	/**
	 * Gets the database manager singleton
	 * @return
	 */
	public static DatabaseManager getSingleton() {
		if(databaseManager == null) {
			databaseManager = new DatabaseManager();
		}
		return databaseManager;
	}
	
	/**
	 * Kills a database connection
	 * @param dbName
	 */
	public void kill(String dbName) {
		DatabaseConnection con = getConnection(dbName);
		try {
			getLogger().info("Killing MYSQL connection: "+dbName);
			con.killConnection();
		} catch (Exception e) {
			System.err.println("Error while killing connection "+dbName+"!");
		}
	}
	
	/**
	 * Add a database to the map
	 * @param conn The connection
	 * @param name The name
	 */
	public void addDatabase(DatabaseConnection conn, String name) throws Exception {
		if(getDatabaseConnections().containsKey(name)) {
			throw new Exception("Connection already exists!");
		}
		getDatabaseConnections().put(name, conn);
	}
	
	/**
	 * Gets a database connection by name
	 * @param name The name of the database (key)
	 * @return The database connection
	 */
	public DatabaseConnection getConnection(String name) {
		return getDatabaseConnections().get(name);
	}

	/**
	 * Getters
	 * @return the databaseConnections
	 */
	private Map<String, DatabaseConnection> getDatabaseConnections() {
		return databaseConnections;
	}

	/**
	 * @return the logger
	 */
	private static Logger getLogger() {
		return logger;
	}
	
}