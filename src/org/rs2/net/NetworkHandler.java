package org.rs2.net;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.rs2.Server;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class NetworkHandler {
	
	/**
	 * The owner of the network handler
	 */
	private Server owner;
	
	/**
	 * The server bootstrap
	 */
	private ServerBootstrap serverBootstrap;
	
	/**
	 * The Executor service used by netty
	 */
	private ExecutorService cachedPool;
	
	/**
	 * Constructor
	 * @param server The server instance that this object is for
	 */
	public NetworkHandler(Server server) {
		setOwner(server);
	}
	
	/**
	 * Binds a port
	 * @param port The port to bind
	 */
	public void bind(int port) {
		if(getServerBootstrap() != null) {
			System.err.println("Bootstrap is already bound");
			return;
		}
		cachedPool = Executors.newCachedThreadPool();
		setServerBootstrap(new ServerBootstrap());
		getServerBootstrap().setFactory(new NioServerSocketChannelFactory(cachedPool, cachedPool));
		getServerBootstrap().setPipelineFactory(new DefaultPipelineFactory());
		getServerBootstrap().bind(new InetSocketAddress("0.0.0.0", port));
	}

	/**
	 * Setter
	 * @param owner the owner to set
	 */
	public void setOwner(Server owner) {
		this.owner = owner;
	}

	/**
	 * Getter
	 * @return the owner
	 */
	public Server getOwner() {
		return owner;
	}



	/**
	 * @param serverBootstrap the serverBootstrap to set
	 */
	public void setServerBootstrap(ServerBootstrap serverBootstrap) {
		this.serverBootstrap = serverBootstrap;
	}



	/**
	 * @return the serverBootstrap
	 */
	public ServerBootstrap getServerBootstrap() {
		return serverBootstrap;
	}

}