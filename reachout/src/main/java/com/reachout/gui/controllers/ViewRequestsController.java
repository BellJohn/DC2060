package com.reachout.gui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.dao.HibernateRequestDAOImpl;
import com.reachout.auth.SystemUser;
import com.reachout.dao.HibernateUserDAOImpl;

@Controller
@RequestMapping("/viewRequests")
public class ViewRequestsController {
	public final Logger logger = LogManager.getLogger(ViewRequestsController.class);


	private Authentication auth;
	private HibernateUserDAOImpl userDAO;


	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		userDAO = new HibernateUserDAOImpl();

		logger.debug("Reached viewRequests Controller");
		ModelAndView mv = new ModelAndView("viewRequests");
		mv.addObject("currentPage", "viewRequests");


		auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username =  (String) auth.getPrincipal();
		}
		int userId = userDAO.getUserIdByUsername(username);



		try (HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl()) {
			mv.addObject("liveRequests", reqDAO.getAllRequestsForDisplay(userId));
		}
		return mv;
	}
}
