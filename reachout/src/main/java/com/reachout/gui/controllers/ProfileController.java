package com.reachout.gui.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
		List<String> healthList = healthDAO.getAllHealthStatuses();
		mv.addObject("healthList", healthList);
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
		try (HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl()) {
			int userId = userDAO.getUserIdByUsername(username);
			
			UserProfile profile = new UserProfile("TESTIMAGE", bio, healthStatus, userId);
			// Populate the user profile db
			try (HibernateUserProfileDAOImpl userProfileDAO = new HibernateUserProfileDAOImpl()) {
				saveUserDetailsSuccess = userProfileDAO.saveOrUpdate(profile);
				if (!saveUserDetailsSuccess) {
					// Something went wrong updating the profile
					logger.error("Unable to update profile at this time");
				}
			}
		}
		
		if(saveUserDetailsSuccess) {
		ModelAndView mv = new ModelAndView("profile");
		mv.addObject("currentPage", "profile");
		mv.addObject("postSent", saveUserDetailsSuccess);
		return mv;
		}
		else {
			ModelAndView mv = new ModelAndView("updateProfile");
			mv.addObject("currentPage", "updateProfile");
			mv.addObject("postSent", saveUserDetailsSuccess);
			mv.addObject("error", "Could not update profile, please try again");
			return mv;
		}
	}
}
