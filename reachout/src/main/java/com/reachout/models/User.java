package com.reachout.models;

import java.time.LocalDate;

/**
 * Represents a User object as stored in the database. </br>
 * The database table is USERS. </br>
 * The Hibernate Mapping is src/main/resources/com/reachout/models/User.hbm.xml
 * </br>
 * Any changes to fields here must be reflected in that file and the table
 * structure </br>
 * <em>Note, any changes with setters done here are "IN MEMORY ONLY" and not
 * reflected in the database. The object must be saved or updated through a DAO
 * to store that change</em>
 * 
 * @author John
 *
 */
public class User {

	private int id;
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String dob;


	/**
	 * 
	 */
	public User() {
	}

	/**
	 * Represents a User object that can be stored in the database through hibernate
	 * mappings
	 * 
	 * @param username
	 * @param email
	 * @param dob
	 * @param firstName
	 * @param lastName
	 */
	public User(String firstName, String lastName, String username, String email, String dob) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.dob = dob;
	}

	/**
	 * Gets the date of birth as it exists in current memory
	 * 
	 * @return dob
	 */
	public String getDob() {
		return dob;
	}

	/**
	 * Gets the firstName as it exists in current memory
	 * 
	 * @return firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Gets the lastName as it exists in current memory
	 * 
	 * @return lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Gets the record ID as it exists in current memory
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the record ID on this User object. Object must be saved in order to
	 * store this in the database.
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the record username as it exists in current memory
	 * 
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the record username on this User object. Object must be saved in order
	 * to store this in the database.
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the record email address as it exists in current memory
	 * 
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the record email address on this User object. Object must be saved in
	 * order to store this in the database.
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Sets the record dob on this User object. Object must be saved in
	 * order to store this in the database.
	 * 
	 * @param dob
	 */
	public void setDob(String dob) {
		this.dob = dob;
	}

	/**
	 * Sets the record firstName on this User object. Object must be saved in
	 * order to store this in the database.
	 * 
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Sets the record lastName on this User object. Object must be saved in
	 * order to store this in the database.
	 * 
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
