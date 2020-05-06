/**
 * 
 */
package com.reachout.im;

import java.util.List;

import com.reachout.models.InternalMessage;

/**
 * @author John
 *
 */
public final class Conversation {

	private final int userBrowsing;
	private final int userOther;
	private final List<InternalMessage> allIMs;
	private final String otherUserName;

	public Conversation(int userBrowsing, int userOther, List<InternalMessage> allIMs, String otherUserName) {
		this.userBrowsing = userBrowsing;
		this.userOther = userOther;
		this.allIMs = allIMs;
		this.otherUserName = otherUserName;
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
}
