package org.rs2.net.pipeline.js5;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.rs2.cache.FileStore;
import org.rs2.net.msg.Message;
import org.rs2.service.ondemand.OnDemandRequest;
import org.rs2.service.ondemand.OnDemandService;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class JS5Decoder extends FrameDecoder {
	
	/**
	 * The executor for writing the cache
	 */
	private final ExecutorService service = Executors.newSingleThreadExecutor();

	@Override
	protected Object decode(ChannelHandlerContext ctx, final Channel chan, ChannelBuffer buff) throws Exception {
		final Queue<int[]> imporantFiles = new LinkedList<int[]>();
		final Queue<int[]> backgroundFiles = new LinkedList<int[]>();
		requestLoop: while (chan.isConnected() &&buff.readableBytes() >= 4) {
			final int type = buff.readByte() & 0xff;
			final int cache = buff.readByte() & 0xff;
			final int id = buff.readShort() & 0xffff;
			switch (type) {
			case 0:
				if (backgroundFiles.size() > 0) {
					break requestLoop;
				}
				backgroundFiles.add(new int[] { cache, id });
				break;
			case 1:
				imporantFiles.offer(new int[] { cache, id });
				break;
			case 2:
			case 3:
				backgroundFiles.clear();
				imporantFiles.clear();
				break;
			case 4:
				break;
			}
		}
		if (imporantFiles.size() > 0) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					handleIteration(imporantFiles.iterator(), chan);
				};
			};
			service.execute(r);
		}
		if (backgroundFiles.size() > 0) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					handleIteration(backgroundFiles.iterator(), chan);
				}
			};
			service.execute(r);
		}
		return null;
	}
	
	/**
	 * Iterates through requests
	 * @param it$
	 * @param c
	 */
	private void handleIteration(Iterator<int[]> it$, Channel c) {
		while(it$.hasNext() && c.isConnected()) {
			int[] req = it$.next();
			if(req != null) {
				pushRequest(c, req[0], req[1]);
			} else {
				it$.remove();
			}
		}
	}
	
	/**
	 * Pushes a request to the js5 workers
	 * @param c
	 * @param cache
	 * @param id
	 */
	private void pushRequest(final Channel c, final int cache, final int id) {
		OnDemandService.getSingleton().pushRequest(new OnDemandRequest(new int[] {cache, id }, c));
		/*
		if(!c.isConnected())
			return;
		Message prepared = null;
		synchronized(FileStore.getSingleton()) {
			prepared = FileStore.getSingleton().getFile(cache, id);
		}
		final Message resp = prepared;
		if(resp != null) {
			c.write(resp);
		}
		*/
	}

}