/**
 * 
 */
package com.reachout.models;

import java.util.Date;

/**
 * @author John
 *
 */
public final class InternalMessage {

	private int id;
	private int origin;
	private int target;
	private String message;
	private long createdDate;
	private boolean viewed;

	/**
	 * Empty Constructor for Hibernate to use
	 */
	public InternalMessage() {
	}

	/**
	 * Proper constructor for use by developers for creating a brand new message
	 * which has yet to be stored
	 * 
	 * @param origin
	 * @param target
	 * @param message
	 */
	public InternalMessage(int origin, int target, String message) {
		this.setOrigin(origin);
		this.setTarget(target);
		this.setMessage(message);
		this.setCreatedDate(System.currentTimeMillis());
		this.setViewed(false);
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
	 * @return the origin
	 */
	public int getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(int origin) {
		this.origin = origin;
	}

	/**
	 * @return the target
	 */
	public int getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(int target) {
		this.target = target;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the createdDate in millis
	 */
	public long getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set in millis
	 */
	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the viewed
	 */
	public boolean isViewed() {
		return viewed;
	}

	/**
	 * @param viewed the viewed to set
	 */
	public void setViewed(boolean viewed) {
		this.viewed = viewed;
	}
	
	public String getPrettyPrintDate() {
		return new Date(createdDate).toString();
	}
	
	/**
	 * Returns String representation of object in format:
	 * </br>
	 * InternalMessage from: [%s] to: [%s] at: [%d]. Viewed: [%s]. Message:[%s]
	 */
	public String toPrettyString() {
		return String.format("InternalMessage from: [%s] to: [%s] at: [%d]. Viewed: [%s]. Message:[%s]", origin, target, createdDate, viewed, message );
	}
}
