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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reachout.auth.SystemUser;
import com.reachout.dao.HibernateLocationDAO;
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
		HibernateLocationDAO locationDAO = new HibernateLocationDAO();
		List<Request> allRequests = reqDAO.getAllRequestsForDisplay(userId);
		List<ListingGUIWrapper> guiData = new ArrayList<>();

		// Build up data for presenting on the GUI
		for (Request req : allRequests) {
			guiData.add(new ListingGUIWrapper(req, userDAO.selectByID(req.getUserId()), locationDAO.selectLocationById(req.getLocationId())));
		}

		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(guiData);
		} catch (JsonProcessingException e) {
			logger.error("Unable to convert to JSON", e);
		}

		
		mv.addObject("liveRequests", guiData);
		mv.addObject("liveListingsJSON", json);


		return mv;
	}
}
