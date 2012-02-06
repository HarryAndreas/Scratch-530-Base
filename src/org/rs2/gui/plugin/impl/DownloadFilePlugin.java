package org.rs2.gui.plugin.impl;

import java.net.URL;

import org.rs2.gui.ServerWindow;
import org.rs2.gui.plugin.type.CommandPlugin;
import org.rs2.util.FileDownload;
import org.rs2.util.FileDownload.CompressionType;

public class DownloadFilePlugin implements CommandPlugin {

	@Override
	public Object execute(Object... args) {
		String usedCmd = (String)args[0];
		String url = (String)args[1];
		FileDownload dl = null;
		if(args.length > 3) {
			String compression = (String) args[2];
			if(compression.equalsIgnoreCase("-c")) {
				CompressionType type = getType((String)args[3]);
				if(type == null) {
					return null;
				}
				ServerWindow.getSingleton().printCommandText(usedCmd+" >> "+url+" COMPRESSION FORMAT = "+type);
				try {
					dl = new FileDownload(new URL(url), true, type);
				} catch(Exception e) {
					e.printStackTrace();
					return null;
				}
			} else {
				ServerWindow.getSingleton().printCommandText("Invalid parameter '"+compression+"' entered.");
				return null;
			}
		} else {
			ServerWindow.getSingleton().printCommandText(usedCmd+" >> "+url);
			try {
				dl = new FileDownload(new URL(url), false, CompressionType.NONE);
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		dl.start();
		return null;
	}
	
	/**
	 * Gets the type of a compression
	 * @param str
	 * @return
	 */
	private static CompressionType getType(String str) {
		CompressionType type = CompressionType.NONE;
		str = str.toLowerCase();
		if(str.equalsIgnoreCase("gzip")) {
			type = CompressionType.GZIP;
		} else if(str.equalsIgnoreCase("zip")) {
			type = CompressionType.ZIP;
		} else if(str.equalsIgnoreCase("rar")) {
			type = CompressionType.RAR;
		} else {
			ServerWindow.getSingleton().printCommandText("Compression type "+str+" not supported!");
			return null;
		}
		return type;
	}

	@Override
	public String[] bindCommands() {
		return new String[] {
			"get", "download", "dl", "grab", "get"	
		};
	}

}