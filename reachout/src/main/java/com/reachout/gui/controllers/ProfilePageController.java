package com.reachout.gui.controllers;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.reachout.models.ListingStatus;
import com.reachout.models.Request;
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

		UserProfile profile = userProfileDAO.getProfileById(userId);
		// If we don't find one, just build one now and try to save
		if (profile == null) {
			logger.error(String.format("No profile found for userID {%s}, generating default one", userId));
			profile = new UserProfile();
			profile.generateStartingData();
			profile.setUserId(userId);
			userProfileDAO.saveOrUpdateProfile(profile);
		}
		
		bio = profile.getBio();
		profilePic = profile.getProfilePic();
		healthStatus = profile.getHealthStatus();
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
		List<Request> myRequests = reqDAO.getAllRequestsForUser(userId);
		mv.addObject("liveRequests", myRequests);
		Map<Integer, Integer> acceptedRequestsByUser = new HashMap<>();

		for (Request req : myRequests) {
			if (req.getStatus().equals(ListingStatus.PENDING)) {
				Integer userAcceptedRequest = reqDAO.getUserIdWhoAcceptedListing(req);
				if (userAcceptedRequest != null) {
					acceptedRequestsByUser.put(req.getId(), userAcceptedRequest);
				}
			}
		}
		mv.addObject("acceptRequestIDUserIDMap", acceptedRequestsByUser);

		Map<Integer, String> userIDtoUsernameMap = new HashMap<>();
		for (Integer uid : acceptedRequestsByUser.values()) {
			userIDtoUsernameMap.put(uid, userDAO.selectByID(uid).getUsername());
		}

		mv.addObject("numRequests", reqDAO.getNumRequestsForUser(userId));
		List<Request> acceptedRequests = reqDAO.getAcceptedRequestsForUser(userId);
		mv.addObject("acceptedRequests", acceptedRequests);
		for (Request req : acceptedRequests) {
			userIDtoUsernameMap.put(req.getUserId(), userDAO.selectByID(req.getUserId()).getUsername());
		}
		mv.addObject("userIDtoUsernameMap", userIDtoUsernameMap);
		HibernateServiceDAOImpl serDAO = new HibernateServiceDAOImpl();
		mv.addObject("liveServices", serDAO.getAllServicesForUser(userId));
		mv.addObject("numServices", serDAO.getNumServicesForUser(userId));

		return mv;
	}
}
