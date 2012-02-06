package org.rs2.net;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.rs2.net.pipeline.DefaultProtocolEncoder;
import org.rs2.net.pipeline.handshake.HandshakeDecoder;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class DefaultPipelineFactory implements ChannelPipelineFactory{

	/**
	 * Gets the pipeline
	 */
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = new DefaultChannelPipeline();
		pipeline.addLast("decoder", new HandshakeDecoder());
		pipeline.addLast("encoder", new DefaultProtocolEncoder());
		pipeline.addLast("handler", new NetworkMessageHandler());
		return pipeline;
	}

}