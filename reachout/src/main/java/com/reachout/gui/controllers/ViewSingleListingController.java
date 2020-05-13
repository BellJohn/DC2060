package com.reachout.gui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.auth.SystemUser;
import com.reachout.dao.HibernateRequestDAOImpl;
import com.reachout.dao.HibernateServiceDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.ListingStatus;
import com.reachout.models.Listing;
import com.reachout.models.ListingType;
import com.reachout.models.User;
import com.reachout.models.exceptions.ListingTypeNotMatchedException;
import com.reachout.processors.ListingHandler;

@Controller
@RequestMapping("/viewListing")
public class ViewSingleListingController {
	public final Logger logger = LogManager.getLogger(ViewSingleListingController.class);

	private static final String VIEW_NAME = "viewListing";

	/**
	 * Method for populating the page with details of the request the user selected.
	 * If no details are present, this method will return to the home page. If an
	 * error occurs, the method will return to the error page If the user owns the
	 * request being viewed, the "offer to help" button will not be visible
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping
	public ModelAndView initPage(HttpServletRequest request) {
		logger.debug("Reached viewSingleListing Controller");

		// Check to see if we should actually be running the offerToAccept method as it
		// takes the same params
		if ("accept".equals(request.getParameter("action"))) {
			return offerToAccept(request);
		}
		Integer listingID = null;
		try {
			listingID = Integer.parseInt(request.getParameter("listingID"));
		} catch (NumberFormatException e) {
			logger.error("Request contained non-numeric listingID, returning user to error page");
			return getErrorPage();
		}
		String listingType = request.getParameter("listingType");

		Listing result = getListingByIdAndType(listingID, listingType);

		if (result == null) {
			logger.error("Found neither Request nor Service with provided ID");
			return getErrorPage();
		}

		logger.debug(
				String.format("Found listing of type {%s} titled {%s}", result.getListingType(), result.getTitle()));

		// If this user is the user who owns the listing, we should disable the "Offer
		// to help" button
		boolean isOwner = userBrowsingOwnsPost(result.getUserId());
		boolean enableOfferButton = true;
		// If it is not currently open or the user browsing owns this post, hide the
		// button
		if (result.getListingType().equals(ListingType.SERVICE) || result.getStatus() != ListingStatus.OPEN
				|| isOwner) {
			enableOfferButton = false;
		}

		ModelAndView mv;
		mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("ListingObj", result);
		mv.addObject("enableButton", enableOfferButton);
		mv.addObject("isOwner", isOwner);

		return mv;
	}

	private ModelAndView getErrorPage() {
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("Something went wrong, please try again");
		return mv;
	}

	/**
	 * Handles the event triggered when a user clicks the "offer to accept" button
	 * on a listing (assuming requests) </br>
	 * Errors in execution result in either the error page being returned or the
	 * user being returned to the original page but a message presented depending on
	 * circumstance
	 * 
	 * @param request
	 * @return
	 */

	public ModelAndView offerToAccept(HttpServletRequest request) {
		Integer listingID;
		try {
			listingID = Integer.parseInt(request.getParameter("listingID"));
		} catch (NumberFormatException e) {
			logger.error("Request contained non-numeric listingID, returning user to error page");
			return getErrorPage();
		}
		String listingType = request.getParameter("listingType");

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		}

		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		User userBrowsing = userDAO.selectUser(username);
		if (userBrowsing == null) {
			logger.error(String.format("Unable to find the user offering to help with request ID {%s}", listingID));
			return getErrorPage();
		}
		boolean offerAccepted = false;
		try {
			offerAccepted = ListingHandler.getInstance().acceptRequest(userBrowsing, listingID, listingType);
		} catch (ListingTypeNotMatchedException e) {
			logger.error(e.getMessage(), e);
		}

		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("ListingObj", getListingByIdAndType(listingID, listingType));
		mv.addObject("offerAccepted", offerAccepted);
		boolean enableButton = false;
		if (!offerAccepted) {
			enableButton = true;
		}
		mv.addObject("enableButton", enableButton);
		return mv;
	}

	/**
	 * Confirms whether a user is browing their own post
	 * 
	 * @param listingOwnerId
	 * @return true/false
	 */
	private boolean userBrowsingOwnsPost(int listingOwnerId) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		}
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		User userBrowsing = userDAO.selectUser(username);
		if (userBrowsing != null) {
			boolean userOwnsPost = userBrowsing.getId() == listingOwnerId;
			logger.debug(String.format("Listing viewed owned by user browsing:{%s}", userOwnsPost));
			return userOwnsPost;
		}
		// If we get here, something likely went wrong so err on the side of safety and
		// don't allow the current user to accept this post
		return true;
	}

	/**
	 * Finds the listing requested based on the listingID and listingType
	 * 
	 * @param listingId
	 * @param listingType
	 * @return Listing the listing found
	 */
	private Listing getListingByIdAndType(int listingId, String listingType) {
		Listing result;
		if (ListingType.REQUEST.getName().equalsIgnoreCase(listingType)) {
			HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl();
			result = reqDAO.selectById(listingId);

		} else if (ListingType.SERVICE.getName().equalsIgnoreCase(listingType)) {
			HibernateServiceDAOImpl serDAO = new HibernateServiceDAOImpl();
			result = serDAO.selectById(listingId);

		} else {
			logger.error("Received neither Request nor Service specifier, returning error page");
			return null;
		}
		return result;
	}
}
