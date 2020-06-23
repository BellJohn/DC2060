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
import com.reachout.dao.HibernateRequestDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.ListingGUIWrapper;
import com.reachout.models.Request;

@Controller
@RequestMapping("/viewRequests")
public class ViewRequestsController {
	public final Logger logger = LogManager.getLogger(ViewRequestsController.class);

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();

		logger.debug("Reached viewRequests Controller");
		ModelAndView mv = new ModelAndView("viewRequests");
		mv.addObject("currentPage", "viewRequests");

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		} 
		int userId = userDAO.getUserIdByUsername(username);

		HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl();
		List<Request> allRequests = reqDAO.getAllRequestsForDisplay(userId);
		List<ListingGUIWrapper> guiData = new ArrayList<>();

		// Build up data for presenting on the GUI
		for (Request req : allRequests) {
			guiData.add(new ListingGUIWrapper(req, userDAO.selectByID(req.getUserId())));
		}

		mv.addObject("liveRequests", guiData);

		return mv;
	}
	
}
