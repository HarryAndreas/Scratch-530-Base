package org.rs2.gui.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.rs2.gui.ServerWindow;
import org.rs2.gui.plugin.cmd.CommandFactory;
import org.rs2.gui.plugin.impl.DownloadFilePlugin;
import org.rs2.gui.plugin.type.CommandPlugin;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class PluginFactory {
	
	/**
	 * Singleton instance of the PluginFactory
	 */
	private static PluginFactory instance;
	
	/**
	 * Logger instance
	 */
	private static Logger logger = Logger.getLogger(PluginFactory.class.getName());
	
	/**
	 * The File for Plugin Information
	 */
	private final File PLUGIN_INFORMATION_DIRECTORY;
	
	/**
	 * Loaded plugins
	 */
	private final Map<String, PluginStub> LOADED_PLUGINS;
	
	/**
	 * Gets the singleton instance 
	 * @return The singleton instance of the PluginFactory
	 */
	public static PluginFactory getSingleton() {
		if(instance == null) {
			instance = new PluginFactory();
		}
		return instance;
	}
	
	/**
	 * Construct the object
	 */
	private PluginFactory() {
		PLUGIN_INFORMATION_DIRECTORY = new File("./plugins/info/");
		LOADED_PLUGINS = new HashMap<String, PluginStub>();
	}
	
	public void test() {
		Plugin p = new DownloadFilePlugin();
		Properties a = new Properties();
		a.setProperty("name", p.getClass().getSimpleName());
		a.setProperty("author", "Harry Andreas");
		a.setProperty("version", "1.0");
		a.setProperty("description", "A plugin command that downloads files from the specified parameters.");
		a.setProperty("build", "http://www.runecore.org/plugins/download.jar");
		a.setProperty("invoke-from", p.getClass().getName());
		a.setProperty("plugin-type", p.getClass().getInterfaces()[0].getSimpleName());
		try {
			a.storeToXML(new FileOutputStream(new File("./plugins/info/download-cmd.xml")), "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts the plugin factory
	 */
	public void init() {
		File[] informationFiles = PLUGIN_INFORMATION_DIRECTORY.listFiles();
		for(File f : informationFiles) {
			if(f.getName().toLowerCase().endsWith(".xml")) {
				PluginStub stub = createStub(f);
				LOADED_PLUGINS.put(f.getName().toLowerCase().replaceAll(".xml", ""), stub);
				boolean isCommand = stub.getPlugin() instanceof CommandPlugin;
				String type = "standard plugin";
				if(isCommand) {
					CommandFactory.getSingleton().register((CommandPlugin)stub.getPlugin());
					type = "command";
				}
				ServerWindow.getSingleton().printCommandText("Registered new plugin; "+stub.getName()+". The plugin is a "+type+".");
				//logger.info("Registered new plugin; "+stub.getName()+". The plugin is a "+type+".");
			}
		}
	}
	
	/**
	 * Creates a PluginStub and attaches the plugin 
	 * @param stubFile The file from which to build the stub from
	 * @return The PluginStub with attached Plugin
	 */
	private PluginStub createStub(File stubFile) {
		PluginStub stub = new PluginStub();
		Properties stubProps = new Properties();
		try {
			stubProps.loadFromXML(new FileInputStream(stubFile));
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		stub.setName(stubProps.getProperty("name"));
		stub.setAuthor(stubProps.getProperty("author"));
		stub.setDescription(stubProps.getProperty("description"));
		try {
			stub.setBuildURL(new URL(stubProps.getProperty("build")));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		stub.setVersion(Double.parseDouble(stubProps.getProperty("version")));
		try {
			stub.setPluginClass((Plugin)Class.forName(stubProps.getProperty("invoke-from")).newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//Clear properites and null instance
		//To prevent memory build ups
		stubProps.clear();
		stubProps = null;
		return stub;
	}
	
}