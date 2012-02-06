package org.rs2.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.logging.Logger;

import org.rs2.gui.ServerWindow;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class FileDownload {
	
	/**
	 * CompressionType
	 * @author Harry Andreas
	 */
	public static enum CompressionType {
		NONE,
		ZIP,
		RAR,
		GZIP;
	}
	
	/**
	 * Download location
	 */
	private final URL downloadLocation;
	private boolean uncompress;
	private CompressionType compressionType = CompressionType.NONE;
	
	/**
	 * Construct the object
	 * @param downloadLocation
	 */
	public FileDownload(URL downloadLocation, boolean uncompress, CompressionType type) {
		this.downloadLocation = downloadLocation;
	}
	
	/**
	 * Starts the download
	 */
	public void start() {
		String[] split = downloadLocation.getPath().replaceAll("/", " ").split(" ");
		String fileName = split[split.length - 1];
		fileName = FileStore.getFileName(fileName, "downloads");
		ServerWindow.getSingleton().printCommandText("Starting download for "+fileName);
		download(fileName, "./downloads/");
	}
	
	private void download(String name, String directory) {
		URLConnection connection;
		InputStream input = null;
		try {
			connection = downloadLocation.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
			//connection.setReadTimeout(2000);
			input = connection.getInputStream();
			long size = connection.getContentLength();
			ServerWindow.getSingleton().printCommandText("Downloading file w/ size "+formatSize(size));
			byte[] buf = new byte[512];
			ByteBuffer buffer = ByteBuffer.allocate(connection.getContentLength());
			int done = 0;
			int percent = 0;
			while (true) {
                int len = input.read(buf);
                if (len == -1) {
                    break;
                }
                done += len;
                buffer.put(buf, 0, len);
                if((int)(((double)done / (double)size) * 100D) != percent) {
					percent = (int)(((double)done / (double)size) * 100D);
					ServerWindow.getSingleton().printCommandText("Downloaded..."+percent+"%");
				}
			}
			new FileOutputStream(new File(directory + name)).write(buffer.array());
			ServerWindow.getSingleton().printCommandText("Done!");
		} catch(Exception e) {
			ServerWindow.getSingleton().textField.setEditable(true);
			ServerWindow.getSingleton().textField.setEnabled(true);
			ServerWindow.getSingleton().printCommandText("Download failed!");
			System.out.println("Download failed :(");
			e.printStackTrace();
		}
	}
	
	/**
	 * Size of the file
	 * @param size The size in a long
	 * @return The size in MB
	 */
	public static String formatSize(long size) {
		if (size <= 0)
			return "0";
		final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size/ Math.pow(1024, digitGroups))+ " " + units[digitGroups];
	}

	/**
	 * @return the uncompress
	 */
	public boolean isUncompress() {
		return uncompress;
	}

	/**
	 * @param uncompress the uncompress to set
	 */
	public void setUncompress(boolean uncompress) {
		this.uncompress = uncompress;
	}

	/**
	 * @return the compressionType
	 */
	public CompressionType getCompressionType() {
		return compressionType;
	}

	/**
	 * @param compressionType the compressionType to set
	 */
	public void setCompressionType(CompressionType compressionType) {
		this.compressionType = compressionType;
	}

}
