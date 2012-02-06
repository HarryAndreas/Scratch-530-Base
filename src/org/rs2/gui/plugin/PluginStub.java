package org.rs2.gui.plugin;

import java.net.URL;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class PluginStub {
	
	/**
	 * The Plugin as a class
	 */
	private Plugin pluginClass;
	
	/**
	 * The URL to grab the build plugin from
	 */
	private URL buildURL;
	
	/**
	 * Version of the plugin
	 */
	private double version;
	
	/**
	 * Author and description of the plugin
	 */
	private String author, description, name, type;
	
	/**
	 * @param pluginClass the pluginClass to set
	 */
	public void setPluginClass(Plugin pluginClass) {
		this.pluginClass = pluginClass;
	}

	/**
	 * @return the buildURL
	 */
	public URL getBuildURL() {
		return buildURL;
	}

	/**
	 * @param buildURL the buildURL to set
	 */
	public void setBuildURL(URL buildURL) {
		this.buildURL = buildURL;
	}
	
	/**
	 * @return the version
	 */
	public double getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(double version) {
		this.version = version;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets a build plugin
	 * @return
	 */
	public Plugin getPlugin() {
		return pluginClass;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
}