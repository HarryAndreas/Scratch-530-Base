package org.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.rs2.mysql.DatabaseConnection;
import org.rs2.mysql.DatabaseManager;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class NPCSpawnPacker {
	
	/**
	 * Invoked on startup of the application
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		DatabaseManager.getSingleton().addDatabase(DatabaseConnection.create("127.0.0.1", "lolserver", "lollol", "server"), "info");
		DatabaseConnection c = DatabaseManager.getSingleton().getConnection("info");
		BufferedReader br = new BufferedReader(new FileReader("npcs.txt"));
		List<int[]> list = new ArrayList<int[]>();
		System.out.println("Building list from file...");
		long started = System.currentTimeMillis();
		while(true) {
			String line = br.readLine();
			if(line == null) {
				break;
			}
			String[] split = line.split(" ");
			list.add(new int[] { Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), 0, 0 });
		}
		long elapsed = System.currentTimeMillis() - started;
		System.out.println("Read from file and built list in "+elapsed+"ms");
		List<PreparedStatement> psl = new ArrayList<PreparedStatement>();
		started = System.currentTimeMillis();
		System.out.println("Building statement list...");
		for(int[] i : list) {
			psl.add(c.getStatement("INSERT INTO npcspawn VALUES('"+i[0]+"', '"+i[1]+"', '"+i[2]+"', '"+i[3]+"', '"+i[4]+"');"));
		}
		elapsed = System.currentTimeMillis() - started;
		System.out.println("Built list in "+elapsed+"ms");
		System.out.println("Executing "+psl.size()+" insert statements");
		started = System.currentTimeMillis();
		for(PreparedStatement ps : psl) {
			ps.execute();
		}
		elapsed = System.currentTimeMillis() - started;
		System.out.println("Inserted in "+elapsed+"ms");
		System.out.println("Done!");
	}

}
