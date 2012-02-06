package org.rs2.net.packethandler;

import java.io.File;
import java.util.logging.Logger;

import org.rs2.model.player.GameSession;
import org.rs2.net.msg.Message;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class PacketManager {
	
	/**
	 * Instance of the packet manager
	 */
	private static PacketManager instance;
	
	/**
	 * Logger instance
	 */
	private static final Logger logger = Logger.getLogger(PacketManager.class.getName());
	
	/**
	 * PacketHandler instances
	 */
	private final PacketHandler[] PACKET_HANDLERS;
	
	/**
	 * Construct the instance
	 */
	private PacketManager() {
		PACKET_HANDLERS = new PacketHandler[255];
	}
	
	/**
	 * Get the instance
	 * @return
	 */
	public static PacketManager getSingleton() {
		if(instance == null) {
			instance = new PacketManager();
		}
		return instance;
	}
	
	/**
	 * Loads the packets
	 */
	@SuppressWarnings("unchecked")
	public void init() throws Exception {
		int loaded = 0;
		String packageName = PacketManager.class.getPackage().getName()+".impl";
		String directory = "./src/"+packageName.replace(".", "/");
		File[] handlers = new File(directory).listFiles();
		String currentHandler = "";
		for(File f : handlers) {
			currentHandler = f.getName().replace(".java", "");
			Class <PacketHandler> loadedClass = (Class<PacketHandler>) Class.forName(packageName+"."+currentHandler);
			PacketHandler handler = loadedClass.newInstance();
			for(int i : handler.getBinds()) {
				getPacketHandlers()[i] = handler;
				loaded++;
			}
		}
		getLogger().info("Attached "+loaded+" opcodes to packet handlers.");
	}
	
	/**
	 * Handles a packet
	 * @param s The session
	 * @param m The packet
	 */
	public void handlePacket(GameSession s, Message m) {
		int opcode = m.getOpcode();
		PacketHandler handler = getPacketHandlers()[opcode];
		if(handler == null) {
			return;
		}
		handler.handlePacket(m, s);
	}
	
	/**
	 * Getter
	 * @return the PACKET_HANDLERS
	 */
	private PacketHandler[] getPacketHandlers() {
		return PACKET_HANDLERS;
	}

	/**
	 * Getter
	 * @return the logger
	 */
	private static Logger getLogger() {
		return logger;
	}

}