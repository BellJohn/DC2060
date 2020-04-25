/**
 * 
 */
package com.reachout.models.exceptions;

/**
 * @author John
 *
 */
public class ListingTypeNotMatchedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4127430322349550990L;

	/**
	 * 
	 */
	public ListingTypeNotMatchedException() {
		super();
	}

	/**
	 * @param arg0
	 */
	public ListingTypeNotMatchedException(String arg0) {
		super(genErrorMessage(arg0));
	}

	/**
	 * @param arg0
	 */
	public ListingTypeNotMatchedException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ListingTypeNotMatchedException(String arg0, Throwable arg1) {
		super(genErrorMessage(arg0), arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public ListingTypeNotMatchedException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(genErrorMessage(arg0), arg1, arg2, arg3);
	}

	private static String genErrorMessage(String invalidData) {
		return String.format("Unable to parse string {%s} into a ListingType", invalidData);
	}
}
