package com.reachout.gui.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.auth.SystemUser;
import com.reachout.dao.HibernateGroupDAOImpl;
import com.reachout.dao.HibernateGroupListingDAOImpl;
import com.reachout.dao.HibernateGroupMemberDAOImpl;
import com.reachout.dao.HibernateRequestDAOImpl;
import com.reachout.dao.HibernateServiceDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.Group;
import com.reachout.models.GroupListing;
import com.reachout.models.Location;
import com.reachout.models.Request;
import com.reachout.models.Service;
import com.reachout.models.User;
import com.reachout.processors.LocationFactory;
import com.reachout.processors.exceptions.MappingAPICallException;

@Controller
@RequestMapping("/createService")
public class ServiceCreateController {

	public final Logger logger = LogManager.getLogger(ServiceCreateController.class);
	private int userId;
	private static final String VIEW_NAME = "createService";

	/**
	 * Arrival on page will trigger this
	 *
	 * @param request
	 * @return The create request view
	 */
	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		logger.debug("Reached ServiceCreate  Controller");

		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		HibernateGroupMemberDAOImpl groupMemberDAO = new HibernateGroupMemberDAOImpl();
		List<Group> userGroups = null;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		}
		userId = userDAO.getUserIdByUsername(username);

		userGroups = groupMemberDAO.getUserGroups(userId);


		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);

		if ( (userGroups = groupMemberDAO.getUserGroups(userId)) != null) {
			ArrayList<String> groupNames = new ArrayList<String>();

			for (Group g : userGroups) {
				groupNames.add(g.getName());
				logger.debug("Groups for the user are : " + groupNames.toString());
				mv.addObject("userGroups", groupNames);
			}
		}
		return mv;
	}

	/**
	 * Submission of the create new request form will trigger this
	 *
	 * @param title
	 * @param description
	 * @param county
	 * @param city
	 * @param request
	 * @param visbility
	 * @return View representing success or failure
	 */
	@PostMapping
	public ModelAndView submitForm(@RequestParam(name = "serTitle") String title,
			@RequestParam(name = "serDesc", required = false) String description,
			@RequestParam(name = "serCounty") String county,
			@RequestParam(name = "group", required = false) String groupVisibility,
			@RequestParam(name = "serCity", required = false) String city,
			@RequestParam(name = "serStreet", required = true) String address, HttpServletRequest request) {

		int publicVisibility = 0;
		boolean createSuccess = false;
		String listingCreateSuccess = "na";
		Service newService = new Service();
		int listingId = 0;

		HibernateGroupDAOImpl groupDAO = new HibernateGroupDAOImpl();
		HibernateGroupListingDAOImpl glDAO = new HibernateGroupListingDAOImpl();
		HibernateServiceDAOImpl serviceDAO = new HibernateServiceDAOImpl();

		//check if the user is a member of any groups, if not automatically set request as public
		HibernateGroupMemberDAOImpl groupMemDAO = new HibernateGroupMemberDAOImpl();
		if (groupMemDAO.getUserGroups(userId).isEmpty()) {
			publicVisibility = 1;
			// Build a new request which will be given the status of "new"
			newService = new Service(title, description, county, city, userId, publicVisibility);
			createSuccess = serviceDAO.save(newService);
			logger.info("Public service created");
		}
		else {
			//if a user in a member of a group check if they want the post public, private or both.

			boolean listingCreateSuccessBool = false;
			List<String> visibility= Arrays.asList(request.getParameterValues("serVisibility")) ;
			//service to public and group visibility
			if (visibility.contains("public") && (visibility.contains("group"))){
				publicVisibility = 1;
				// Build a new request which will be given the status of "new"
				newService = new Service(title, description, county, city, userId, publicVisibility);
				createSuccess = serviceDAO.save(newService);
				logger.info("Public service created");
				try {
					logger.debug("Group selected from dropdown list: " + groupVisibility);
					Group group = groupDAO.selectByName(groupVisibility);
					GroupListing gl = new GroupListing(group.getId(), serviceDAO.getNewServiceId(userId));
					listingCreateSuccessBool = glDAO.save(gl);
					logger.info("Group service created");
				}
				catch (Exception e) {
					logger.error("Could not find group") ;
				}
			}

			//if it is just for public visibility
			if (visibility.contains("public") && !(visibility.contains("group"))){
				publicVisibility = 1;
				// Build a new request which will be given the status of "new"
				newService = new Service(title, description, county, city, userId, publicVisibility);
				createSuccess = serviceDAO.save(newService);
				logger.info("Public service created");
			}

			///if they have selected visibile only within a group, get the listingID and save to group listing table
			if(visibility.contains("group") && !(visibility.contains("public"))) {
				logger.info("Group service created");
				newService = new Service(title, description, county, city, userId, 0);
				createSuccess = serviceDAO.save(newService);
				try {
					logger.debug("Group selected from dropdown list: " + groupVisibility);
					Group group = groupDAO.selectByName(groupVisibility);
					GroupListing gl = new GroupListing(group.getId(), serviceDAO.getNewServiceId(userId));
					listingCreateSuccessBool = glDAO.save(gl);
				}
				catch (Exception e) {
					logger.error("Could not find group") ;
				}
				if(listingCreateSuccessBool == false) {
					logger.error("Could not add entry to GroupListing table");
					listingCreateSuccess = "unsuccessful";
				}
				else {
					listingCreateSuccess = "success";
					logger.info("Success, group listing added");
				}
			}
		}


		if((createSuccess == false) && (listingCreateSuccess == "success")) {
			logger.error("Error - unable to create service");
			if(listingCreateSuccess == "success") {
				glDAO.groupListingDelete(listingId);
			}
		}

		LocationFactory locationFactory = new LocationFactory();
		Location location = null;
		try {
			location = locationFactory.buildAndSaveLocation(address, city, county);
		} catch (MappingAPICallException e) {
			return returnErrorResult("Unable to determine the location of the address provided");

		}
		if (location == null) {
			return returnErrorResult("Something went wrong creating your Service, please try again");
		}

		// Build a new request which will be given the status of "new"
		Service newService = new Service(title, description, county, city, userId, location.getLocId());
		boolean createSuccess = false;
		HibernateServiceDAOImpl serviceDAO = new HibernateServiceDAOImpl();
		createSuccess = serviceDAO.save(newService);

		if((listingCreateSuccess == "unsuccessful") && (createSuccess == true)) {
			if (serviceDAO.deleteById(listingId)) {
				createSuccess = false;
				logger.debug("Listing deleted as group listing was unable to be created");
			}
			else {
				logger.debug("Unable to delete service listing and unable to create the group listing");
			}
		}

		ModelAndView mv = new ModelAndView(VIEW_NAME);
		if (createSuccess) {
			logger.debug(String.format("Built new service as %s", newService.toString()));
		}
		mv.addObject("postSent", true);
		mv.addObject("createSuccess", createSuccess);
		mv.addObject("currentPage", VIEW_NAME);
		return mv;
	}

	private ModelAndView returnErrorResult(String error) {
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("postSent", false);
		mv.addObject("createSuccess", false);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("error", error);
		return mv;
	}
}
