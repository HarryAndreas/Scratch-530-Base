package org.rs2.cache;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * The 'jagfs' which is called Jagex file server, this is the process of sending
 * files over a {@link Socket}, which will be an easier and faster way for
 * people to recieve the game <code>cache</code>.
 * 
 * @author Thomas LeGodais
 * @since Dec 30, 2011
 * @version 1.0
 * 
 */
public class JagFS {

	/**
	 * The id of the cache.
	 */
	private int id = 0;

	/**
	 * The data file.
	 */
	private RandomAccessFile data;

	/**
	 * The index file.
	 */
	private RandomAccessFile index;

	/**
	 * Creates the new Jagex File-Server.
	 * 
	 * @param id
	 *            The id of the cache file.
	 * @param data
	 *            The data file of the cache.
	 * @param index
	 *            The index files of the cache.
	 */
	public JagFS(int id, RandomAccessFile data, RandomAccessFile index) {
		this.id = id;
		this.data = data;
		this.index = index;
	}

	/**
	 * Gets the length of the index file.
	 * 
	 * @return The length.
	 * @throws IOException
	 *             Error getting the length.
	 */
	public int getLength() throws IOException {
		int length = (int) (index.length() / 6);
		if (length == 0) {
			return 0;
		} else {
			return length;
		}
	}
	
	/**
	 * Gets an index file from the cache.
	 * 
	 * @param id The file id.
	 * @return The index file.
	 */
	public byte[] get(int id) {
			try {
				byte header[] = new byte[6];
				index.seek(id * 6);
	            index.readFully(header);
	            int length = ((header[0] & 0xff) << 16) + ((header[1] & 0xff) << 8) + (header[2] & 0xff);
	            int position = ((header[3] & 0xff) << 16) + ((header[4] & 0xff) << 8) + (header[5] & 0xff); // 24 bit
	            if (position > 0) {
	                int offset = 0;
	                int blockOffset = 0;
	                byte file[] = new byte[length];
	                while (offset < length) {
	                    byte header2[] = new byte[8];
	                    
	                    int pos = position * 520;
	                    data.seek(pos);
	                   // data.seek(pos < 0 ? 0 : pos);
	                    data.readFully(header2);
	                    position = ((header2[4] & 0xff) << 16) + ((header2[5] & 0xff) << 8) + (header2[6] & 0xff); 
	                    while (offset < length && blockOffset < 512) {
	                        file[offset] = (byte) data.read();
	                        offset++;
	                        blockOffset++;
	                    }
	                    blockOffset = 0;
	                }
	                return file;
	            }
			} catch(Exception e) {
				e.printStackTrace();
			}
		
		return null;
	}
	
	/**
	 * Writes to a file.
	 * 
	 * @param id The cache id.
	 * @param file The file.
	 * @return Do we write?
	 */
	public boolean write(int id, byte... file) {
		synchronized(this) {
			try {
	            byte header[] = new byte[6];
	            int length = file.length;
	            int position = (int) ((data.length() + 519L) / 520L); // writing the
	            header[0] = (byte) (length >> 16);
	            header[1] = (byte) (length >> 8);
	            header[2] = (byte) length;
	            header[3] = (byte) (position >> 16);
	            header[4] = (byte) (position >> 8);
	            header[5] = (byte) position;
	            index.seek(id * 6);
	            index.write(header, 0, 6);
	            int offset = 0;
	            int blockIndex = 0;
	            byte[] header2 = new byte[8];
	            while (offset < length) {
	                position++; // simple
	                data.seek(position * 520);
	                header2[0] = (byte) (id >> 8);
	                header2[1] = (byte) id;
	                header2[2] = (byte) (blockIndex >> 8);
	                header2[3] = (byte) blockIndex;
	                header2[4] = (byte) (position >> 16);
	                header2[5] = (byte) (position >> 8);
	                header2[6] = (byte) position;
	                header2[7] = (byte) id;
	                data.write(header2, 0, 8);
	                int amt = length - offset;
	                if (amt > 512) {
	                    amt = 512;
	                }
	                data.write(file, offset, amt);
	                offset += amt;
	                blockIndex++;
	            }
	            return true;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * Obtains a cache id.
	 * 
	 * @return The cache id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the data file.
	 * 
	 * @return The data file.
	 */
	public RandomAccessFile getData() {
		return data;
	}

	/**
	 * Gets the index file.
	 * 
	 * @return The index file.
	 */
	public RandomAccessFile getIndex() {
		return index;
	}
}