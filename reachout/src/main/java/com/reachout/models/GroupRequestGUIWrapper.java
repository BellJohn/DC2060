/**
 * 
 */
package com.reachout.models;


/**
 * Wrapper object for the listing and user data for presentation on the GUI
 * 
 * @author John
 *
 */
public class GroupRequestGUIWrapper {

	public final Group group;
	public final User user;
	public final int groupMemberId;

	public GroupRequestGUIWrapper(Group group, User user, int groupMemberId) {
		this.group = group;
		this.user = user;
		this.groupMemberId = groupMemberId;
	}

	public String getUsername() {
		return user.getUsername();
	}
	
	public int getID() {
		return groupMemberId;
	}
	
	public String getFirstName() {
		return user.getFirstName();
	}
	
	public String getLastName() {
		return user.getLastName();
	}

	public String getName() {
		return group.getName();
	}

	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public int getGroupID() {
		return group.getId();
	}

	public int getUserID() {
		return user.getId();
	}

}
