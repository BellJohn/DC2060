package com.reachout.gui.controllers;

import java.util.ArrayList;
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
import com.reachout.dao.HibernateGroupMemberDAOImpl;
import com.reachout.dao.HibernateServiceDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.Group;
import com.reachout.models.Service;
import com.reachout.models.User;

@Controller
@RequestMapping("/createService")
public class ServiceCreateController {

	public final Logger logger = LogManager.getLogger(ServiceCreateController.class);

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
		int userId = userDAO.getUserIdByUsername(username);

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
			@RequestParam(name = "publicVisibility", required = false) String publicVisibility,
			@RequestParam(name = "groupVisibility", required = false) String groupVisibility,
			@RequestParam(name = "serCity", required = false) String city, HttpServletRequest request) {

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

		int visibility = 0;
		if (publicVisibility == "visible") {
			visibility = 1;
			logger.info("Public request created");
		}


		// Build a new request which will be given the status of "new"
		Service newService = new Service(title, description, county, city, userId, visibility);
		boolean createSuccess = false;
		HibernateServiceDAOImpl serviceDAO = new HibernateServiceDAOImpl();
		createSuccess = serviceDAO.save(newService);

		ModelAndView mv = new ModelAndView(VIEW_NAME);
		if (createSuccess) {
			logger.debug(String.format("Built new service as %s", newService.toString()));
		}
		mv.addObject("postSent", true);
		mv.addObject("createSuccess", createSuccess);
		mv.addObject("currentPage", VIEW_NAME);
		return mv;
	}
}
