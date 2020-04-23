package com.reachout.models;

/**
 * Represents a user's healthstatus that can be stored in the database. </br>
 * The database table is HEALTHSTATUS. </br>
 * The Hibernate Mapping is
 * src/main/resources/com/reachout/models/HealthStatus.hbm.xml </br>
 * Any changes to fields here must be reflected in that file and the table
 * structure </br>
 * <em>Note, any changes with setters done here are "IN MEMORY ONLY" and not
 * reflected in the database. The object must be saved or updated through a DAO
 * to store that change</em>
 * 
 * @author Jess
 *
 */
public class HealthStatus {
	
	private String status;
	private int id;
	
	public HealthStatus() {
		
	}

	public HealthStatus(String status) {
		this.status = status;
	}
	
	/**
	 * Returns the current health status string as held in memory
	 * 
	 * @return status
	 */
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

}
