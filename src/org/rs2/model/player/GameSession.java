package org.rs2.model.player;

import java.util.LinkedList;
import java.util.Queue;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.rs2.net.msg.Message;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class GameSession {
	
	/**
	 * Player instance
	 */
	private Player player;
	
	/**
	 * Channel instance
	 */
	private Channel channel;
	
	/**
	 * A queue of packets to be processed
	 */
	private Queue<Message> queuedPackets = new LinkedList<Message>();
	
	/**
	 * Construct the instance
	 * @param channel
	 */
	public GameSession(Channel channel) {
		setChannel(channel);
	}
	
	/**
	 * Writes an object to the channel
	 * @param o The object to write
	 * @return The ChannelFuture
	 */
	public ChannelFuture write(Object o) {
		return getChannel().write(o);
	}
	
	/**
	 * Pushes a packet to be processed ASAP
	 * @param m The packet to be processed
	 */
	public void pushPacket(Message m) {
		queuedPackets.offer(m);
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	/**
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * @param queuedPackets the queuedPackets to set
	 */
	public void setQueuedPackets(Queue<Message> queuedPackets) {
		this.queuedPackets = queuedPackets;
	}

	/**
	 * @return the queuedPackets
	 */
	public Queue<Message> getQueuedPackets() {
		return queuedPackets;
	}

}