/**
 * 
 */
package com.reachout.models;

/**
 * @author John
 *
 */
public class Location {

	private int locId;
	private double locLat;
	private double locLong;
	
	public Location() {}

	public Location(int locId, double locLat, double locLong) {
		this.locId = locId;
		this.locLat = locLat;
		this.locLong = locLong;
	}

	/**
	 * @return the locId
	 */
	public int getLocId() {
		return locId;
	}

	/**
	 * @param locId the locId to set
	 */
	public void setLocId(int locId) {
		this.locId = locId;
	}

	/**
	 * @return the locLat
	 */
	public double getLocLat() {
		return locLat;
	}

	/**
	 * @param locLat the locLat to set
	 */
	public void setLocLat(double locLat) {
		this.locLat = locLat;
	}

	/**
	 * @return the locLong
	 */
	public double getLocLong() {
		return locLong;
	}

	/**
	 * @param locLong the locLong to set
	 */
	public void setLocLong(double locLong) {
		this.locLong = locLong;
	}
	
	public String toString() {
		return String.format("%s,%s", locLat, locLong);
	}
	
}
