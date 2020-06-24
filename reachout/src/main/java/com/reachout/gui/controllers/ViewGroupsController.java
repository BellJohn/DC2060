package com.reachout.gui.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.reachout.dao.HibernateGroupDAOImpl;
import com.reachout.dao.HibernateGroupMemberDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.Group;
import com.reachout.processors.GroupHandler;
import com.reachout.processors.SystemPropertiesService;

/**
 * * Controller to display a list of all the created groups
 * 
 * @author Jessica
 *
 */

@Controller
@RequestMapping("/viewGroups")
public class ViewGroupsController {
	public final Logger logger = LogManager.getLogger(ViewGroupsController.class);
	private static final String VIEW_NAME = "viewGroups";

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		HibernateGroupDAOImpl groupDAO = new HibernateGroupDAOImpl();
		HibernateGroupMemberDAOImpl groupMemberDAO = new HibernateGroupMemberDAOImpl();

		logger.debug("Reached viewGroups Controller");
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		List<Group> userGroups = null;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		}
		int userId = userDAO.getUserIdByUsername(username);

		userGroups = groupMemberDAO.getUserGroups(userId);
		Set<Integer> groupIds = groupMemberDAO.getNonUserGroups(userId);
		Set<Group> otherGroups = new HashSet<>();
		for (int groupId : groupIds) {
			otherGroups.add(groupDAO.selectById(groupId));
		}

		List<Integer> pendingIds = groupMemberDAO.getPendingGroups(userId);
		List<Group> pendingGroups = new ArrayList<>();
		for (int pendingId : pendingIds) {
			pendingGroups.add(groupDAO.selectById(pendingId));
		}

		// Build up data for presenting on the GUI

		mv.addObject("userGroups", userGroups);
		SystemPropertiesService sps = SystemPropertiesService.getInstance();
		String uploadDirectory = File.separator + sps.getProperty("IMAGE_DIR") + File.separator;

		mv.addObject("uploadDirectory", uploadDirectory);
		mv.addObject("otherGroups", otherGroups);
		mv.addObject("pendingGroups", pendingGroups);
		mv.addObject("username", username);
		return mv;
	}

	// Error page
	private ModelAndView getErrorPage() {
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("Something went wrong, please try again");
		return mv;
	}

	/**
	 * Handles the event triggered when a user clicks the "ask to join group" button
	 * 
	 * Errors in execution result in either the error page being returned or the
	 * user being returned to the original page but a message presented depending on
	 * circumstance
	 * 
	 * @param request
	 * @return
	 */

	@PostMapping
	public ModelAndView askToJoin(HttpServletRequest request) {
		Integer groupID;
		try {
			groupID = Integer.parseInt(request.getParameter("groupID"));
		} catch (NumberFormatException e) {
			logger.error("Request contained non-numeric groupID, returning user to error page");
			return getErrorPage();
		}

		// get username and ID
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		}

		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		int userID = userDAO.getUserIdByUsername(username);

		boolean successfulRequest = false;
		successfulRequest = GroupHandler.getInstance().requestToJoin(userID, groupID);

		ModelAndView mv = initPage(request);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("requested", successfulRequest);
		boolean joinButton = false;
		if (!successfulRequest) {
			joinButton = true;
		}
		mv.addObject("joinButton", joinButton);
		return mv;
	}
}
