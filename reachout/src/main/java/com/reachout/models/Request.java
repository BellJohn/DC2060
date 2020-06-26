/**
 *
 */
package com.reachout.models;

/**
 * @author John
 *
 */
public class Request extends Listing {

	private static final long serialVersionUID = -7360265992017228189L;

	/**
	 * Fully formed constructor including request status. Calls other partially
	 * formed constructor which handles setting this Listing child type as a Request
	 * Type for the database
	 *
	 * @param title
	 * @param description
	 * @param county
	 * @param city
	 * @param userId
	 * @param status
	 * @param priority
	 */
	public Request(String title, String description, String county, String city, String street, int userId, ListingStatus status,
			String priority, int visibility, int locationId) {
		this(title, description, county, city, street, userId, priority, visibility, locationId);
		this.status = status;
	}

	/**
	 * Fully formed constructor minus request status. Handles the setting of this
	 * Listing child type to that of a Request Type
	 *
	 * @param title
	 * @param description
	 * @param county
	 * @param city
	 */
	public Request(String title, String description, String county, String city, String street, int userId, String priority, int visibility, int locationId) {
		super(title, description, county, city, street, userId, priority, visibility, locationId);
		this.listingType = ListingType.REQUEST;
		this.priority = priority;

	}

	/**
	 * Empty constructor for Hibernate. Do not use
	 */
	public Request() {
		super();
	}

}
