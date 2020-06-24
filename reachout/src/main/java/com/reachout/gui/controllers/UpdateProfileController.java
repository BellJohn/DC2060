package com.reachout.gui.controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
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
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.dao.HibernateUserProfileDAOImpl;
import com.reachout.models.UserProfile;
import com.reachout.processors.SystemPropertiesService;
import com.reachout.utils.ROUtils;

/**
 * Used for converting updating a user's profile
 *
 * @author Jess
 *
 */

@Controller
@RequestMapping("/updateProfile")
public class UpdateProfileController {

	public final Logger logger = LogManager.getLogger(UpdateProfileController.class);

	private static final String VIEW_NAME = "updateProfile";

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {

		// Test to see if the user is logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		}
		logger.debug("Reached Update Profile Controller");

		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("user", username);

		// Get all possible health status' to the dropdown list
		List<String> healthList;
		HibernateHealthStatusDAOImpl healthDAO = new HibernateHealthStatusDAOImpl();
		healthList = healthDAO.getAllHealthStatuses();

		mv.addObject("healthList", healthList);
		String firstName = "";
		String lastName = "";
		String bio = "";
		String healthStatus = "";
		UserProfile profile = null;
		String profilePic = "";

		// Get all relevant information to display on the page for the user to edit
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		HibernateUserProfileDAOImpl userProfileDAO = new HibernateUserProfileDAOImpl();

		firstName = userDAO.selectUser(username).getFirstName();
		lastName = userDAO.selectUser(username).getLastName();
		int userId = userDAO.getUserIdByUsername(username);

		profile = new UserProfile();
		try {
			profile = userProfileDAO.getProfileById(userId);
			bio = profile.getBio();
			healthStatus = profile.getHealthStatus();
			profilePic = profile.getProfilePic();
		} catch (Exception e) {
			logger.error(String.format("No profile found for userID {%s}", userId), e);
		}

		SystemPropertiesService sps = SystemPropertiesService.getInstance();
		String uploadDirectory = sps.getProperty("IMAGE_DIR");
		profilePic = File.separator + uploadDirectory + File.separator + profilePic;
		mv.addObject("firstName", firstName);
		mv.addObject("lastName", lastName);
		mv.addObject("bio", bio);
		mv.addObject("healthStatus", healthStatus);
		mv.addObject("profilePic", profilePic);
		return mv;
	}

	/**
	 * Update user profile. Parameters are profilePic, bio, healthStatus
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	@PostMapping()
	public ModelAndView saveOrUpdate(HttpServletRequest request, @RequestParam("file") MultipartFile profilePic) {
		logger.debug("Attempting profile update");

		boolean saveUserDetailsSuccess = false;

		// determine picture file extension
		boolean validPic = ROUtils.validPic(profilePic);
		String extension = "";
		boolean changedImage = false;
		if (!profilePic.isEmpty() && validPic) {
			validPic = true;
			extension = ROUtils.getPictureExtension(profilePic);
			changedImage = true;
		}

		if (!validPic) {
			ModelAndView mv = initPage(request);
			mv.addObject("postSent", saveUserDetailsSuccess);
			mv.addObject("error", "Image uploads must be either [.png, .jpg, .jfif] and below 10mb");
			return mv;
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;

		// retrieve username of logged in user
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		}

		String bio = request.getParameter("userBio");
		String healthStatus = request.getParameter("healthStatus");

		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		int userId = userDAO.getUserIdByUsername(username);
		String profilePicName = username + "_" + userId + extension;

		// Populate the user profile db
		HibernateUserProfileDAOImpl userProfileDAO = new HibernateUserProfileDAOImpl();
		UserProfile profile = userProfileDAO.getProfileById(userId);
		if (profile == null) {
			profile = new UserProfile();
			profile.setUserId(userId);
		}

		profile.setBio(bio);
		profile.setHealthStatus(healthStatus);
		if (changedImage) {
			profile.setProfilePic(profilePicName);

			// save picture to directory
			if (ROUtils.saveImageToDisk(profilePic, profilePicName)) {
				saveUserDetailsSuccess = userProfileDAO.saveOrUpdateProfile(profile);
				if (!saveUserDetailsSuccess) {
					// Something went wrong updating the profile
					logger.error("Unable to update profile at this time");
				}
			}
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
