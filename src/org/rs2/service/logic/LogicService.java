package org.rs2.service.logic;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

import org.rs2.Launch;
import org.rs2.event.EventManager;
import org.rs2.gui.ServerWindow;
import org.rs2.model.World;
import org.rs2.model.player.Player;
import org.rs2.model.player.PlayerUpdating;
import org.rs2.task.SequentialTaskExecutor;
import org.rs2.task.Task;
import org.rs2.util.Configuration;

/**
 * 508 Base
 * 
 * @author Harry Andreas
 */
public class LogicService implements Runnable {

	/**
	 * Logger for the service
	 */
	private static final Logger logger = Logger.getLogger(LogicService.class
			.getName());

	/**
	 * Instance of the logic service
	 */
	private static LogicService instance;

	/**
	 * The worker thread
	 */
	private static final ExecutorService workerThread = Executors
			.newSingleThreadExecutor();

	/**
	 * The executor service for when multi threading is active
	 */
	private final ExecutorService processorThreads = Executors
			.newFixedThreadPool(4);

	/**
	 * Is the service running
	 */
	private boolean serviceRunning = Boolean.TRUE;

	/**
	 * The game ticks that have passed
	 */
	private int gameTicks = 0;
	
	/**
	 * The bad ticks that have taken place
	 */
	private int badTicks = 0;
	
	/**
	 * The last tick time
	 */
	private int lastTickTime = 0;
	
	/**
	 * A latched used when the multi threader is active
	 */
	private CountDownLatch latch;
	
	/**
	 * Lists of tasks
	 */
	private List<Task> packetTasks = new ArrayList<Task>();
	private List<Task> tickTasks =  new ArrayList<Task>();
	private List<Task> resetTasks = new ArrayList<Task>();

	/**
	 * Gets the singleton
	 * 
	 * @return
	 */
	public static LogicService getSingleton() {
		if (instance == null) {
			instance = new LogicService();
		}
		return instance;
	}

	/**
	 * Starts the logic service
	 */
	public void init() {
		getWorkerThread().submit(this);
	}

	/**
	 * Processes the server
	 */
	@Override
	public void run() {
		getLogger().info("Logic service is now running!");
		while (serviceRunning) {
			long before = System.currentTimeMillis();
			if(!World.getSingleton().getRegisteredPlayers().isEmpty()) {
				Iterator<Player> it$ = World.getSingleton().getRegisteredPlayers().iterator();
				while (it$.hasNext()) {
					Player player = it$.next();
					if (player == null) {
						continue;
					}
					if (!player.getSession().getChannel().isConnected()) {
						it$.remove();
						continue;
					}
					packetTasks.add(player.getPacketTask());
					tickTasks.add(player.getTickTask());
					resetTasks.add(player.getResetTask());
				}
			}
			World.getSingleton().getGroundItemManager().tick();
			EventManager.getSingleton().tick();
			if(!World.getSingleton().getRegisteredPlayers().isEmpty()) {
				SequentialTaskExecutor.executeTasks(packetTasks);
				SequentialTaskExecutor.executeTasks(tickTasks);
				final CountDownLatch latch = new CountDownLatch(World.getSingleton().getRegisteredPlayers().size());
				for (final Player p : World.getSingleton().getRegisteredPlayers()) {
					getProcessorThreads().execute(new Runnable() {
						@Override
						public void run() {
							try {
								synchronized (p) {
									PlayerUpdating.updatePlayer(p);
								}
							} finally {
								latch.countDown();
							}
						}
					});
				}
				try {
					latch.await();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				SequentialTaskExecutor.executeTasks(resetTasks);
				packetTasks.clear();
				tickTasks.clear();
				resetTasks.clear();
			}
			gameTicks++;
			long elapsed = System.currentTimeMillis() - before;
			setLastTickTime((int) elapsed);
			if(Launch.guiMode) {
				ServerWindow.getSingleton().updateServerInfo();
			}
			if (elapsed < 600) {
				try {
					Thread.sleep(Configuration.TICK_TIME - elapsed);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				badTicks++;
			}
		}
		getLogger()
				.info("The logic service is now offline. The server will not process.");
		System.exit(-1);
	}

	/**
	 * Gets the worker thread
	 * 
	 * @return the workerThread
	 */
	public static ExecutorService getWorkerThread() {
		return workerThread;
	}

	/**
	 * Gets the logger
	 * 
	 * @return the logger
	 */
	private static Logger getLogger() {
		return logger;
	}

	/**
	 * @param latch
	 *            the latch to set
	 */
	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}

	/**
	 * @return the latch
	 */
	public CountDownLatch getLatch() {
		return latch;
	}

	/**
	 * @return the processorThreads
	 */
	public ExecutorService getProcessorThreads() {
		return processorThreads;
	}

	/**
	 * @return the gameTicks
	 */
	public int getGameTicks() {
		return gameTicks;
	}

	/**
	 * @return the badTicks
	 */
	public int getBadTicks() {
		return badTicks;
	}

	/**
	 * @param badTicks the badTicks to set
	 */
	public void setBadTicks(int badTicks) {
		this.badTicks = badTicks;
	}

	/**
	 * @return the lastTickTime
	 */
	public int getLastTickTime() {
		return lastTickTime;
	}

	/**
	 * @param lastTickTime the lastTickTime to set
	 */
	public void setLastTickTime(int lastTickTime) {
		this.lastTickTime = lastTickTime;
	}

}