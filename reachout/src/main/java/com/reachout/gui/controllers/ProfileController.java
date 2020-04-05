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

import com.reachout.auth.SystemUser;

@Controller
@RequestMapping("/profile")
public class ProfileController {
	public final Logger logger = LogManager.getLogger(ProfileController.class);
	private static final String VIEW_NAME = "profile";

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		// Test to see if the user is logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username =  (String) auth.getPrincipal();
		}
		logger.debug("Reached profile Controller");
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("user", username);
		return mv;
	}
}
