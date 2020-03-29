package com.reachout.gui.controllers;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.gui.validators.SignupValidator;
import com.reachout.gui.validators.ValidationResult;

@Controller
@RequestMapping("/signup")
public class SignupController {

	private static final String VIEW_NAME = "signup";

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		System.out.println("Reached SignUp Controller");
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("homePage", "http://localhost:8080/CommunityConcern/home");
		mv.addObject("currentPage", VIEW_NAME);
		return mv;
	}

	/**
	 * Register a new user. Required parameters are
	 * username,email,password,password_confirm
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping
	public ModelAndView signup(HttpServletRequest request) {
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String param = paramNames.nextElement();
			System.out.println(param + " : " + request.getParameter(param));
		}
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String passwordConfirm = request.getParameter("password_confirm");

		String[] data = new String[4];
		data[0] = username;
		data[1] = email;
		data[2] = password;
		data[3] = passwordConfirm;
		ValidationResult result = SignupValidator.validateSignupForm(data);
		if (!result.getOutcome()) {
			for (String s : result.getErrors().keySet()) {
				System.out.println(s + " : " + result.getErrors().get(s));
			}
		}
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("homePage", "http://localhost:8080/CommunityConcern/home");
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("postSent", true);
		mv.addObject("postResult", result.getOutcome());
		mv.addObject("emailAddress", request.getParameter("email"));
		mv.addObject("validationErrors", result.getErrors());
		return mv;
	}

}
