/**
 * 
 */
package com.reachout.models;

/**
 * Enum of possible statuses of a listing. Ordinal is the request status value
 * which will be stored in the database.
 * 
 * @author John
 *
 */
public enum ListingStatus {

	OPEN(0, "OPEN"), PENDING(1, "PENDING"), CLOSED(2, "CLOSED");

	private final int ordinal;
	private final String name;

	/**
	 * Full constructor
	 * 
	 * @param ordinal
	 */
	ListingStatus(int ordinal, String name) {
		this.ordinal = ordinal;
		this.name = name;
	}

	/**
	 * Returns the ordinal of the ListingStatus
	 * 
	 * @return
	 */
	public int getOrdinal() {
		return this.ordinal;
	}

	public String getName() {
		return name;
	}
	
	/**
	 * Returns the corresponding ListingStatus by ordindal
	 * 
	 * @param statusValue
	 * @return
	 */
	public static ListingStatus getByOrdinal(int statusValue) {
		return ListingStatus.values()[statusValue];
	}

	@Override
	public String toString() {
		return getName();
	}
}
