package org.rs2.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class BandwidthMonitor {
	
	public static AtomicLong bytesSent = new AtomicLong();
	public static AtomicLong bytesReceived = new AtomicLong();
	

}
