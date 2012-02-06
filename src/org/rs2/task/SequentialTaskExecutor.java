package org.rs2.task;

import java.util.List;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class SequentialTaskExecutor {
	
	/**
	 * Executes tasks one after another
	 * @param packetTasks
	 */
	public static void executeTasks(List<Task> packetTasks) {
		for(Task t : packetTasks) {
			try { 
				t.execute();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}