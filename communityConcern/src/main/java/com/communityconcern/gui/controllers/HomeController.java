package com.communityconcern.gui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/home")
public class HomeController {
	@GetMapping
	public ModelAndView initPage(ModelAndView model, HttpServletRequest request) {
		System.out.println("Reached controller");
		ModelAndView mv = new ModelAndView("home");
		mv.addObject("homePage","http://localhost:8080/CommunityConcern/home");
		
		
		return mv;
	}
}
