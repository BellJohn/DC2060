package com.reachout.gui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.gui.validators.SignupValidator;
import com.reachout.gui.validators.ValidationResult;
import com.reachout.models.User;

@Controller
@RequestMapping("/signup")
public class SignupController {

	public final Logger logger = LogManager.getLogger(SignupController.class);

	private static final String VIEW_NAME = "signup";

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {

		// Test to see if the user is logged in
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		if (!(auth instanceof AnonymousAuthenticationToken)) {
//		        return new ModelAndView("/accessDenied");
//		}
		logger.debug("Reached SignUp Controller");
		ModelAndView mv = new ModelAndView(VIEW_NAME);
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

		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String passwordConfirm = request.getParameter("password_confirm");
		// TODO This is pretty rubbish was of collating the data, going to be a pain to
		// expand later. Maybe build custom newUser data type?
		String[] data = new String[4];
		data[0] = username;
		data[1] = email;
		data[2] = password;
		data[3] = passwordConfirm;
		ValidationResult result = SignupValidator.validateSignupForm(data);
		// If we failed the validation, log some reasons why to the console for now
		if (!result.getOutcome()) {
			for (String s : result.getErrors().keySet()) {
				logger.error(String.format("%s : %s", s, result.getErrors().get(s)));
			}
		} else {
			// Otherwise, build a new User and populate the db
			User newUser = new User(username, email, password);
			HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
			try {
				if (!userDAO.saveUser(newUser)) {
					// Something went wrong building the user
					logger.error("Unable to save the user");
				}
			} catch (Exception e) {
				result.addError("Duplicate Username", "This username is already taken");
				logger.error("Unable to save the user", e);

			}
		}
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("postSent", true);
		mv.addObject("postResult", result.getOutcome());
		mv.addObject("emailAddress", request.getParameter("email"));
		mv.addObject("validationErrors", result.getErrors());
		return mv;
	}

}
