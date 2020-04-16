package com.reachout.gui.controllers;

import java.util.HashMap;
import java.util.Map;

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
import com.reachout.dao.HibernatePasswordDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.dao.HibernateUserProfileDAOImpl;
import com.reachout.gui.validators.SignupValidator;
import com.reachout.gui.validators.ValidationResult;
import com.reachout.models.Password;
import com.reachout.models.User;
import com.reachout.models.UserProfile;

@Controller
@RequestMapping("/profile")
public class ProfileController {
	public final Logger logger = LogManager.getLogger(ProfileController.class);
	private static final String VIEW_NAME = "profile";
	private Authentication auth;

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		// Test to see if the user is logged in
		auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username =  (String) auth.getPrincipal();
		}
		logger.debug("Reached profile Controller");
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("user", username);
		return mv;
	}
	
	/**
	 * Update user profile. Parameters are profilePic, bio, healthStatus
	 *TO DO - might have to check that empty fields don't erase data in the db?
	 * 
	 * @param request
	 * @return
	 */
	//@PostMapping
	public ModelAndView update(HttpServletRequest request) {
		boolean saveUserDetailsSuccess = false;
		String profilePic = request.getParameter("profilePic");
		String bio = request.getParameter("userBio");
		String healthStatus = request.getParameter("healthStatus");
		String username = ((SystemUser) auth.getPrincipal()).getUsername();
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		int userId = userDAO.getUserIdByUsername(username);
		
		// Populate the user profile db
			UserProfile profile = new UserProfile(profilePic, bio, healthStatus, userId);
			
			try (HibernateUserProfileDAOImpl userProfileDAO = new HibernateUserProfileDAOImpl()) {
				saveUserDetailsSuccess = userProfileDAO.save(profile);
				if (!saveUserDetailsSuccess) {
					// Something went wrong updating the profile
					logger.error("Unable to update profile at this time");
				}
		}
	
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("postSent", true);
		return mv;
	}

}

	

