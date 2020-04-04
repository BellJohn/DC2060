package com.reachout.gui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class EmptyHomeController {

	@GetMapping
	public ModelAndView initPageRedirect(HttpServletRequest request) {
		return new ModelAndView("/home");
	}
}
