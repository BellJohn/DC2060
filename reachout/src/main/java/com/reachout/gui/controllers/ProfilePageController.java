package com.reachout.gui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.auth.SystemUser;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.dao.HibernateUserProfileDAOImpl;
import com.reachout.models.*;

import com.reachout.dao.HibernateRequestDAOImpl;
import com.reachout.dao.HibernateServiceDAOImpl;

@Controller
@RequestMapping("/profile")
public class ProfilePageController {
	public final Logger logger = LogManager.getLogger(ProfilePageController.class);

	private static final String VIEW_NAME = "profile";
	private HibernateUserDAOImpl userDAO;
	private HibernateUserProfileDAOImpl userProfileDAO;
	private String firstName;
	private String lastName;
	private String profilePic;
	private String bio;
	private String healthStatus;
	private Authentication auth;

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		userDAO = new HibernateUserDAOImpl();
		userProfileDAO = new HibernateUserProfileDAOImpl();
		firstName = null;
		lastName = null;
		bio = null;
		profilePic = null;
		healthStatus = null;
		
		// Test to see if the user is logged in
		auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username =  (String) auth.getPrincipal();
		}

		logger.debug("Reached profilePage Controller");
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		firstName = userDAO.selectUser(username).getFirstName();
		lastName = userDAO.selectUser(username).getLastName();
		int userId = userDAO.getUserIdByUsername(username);

		UserProfile profile = new UserProfile();
		try {
			profile = userProfileDAO.getProfileById(userId);
			bio = profile.getBio();
			profilePic = profile.getProfilePic();
			healthStatus = profile.getHealthStatus();
		}
		catch (Exception e) {
			System.out.println("No result found");
		}
		
		mv.addObject("firstName", firstName);
		mv.addObject("lastName", lastName);
		mv.addObject("bio", bio);
		mv.addObject("profilePic", profilePic);
		mv.addObject("healthStatus", healthStatus);

		try (HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl()) {
			mv.addObject("liveRequests", reqDAO.getAllRequestsForUser(userId));
			mv.addObject("numRequests", reqDAO.getNumRequestsForUser(userId));
		}

		try (HibernateServiceDAOImpl serDAO = new HibernateServiceDAOImpl()) {
			mv.addObject("liveServices", serDAO.getAllServicesForUser(userId));
			mv.addObject("numServices", serDAO.getNumServicesForUser(userId));
		}

		return mv;
		}
	}



