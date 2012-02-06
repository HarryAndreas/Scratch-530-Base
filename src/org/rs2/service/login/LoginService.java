package org.rs2.service.login;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class LoginService implements Runnable {
	
	/**
	 * The service thread
	 */
	private final static ExecutorService serviceThread = Executors.newSingleThreadExecutor();
	
	/**
	 * Login service instance
	 */
	private static LoginService loginService;
	
	/**
	 * A login task BlockingQueue
	 */
	private BlockingQueue<LoginServiceTask> loginQueue;
	
	/**
	 * Is the service running?
	 */
	private boolean serviceRunning = Boolean.FALSE;
	
	/**
	 * The logger instance
	 */
	private final Logger logger;
	
	/**
	 * Construct the service
	 */
	private LoginService() {
		logger = Logger.getLogger(LoginService.class.getName());
	}
	
	/**
	 * Gets the singleton instance
	 * @return The login service
	 */
	public static LoginService getSingleton() {
		if(loginService == null) {
			loginService = new LoginService();
		}
		return loginService;
	}
	
	/**
	 * Starts the services
	 */
	public void init() {
		loginQueue = new LinkedBlockingQueue<LoginServiceTask>();
		serviceRunning = Boolean.TRUE;
		getServiceThread().submit(this);
	}
	
	/**
	 * Offers a login task to be processed
	 * @param task
	 */
	public void submit(LoginServiceTask task) {
		getLoginQueue().offer(task);
	}

	/**
	 * Runs the service
	 */
	@Override
	public void run() {
		getLogger().info("Login service is now running!");
		while(serviceRunning) {
			try {
				LoginServiceTask task = getLoginQueue().take();
				task.execute(getLogger());
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		getLogger().info("The login service has shutdown. All login attempts will be ignored.");
	}

	/**
	 * Gets the service thread
	 * @return the servicethread
	 */
	private static ExecutorService getServiceThread() {
		return serviceThread;
	}

	/**
	 * Gets the login queue
	 * @return the loginQueue
	 */
	private BlockingQueue<LoginServiceTask> getLoginQueue() {
		return loginQueue;
	}

	/**
	 * Gets the logger
	 * @return the logger
	 */
	private Logger getLogger() {
		return logger;
	}

}