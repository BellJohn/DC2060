/**
 *
 */
package com.reachout.models;

/**
 * @author John
 *
 */
public class Service extends Listing {

	private static final long serialVersionUID = -7360265992017228189L;
	private static final String priority = null;

	/**
	 * Fully formed constructor including service status. Calls other partially
	 * formed constructor which handles setting this Listing child type as a Service
	 * Type for the database
	 *
	 * @param title
	 * @param description
	 * @param county
	 * @param city
	 * @param userId
	 * @param status
	 */
	public Service(String title, String description, String county, String city, String street, int userId,
			ListingStatus status, int visibility, int locationId) {
		this(title, description, county, city, street, userId, visibility, locationId);
		this.status = status;
	}

	/**
	 * Fully formed constructor minus request status. Handles the setting of this
	 * Listing child type to that of a Service Type
	 *
	 * @param title
	 * @param description
	 * @param county
	 * @param city
	 */
	public Service(String title, String description, String county, String city, String street, int userId,
			int visibility, int locationId) {
		super(title, description, county, city, street, userId, priority, visibility, locationId);
		this.listingType = ListingType.SERVICE;
	}

	public Service() {
		super();
	}

}
