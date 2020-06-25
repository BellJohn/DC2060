package com.reachout.gui.controllers;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.dao.HibernatePasswordDAOImpl;
import com.reachout.dao.HibernatePasswordResetDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.Password;
import com.reachout.models.PasswordReset;
import com.reachout.processors.EmailHandler;
import com.reachout.utils.CodeGenerator;

@Controller
@RequestMapping("/resetPassword")
public class ResetPasswordController {
	public final Logger logger = LogManager.getLogger(ResetPasswordController.class);
	private static final String VIEW_NAME = "resetPassword";

	/**
	 * When the user visits the page to reset their email, or when visiting the page with their reset code
	 */
	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		logger.debug("Reached ResetPassword Controller");
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("submit", false);

		// The user has a code to reset their password
		if(request.getParameterMap().containsKey("uid")) {
			String uid = request.getParameter("uid");

			HibernatePasswordResetDAOImpl prDAO = new HibernatePasswordResetDAOImpl();
			if(prDAO.checkIfCodeValid(uid)) {
				mv.addObject("codeValid", true);
				mv.addObject("userId", prDAO.selectByCode(uid).getUserId());
			}
			else {
				mv.addObject("codeValid", false);
			}
		}
		return mv;
	}

	/**
	 * When the user enters their email and presses Reset, or the user enters their new password and hits save
	 */
	@PostMapping
	public ModelAndView postPage(HttpServletRequest request) throws MessagingException {
		logger.debug("Reached ResetPassword Controller");
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("submit", true);

		// Check if the user has submitted their email for a reset
		if(request.getParameterMap().containsKey("email")) {
			// Check if the user exists
			HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
			if (userDAO.getUserByEmail(request.getParameter("email")) != null){
				mv.addObject("reset", false);

				// Get the users email and ID, and generate them a new reset code
				String email = request.getParameter("email");
				CodeGenerator cg = new CodeGenerator();
				String code = cg.getUniqueCode();
				int userId = userDAO.getUserByEmail(email).getId();
				
				// Create a new password reset object and save it to the database
				PasswordReset newPasswordReset = new PasswordReset(userId, code);
				boolean createSuccess = false;
				HibernatePasswordResetDAOImpl prDAO = new HibernatePasswordResetDAOImpl();
				createSuccess = prDAO.save(newPasswordReset);
				if (createSuccess) {
					logger.debug(String.format("Built new request as %s", newPasswordReset.toString()));
				}

				// Build the users password reset URL and email it to them
				String url = "http://reachout.space/resetPassword?uid=" + code;
				EmailHandler.generateAndSendPasswordResetEmail(email, "emails/passwordResetEmail.html", "ReachOut | Password Reset", url);
			}
		}
		else {
			// The user has just submitted their new password
			mv.addObject("reset", true);
			String errors = null;

			HibernatePasswordResetDAOImpl prDAO = new HibernatePasswordResetDAOImpl();
			String code = request.getParameter("uid");
			int correctUserId = prDAO.selectByCode(code).getUserId();
			String actualUserIdString = request.getParameter("userId");
			int actualUserId = Integer.parseInt(actualUserIdString);

			// Verify that the password is being changed for the correct user
			if(actualUserId == correctUserId) {
				String password = request.getParameter("password");
				String passwordConfirm = request.getParameter("password_confirm");

				// Make sure that the passwords match
				if(password.equals(passwordConfirm)) {
					HibernatePasswordDAOImpl passwordDAO = new HibernatePasswordDAOImpl();
					
					// Save the new password
					try {
						Password newPassword = new Password();
						newPassword.setUserId(correctUserId);
						newPassword.setHashedPasswordString(password);
						newPassword.setCreatedDate(System.currentTimeMillis());
						passwordDAO.save(newPassword);
					} catch (Exception e) {
						errors = "An error occured. Please try again.";
						logger.error("Unable to update password. Please retry reset.");
					}
					
					// If all went well, delete the password reset code so it cannot be used again
					if(errors == null) {
						prDAO.deletePasswordResetByCode(code);
					}
				}
				else {
					errors = "Entered passwords didn't match. Please try again.";
				}
			} 
			else {
				// User is trying to change someone elses password
				errors = "An error occured. Please try again.";
			}
			mv.addObject("errors", errors);
		}
		return mv;
	}

}