package com.reachout.gui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/home")
public class HomeController {
	public final Logger logger = LogManager.getLogger(HomeController.class);

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		
		logger.debug("Reached Home Controller");
		return new ModelAndView("redirect:/profile");
	}
}
