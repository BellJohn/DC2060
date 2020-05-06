/**
 * 
 */
package com.reachout.gui.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.reachout.auth.SystemUser;
import com.reachout.processors.InternalMessageHandler;

/**
 * Page non-specific web facing entry point for users to send messages
 * internally (within ReachOut) to other users. Example usage in AJAX function
 * on viewListing.jsp
 * 
 * @author John
 *
 */
@Controller
@RequestMapping("/SendUserMessage")
public class SendUserMessage {
	public final Logger logger = LogManager.getLogger(SendUserMessage.class);

	private static final long serialVersionUID = 1249025579619645855L;

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Entry point for a message attempt. </br>
	 * Request must contain a "message" string for this to be successful. </br>
	 * Successful sending results in the string "SUCCESS" to be returned to the
	 * calling code
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@PostMapping
	public void sendMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String message = request.getParameter("message");
		String userTarget = request.getParameter("targetID");
		logger.debug("Reached SendUserMessage Controller");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		}
		logger.debug("Reached viewMyMessages Controller");
		String outcome = "FAILURE";
		if (message == null || "".equals(message)) {
			logger.info("No data Found");
		} else {
			logger.info(String.format("Creating message from user {%s} to user {%s}", username, userTarget));
			InternalMessageHandler imh = InternalMessageHandler.getInstance();
			if (imh.createAndStoreMessage(username, userTarget, message)) {
				outcome = "SUCCESS";
			}
		}
		// On success, write SENT to response for AJAX to process
		response.setContentType("text/plain");
		response.getWriter().write(outcome);
	}

}
