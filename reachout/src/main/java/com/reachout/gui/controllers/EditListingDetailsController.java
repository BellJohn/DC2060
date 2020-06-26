package com.reachout.gui.controllers;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.auth.SystemUser;
import com.reachout.dao.HibernateListingDAOImpl;
import com.reachout.dao.HibernateLocationDAO;
import com.reachout.dao.HibernateRequestDAOImpl;
import com.reachout.dao.HibernateServiceDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.Listing;
import com.reachout.models.ListingStatus;
import com.reachout.models.Location;
import com.reachout.models.Request;
import com.reachout.models.Service;
import com.reachout.processors.LocationFactory;
import com.reachout.processors.exceptions.MappingAPICallException;
import com.reachout.utils.ROUtils;

@Controller
@RequestMapping("/editListing")
public class EditListingDetailsController {

	public final Logger logger = LogManager.getLogger(EditListingDetailsController.class);

	private static final String VIEW_NAME = "editListing";
	private static final String ERROR_STRING = "error";

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {

		// Make sure the user attempting to edit this actually owns the listing
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		}
		logger.debug("Reached editListing Controller");

		String listingIDFromRequest = request.getParameter("listingID");
		if (StringUtils.isEmpty(listingIDFromRequest) || !ROUtils.isNumericInt(listingIDFromRequest)) {
			// Wont be able to find the listing so return to the user's profile page
			return new ProfilePageController().initPage(request);
		}

		// Safe to do this as it will have been checked in the condition above
		int listingID = Integer.parseInt(listingIDFromRequest);

		Listing listingRequested;
		HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl();
		listingRequested = reqDAO.selectListingByIDofUnknownType(listingID);

		// if it's null, something went wrong so return to the user's profile
		if (listingRequested == null) {
			return new ProfilePageController().initPage(request);
		}

