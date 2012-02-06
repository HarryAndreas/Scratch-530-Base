package org.rs2.task;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class ThreadedTaskExecutor {
	
	/**
	 * Executes tasks in parallel
	 * @param packetTasks
	 */
	public static void executeTasks(List<Task> tasks, final CountDownLatch latch, ExecutorService pool) {
		for(final Task t : tasks) {
			pool.submit(new Runnable() {
				@Override
				public void run() {
					try { 
						t.execute();
					} catch(Exception e) {
						e.printStackTrace();
					} finally {
						latch.countDown();
					}
				}
			});
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
