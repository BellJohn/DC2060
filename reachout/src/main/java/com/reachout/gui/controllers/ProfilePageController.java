package com.reachout.gui.controllers;

import java.io.File;
import java.util.Random;

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
import com.reachout.dao.HibernateRequestDAOImpl;
import com.reachout.dao.HibernateServiceDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.dao.HibernateUserProfileDAOImpl;
import com.reachout.models.UserProfile;
import com.reachout.processors.SystemPropertiesService;

@Controller
@RequestMapping("/profile")
public class ProfilePageController {
	public final Logger logger = LogManager.getLogger(ProfilePageController.class);

	private static final String VIEW_NAME = "profile";

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		HibernateUserProfileDAOImpl userProfileDAO = new HibernateUserProfileDAOImpl();
		String firstName = null;
		String lastName = null;
		String bio = null;
		String profilePic = null;
		String healthStatus = null;

		// Test to see if the user is logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
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
		} catch (Exception e) {
			logger.error("No result found");
		}
		SystemPropertiesService sps = SystemPropertiesService.getInstance();
		String uploadDirectory = sps.getProperty("IMAGE_DIR");
		// We need an arbitrary random value to assign to the file fetch to ensure the
		// cache gets reset client side otherwise the picture wont update on their end.
		// Yes it's stupid but it works :)
		int arbitraryValue = new Random().nextInt(500);
		profilePic = File.separator + uploadDirectory + File.separator + profilePic + "?" + arbitraryValue;
		mv.addObject("firstName", firstName);
		mv.addObject("lastName", lastName);
		mv.addObject("bio", bio);
		mv.addObject("profilePic", profilePic);
		mv.addObject("healthStatus", healthStatus);

		HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl();
		mv.addObject("liveRequests", reqDAO.getAllRequestsForUser(userId));
		mv.addObject("numRequests", reqDAO.getNumRequestsForUser(userId));
		mv.addObject("acceptedRequests", reqDAO.getAcceptedRequestsForUser(userId));

		HibernateServiceDAOImpl serDAO = new HibernateServiceDAOImpl();
		mv.addObject("liveServices", serDAO.getAllServicesForUser(userId));
		mv.addObject("numServices", serDAO.getNumServicesForUser(userId));

		return mv;
	}
}
