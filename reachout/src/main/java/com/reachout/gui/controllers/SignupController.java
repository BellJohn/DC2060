package com.reachout.gui.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.dao.HibernatePasswordDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.gui.validators.SignupValidator;
import com.reachout.gui.validators.ValidationResult;
import com.reachout.models.Password;
import com.reachout.models.User;
import com.reachout.processors.EmailHandler;

@Controller
@RequestMapping("/signup")
public class SignupController {

	public final Logger logger = LogManager.getLogger(SignupController.class);

	private static final String VIEW_NAME = "signup";

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		logger.debug("Reached SignUp Controller");

		// Test to see if the user is logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		for (GrantedAuthority ga : auth.getAuthorities()) {
			if (ga.getAuthority().equals("USER")) {
				logger.debug("User is already logged in, redirecting to their profile");
				return new ModelAndView("redirect:/profile");
			}
		}

		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		return mv;
	}

	/**
	 * Register a new user. Required parameters are firstname, lastname, username,
	 * email, dob,password,password_confirm
	 * 
	 * @param request
	 * @return
	 * @throws MessagingException
	 */
	@PostMapping
	public ModelAndView signup(HttpServletRequest request) {

		boolean saveUserSuccess = false;
		String username = request.getParameter("username");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String dob = request.getParameter("dob");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String passwordConfirm = request.getParameter("password_confirm");

		Map<String, String> userData = new HashMap<>();
		userData.put("firstName", firstName);
		userData.put("lastName", lastName);
		userData.put("username", username);
		userData.put("dob", dob);
		userData.put("email", email);
		userData.put("password", password);
		userData.put("passwordConfirm", passwordConfirm);

		ValidationResult result = SignupValidator.validateSignupForm(userData);
		// If we failed the validation, log some reasons why to the console for now
		if (!result.getOutcome()) {
			for (String s : result.getErrors().keySet()) {
				logger.error(String.format("%s : %s", s, result.getErrors().get(s)));
			}
		} else {
			// Otherwise, build a new User and populate the db
			User newUser = new User(firstName, lastName, username, email, dob);

			try {
				HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
				HibernatePasswordDAOImpl passwordDAO = new HibernatePasswordDAOImpl();
				saveUserSuccess = userDAO.save(newUser);
				if (saveUserSuccess) {
					Password newPassword = new Password();
					newPassword.setUserId(newUser.getId());
					newPassword.setHashedPasswordString(password);
					newPassword.setCreatedDate(System.currentTimeMillis());
					passwordDAO.save(newPassword);
				}
				if (!saveUserSuccess) {
					// Something went wrong building the user
					logger.error("Unable to save the user");
					result.setOutcome(false);
				}
			} catch (Exception e) {
				result.addError("Duplicate Username", "This username is already taken");
				result.setOutcome(false);
				logger.error("Unable to save the user: This username is already taken");
			}
		}
		// If we saved the new user, we should log them in
		if (saveUserSuccess) {
			try {
				request.login(username, password);

			} catch (ServletException e) {
				logger.error(e.getStackTrace());
			}
		}

		// send new email to the user to confirm they have signed up
		EmailHandler.generateAndSendEmail(email, "emails/signupEmail.html", "Welcome to ReachOut...", username);

		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("postSent", true);
		mv.addObject("postResult", result.getOutcome());
		mv.addObject("emailAddress", request.getParameter("email"));
		mv.addObject("validationErrors", result.getErrors());
		return mv;
	}
}