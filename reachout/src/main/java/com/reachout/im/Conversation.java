/**
 * 
 */
package com.reachout.im;

import java.util.List;

import com.reachout.models.InternalMessage;

/**
 * Conversation object wraps all InternalMessages between the user browsing and another user.
 * <br>
 * Messages stored in allIMs are in chronological order - Oldest to Newest - 0 to N
 * @author John
 *
 */
public final class Conversation {

	private final int userBrowsing;
	private final int userOther;
	private final List<InternalMessage> allIMs;
	private final String otherUserName;
	private final String browsingUserName;
	private final String otherProfilePic;
	private final String browsingProfilePic;
	
	public Conversation(int userBrowsing, int userOther, List<InternalMessage> allIMs, String otherUserName, String browsingUserName, String otherProfilePic, String browsingProfilePic) {
		this.userBrowsing = userBrowsing;
		this.userOther = userOther;
		this.allIMs = allIMs;
		this.otherUserName = otherUserName;
		this.browsingUserName = browsingUserName;
		this.otherProfilePic = otherProfilePic;
		this.browsingProfilePic = browsingProfilePic;
	}

	public String getBrowsingProfilePic() {
		return this.browsingProfilePic;
	}
	
	public String getOtherProfilePic() {
		return this.otherProfilePic;
	}
	/**
	 * @return the userOther
	 */
	public int getUserOther() {
		return userOther;
	}

	/**
	 * @return the allIMs
	 */
	public List<InternalMessage> getAllIMsAsList() {
		return allIMs;
	}

	/**
	 * @return the userBrowsing
	 */
	public int getUserBrowsing() {
		return userBrowsing;
	}

	/**
	 * @return the otherUserName
	 */
	public String getOtherUserName() {
		return otherUserName;
	}
	
	/**
	 * @return the browsingUserName
	 */
	public String getBrowsingUserName() {
		return browsingUserName;
	}

	@Override
	public String toString() {
		return String.format("Conversation [userBrowsing=%s, userOther=%s, allIMs=%s, otherUserName=%s]", userBrowsing, userOther, allIMs, otherUserName);
	}
	
	
}
