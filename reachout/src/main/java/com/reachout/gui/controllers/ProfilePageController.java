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
@RequestMapping("/profile")
public class ProfilePageController {

	public final Logger logger = LogManager.getLogger(ProfilePageController.class);

	private static final String VIEW_NAME = "profile";
	private HibernateHealthStatusDAOImpl healthDAO;
	private HibernateUserDAOImpl userDAO;
	private HibernateUserProfileDAOImpl userProfileDAO;
	private String firstName;
	private String lastName;
	private String profilePic;
	private String bio;

	private Authentication auth;

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		healthDAO = new HibernateHealthStatusDAOImpl();
		userDAO = new HibernateUserDAOImpl();
		userProfileDAO = new HibernateUserProfileDAOImpl();
		firstName = null;
		lastName = null;
		bio = null;
		profilePic = null;
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
		UserProfile profile = userProfileDAO.getProfileById(userId);
		bio = profile.getBio();
		profilePic = profile.getProfilePic();
		mv.addObject("firstName", firstName);
		mv.addObject("lastName", lastName);
		mv.addObject("bio", bio);
		mv.addObject("profilePic", profilePic);
		List<String> healthList = new ArrayList<String>();
		healthList = healthDAO.getAllHealthStatuses();
		System.out.println(healthList);
		mv.addObject("healthList", healthList);
		return mv;
	}
}



