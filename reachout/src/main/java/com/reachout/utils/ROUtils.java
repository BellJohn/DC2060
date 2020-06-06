/**
 * 
 */
package com.reachout.utils;

/**
 * @author John
 *
 */
public class ROUtils {

	/**
	 * Private constructor as all methods are statics
	 */
	private ROUtils() {
	}

	/**
	 * Test to see if the string passed is actually an integer representation
	 * @param val
	 * @return
	 */
	public static boolean isNumericInt(String val) {
		try{
			Integer.parseInt(val);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
}
