package com.reachout.gui.controllers;

import java.io.IOException;
import java.util.HashSet;
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
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.ListingGUIWrapper;
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

		logger.debug("Reached Listing FetchService with fields: " + type + "," + lat + "," + lng + "," + radius);
		Set<ListingGUIWrapper> results = null;
		try {
			Double latDouble = Double.parseDouble(lat);
			Double longDouble = Double.parseDouble(lng);
			Double radiusDouble = Double.parseDouble(radius);
			ListingFetchService fetch = new ListingFetchService(type, latDouble, longDouble, radiusDouble);
			results = fetch.fetchLocationsWithinRadius();
			removeOwnData(results);

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

	private void sanitizePersonalData(ListingGUIWrapper wrapper) {
		wrapper.user.setEmail("");
		wrapper.user.setDob("");
	}

	private Set<ListingGUIWrapper> removeOwnData(Set<ListingGUIWrapper> results) {
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		Set<ListingGUIWrapper> clean = new HashSet<>();
		clean.addAll(results);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		}
		int userId = userDAO.getUserIdByUsername(username);
		for (ListingGUIWrapper wrapper : results) {
			sanitizePersonalData(wrapper);
			if (wrapper.getUserID() == userId) {
				clean.remove(wrapper);
			}
		}

		return clean;
	}

}
