package org.rs2.util;

import java.io.File;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class FileStore {
	
	/**
	 * Exists
	 * @param name Name of the file
	 * @param dir Directory of the file
	 * @return If the file exists
	 */
	public static boolean exists(String name, String dir) {
		return new File("./"+dir+"/"+name).exists();
	}
	
	/**
	 * Gets the file name, renames if needed
	 * @param name The original name of the file
	 * @param dir The directory of the file
	 * @return The final name of the file
	 */
	public static String getFileName(String name, String dir) {
		String fileName = name;
		if(exists(name, dir)) {
			for(int i = 1; i < 100000; i++) {
				String constructed = name + "("+i+")";
				if(!exists(constructed, dir)) {
					fileName = constructed;
					break;
				}
			}
		}
		return fileName;
	}

}
