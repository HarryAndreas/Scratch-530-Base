package org.rs2.model;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class RSTile {
	
	/**
	 * Variables for the tile
	 */
	private final int x, y, z;
	
	/**
	 * Construct the tile
	 * @param x The x coord
	 * @param y The y coord
	 * @param z The height
	 */
	public RSTile(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Creates a new location
	 * @param x The x of the location
	 * @param y The y of the location
	 * @param z The heigh of the location
	 * @return
	 */
	public static RSTile locate(int x, int y, int z) {
		return new RSTile(x, y, z);
	}
	
	/**
	 * The x of the location
	 * @return
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * The y of the location
	 * @return
	 */
	public int getY() {
		return y;
	}

	/**
	 * The z of the location
	 * @return
	 */
	public int getZ() {
		return z;
	}
	
	public int getLocalX() {
		return x - 8 * (getRegionX() - 6);
	}
	
	public int getLocalY() {
		return y - 8 * (getRegionY() - 6);
	}
	
	public int getLocalX(RSTile loc) {
		return x - 8 * (loc.getRegionX() - 6);
	}
	
	public int getLocalY(RSTile loc) {
		return y - 8 * (loc.getRegionY() - 6);
	}
	
	public int getRegionX() {
		return (x >> 3);
	}
	
	public int getRegionY() {
		return (y >> 3);
	}
	
	public boolean withinDistance(RSTile other, int dist) {
		if(other.z != z) {
			return false;
		}
		return distance(other) <= dist;
	}
	
	public boolean withinDistance(RSTile other) {
		if (other.z != z) {
			return false;
		}
		int deltaX = other.x - x, deltaY = other.y - y;
		return deltaX <= 16 && deltaX >= -15 && deltaY <= 16 && deltaY >= -15;
	}
	
	public int getDistance(RSTile pos) {
		return distance(pos);
	}
	
	public int distance(RSTile other) {
		return (int) distanceFormula(x, y, other.x, other.y);
	}
	
	public static double distanceFormula(int x, int y, int x2, int y2) {
		return Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof RSTile) {
			RSTile other = (RSTile) object;
			return x == other.x && y == other.y && other.z == z;
		}
		return false;
	}

}