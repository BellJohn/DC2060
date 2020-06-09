/**
 *
 */
package com.reachout.models;

/**
 * @author Jess
 *
 */
public class Group {

	private static final long serialVersionUID = -7360265992017228189L;
	private int id;
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


	private String description;
	private String name;
	private String picture;
	private int locationId;

	/**
	 * Fully formed constructor for Group.
	 * 
	 * @param id
	 * @param name
	 * @param description
	 * @param locationId
	 * @param picture
	
	 */
	public Group(String name, String description, String picture, int locationId){
		this.name =name;
		this.description = description;
		this.picture = picture;
		this.locationId = locationId;
	}

	
	/**
	 * @return the locationId
	 */
	public int getLocationId() {
		return locationId;
	}


	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the picture
	 */
	public String getPicture() {
		return picture;
	}


	/**
	 * @param picture the picture to set
	 */
	public void setPicture(String picture) {
		this.picture = picture;
	}


	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	/**
	 * Empty constructor for Hibernate. Do not use
	 */
	public Group() {
	}

}
