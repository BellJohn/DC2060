package com.reachout.gui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/login")
public class LoginController {

	public final Logger logger = LogManager.getLogger(LoginController.class);

	private static final String VIEW_NAME = "login";

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {

		// Test to see if the user is logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			System.out.println("auth not auth token");
		} else {
			System.out.println("princ:" + auth.getPrincipal());
			System.out.println("cred: " + auth.getCredentials());
		}
		logger.debug("Reached Login Controller");
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		return mv;
	}

}