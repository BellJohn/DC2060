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
import org.springframework.web.servlet.ModelAndView;

import com.reachout.dao.HibernateGroupDAOImpl;
import com.reachout.dao.HibernateGroupMemberDAOImpl;
import com.reachout.dao.HibernateServiceDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.Group;
import com.reachout.models.GroupMember;
import com.reachout.models.Service;
import com.reachout.models.User;

/**
 * Controller for the create group, responsible for creating groups and adding entries into the 
 * groups database.
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
			@RequestParam(name = "groupDesc" ) String description,
			@RequestParam(name =  "groupPicture", required = false) String picture,
			HttpServletRequest request) {

		// TODO Check to see if the content is valid

		int userId = 0;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof UsernamePasswordAuthenticationToken) {
			String username = ((UsernamePasswordAuthenticationToken) auth).getName();
			HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
			User user = userDAO.selectUser(username);
			if (user != null) {
				userId = user.getId();
			}
		}
		// Build a new group and save to database
		Group group = new Group(name, description, picture, 0);
		boolean createSuccess = false;
		try {
			HibernateGroupDAOImpl groupDAO = new HibernateGroupDAOImpl();
			createSuccess = groupDAO.save(group);
			group = groupDAO.selectByName(name);
			//Create a new entry in the group member table for the admin user (1 is admin)
			GroupMember groupMember = new GroupMember(group.getId(), userId, 1);
			HibernateGroupMemberDAOImpl groupMemberDAO = new HibernateGroupMemberDAOImpl();
			createSuccess= groupMemberDAO.save(groupMember);
		}
		catch (Exception e) {
			logger.error("Unable to create new group");
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
}