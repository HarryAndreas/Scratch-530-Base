package org.rs2.service.ondemand;

import org.jboss.netty.channel.Channel;
import org.rs2.cache.FileStore;
import org.rs2.net.msg.Message;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class OnDemandRequest {
	
	/**
	 * The channel which requested a file
	 */
	private Channel channel;
	
	/**
	 * The requests for the file
	 */
	private int[] request;
	
	/**
	 * Construct the request
	 * @param request
	 * @param channel
	 */
	public OnDemandRequest(int[] request, Channel channel) {
		this.channel = channel;
		this.request = request;
	}
	
	/**
	 * Serves the request
	 */
	public void serveRequest() {
		if(!channel.isConnected())
			return;
		Message prepared = FileStore.getSingleton().getFile(request[0], request[1]);
		final Message resp = prepared;
		if(resp != null) {
			channel.write(resp);
		}
	}

}