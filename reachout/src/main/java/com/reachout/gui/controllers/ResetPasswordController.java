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

import com.reachout.passwordreset.PasswordResetManager;
import com.reachout.models.PasswordReset;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.dao.HibernatePasswordResetDAOImpl;
import com.reachout.processors.EmailHandler;


@Controller
@RequestMapping("/resetPassword")
public class ResetPasswordController {

	public final Logger logger = LogManager.getLogger(ResetPasswordController.class);
	private static final String VIEW_NAME = "resetPassword";

	// User clicks forgotten password, or loads page with a uid
	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		logger.debug("Reached ResetPassword Controller");
		
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("submit", false);

		if(request.getParameterMap().containsKey("uid")) {
			logger.debug("Request has registered a UID");    //REMOVE
			String uid = request.getParameter("uid");
			logger.debug("UID is: " + uid);                  //REMOVE
			mv.addObject("uid", uid);
		}
		else {												 //REMOVE
			logger.debug("Request has no UID registered");   //REMOVE
		}													 //REMOVE

		return mv;
	}

	/**
	 * When the user enters their email and presses Reset, this method is called
	 */
	@PostMapping
	public ModelAndView postPage(HttpServletRequest request) throws MessagingException {
		logger.debug("Reached ResetPassword Controller");
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("submit", true);

		// Check if the user exists
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		if (userDAO.getUserExistsByEmail(request.getParameter("email"))){

			// Get the users email and ID, and generate them a new reset code
			String email = request.getParameter("email");
			PasswordResetManager pr = new PasswordResetManager();
			String code = pr.getUniqueCode();
			int userId = userDAO.getUserByEmail(email).getId();
			
			// Create a new password reset object and save it to the database
			PasswordReset newPasswordReset = new PasswordReset(userId, code);
			boolean createSuccess = false;
			HibernatePasswordResetDAOImpl prDAO = new HibernatePasswordResetDAOImpl();
			createSuccess = prDAO.save(newPasswordReset);
			if (createSuccess) {
				logger.debug(String.format("Built new request as %s", newPasswordReset.toString()));
			}

			// Create the users password reset URL and email it to them
			String url = "http://localhost:8080/ReachOut/resetPassword?uid=" + code; // THIS NEEDS TO BE CHANGED TO BE THE LIVE URL RATHER THAN THE LOCAL ONE!!!!!!<><><><><><><>
			EmailHandler.generateAndSendPasswordResetEmail(email, "emails/passwordResetEmail.html", "ReachOut | Password Reset", url);
		}
		return mv;
	}

}