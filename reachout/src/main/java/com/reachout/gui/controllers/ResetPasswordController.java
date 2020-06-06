package com.reachout.gui.controllers;

import javax.servlet.http.HttpServletRequest;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.utils.CodeGenerator;
import com.reachout.models.PasswordReset;
import com.reachout.models.Password;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.dao.HibernatePasswordResetDAOImpl;
import com.reachout.dao.HibernatePasswordDAOImpl;
import com.reachout.processors.EmailHandler;


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
				String url = "http://localhost:8080/ReachOut/resetPassword?uid=" + code; // THIS NEEDS TO BE CHANGED TO BE THE LIVE URL RATHER THAN THE LOCAL ONE!!!!!!<><><><><><><>
				EmailHandler.generateAndSendPasswordResetEmail(email, "emails/passwordResetEmail.html", "ReachOut | Password Reset", url);
			}
		}
		else {
			// The user has just submitted their new password
			mv.addObject("reset", true);

			HibernatePasswordDAOImpl passwordDAO = new HibernatePasswordDAOImpl();

			// ---TODO ENSURE THIS IS CHECKED AGAINST THE DATABASE FOR THE ID THE CODE MATCHES AGAINST
			String userIdString = request.getParameter("userId");
			int userId = Integer.parseInt(userIdString);
			// ---TODO MAKE SURE THAT THE INPUTTED PASSWORDS MATCH
			String password = request.getParameter("password");
			
			try {
				Password newPassword = new Password();
				newPassword.setUserId(userId);
				newPassword.setHashedPasswordString(password);
				newPassword.setCreatedDate(System.currentTimeMillis());
				passwordDAO.save(newPassword);
			} catch (Exception e) {
				logger.error("Unable to save the user: This username is already taken");
			}

			// --TODO DELETE A PASSWORD RESET ENTRY SO PASSWORD CANNOT BE RESET WITH IT MULTIPLE TIMES

		}

		return mv;
	}

}