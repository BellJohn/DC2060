package com.reachout.gui.controllers;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reachout.auth.SystemUser;
import com.reachout.dao.HibernateGroupListingDAOImpl;
import com.reachout.dao.HibernateGroupMemberDAOImpl;
import com.reachout.dao.HibernateRequestDAOImpl;
import com.reachout.dao.HibernateServiceDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.ListingGUIWrapper;
import com.reachout.models.ListingType;
import com.reachout.processors.ListingFetchService;
import com.reachout.processors.exceptions.ListingFetchServiceConstructorException;

@Controller
@RequestMapping("/ListingFetchController")
public class ListingFetchController {

	private static final Logger logger = LogManager.getLogger(ListingFetchController.class);

	/**
	 * Primary entry point for this class. Works to collate all listings of a given
	 * type within a given radius of a central location and returns as a JSON string
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping
	public void dataFetch(@RequestParam(name = "type", required = true) String type,
			@RequestParam(name = "lat", required = true) String lat,
			@RequestParam(name = "lng", required = true) String lng,
			@RequestParam(name = "radius", required = true) String radius, HttpServletRequest request,
			HttpServletResponse response) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		}
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		int userId = userDAO.getUserIdByUsername(username);

		logger.debug("Reached Listing FetchService with fields: " + type + "," + lat + "," + lng + "," + radius);
		Set<ListingGUIWrapper> results = null;
		try {
			Double latDouble = Double.parseDouble(lat);
			Double longDouble = Double.parseDouble(lng);
			Double radiusDouble = Double.parseDouble(radius);
			ListingFetchService fetch = new ListingFetchService(type, latDouble, longDouble, radiusDouble);
			results = fetch.fetchLocationsWithinRadius();
			removeOwnData(results, userId);
			results = limitResultsByGroupVisibility(type, results, userId);

		} catch (NumberFormatException e) {
			logger.error(String.format("User sent invalid data for search - Lat:%s Long:%s Type:%s Radius:%s", lat, lng,
					type, radius));
		} catch (ListingFetchServiceConstructorException e) {
			logger.error("An error occured fetching requested data", e);
		}

		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(results);
		} catch (JsonProcessingException e) {
			logger.error("Unable to convert to JSON", e);
		}
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			logger.error("Unable to return user requested data", e);
		}
	}

	/**
	 * Cuts the current collection of listings down to only those the user has the rights to see by visibility
	 * @param type
	 * @param currentData
	 * @param userId
	 * @return
	 */
	private Set<ListingGUIWrapper> limitResultsByGroupVisibility(String type, Set<ListingGUIWrapper> currentData, int userId) {

		HibernateGroupMemberDAOImpl gmDAO = new HibernateGroupMemberDAOImpl();
		List<Integer> usersGroups = gmDAO.getUserGroupIDs(userId);
		Set<Integer> allListingIDs = new HashSet<>();
		HibernateGroupListingDAOImpl glDAO = new HibernateGroupListingDAOImpl();
		for (Integer groupID : usersGroups) {
			allListingIDs.addAll(glDAO.getGroupListingsIds(groupID));
		}
		if(ListingType.getByValue(type).equals(ListingType.REQUEST)){
			allListingIDs.addAll(new HibernateRequestDAOImpl().getAllRequestIDsForDisplay(userId));
		}
		else {
			allListingIDs.addAll(new HibernateServiceDAOImpl().getAllServiceIDsForDisplay(userId));
		}

		// Loop through all the possible listings a user is authorised to view based on visibility.
		// If there is a match within the collection that we found based on location
		// Add it to the result set
		Set<ListingGUIWrapper> result = new HashSet<>();
		for(ListingGUIWrapper wrapper : currentData) {
			if(allListingIDs.contains(wrapper.listing.getId())){
				result.add(wrapper);
			}
		}
		return result;
	}

	/**
	 * We shouldn't send personal datga like email/dob/firstname/lastname to the GUI. Strip that data off.
	 * @param wrapper
	 */
	private void sanitizePersonalData(ListingGUIWrapper wrapper) {
		wrapper.user.setEmail("");
		wrapper.user.setDob("");
		wrapper.user.setFirstName("");
		wrapper.user.setLastName("");
	}

	/**
	 * Removes own listings from the collection as we don't show own results on the search pages
	 * @param results
	 * @param userId
	 * @return
	 */
	private Set<ListingGUIWrapper> removeOwnData(Set<ListingGUIWrapper> results, int userId) {
		Set<ListingGUIWrapper> clean = new HashSet<>();
		clean.addAll(results);

		for (ListingGUIWrapper wrapper : results) {
			sanitizePersonalData(wrapper);
			if (wrapper.getUserID() == userId) {
				clean.remove(wrapper);
			}
		}

		return clean;
	}

}
