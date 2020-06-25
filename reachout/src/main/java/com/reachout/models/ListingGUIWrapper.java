/**
 * 
 */
package com.reachout.models;

import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Wrapper object for the listing and user data for presentation on the GUI
 * 
 * @author John
 *
 */
public class ListingGUIWrapper {

	Logger logger = LogManager.getLogger(ListingGUIWrapper.class);
	public final Listing listing;
	public final User user;
	public final Location location;

	public ListingGUIWrapper(Listing listing, User user, Location location) {
		this.listing = listing;
		this.user = user;
		this.location = location;
	}

	public double getLocationLat() {
		return location.getLocLat();
	}

	public double getLocationLong() {
		return location.getLocLong();
	}

	public String getUsername() {
		return user.getUsername();
	}

	public String getTitle() {
		return listing.getTitle();
	}

	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCounty() {
		return listing.county;
	}

	public String getCity() {
		return listing.city;
	}

	public int getListingID() {
		return listing.id;
	}

	public String getStatus() {
		return listing.status.getName();
	}

	public String getCreatedDate() {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(listing.getCreatedDateAsDate());
	}

	public String getCreatedTime() {
		String pattern = "HH:mm";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(listing.getCreatedDateAsDate());
	}

	public String getListingType() {
		return listing.listingType.getName();
	}

	public String getFormattedDescription() {
		return listing.getFormattedDescription();
	}

	public int getUserID() {
		return user.getId();
	}

	public String getPriority() {
		return listing.getPriority();
	}

}
