package org.rs2.model;

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
public class MapDataManager {
	
	/**
	 * MapData manager instance
	 */
	private static MapDataManager instance;
	
	/**
	 * Map data map
	 */
	private final Map<Integer, int[]> mapData;
	
	/**
	 * Construct the instance
	 */
	private MapDataManager() {
		mapData = new HashMap<Integer, int[]>();
	}
	
	/**
	 * Gets the singleton instance
	 * @return
	 */
	public static MapDataManager getSingleton() {
		if(instance == null) {
			instance = new MapDataManager();
		}
		return instance;
	}
	
	/**
	 * Loads map data from the database
	 */
	public void init() throws Exception {
		DatabaseConnection con = DatabaseManager.getSingleton().getConnection("info");
		PreparedStatement statement = con.getStatement("SELECT * FROM `mapdata`");
		ResultSet results = statement.executeQuery();
		while(results.next()) {
			getMapData().put(results.getInt("regionid"), new int[] { 
				results.getInt("value-one"), results.getInt("value-two"),
				results.getInt("value-three"), results.getInt("value-four")
			});
		}
	}
	
	/**
	 * Gets the map data for the region
	 * @param region The region ID
	 * @return The region data
	 */
	public int[] getMapData(int region) {
		return getMapData().get(region);
	}

	/**
	 * @return the mapData
	 */
	private Map<Integer, int[]> getMapData() {
		return mapData;
	}

}