		List<ListingStatus> listingStatusList = new ArrayList<>();
		listingStatusList.add(ListingStatus.OPEN);
		listingStatusList.add(ListingStatus.CLOSED);
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("listing", listingRequested);
		mv.addObject("listingStatusList", listingStatusList);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("user", username);
		return mv;

	}

	/**
	 * Update user profile. Parameters are profilePic, bio, healthStatus
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping()
	public ModelAndView postRequest(HttpServletRequest request) {
		String intent = request.getParameter("submit");
		if (StringUtils.isEmpty(intent)) {
			return new ProfilePageController().initPage(request);
		}

		if ("update".equals(intent)) {
			return handleUpdate(request);
		} else if ("delete".equals(intent)) {
			return handleDelete(request);
		} else {
			return new ProfilePageController().initPage(request);
		}
	}

	private ModelAndView handleUpdate(HttpServletRequest request) {
		logger.debug("Attempting listing update");
		String listingIDFromRequest = request.getParameter("listingID");
		String newTitle = request.getParameter("Title");
		String newDesc = request.getParameter("Desc");
		String newAddress = request.getParameter("Street");
		String newCounty = request.getParameter("County");
		String newCity = request.getParameter("City");
		String newStatus = request.getParameter("listingStatus");

		if (StringUtils.isEmpty(newTitle) || StringUtils.isEmpty(newDesc) || StringUtils.isEmpty(newCounty)
				|| StringUtils.isEmpty(newCity) || StringUtils.isEmpty(newStatus)) {
			logger.error("A field was empty in the update request");
			ModelAndView returnView = initPage(request);
			returnView.addObject(ERROR_STRING, "Something went wrong, try again and esure all fields are populated");
			return returnView;
		}

		if (!ROUtils.isNumericInt(listingIDFromRequest)) {
			logger.error("Listing ID was not a numeric in in the update request");
			return new ProfilePageController().initPage(request);
		}

		int listingID = Integer.parseInt(listingIDFromRequest);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();

		} else {
			username = (String) auth.getPrincipal();
		}
		int userId = -1;
		Listing listingToUpdate;
		HibernateListingDAOImpl listingDAO = new HibernateRequestDAOImpl();
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		userId = userDAO.getUserIdByUsername(username);
		listingToUpdate = listingDAO.selectListingByIDofUnknownType(listingID);

		if (listingToUpdate.getUserId() != userId) {
			// This user should not be able to update this listing. Something has gone
			// wrong, return them to the listing with an error message
			logger.info("A user attempted to update a listing which was not theirs");
			ModelAndView errorReturn = new ViewSingleListingController().initPage(request);
			errorReturn.addObject(ERROR_STRING, "You are not authorised to edit this listing");
			return errorReturn;
		}

		HibernateLocationDAO locDAO = new HibernateLocationDAO();
		Location location = locDAO.selectLocationById(listingToUpdate.getLocationId());
		LocationFactory locFac = new LocationFactory();
		boolean locationUpdated = false;
		try {
			Location tempLoc = locFac.buildLocation(newAddress, newCity, newCounty);
			location.setLocLat(tempLoc.getLocLat());
			location.setLocLong(tempLoc.getLocLong());
			locationUpdated = locDAO.saveOrUpdate(location);
		} catch (MappingAPICallException e) {
			return returnErrorResult("Unable to determine the location of the address provided");
		}
		if (!locationUpdated) {
			return returnErrorResult("Something went wrong creating your Request, please try again");
		}

		// If we have made it this far, lets update the record.
		listingToUpdate.setTitle(newTitle);
		listingToUpdate.setDescription(newDesc);
		listingToUpdate.setCounty(newCounty);
		listingToUpdate.setCity(newCity);
		listingToUpdate.setStreet(newAddress);
		listingToUpdate.setStatus(ListingStatus.valueOf(newStatus));

		boolean changeSuccess = false;
		if (listingToUpdate instanceof Request) {
			HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl();
			changeSuccess = reqDAO.update((Request) listingToUpdate);
		} else {
			HibernateServiceDAOImpl serDAO = new HibernateServiceDAOImpl();
			changeSuccess = serDAO.update((Service) listingToUpdate);
		}

		logger.info(String.format("Update success [%s]", changeSuccess));
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("postSent", "TRUE");
		mv.addObject("changeSuccess", changeSuccess);
		if (!changeSuccess) {
			mv.addObject(ERROR_STRING, "Something went wrong with the update. Try again");
		}
		mv.addObject("changeType", "updated");
		return mv;
	}

	private ModelAndView returnErrorResult(String message) {
		logger.info(String.format("Update failed [%s]", message));
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("postSent", "FALSE");
		mv.addObject("changeSuccess", false);
		mv.addObject(ERROR_STRING, message);
		return mv;
	}

	private ModelAndView handleDelete(HttpServletRequest request) {
		logger.debug("Attempting listing delete");
		String listingIDFromRequest = request.getParameter("listingID");

		if (!ROUtils.isNumericInt(listingIDFromRequest)) {
			logger.error("Listing ID was not a numeric in in the update request");
			return new ProfilePageController().initPage(request);
		}

		int listingID = Integer.parseInt(listingIDFromRequest);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();

		} else {
			username = (String) auth.getPrincipal();
		}
		int userId = -1;
		Listing listingToDelete;
		HibernateListingDAOImpl listingDAO = new HibernateRequestDAOImpl();
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		userId = userDAO.getUserIdByUsername(username);
		listingToDelete = listingDAO.selectListingByIDofUnknownType(listingID);

		if (listingToDelete == null) {
			logger.error("No listing found to delete");
			ModelAndView returnView = initPage(request);
			returnView.addObject(ERROR_STRING, "Something went wrong, try again.");
			return returnView;
		}

		if (listingToDelete.getUserId() != userId) {
			// This user should not be able to update this listing. Something has gone
			// wrong, return them to the listing with an error message
			logger.info("A user attempted to update a listing which was not theirs");
			ModelAndView errorReturn = new ViewSingleListingController().initPage(request);
			errorReturn.addObject(ERROR_STRING, "You are not authorised to edit this listing");
			return errorReturn;
		}

		boolean changeSuccess = false;
		if (listingToDelete instanceof Request) {
			HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl();
			changeSuccess = reqDAO.delete((Request) listingToDelete);
		} else {
			HibernateServiceDAOImpl serDAO = new HibernateServiceDAOImpl();
			changeSuccess = serDAO.delete((Service) listingToDelete);
		}

		logger.info(String.format("Delete success [%s]", changeSuccess));
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("postSent", "TRUE");
		mv.addObject("changeSuccess", changeSuccess);
		if (!changeSuccess) {
			mv.addObject(ERROR_STRING, "Something went wrong with the deletion. Try again");
		}
		mv.addObject("changeType", "deleted");
		return mv;
	}
}
