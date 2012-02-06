package org.rs2.service.ondemand;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class OnDemandService {
	
	/**
	 * Instance of the OnDemandService
	 */
	private static OnDemandService instance;
	
	/**
	 * The worker pool of threads
	 */
	private final Executor workerPool;
	
	/**
	 * Logger instance
	 */
	private final Logger logger = Logger.getLogger(OnDemandService.class.getName());
	
	/**
	 * A work queue of requests to be dealt with
	 */
	public BlockingQueue<OnDemandRequest> workQueue = new LinkedBlockingQueue<OnDemandRequest>();
	
	/**
	 * Is the service working
	 */
	public boolean serviceWorking = Boolean.FALSE;
	
	/**
	 * The amount of workers
	 */
	private static final int WORKER_AMOUNT = 2;
	
	/**
	 * Creates the instance
	 */
	private OnDemandService() {
		workerPool = Executors.newCachedThreadPool();
	}
	
	/**
	 * Gets the singleton instance
	 * @return The single instance of this service
	 */
	public static OnDemandService getSingleton() {
		if(instance == null) {
			instance = new OnDemandService();
		}
		return instance;
	}
	
	/**
	 * Pushes a request to work
	 * @param req
	 */
	public void pushRequest(OnDemandRequest req) {
		try {
			workQueue.offer(req, 10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts the service
	 */
	public void init() {
		serviceWorking = Boolean.TRUE;
		for(int i = 0; i < WORKER_AMOUNT; i++) {
			workerPool.execute(new OnDemandWorker());
		}
		getLogger().info(WORKER_AMOUNT+" On-Demand Workers are now online!");
	}

	/**
	 * The logger instance
	 * @return The logger
	 */
	private Logger getLogger() {
		return logger;
	}

}