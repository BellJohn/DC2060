package com.reachout.gui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.dao.HibernateRequestDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.Request;
import com.reachout.models.User;

@Controller
@RequestMapping("/createRequest")
public class RequestCreateController {

	public final Logger logger = LogManager.getLogger(RequestCreateController.class);

	private static final String VIEW_NAME = "createRequest";

	/**
	 * Arrival on page will trigger this
	 * 
	 * @param request
	 * @return The create request view
	 */
	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {

		logger.debug("Reached Request Create Controller");
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		return mv;
	}

	/**
	 * Submission of the create new request form will trigger this
	 * 
	 * @param title
	 * @param description
	 * @param county
	 * @param city
	 * @param request
	 * @return View representing success or failure
	 */
	@PostMapping
	public ModelAndView submitForm(@RequestParam(name = "reqTitle") String title,
			@RequestParam(name = "reqDesc", required = false) String description,
			@RequestParam(name = "reqCounty") String county,
			@RequestParam(name = "reqPriority") String priority,
			@RequestParam(name = "reqCity", required = false) String city, HttpServletRequest request) {

		// TODO Check to see if the content is valid

		int userId = 0;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof UsernamePasswordAuthenticationToken) {
			String username = ((UsernamePasswordAuthenticationToken) auth).getName();
			try (HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl()) {
				User user = userDAO.selectUser(username);
				if (user != null) {
					userId = user.getId();
				}
			}
		}
		// Build a new request which will be given the status of "new"
		Request newRequest = new Request(title, description, county, city, userId, priority);
		boolean createSuccess = false;
		try (HibernateRequestDAOImpl requestDAO = new HibernateRequestDAOImpl()) {
			createSuccess = requestDAO.save(newRequest);

		}
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		if (createSuccess) {
			logger.debug(String.format("Built new request as %s", newRequest.toString()));
		}
		mv.addObject("postSent", true);
		mv.addObject("createSuccess", createSuccess);
		mv.addObject("currentPage", VIEW_NAME);
		return mv;
	}
}
