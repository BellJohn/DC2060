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
		SystemUser sysUser = null;
		if (auth.getPrincipal() instanceof SystemUser) {
			sysUser = (SystemUser) auth.getPrincipal();
		} else {
			// This user should not be able to get here. Send them home
			logger.debug("UNAUTHORISED USER ACCESS TO PROFILE");
			return new ModelAndView("home");
		}
		logger.debug("Reached profile Controller");
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("user", sysUser.getUsername());
		return mv;
	}
}
