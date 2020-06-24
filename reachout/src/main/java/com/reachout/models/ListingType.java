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
	 * Returns the corresponding ListingType by ordindal
	 * 
	 * @param statusValue
	 * @return
	 */
	public static ListingType getByOrdinal(int listingType) {
		return ListingType.values()[listingType];
	}
	
	public static ListingType getByValue(String listingType) {
		for(ListingType lt : ListingType.values()) {
			if(lt.name.equalsIgnoreCase(listingType)) {
				return lt;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return getName();
	}
}
