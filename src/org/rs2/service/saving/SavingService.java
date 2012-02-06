package org.rs2.service.saving;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.rs2.model.RSTile;
import org.rs2.model.item.Item;
import org.rs2.model.player.DatabaseResult;
import org.rs2.model.player.Levels;
import org.rs2.model.player.Player;
import org.rs2.model.player.containers.Equipment;
import org.rs2.mysql.DatabaseConnection;
import org.rs2.mysql.DatabaseManager;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class SavingService {
	
	/**
	 * Logger instance
	 */
	private static final Logger logger = Logger.getAnonymousLogger();
	
	/**
	 * Singleton instance
	 */
	private static SavingService instance;
	
	/**
	 * The worker thread
	 */
	private final ExecutorService workerPool;
	
	/**
	 * Construct the service
	 */
	private SavingService() {
		int nProc = Runtime.getRuntime().availableProcessors();
		workerPool = Executors.newFixedThreadPool(nProc >= 4 ? nProc / 2 : 2);
	}
	
	/**
	 * The player to save
	 * @param p
	 */
	public void savePlayer(final Player p) {
		final String name = p.getUnformattedName();
		final String pass = p.getHashedPassword();
		final int rights = p.getRights();
		final int x = p.getLocation().getX();
		final int y = p.getLocation().getY();
		final int z = p.getLocation().getZ();
		final int[] looks = p.getLooks().getLooks();
		final int[] colours = p.getLooks().getColours();
		final int gender = p.getLooks().getGender();
		final ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeShort(x);
		buffer.writeShort(y);
		buffer.writeByte(z);
		for(int i : looks) {
			buffer.writeByte(i);
		}
		for(int i : colours) {
			buffer.writeByte(i);
		}
		buffer.writeByte(gender);
		for(int i = 0; i < Levels.MAX_SKILLS; i++) {
			buffer.writeByte(p.getLevels().getLevel()[i]);
			buffer.writeInt((int)p.getLevels().getXp()[i]);
		}
		for(int i = 0; i < Equipment.SIZE; i++) {
			Item item = p.getEquipment().getContainer().get(i);
			if(item == null) {
				buffer.writeShort(30000);
			} else {
				buffer.writeShort(item.getItemId());
				buffer.writeInt(item.getItemAmount());
			}
		}
		for(int i = 0; i < 28; i++) {
			Item item = p.getInventory().get(i);
			if(item == null) {
				buffer.writeShort(30000);
			} else {
				buffer.writeShort(item.getItemId());
				buffer.writeInt(item.getItemAmount());
			}
		}
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					DatabaseConnection c = DatabaseManager.getSingleton().getConnection("player-loader");
					PreparedStatement s = c.getStatement("SELECT null FROM players WHERE name = '"+ name + "' limit 1");
					PreparedStatement ps = null;
					ResultSet rs = s.executeQuery();
					if (!rs.next()) {
						ps = c.getStatement("INSERT INTO players (name, password, rights, data) VALUES(?, ?, ?, ?)");
						ps.setString(1, name);
						ps.setString(2, pass);
						ps.setInt(3, rights);
						ps.setBytes(4, buffer.array());
					} else {
						ps = c.getStatement(
						"UPDATE players SET data = ? WHERE name = ?");
						ps.setObject(1, buffer.array());
						ps.setString(2, name);
					}
					ps.executeUpdate();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		};
		getWorkerPool().submit(r);
	}
	
	/**
	 * Loads data from the result
	 * @param p
	 * @param result
	 */
	public static void loadData(Player p, DatabaseResult result) {
		ByteBuffer profile = ByteBuffer.wrap(result.getProfile());
		p.setLocation(RSTile.locate(profile.getShort(), profile.getShort(), profile.get()));
		for(int i = 0; i < p.getLooks().getLooks().length; i++) {
			p.getLooks().getLooks()[i] = profile.get();
		}
		for(int i = 0; i < p.getLooks().getColours().length; i++) {
			p.getLooks().getColours()[i] = profile.get();
		}
		p.getLooks().setGender(profile.get());
		for(int i = 0; i < Levels.MAX_SKILLS; i++) {
			p.getLevels().getLevel()[i] = profile.get();
			p.getLevels().getXp()[i] = profile.getInt();
		}
		for(int i = 0; i < Equipment.SIZE; i++) {
			int id = profile.getShort();
			if(id == 30000) {
				p.getEquipment().getContainer().set(i, null);
			} else {
				int am = profile.getInt();
				p.getEquipment().getContainer().set(i, new Item(id, am));
			}
		}
		for(int i = 0; i < 28; i++) {
			int id = profile.getShort();
			if(id == 30000) {
				p.getInventory().set(i, null);
			} else {
				int am = profile.getInt();
				p.getInventory().set(i, new Item(id, am));
			}
		}
	}
	
	/**
	 * Gets the singleton instance
	 * @return
	 */
	public static SavingService getSingleton() {
		if(instance == null) {
			instance = new SavingService();
		}
		return instance;
	}

	/**
	 * @return the workerPool
	 */
	public ExecutorService getWorkerPool() {
		return workerPool;
	}

	/**
	 * @return the logger
	 */
	public static Logger getLogger() {
		return logger;
	}

}