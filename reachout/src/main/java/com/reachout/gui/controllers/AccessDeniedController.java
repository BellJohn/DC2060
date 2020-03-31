package com.reachout.gui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/accessDenied")
public class AccessDeniedController {

	private static final String REDIRECT_VIEW_NAME = "home";
	
	@GetMapping
	@PostMapping
	public ModelAndView initPage(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView(REDIRECT_VIEW_NAME);
		mv.addObject("currentPage", REDIRECT_VIEW_NAME);
		mv.addObject("msg", "You need to sign in first");
		return mv;
	}
	
}
