package com.reachout.gui.controllers;

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
import org.springframework.web.servlet.ModelAndView;

import com.reachout.auth.SystemUser;
import com.reachout.dao.HibernateHealthStatusDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.dao.HibernateUserProfileDAOImpl;
import com.reachout.models.*;

@Controller
@RequestMapping("/updateProfile")
public class ProfileController {

	public final Logger logger = LogManager.getLogger(ProfileController.class);

	private static final String VIEW_NAME = "updateProfile";
	private HibernateHealthStatusDAOImpl healthDAO;

	private HibernateUserDAOImpl userDAO;
	private HibernateUserProfileDAOImpl userProfileDAO;
	private String firstName;
	private String lastName;
	private String profilePic;
	private String bio;
	private String healthStatus;

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		healthDAO = new HibernateHealthStatusDAOImpl();

		// Test to see if the user is logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		}
		logger.debug("Reached profile Controller");

		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("user", username);

		// Get all possible health status' to the dropdown list
		List<String> healthList = healthDAO.getAllHealthStatuses();
		mv.addObject("healthList", healthList);

		// Get all relevant information to display on the page for the user to edit
		userDAO = new HibernateUserDAOImpl();
		userProfileDAO = new HibernateUserProfileDAOImpl();
		firstName = null;
		lastName = null;
		bio = null;
		profilePic = null;
		healthStatus = null;

		firstName = userDAO.selectUser(username).getFirstName();
		lastName = userDAO.selectUser(username).getLastName();
		int userId = userDAO.getUserIdByUsername(username);

		UserProfile profile = new UserProfile();
		try {
			profile = userProfileDAO.getProfileById(userId);
			bio = profile.getBio();
			profilePic = profile.getProfilePic();
			healthStatus = profile.getHealthStatus();
		} catch (Exception e) {
			System.out.println("No result found");
		}

		mv.addObject("firstName", firstName);
		mv.addObject("lastName", lastName);
		mv.addObject("bio", bio);
		mv.addObject("profilePic", profilePic);
		mv.addObject("healthStatus", healthStatus);

		return mv;
	}

	/**
	 * Update user profile. Parameters are profilePic, bio, healthStatus
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping()
	public ModelAndView update(HttpServletRequest request) {
		logger.debug("Attempting profile update");

		boolean saveUserDetailsSuccess = false;
//		String profilePic = request.getParameter("profilePic");
		String bio = request.getParameter("userBio");
		String healthStatus = request.getParameter("healthStatus");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();

		} else {
			username = (String) auth.getPrincipal();
		}
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		int userId = userDAO.getUserIdByUsername(username);

		UserProfile profile = new UserProfile("TESTIMAGE", bio, healthStatus, userId);
		// Populate the user profile db
		HibernateUserProfileDAOImpl userProfileDAO = new HibernateUserProfileDAOImpl();
		saveUserDetailsSuccess = userProfileDAO.saveOrUpdate(profile);
		if (!saveUserDetailsSuccess) {
			// Something went wrong updating the profile
			logger.error("Unable to update profile at this time");
		}

		if (saveUserDetailsSuccess) {
			return new ModelAndView("redirect:/profile");
		} else {
			ModelAndView mv = new ModelAndView(VIEW_NAME);
			mv.addObject("currentPage", VIEW_NAME);
			mv.addObject("postSent", saveUserDetailsSuccess);
			mv.addObject("error", "Could not update profile, please try again");
			return mv;
		}
	}
}
