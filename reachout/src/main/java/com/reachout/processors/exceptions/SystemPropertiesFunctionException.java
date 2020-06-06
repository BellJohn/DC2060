/**
 * 
 */
package com.reachout.processors.exceptions;

import java.sql.SQLException;

/**
 * @author John
 *
 */
public class SystemPropertiesFunctionException extends RuntimeException {

	private static final long serialVersionUID = -6185623814559151567L;

	public SystemPropertiesFunctionException(SQLException e) {
		super(e);
	}

}
