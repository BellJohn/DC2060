/**
 * 
 */
package com.reachout.gui.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reachout.auth.SystemUser;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.im.Conversation;
import com.reachout.processors.InternalMessageHandler;

/**
 * @author John
 *
 */
@Controller
@RequestMapping("/viewMyMessages")
public class ViewMyMessagesController {

	public final Logger logger = LogManager.getLogger(ViewMyMessagesController.class);

	private static final String VIEW_NAME = "viewMyMessages";

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		}
		logger.debug("Reached viewMyMessages Controller");
		String previousUserSelected = request.getParameter("targetID");
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("user", username);
		List<Conversation> convos = getAllMyConversations(username);
		mv.addObject("conversations", convos);
		mv.addObject("previousUser", previousUserSelected);
		
		ObjectMapper mapper = new ObjectMapper(); 
		String jsonStr = "";
        try { 
        	// Convert conversations to JSON for the AJAX script
            jsonStr = mapper.writeValueAsString(convos); 
        } 
        catch (IOException e) { 
            logger.error("An error occured generating the JSON of the user's conversations",e); 
        } 
		response.setHeader("conversationString", jsonStr);
		return mv;
	}

	/**
	 * Collects all conversations from the InternalMessageHandler which this user
	 * has been involved in by username
	 * 
	 * @param username
	 * @return conversations containing this user
	 */
	private List<Conversation> getAllMyConversations(String username) {
		int userID = -1;
		try (HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl()) {
			userID = userDAO.getUserIdByUsername(username);
		}
		logger.debug(String.format("Fetching messages for user: %s", username));
		InternalMessageHandler imh = InternalMessageHandler.getInstance();
		Set<Integer> associatedUserIDS = imh.getAllUsersWithConversationWith(userID);

		List<Conversation> conversations = new ArrayList<>();
		for (Integer otherUserID : associatedUserIDS) {
			conversations.add(imh.getConversationBetween(userID, otherUserID));
		}
		logger.debug(String.format("Found {%s} conversations with user {%s}", conversations.size(), userID));
		return conversations;
	}
}
