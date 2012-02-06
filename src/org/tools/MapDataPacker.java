package org.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.rs2.mysql.DatabaseConnection;
import org.rs2.mysql.DatabaseManager;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class MapDataPacker {
	
	/**
	 * Logger
	 */
	private static Logger logger = Logger.getAnonymousLogger();
	
	/**
	 * @throws Exception 
	 * 
	 */
	public static void packData(String from) throws Exception {
		logger.info("Starting to pack map data");
		DatabaseConnection con = DatabaseManager.getSingleton().getConnection("info");
		List<PreparedStatement> statements = new ArrayList<PreparedStatement>();
		for (int i = 0; i < 16384; i++) {
			if (new File(from + i + ".txt").exists()) {
				int[] data = new int[4];
				BufferedReader in = new BufferedReader(new FileReader(from + i + ".txt"));
				for (int j = 0; j < 4; j++) {
					data[j] = Integer.parseInt(in.readLine());
				}
				PreparedStatement stat = con.getStatement("INSERT INTO mapdata VALUES("+i+", "+data[0]+", "+data[1]+", "+data[2]+", "+data[3]+");");
				statements.add(stat);
				in.close();
			}
			logger.info("Created statement for region "+i);
		}
		logger.info("Executing statements");
		for(PreparedStatement s : statements) {
			s.execute();
		}
		logger.info("Done!");
		
	}

}
