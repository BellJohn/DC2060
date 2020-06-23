/**
 *
 */
package com.reachout.models;

/**
 * @author Jess
 *
 */
public class GroupMember {
	
	private int id;
	private int groupId;
	private int userId;
	private int userStatus;

	/**
	 * Fully formed constructor for GroupMember 
	 * 
	 * @param id
	 * @param groupId
	 * @param userId
	 * @param userStatus
	
	 */
	public GroupMember(int groupId, int userId, int userStatus){
		this.groupId = groupId;
		this.userId = userId;
		this.userStatus = userStatus;
		
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
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}




	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}




	/**
	 * @return the userStatus
	 */
	public int getUserStatus() {
		return userStatus;
	}




	/**
	 * @param userStatus the userStatus to set
	 */
	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}




	/**
	 * Empty constructor for Hibernate. Do not use
	 */
	public GroupMember() {
	}

}
