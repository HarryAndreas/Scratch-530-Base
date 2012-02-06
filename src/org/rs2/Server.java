package org.rs2;

import org.rs2.net.NetworkHandler;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class Server {
	
	/**
	 * Server singleton instance
	 */
	private static Server serverSingleton;
	
	/**
	 * Network handler instance
	 */
	private NetworkHandler networkHandler;
	
	/**
	 * Configuration variables for the server
	 */
	private final int PORT, MAX_PLAYERS;
	
	/**
	 * Constructor 
	 * @param port The port on which the server to bind 
	 * @param maxPlayers The max amount of players allowed to be registered
	 */
	private Server(int port, int maxPlayers) {
		this.PORT = port;
		this.MAX_PLAYERS = maxPlayers;
	}
	
	/**
	 * Creates the server singleton
	 * @param port
	 * @param maxPlayers
	 */
	public static void createServer(int port, int maxPlayers) throws Exception {
		if(serverSingleton != null) {
			throw new Exception("SINGLETON ALREADY EXISTS!");
		}
		serverSingleton = new Server(port, maxPlayers);
	}
	
	/**
	 * Sets up the server essentially
	 */
	public void init() {
		if(getNetworkHandler() != null) {
			System.err.println("Network handler is already setup!");
			return;
		}
		setNetworkHandler(new NetworkHandler(this));
		getNetworkHandler().bind(PORT);
	}
	
	/**
	 * Gets the server singleton instance
	 */
	public static Server getSingleton() {
		return serverSingleton;
	}

	/**
	 * Getter
	 * @return the port to bind
	 */
	public int getPort() {
		return PORT;
	}

	/**
	 * Getter
	 * @return the Max amount of players
	 */
	public int getMaxPlayers() {
		return MAX_PLAYERS;
	}

	/**
	 * @param networkHandler the networkHandler to set
	 */
	private void setNetworkHandler(NetworkHandler networkHandler) {
		this.networkHandler = networkHandler;
	}

	/**
	 * @return the networkHandler
	 */
	public NetworkHandler getNetworkHandler() {
		return networkHandler;
	}

}