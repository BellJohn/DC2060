/**
 * 
 */
package com.reachout.models;

/**
 * @author John
 *
 */
public enum ListingType {
	REQUEST(0,"Request"), SERVICE(1,"Service");

	private final int ordinal;
	private final String name;
	ListingType(int ordinal, String name) {
		this.ordinal = ordinal;
		this.name = name;
	}
	
	public int getOrdindal() {
		return ordinal;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the corresponding EntityStatus by ordindal
	 * 
	 * @param statusValue
	 * @return
	 */
	public static EntityStatus getByOrdinal(int listingValue) {
		return EntityStatus.values()[listingValue];
	}

	@Override
	public String toString() {
		return getName();
	}
}
