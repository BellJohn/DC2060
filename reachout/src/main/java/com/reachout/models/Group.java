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
	private String description;
	private String name;
	private String picture;
	private int locationId;
	private String city;
	private String county;
	
	/**
	 * Fully formed constructor for Group.
	 * 
	 * @param id
	 * @param name
	 * @param description
	 * @param locationId
	 * @param picture
	 * @param visibility
	
	 */
	public Group(String name, String description, String picture, int locationId, String city, String county){
		this.name =name;
		this.description = description;
		this.picture = picture;
		this.locationId = locationId;
		this.setCity(city);
		this.setCounty(county);
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
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}

	/**
	 * @param county the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
	}

	/**
	 * Empty constructor for Hibernate. Do not use
	 */
	public Group() {
	}

}
