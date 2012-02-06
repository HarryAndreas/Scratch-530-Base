package org.rs2.model.player;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class DatabaseResult {
	
	/**
	 * The password for the result
	 */
	private String password;
	
	/**
	 * The rights of the result
	 */
	private int rights;
	
	/**
	 * The profile data
	 */
	private byte[] profile;
	
	/**
	 * Database results
	 */
	public DatabaseResult(String password, int rights, byte[] profile) {
		setPassword(password);
		setRights(rights);
		setProfile(profile);
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param rights the rights to set
	 */
	public void setRights(int rights) {
		this.rights = rights;
	}

	/**
	 * @return the rights
	 */
	public int getRights() {
		return rights;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(byte[] profile) {
		this.profile = profile;
	}

	/**
	 * @return the profile
	 */
	public byte[] getProfile() {
		return profile;
	}

}