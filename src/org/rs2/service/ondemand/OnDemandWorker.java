package org.rs2.service.ondemand;

import java.util.concurrent.BlockingQueue;

import org.rs2.cache.FileStore;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class OnDemandWorker implements Runnable {

	/**
	 * Runs the worker
	 */
	@Override
	public void run() {
		BlockingQueue<OnDemandRequest> tasks = OnDemandService.getSingleton().workQueue;
		while(OnDemandService.getSingleton().serviceWorking) {
			OnDemandRequest request = null;
			try {
				request = tasks.take();
			} catch(Exception e) {
				e.printStackTrace();
				continue;
			}
			synchronized(FileStore.getSingleton()) {
				request.serveRequest();
			}
 		}
	}

}
