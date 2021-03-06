package com.reachout.gui.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.Group;
import com.reachout.models.GroupListing;
import com.reachout.models.Location;
import com.reachout.models.Request;
import com.reachout.processors.LocationFactory;
import com.reachout.processors.exceptions.MappingAPICallException;

@Controller
@RequestMapping("/createRequest")
public class RequestCreateController {

	public final Logger logger = LogManager.getLogger(RequestCreateController.class);

	private static final String VIEW_NAME = "createRequest";
	private int userId;

	/**
	 * Arrival on page will trigger this
	 *
	 * @param request
	 * @return The create request view
	 */
	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {

		logger.debug("Reached Request Create Controller");

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

		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);

		if ((userGroups = groupMemberDAO.getUserGroups(userId)) != null) {
			ArrayList<String> groupNames = new ArrayList<>();

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
	 * @param priority
	 * @param visibility
	 * @return View representing success or failure
	 */
	@PostMapping
	public ModelAndView submitForm(@RequestParam(name = "reqTitle") String title,
			@RequestParam(name = "reqDesc", required = false) String description,
			@RequestParam(name = "reqCounty") String county, @RequestParam(name = "reqPriority") String priority,
			@RequestParam(name = "group", required = false) String groupVisibility,
			@RequestParam(name = "reqCity", required = false) String city,
			@RequestParam(name = "reqStreet", required = true) String street, HttpServletRequest request) {

		boolean createSuccess = false;
		int publicVisibility = 0;
		Request newRequest = new Request();
		String listingCreateSuccess = "na";
		int listingId = 0;

		LocationFactory locationFactory = new LocationFactory();
		Location location = null;
		try {
			location = locationFactory.buildAndSaveLocation(street, city, county);
		} catch (MappingAPICallException e) {
			return returnErrorResult("Unable to determine the location of the address provided");
		}
		if (location == null) {
			return returnErrorResult("Something went wrong creating your Request, please try again");
		}

		HibernateGroupDAOImpl groupDAO = new HibernateGroupDAOImpl();
		HibernateGroupListingDAOImpl glDAO = new HibernateGroupListingDAOImpl();
		HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl();

		// check if user is in any groups, other wise make request public
		HibernateGroupMemberDAOImpl groupMemDAO = new HibernateGroupMemberDAOImpl();
		if (groupMemDAO.getUserGroups(userId).isEmpty()) {
			publicVisibility = 1;
			newRequest = new Request(title, description, county, city, street, userId, priority, publicVisibility,
					location.getLocId());
			createSuccess = reqDAO.save(newRequest);
			logger.info("Public request created");
		}

		else {
			// if a user in a member of a group check if they want the post public, private
			// or both.
			boolean listingCreateSuccessBool = false;
			List<String> visibility = new ArrayList<>();
			String[] serVisibilityValues = request.getParameterValues("reqVisibility");
			if (serVisibilityValues == null) {
				serVisibilityValues = new String[0];
			}
			visibility.addAll(Arrays.asList(serVisibilityValues));

			// If the user selected the group tickbox but provided no groups then ignore the
			// group selection and treat as though it's public
			if ((groupVisibility == null || groupVisibility.isEmpty()) && visibility.contains("group")) {
				visibility.remove("group");
			}

			if (visibility.isEmpty()) {
				visibility.add("public");
			}

			if (visibility.contains("public") && !visibility.contains("group")) {
				publicVisibility = 1;
				newRequest = new Request(title, description, county, city, street, userId, priority, publicVisibility,
						location.getLocId());
				createSuccess = reqDAO.save(newRequest);
				logger.info("Public request created");
			}

			// visible to group and public
			if (visibility.contains("group") && visibility.contains("public")) {
				publicVisibility = 1;
				newRequest = new Request(title, description, county, city, street, userId, priority, publicVisibility,
						location.getLocId());
				createSuccess = reqDAO.save(newRequest);
				logger.info("Public request created");
				listingId = reqDAO.getNewRequestId(userId);
				logger.info("Group Request created with ID " + listingId);
				try {
					Group group = groupDAO.selectByName(groupVisibility);
					GroupListing gl = new GroupListing(group.getId(), listingId);
					listingCreateSuccessBool = glDAO.save(gl);
				} catch (Exception e) {
					logger.error(String.format("Could not find group: %s", groupVisibility), e);
				}

			}

			// if they have selected visibile only within a group, get the listingID and
			// save to group listing table
			if (visibility.contains("group") && !(visibility.contains("public"))) {
				newRequest = new Request(title, description, county, city, street, userId, priority, publicVisibility,
						location.getLocId());
				createSuccess = reqDAO.save(newRequest);
				listingId = reqDAO.getNewRequestId(userId);
				logger.info("Group Request created with ID " + listingId);
				try {
					Group group = groupDAO.selectByName(groupVisibility);
					GroupListing gl = new GroupListing(group.getId(), listingId);
					listingCreateSuccessBool = glDAO.save(gl);
				} catch (Exception e) {
					logger.error(String.format("Could not find group: %s", groupVisibility), e);
				}
				if (listingCreateSuccessBool == false) {
					logger.error("Could not add entry to GroupListing table");
					listingCreateSuccess = "unsuccessful";
				} else {
					listingCreateSuccess = "success";
					logger.info("Success, group listing added");
				}
			}
		}

		if ((createSuccess == false) && (listingCreateSuccess == "success")) {
			logger.error("Error - unable to create service");
			if (listingCreateSuccess == "success") {
				glDAO.groupListingDelete(listingId);
			}
		}

		if ((listingCreateSuccess == "unsuccessful") && (createSuccess == true)) {
			if (reqDAO.deleteById(listingId)) {
				createSuccess = false;
				logger.debug("Listing deleted as group listing was unable to be created");
			} else {
				logger.debug("Unable to delete request listing and unable to create the group listing");
			}
		}

		ModelAndView mv = new ModelAndView(VIEW_NAME);
		if (createSuccess) {
			logger.debug(String.format("Built new request as %s", newRequest.toString()));
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
