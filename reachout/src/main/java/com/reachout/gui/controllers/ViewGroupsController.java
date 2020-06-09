package com.reachout.gui.controllers;

import java.util.ArrayList;
import java.util.List;

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
import com.reachout.dao.HibernateGroupDAOImpl;
import com.reachout.dao.HibernateGroupMemberDAOImpl;
import com.reachout.dao.HibernateRequestDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.Group;
import com.reachout.models.ListingGUIWrapper;
import com.reachout.models.Request;

@Controller
@RequestMapping("/viewGroups")
public class ViewGroupsController {
	public final Logger logger = LogManager.getLogger(ViewGroupsController.class);

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		HibernateGroupDAOImpl groupDAO = new HibernateGroupDAOImpl();
		HibernateGroupMemberDAOImpl groupMemberDAO = new HibernateGroupMemberDAOImpl();

		logger.debug("Reached viewGroups Controller");
		ModelAndView mv = new ModelAndView("viewGroups");
		mv.addObject("currentPage", "viewGroups");

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		}
		int userId = userDAO.getUserIdByUsername(username);

		List<Group> userGroups = groupMemberDAO.getUserGroups(userId);
		List<Group> otherGroups = groupMemberDAO.getNonUserGroups(userId);

		// Build up data for presenting on the GUI
		
		mv.addObject("userGroups", userGroups);
		mv.addObject("otherGroups", otherGroups);
		mv.addObject("username", username);
		return mv;
	}
}
