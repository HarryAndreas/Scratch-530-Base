package org.rs2.cache;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.logging.Logger;
import java.util.zip.CRC32;

import org.rs2.net.msg.Message;
import org.rs2.net.msg.MessageBuilder;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class FileStore {
	
	/**
	 * The instance of the FileStore
	 */
	private static FileStore instance;
	
	/**
	 * Gets the FileStore instance
	 * @return
	 */
	public static FileStore getSingleton() {
		if(instance == null) {
			try {
				instance = new FileStore(new File("./cache/"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	/**
	 * The data file.
	 */
	private RandomAccessFile dataFile = null;
	
	/**
	 * The cache indexes.
	 */
	private RandomAccessFile indexes[] = new RandomAccessFile[27];
	
	/**
	 * The crc file.
	 */
	private RandomAccessFile crcFile = null;
	
	/**
	 * The jagex file system.
	 */
	private JagFS jagFS[] = new JagFS[27];
	
	/**
	 * The jagex crc file.
	 */
	private JagFS jagCRC = null;
	
	/**
	 * The packet for CRC.
	 */
	private Message crcPacket;
	
	/**
	 * The logger instance.
	 */
	private Logger logger = Logger.getLogger(FileStore.class.getName());
	
	/**
	 * Creates a new FileStore, and loads the cache.
	 * 
	 * @param directory The directory of the cache.
	 * @throws IOException Can not load.
	 */
	private FileStore(File directory) throws Exception {
		int filesLoaded = 0;
		for(File file : directory.listFiles()) {
			if(file.isFile()) {
				if(file.getName().equals("main_file_cache.dat2")) {
					dataFile = new RandomAccessFile(file, "r");
				} else {
					if(file.getName().startsWith("main_file_cache.idx")) {
						int id = Integer.parseInt(file.getName().substring(file.getName().indexOf(".idx") + 4, file.getName().length()));					
						if(id != 255) {
							indexes[id] = new RandomAccessFile(file, "r");
						} else {
							crcFile = new RandomAccessFile(file, "r");
						}
						filesLoaded++;
					}
				}
			}
		}
		for(int i = 0; i < indexes.length; i++) {
			RandomAccessFile idxs = indexes[i];
			if(idxs != null) {
				jagFS[i] = new JagFS(i, dataFile, idxs);
			}
		}
		if(crcFile != null) {
			jagCRC = new JagFS(255, dataFile, crcFile);
		}
		MessageBuilder crct = new MessageBuilder();
		crct.writeByte((byte) 255).writeShort((short) 255).
		writeByte((byte) 0).writeInt(jagCRC.getLength() * 8);
		CRC32 crcc = new CRC32();
		for (int i = 0; i < jagCRC.getLength(); i++) {
			crcc.update(jagCRC.get(i));
			crct.writeInt((int) crcc.getValue());
			crct.writeInt(0);
			crcc.reset();
		}
		crcPacket = crct.toMessage();
		logger.info("Loaded " + filesLoaded + " cache files.");
	}
	
	/**
	 * Gets the file system.
	 * 
	 * @param i The file.
	 * @return The file system.
	 */
	public JagFS getFileSystem(int i) {
		if (i == 255) {
			return jagCRC;
		}
		return jagFS[i];
	}

	/**
	 * Gets the file requested.
	 * 
	 * @param cache The cache.
	 * @param id The file id.
	 * @return The file.
	 */
	public Message getFile(int cache, int id) {
		if (cache == 255 && id == 255) {
			return crcPacket;
		}
		try {
			JagFS fs = cache == 255 ? jagCRC : jagFS[cache];
			MessageBuilder gen = new MessageBuilder();
			gen.writeByte((byte) cache).writeShort((short) id);
			byte[] file = null;
			file = fs.get(id);
			if (file == null)
				return null;
			int length = (((file[1] & 0xff) << 24) + ((file[2] & 0xff) << 16) + ((file[3] & 0xff) << 8) + (file[4] & 0xff)) + 9;
			if (file[0] == 0) {
				length -= 4;
			}
			int blockOffset = 3;
			for (int offset = 0; offset < length; offset++) {
				if (blockOffset == 512) {
					gen.writeByte((byte) 0xFF);
					blockOffset = 1;
				}
				gen.writeByte(file[offset]);
				blockOffset++;
			}
			return gen.toMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}