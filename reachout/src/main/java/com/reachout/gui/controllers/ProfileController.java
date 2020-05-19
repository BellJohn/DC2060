package com.reachout.gui.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.auth.SystemUser;
import com.reachout.dao.HibernateHealthStatusDAOImpl;
import com.reachout.dao.HibernateImageDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.dao.HibernateUserProfileDAOImpl;
import com.reachout.models.*;


/**
 * Used for converting updating a user's profile
 * 
 * @author Jess
 *
 */

@Controller
@RequestMapping("/updateProfile")
public class ProfileController {

	public final Logger logger = LogManager.getLogger(ProfileController.class);

	private static final String VIEW_NAME = "updateProfile";
	private HibernateHealthStatusDAOImpl healthDAO;

	private HibernateUserDAOImpl userDAO;
	private HibernateUserProfileDAOImpl userProfileDAO;
	private HibernateImageDAOImpl imageDAO;
	private String firstName;
	private String lastName;
	private String bio;
	private String healthStatus;
	private UserProfile profile;
	private boolean hasCreatedProfile;


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
		imageDAO = new HibernateImageDAOImpl();
		firstName = null;
		lastName = null;
		bio = null;
		healthStatus = null;

		firstName = userDAO.selectUser(username).getFirstName();
		lastName = userDAO.selectUser(username).getLastName();
		int userId = userDAO.getUserIdByUsername(username);

		profile = new UserProfile();
		try {
			profile = userProfileDAO.getProfileById(userId);
			bio = profile.getBio();
			healthStatus = profile.getHealthStatus();
			hasCreatedProfile = true;
		}
		catch (Exception e) {
			hasCreatedProfile = false;
			System.out.println("No result found");
		}

		mv.addObject("firstName", firstName);
		mv.addObject("lastName", lastName);
		mv.addObject("bio", bio);
		mv.addObject("healthStatus", healthStatus);

		return mv;
	}

	/**
	 * Update user profile. Parameters are profilePic, bio, healthStatus
	 * 
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@PostMapping()
	public ModelAndView saveOrUpdate(HttpServletRequest request, @RequestParam("file") MultipartFile profilePic) throws IOException {
		logger.debug("Attempting profile update");

		boolean saveUserDetailsSuccess = false;

		String bio = request.getParameter("userBio");
		String healthStatus = request.getParameter("healthStatus");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;

		//retrieve username of logged in user
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		}		

		try (HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl()) {
			int userId = userDAO.getUserIdByUsername(username);

			UserProfile profile = new UserProfile("test image", bio, healthStatus, userId);

			// Populate the user profile db
			try (HibernateUserProfileDAOImpl userProfileDAO = new HibernateUserProfileDAOImpl()) {

				if (hasCreatedProfile = false) {
					saveUserDetailsSuccess = userProfileDAO.save(profile);
					if (!saveUserDetailsSuccess) {
						// Something went wrong updating the profile
						logger.error("Unable to create new profile at this time");
					}
				}
				else {
					saveUserDetailsSuccess = userProfileDAO.updateUserProfile(profile);
					if (!saveUserDetailsSuccess) {
						// Something went wrong updating the profile
						logger.error("Unable to update profile at this time");
					}
					//create an image object from the file		
					ImageController ic = new ImageController();
					saveUserDetailsSuccess = ic.saveImage(profilePic, userId);
				}
			}
		}
		if(saveUserDetailsSuccess) {
			return new ModelAndView("redirect:/profile");
		}
		else {
			ModelAndView mv = new ModelAndView(VIEW_NAME);
			mv.addObject("currentPage", VIEW_NAME);
			mv.addObject("postSent", saveUserDetailsSuccess);
			mv.addObject("error", "Could not update profile, please try again");
			return mv;
		}
	}
}
