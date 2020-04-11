/**
 * 
 */
package com.reachout.dao.exceptions;

/**
 * To be thrown when an Entity was required from the database which future
 * database access relied on.
 * 
 * @author John
 *
 */
public class EntityNotFoundException extends Exception {

	private static final long serialVersionUID = -7596765700372042435L;

	/**
	 * @param message
	 */
	public EntityNotFoundException(String message) {
		super(message);
	}

}
