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
	protected int userId;
	protected int id;
	protected ListingStatus status;
	protected long createdDate;
	protected ListingType listingType;

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
	 */
	public Listing(String title, String description, String county, String city, int userId, ListingStatus status) {
		this(title, description, county, city, userId);
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
	public Listing(String title, String description, String county, String city, int userId) {
		this.title = title;
		this.description = description;
		this.county = county;
		this.city = city;
		this.userId = userId;
		status = ListingStatus.OPEN;
		this.createdDate = (System.currentTimeMillis());
	}


	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
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
		status = ListingType.getByOrdinal(listingType);
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
