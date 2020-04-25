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
public enum EntityStatus {

	OPEN(0, "OPEN"), ACCEPTED(1, "ACCEPTED"), COMPLETE(2, "COMPLETE");

	private final int ordinal;
	private final String name;

	/**
	 * Full constructor
	 * 
	 * @param ordinal
	 */
	EntityStatus(int ordinal, String name) {
		this.ordinal = ordinal;
		this.name = name;
	}

	/**
	 * Returns the ordinal of the EntityStatus
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
	 * Returns the corresponding EntityStatus by ordindal
	 * 
	 * @param statusValue
	 * @return
	 */
	public static EntityStatus getByOrdinal(int statusValue) {
		return EntityStatus.values()[statusValue];
	}

	@Override
	public String toString() {
		return getName();
	}
}
