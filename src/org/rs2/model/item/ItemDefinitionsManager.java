package org.rs2.model.item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.rs2.mysql.DatabaseConnection;
import org.rs2.mysql.DatabaseManager;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class ItemDefinitionsManager {
	
	/**
	 * Instance of the definition manager
	 */
	private static ItemDefinitionsManager instance;
	
	/**
	 * A map of item definitions
	 */
	private final Map<Integer, ItemDefinition> itemDefinitions;
	
	/**
	 * Construct the instance
	 */
	private ItemDefinitionsManager() {
		itemDefinitions = new HashMap<Integer, ItemDefinition>();
	}
	
	/**
	 * Load the definitions
	 * @throws Exception 
	 */
	public void init() throws Exception {
		DatabaseConnection con = DatabaseManager.getSingleton().getConnection("info");
		PreparedStatement statement = con.getStatement("SELECT * FROM `items`");
		ResultSet results = statement.executeQuery();
		while(results.next()) {
			int id = results.getInt(1);
			String name = results.getString(2).replaceAll("@", "'");
			String examine = results.getString(3).replaceAll("@", "'");
			int equip = results.getInt(4);
			boolean stackable = results.getInt(5) == 1;
			boolean noted = results.getInt(6) == 1;
			String bonuses = results.getString(7);
			int[] bonus = ItemDefinitionsManager.splitString(bonuses);
			getItemDefinitions().put(id, new ItemDefinition(id, name, examine, noted, stackable, equip, bonus));
		}
	}
	
	/**
	 * Splits a string into the item bonuses
	 * @param bonus
	 * @return
	 */
	private static int[] splitString(String bonus) {
		String[] split = bonus.split(" ");
		int[] bonuses = new int[split.length];
		for(int i = 0; i < split.length; i++) {
			bonuses[i] = Integer.parseInt(split[i]);
		}
		return bonuses;
	}
	
	/**
	 * Gets the item definition by id
	 * @param id The item id
	 * @return The definition
	 */
	public ItemDefinition getDefinition(int id) {
		return getItemDefinitions().get(id);
	}
	
	/**
	 * Gets the singleton instance
	 * @return
	 */
	public static ItemDefinitionsManager getSingleton() {
		if(instance == null) {
			instance = new ItemDefinitionsManager();
		}
		return instance;
	}

	/**
	 * @return the itemDefinitions
	 */
	private Map<Integer, ItemDefinition> getItemDefinitions() {
		return itemDefinitions;
	}

}