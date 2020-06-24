package com.reachout.gui.controllers;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.dao.HibernateGroupDAOImpl;
import com.reachout.dao.HibernateGroupMemberDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.Group;
import com.reachout.models.GroupMember;
import com.reachout.models.Location;
import com.reachout.models.User;
import com.reachout.processors.LocationFactory;
import com.reachout.processors.exceptions.MappingAPICallException;
import com.reachout.utils.ROUtils;

/**
 * Controller for the create group, responsible for creating groups and adding
 * entries into the groups database.
 * 
 * @author Jessica
 * 
 */

@Controller
@RequestMapping("/createGroup")
public class GroupCreateController {

	public final Logger logger = LogManager.getLogger(GroupCreateController.class);

	private static final String VIEW_NAME = "createGroup";

	/**
	 * Arrival on page will trigger this
	 * 
	 * @param request
	 * @return The create group view
	 */
	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {

		logger.debug("Reached Create Group Controller");
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		return mv;
	}

	/**
	 * Submission of the create new request form will trigger this
	 * 
	 * @param name
	 * @param description
	 * @param picture
	 * @param location
	 * @param request
	 * @return View representing success or failure
	 */
	@PostMapping
	public ModelAndView submitForm(@RequestParam(name = "groupName") String name,
			@RequestParam(name = "groupDesc") String description,
			@RequestParam(name = "groupPicture") MultipartFile groupPic,
			@RequestParam(name = "groupCity", required = true) String city,
			@RequestParam(name = "groupCounty", required = true) String county, HttpServletRequest request) {

		int userId = 0;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof UsernamePasswordAuthenticationToken) {
			String username = ((UsernamePasswordAuthenticationToken) auth).getName();
			HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
			User user = userDAO.selectUser(username);
			if (user == null) {
				return returnErrorPage("Unable to determine user, try refreshing the page and try again");
			}
			userId = user.getId();
		}
		// Build a new group and save to database
		LocationFactory locFactory = new LocationFactory();
		Location location = null;
		try {
			location = locFactory.buildAndSaveLocation("", city, county);
		} catch (MappingAPICallException e) {
			logger.error(String.format("Unable to construct location for group, {%s}", e.getMessage()), e);
			return returnErrorPage(
					"Something went wrong locating your group. Please check the address provided and try again");

		}
		if (location == null) {
			return returnErrorPage(
					"Something went wrong locating your group. Please check the address provided and try again");
		}

		boolean validPic = false;
		String extension = "";
		if (!groupPic.isEmpty() && ROUtils.validPic(groupPic)) {
			validPic = true;
			extension = ROUtils.getPictureExtension(groupPic);
		}

		if (!validPic) {
			return returnErrorPage("Image uploads must be either [.png, .jpg, .jfif] and below 10mb");
		}
		String groupPicName = "GROUP_" + name + "_" + userId + extension;

		Group group = new Group(name, description, groupPicName, location.getLocId(), city, county);
		boolean createSuccess = false;
		try {
			HibernateGroupDAOImpl groupDAO = new HibernateGroupDAOImpl();
			if (!ROUtils.saveImageToDisk(groupPic, groupPicName)) {
				return returnErrorPage("Something went wrong saving your group. Please try again");
			}
			createSuccess = groupDAO.save(group);
			group = groupDAO.selectByName(name);
			// Create a new entry in the group member table for the admin user (2 is admin)
			GroupMember groupMember = new GroupMember(group.getId(), userId, 2);
			HibernateGroupMemberDAOImpl groupMemberDAO = new HibernateGroupMemberDAOImpl();
			createSuccess = groupMemberDAO.save(groupMember);

		} catch (Exception e) {
			logger.error("Unable to create new group", e);
		}

		ModelAndView mv = new ModelAndView(VIEW_NAME);
		if (createSuccess) {
			logger.debug(String.format("Built new group as %s", group.toString()));
		}
		mv.addObject("postSent", true);
		mv.addObject("createSuccess", createSuccess);
		mv.addObject("currentPage", VIEW_NAME);
		return mv;
	}

	private ModelAndView returnErrorPage(String errorMessage) {
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("error", errorMessage);
		mv.addObject("postSent", false);
		mv.addObject("createSuccess", false);
		mv.addObject("currentPage", VIEW_NAME);
		return mv;
	}
}