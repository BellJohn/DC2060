package com.reachout.gui.controllers;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.auth.SystemUser;
import com.reachout.dao.HibernateHealthStatusDAOImpl;
import com.reachout.dao.HibernatePasswordDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.dao.HibernateUserProfileDAOImpl;
import com.reachout.gui.validators.SignupValidator;
import com.reachout.gui.validators.ValidationResult;
import com.reachout.models.*;


@Controller
@RequestMapping("/updateProfile")
public class ProfileController {

	public final Logger logger = LogManager.getLogger(ProfileController.class);

	private static final String VIEW_NAME = "updateProfile";
	private HibernateHealthStatusDAOImpl healthDAO;

	private Authentication auth;

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		
		healthDAO = new HibernateHealthStatusDAOImpl();
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
		List<String> healthList = healthDAO.getAllHealthStatuses();
		mv.addObject("healthList", healthList);
		return mv;
	}

	/**
	 * Update user profile. Parameters are profilePic, bio, healthStatus
	 *TO DO - might have to check that empty fields don't erase data in the db?
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping
	public ModelAndView update(HttpServletRequest request) {
		boolean saveUserDetailsSuccess = false;
		String profilePic = request.getParameter("profilePic");
		String bio = request.getParameter("userBio");
		HealthStatus healthStatus =new HealthStatus(request.getParameter("healthStatus"));
		
		String username = auth.getName();
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
		}catch (Exception e) {
			logger.error("Problem updating user profile");
		}
		ModelAndView mv = new ModelAndView("profile");
		mv.addObject("redirect", "profile");
		mv.addObject("postSent", true);
		return mv;
	}
	

}



