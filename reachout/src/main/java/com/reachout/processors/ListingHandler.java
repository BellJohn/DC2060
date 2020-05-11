/**
 * 
 */
package com.reachout.processors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.reachout.dao.HibernateListingDAOImpl;
import com.reachout.dao.HibernateRequestDAOImpl;
import com.reachout.dao.HibernateServiceDAOImpl;
import com.reachout.models.ListingStatus;
import com.reachout.models.Listing;
import com.reachout.models.ListingType;
import com.reachout.models.User;
import com.reachout.models.exceptions.ListingTypeNotMatchedException;

/**
 * @author John
 *
 */
public class ListingHandler {

	private static ListingHandler listingHandlerInstance;
	public final Logger logger = LogManager.getLogger(ListingHandler.class);

	private ListingHandler() {
	}

	public static ListingHandler getInstance() {
		if (listingHandlerInstance == null) {
			listingHandlerInstance = new ListingHandler();
		}
		return listingHandlerInstance;
	}

	/**
	 * Method for handling the business logic of a user accepting a particular
	 * listing
	 * 
	 * @param id
	 * @param listingID
	 * @return
	 * @throws ListingTypeNotMatchedException
	 */
	public boolean acceptRequest(User userBrowsing, Integer listingID, String listingType)
			throws ListingTypeNotMatchedException {
		Listing listingFound = null;
		try {
			if (ListingType.valueOf(listingType.toUpperCase()) == ListingType.REQUEST) {
				try (HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl()) {
					listingFound = reqDAO.selectById(listingID);
				}
			} else if (ListingType.valueOf(listingType.toUpperCase()) == ListingType.SERVICE) {
				try (HibernateServiceDAOImpl serDAO = new HibernateServiceDAOImpl()) {
					listingFound = serDAO.selectById(listingID);
				}
			}
		} catch (IllegalArgumentException e) {
			throw new ListingTypeNotMatchedException(listingType, e);
		}

		if (listingFound == null) {
			logger.error(String.format("Unable to find a listing of type {%s} with id {%s}", listingType, listingID));
			return false;
		}
		if (userBrowsing.getId() == listingFound.getUserId()) {
			logger.error(String.format(
					"Cannot accept listing with id {%s} as user requesting to accept owns the listing", listingID));
			return false;
		}

		// If we reached here, this user is valid to accept this request and we are
		// confident we have appropriate objects for both
		boolean success = false;
		listingFound.setStatus(ListingStatus.PENDING);
		try (HibernateListingDAOImpl listingDAO = new HibernateRequestDAOImpl()) {
			success = listingDAO.assignListingToUser(listingFound, userBrowsing);
		}

		return success;

	}

}
