package com.reachout.gui.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reachout.auth.SystemUser;
import com.reachout.dao.HibernateGroupListingDAOImpl;
import com.reachout.dao.HibernateGroupMemberDAOImpl;
import com.reachout.dao.HibernateLocationDAO;
import com.reachout.dao.HibernateServiceDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.Listing;
import com.reachout.models.ListingGUIWrapper;
import com.reachout.models.ListingType;
import com.reachout.models.Service;
import com.reachout.processors.SystemPropertiesService;

@Controller
@RequestMapping("/viewServices")
public class ViewServicesController {
	public final Logger logger = LogManager.getLogger(ViewServicesController.class);

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();

		logger.debug("Reached viewServices Controller");
		ModelAndView mv = new ModelAndView("viewServices");
		mv.addObject("currentPage", "viewServices");

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		}
		int userId = userDAO.getUserIdByUsername(username);

		HibernateServiceDAOImpl serDAO = new HibernateServiceDAOImpl();
		HibernateLocationDAO locationDAO = new HibernateLocationDAO();
		Set<Service> allServices = new HashSet<>();

		// Get all the public services
		allServices.addAll(serDAO.getAllServicesForDisplay(userId));

		HibernateGroupMemberDAOImpl groupMemberDAO = new HibernateGroupMemberDAOImpl();
		HibernateGroupListingDAOImpl groupListingDAO = new HibernateGroupListingDAOImpl();

		// Get all the listings that the user is part of the group they are visible to
		Set<Listing> allListings = new HashSet<>();
		for (Integer i : groupMemberDAO.getUserGroupIDs(userId)) {
			allListings.addAll(groupListingDAO.getGroupListings(i));
		}
		// Combine the collections
		for (Listing listing : allListings) {
			if (ListingType.SERVICE.equals(listing.getListingType()) && listing.getUserId() != userId) {
				allServices.add((Service) listing);
			}
		}

		List<ListingGUIWrapper> guiData = new ArrayList<>();

		// Build up data for presenting on the GUI
		for (Service ser : allServices) {
			guiData.add(new ListingGUIWrapper(ser, userDAO.selectByID(ser.getUserId()),
					locationDAO.selectLocationById(ser.getLocationId())));
		}

		// Sanitize the data for personal info
		for(ListingGUIWrapper wrapper : guiData) {
			wrapper.user.setDob("");
			wrapper.user.setEmail("");
		}
		
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(guiData);
		} catch (JsonProcessingException e) {
			logger.error("Unable to convert to JSON", e);
		}

		mv.addObject("API_KEY", SystemPropertiesService.getInstance().getProperty("API_KEY"));
		mv.addObject("liveServices", guiData);
		mv.addObject("liveServicesJSON", json);

		return mv;
	}
}
