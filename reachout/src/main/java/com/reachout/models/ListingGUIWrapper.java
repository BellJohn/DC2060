/**
 * 
 */
package com.reachout.models;

import java.text.SimpleDateFormat;

/**
 * Wrapper object for the listing and user data for presentation on the GUI
 * 
 * @author John
 *
 */
public class ListingGUIWrapper {

	public final Listing listing;
	public final User user;

	public ListingGUIWrapper(Listing listing, User user) {
		this.listing = listing;
		this.user = user;
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
}
