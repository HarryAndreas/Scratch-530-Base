package org.rs2.mysql;

import java.sql.*;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class DatabaseConnection {
	
	/**
	 * Creates a new instance 
	 * @param args Details for the connection
	 * @return
	 */
	public static DatabaseConnection create(String... args) {
		return new DatabaseConnection(args);
	}
	
	/**
	 * The database connection
	 */
	private Connection connection;
	
	/**
	 * Connection details
	 */
	private final String[] details;
	
	/**
	 * Constructor
	 * @param host
	 * @param username
	 * @param password
	 * @param database
	 */
	private DatabaseConnection(String... details) {
		this.details = details;
		try {
			connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Connects to the database
	 * @throws Exception
	 */
	private void connect() throws Exception {
		Connection c = DriverManager.getConnection("jdbc:mysql://" + details[0] + ":3306/" + details[3], details[1], details[2]);
		setConnection(c);
	}
	
	/**
	 * Kills the connection
	 * @throws Exception 
	 */
	public void killConnection() throws Exception {
		getConnection().close();
	}
	
	/**
	 * Gets a statement
	 * @param query
	 * @return
	 */
	public PreparedStatement getStatement(String query) {
		PreparedStatement statement = null;
		try {
			if(getConnection().isClosed() || (getConnection() == null)) {
				connect();
			}
			statement = getConnection().prepareStatement(query);
			return statement;
		} catch(Exception e) {
			e.printStackTrace();
			try {
				connect();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * Get the connection details
	 * @return the details
	 */
	public String[] getDetails() {
		return details;
	}

	/**
	 * Sets the connection
	 * @param databaseConnection the databaseConnection to set
	 */
	private void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @return the databaseConnection
	 */
	private Connection getConnection() {
		return connection;
	}

}