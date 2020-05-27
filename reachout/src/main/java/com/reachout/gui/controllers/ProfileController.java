package com.reachout.gui.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import com.reachout.models.*;
import com.reachout.dao.*;


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
	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/uploads";
	private HibernateHealthStatusDAOImpl healthDAO;
	private HibernateUserDAOImpl userDAO;
	private HibernateUserProfileDAOImpl userProfileDAO;
	private String firstName;
	private String lastName;
	private String bio;
	private String healthStatus;
	private UserProfile profile;
	private boolean hasCreatedProfile;
	private String profilePic;

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) throws ServletException {
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
		healthStatus = null;
		profilePic = null;

		firstName = userDAO.selectUser(username).getFirstName();
		lastName = userDAO.selectUser(username).getLastName();
		int userId = userDAO.getUserIdByUsername(username);

		profile = new UserProfile();
		try {
			profile = userProfileDAO.getProfileById(userId);
			bio = profile.getBio();
			healthStatus = profile.getHealthStatus();
			hasCreatedProfile = true;
			profilePic = profile.getProfilePic();
		}
		catch (Exception e) {
			hasCreatedProfile = false;
			System.out.println("No result found");
		}

		mv.addObject("firstName", firstName);
		mv.addObject("lastName", lastName);
		mv.addObject("bio", bio);
		mv.addObject("healthStatus", healthStatus);
		mv.addObject("profilePic", profilePic );
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
	public ModelAndView saveOrUpdate(HttpServletRequest request, @RequestParam("file") MultipartFile profilePic) throws IOException {
		logger.debug("Attempting profile update");

		boolean saveUserDetailsSuccess = false;

		//determine picture file extension
		String extension = "";
		if (profilePic.getOriginalFilename().contains(".png")) {
			extension = ".png";
		}
		if (profilePic.getOriginalFilename().contains(".jpg")) {
			extension = ".jpg";
		}
		if (profilePic.getOriginalFilename().contains(".jfif")) {
			extension = ".jfif";
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;

		//retrieve username of logged in user
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		}

		String bio = request.getParameter("userBio");
		String healthStatus = request.getParameter("healthStatus");
		Path fileNameAndPath = Paths.get(uploadDirectory, username + extension);
		String profilePicName = fileNameAndPath.toString();


		try (HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl()) {
			int userId = userDAO.getUserIdByUsername(username);

			UserProfile profile = new UserProfile(profilePicName, bio, healthStatus, userId);

			// Populate the user profile db
			try (HibernateUserProfileDAOImpl userProfileDAO = new HibernateUserProfileDAOImpl()) {

				saveUserDetailsSuccess = userProfileDAO.saveOrUpdateProfile(profile);
				if (!saveUserDetailsSuccess) {
					// Something went wrong updating the profile
					logger.error("Unable to update profile at this time");
				}

				//save picture to directory
				fileNameAndPath.toFile().mkdir();					
				Files.write(fileNameAndPath, profilePic.getBytes());
				logger.info("Successfully uploaded file " + fileNameAndPath.toString());
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
