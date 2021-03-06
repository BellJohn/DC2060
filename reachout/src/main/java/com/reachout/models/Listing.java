/**
 *
 */
package com.reachout.models;

import java.io.Serializable;
import java.util.Date;

/**
 * @author John
 *
 */
public abstract class Listing implements Serializable {

	private static final long serialVersionUID = -5986469962748716091L;
	protected String title;
	protected String description;
	protected String county;
	protected String city;
	protected String street;
	protected int userId;
	protected int id;
	protected ListingStatus status;
	protected long createdDate;
	protected ListingType listingType;
	protected String priority;
	protected int locationId;
	protected int visibility;

	/*
	 * Empty request constructor for hibernate to use
	 */
	public Listing() {
	}

	/**
	 * Full constructor with all fields represented
	 *
	 * @param title
	 * @param description
	 * @param county
	 * @param city
	 * @param userId
	 * @param status
	 * @param priority
	 */
	public Listing(String title, String description, String county, String city, String street, int userId,
			ListingStatus status, String priority, int visibility, int locationId) {
		this(title, description, county, city, street, userId, priority, visibility, locationId);
		this.status = status;
	}

	/**
	 * Fully formed constructor minus request status.
	 *
	 * @param title
	 * @param description
	 * @param county
	 * @param city
	 */
	public Listing(String title, String description, String county, String city, String street, int userId,
			String priority, int visibility, int locationId) {
		this.title = title;
		this.description = description;
		this.county = county;
		this.city = city;
		this.street = street;
		this.userId = userId;
		this.priority = priority;
		this.visibility = visibility;
		status = ListingStatus.OPEN;
		this.createdDate = (System.currentTimeMillis());
		this.locationId = locationId;
	}

	public int getLocationId() {
		return locationId;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the visibility
	 */
	public int getVisibility() {
		return visibility;
	}

	/**
	 * @param visibility the visibility to set
	 */
	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the description ready to be displayed on a page
	 */
	public String getFormattedDescription() {
		int strLen = description.length();
		int cutoff = 300;
		if (strLen > cutoff) {
			String formattedDescription = description.substring(0, cutoff);
			formattedDescription += "... (View Details to read more)";
			return formattedDescription;
		} else {
			return description;
		}
	}

	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the userId;
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the status
	 */
	public ListingStatus getStatus() {
		return status;
	}

	/**
	 *
	 * @return the listingType
	 */
	public ListingType getListingType() {
		return listingType;
	}

	/**
	 * @return the createdDate as a long
	 */
	public long getCreatedDate() {
		return createdDate;
	}

	/**
	 * @return the createdDate as a Date object
	 */
	public Date getCreatedDateAsDate() {
		return new Date(createdDate);
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param county the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the status by the ListingStatus passed
	 *
	 * @param status the status to set
	 */
	public void setStatus(ListingStatus status) {
		this.status = status;
	}

	/**
	 * Sets the status via ordinal value passed. </br>
	 * Value passed must be valid within {@link com.reachout.models.ListingStatus}
	 *
	 * @param statusValue
	 */
	public void setListingType(int listingType) {
		this.listingType = ListingType.getByOrdinal(listingType);
	}

	/**
	 * Sets the status by the ListingStatus passed
	 *
	 * @param status the status to set
	 */
	public void setListingType(ListingType listingType) {
		this.listingType = listingType;
	}

	/**
	 * Sets the status via ordinal value passed. </br>
	 * Value passed must be valid within {@link com.reachout.models.ListingStatus}
	 *
	 * @param statusValue
	 */
	public void setStatus(int statusValue) {
		status = ListingStatus.getByOrdinal(statusValue);
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/*
	 * Returns a pretty printed string containing the request's information in the
	 * following format </br>
	 * "This is a request called {%s}, in status {%s} with the description {%s}. It is for user ID {%s} in county {%s}, city {%s}"
	 */
	@Override
	public String toString() {
		return String.format(
				"This is a {%s} called {%s}, in status {%s} with the description {%s}. It is for user ID {%s} in county {%s}, city {%s}",
				listingType.getName(), title, status, description, userId, county, city);
	}

}
