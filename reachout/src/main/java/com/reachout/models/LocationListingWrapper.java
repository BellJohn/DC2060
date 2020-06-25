/**
 * 
 */
package com.reachout.models;

/**
 * @author John
 *
 */
public class LocationListingWrapper {

	private Location location;
	private Listing listing;
	
	public LocationListingWrapper(Location location, Listing listing) {
		this.setLocation(location);
		this.setListing(listing);
	}

	/**
	 * @return the listing
	 */
	public Listing getListing() {
		return listing;
	}

	/**
	 * @param listing the listing to set
	 */
	public void setListing(Listing listing) {
		this.listing = listing;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	
	@Override
	public String toString() {
		return String.format("LocationListingWrapper: [Location: [%s] , Listing [%s] ]", location.toString(), listing.toString());
	}
}
