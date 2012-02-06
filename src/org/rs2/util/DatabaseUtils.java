package org.rs2.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.rs2.model.player.DatabaseResult;
import org.rs2.mysql.DatabaseConnection;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class DatabaseUtils {
	
	/**
	 * Does the player exist in the database?
	 * @param name The name of the player
	 * @param con The connection
	 * @return If it exists
	 */
	public static boolean playerExists(String name, DatabaseConnection con) {
		try {
			PreparedStatement statement = con.getStatement("SELECT * FROM `players` WHERE `name` = '"+name+"' LIMIT 1;");
			ResultSet results = statement.executeQuery();
			return results.next();
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Fetches the profile from the server
	 * @param name The name of the username
	 * @param con The connection
	 * @return The profile
	 * @throws Exception Any exceptions 
	 */
	public static DatabaseResult fetchProfile(String name, DatabaseConnection con) throws Exception {
		PreparedStatement statement = con.getStatement("SELECT * FROM `players` WHERE `name` = '"+name+"' LIMIT 1;");
		ResultSet results = statement.executeQuery();
		if(results.next()) {
			return new DatabaseResult(results.getString("password"), results.getInt("rights"), results.getBytes("data"));
		}
		return null;
	}

}