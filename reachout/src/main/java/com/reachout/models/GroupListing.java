/**
 *
 */
package com.reachout.models;

/**
 * @author Jess
 *
 */
public class GroupListing {
	
	private int id;
	private int groupId;
	private int listingId;

	/**
	 * Fully formed constructor for GroupMember 
	 * 
	 * @param id
	 * @param groupId
	 * @param listingId
	
	 */
	public GroupListing(int groupId, int listingId){
		this.groupId = groupId;
		this.listingId = listingId;		
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the groupId
	 */
	public int getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the id
	 */
	public int getListingId() {
		return listingId;
	}

	/**
	 * @param id the id to set
	 */
	public void setListingId(int listingId) {
		this.listingId =listingId;
	}


	/**
	 * Empty constructor for Hibernate. Do not use
	 */
	public GroupListing() {
	}

	
}
