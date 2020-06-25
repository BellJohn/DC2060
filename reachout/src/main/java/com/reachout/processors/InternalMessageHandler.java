/**
 * 
 */
package com.reachout.processors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import com.reachout.dao.HibernateInternalMessageDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.im.Conversation;
import com.reachout.models.InternalMessage;

/**
 * @author John
 *
 */
public class InternalMessageHandler {

	private static InternalMessageHandler internalMessageHandlerInstance;
	private final Logger logger = LogManager.getLogger(InternalMessage.class);

	private InternalMessageHandler() {
	}

	public static InternalMessageHandler getInstance() {
		if (internalMessageHandlerInstance == null) {
			internalMessageHandlerInstance = new InternalMessageHandler();
		}
		return internalMessageHandlerInstance;
	}

	/**
	 * Orchestrates the validity checks before storing a NEW message in the database
	 * 
	 * @param origin
	 * @param target
	 * @param message
	 * @return
	 */
	public boolean createAndStoreMessage(int origin, int target, String message) {
		// If we have any invalid data then return false
		if (!isDataValid(origin, target, message)) {
			return false;
		}

		InternalMessage im = new InternalMessage(origin, target, message);
		HibernateInternalMessageDAOImpl imDAO = new HibernateInternalMessageDAOImpl();
		return imDAO.save(im);

	}

	/**
	 * Checks to see if the user IDs passed correspond to existing users and that
	 * the message is neither null nor empty
	 * 
	 * @param origin
	 * @param target
	 * @param message
	 * @return true if the data is ALL valid, false for any issues
	 */
	private boolean isDataValid(int origin, int target, String message) {
		// If one or the other user doesn't exist or the message is empty, the data is
		// invalid
		return userExists(origin) && userExists(target) && !StringUtils.isEmpty(message);
	}

	/**
	 * Checks to see if the users exist in the database
	 * 
	 * @param uid
	 * @return true if they exist, false otherwise
	 */
	private boolean userExists(int uid) {
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		return (userDAO.selectByID(uid) != null);
	}

	/**
	 * Orchestration of conversation creation between two users including user
	 * validation, message content is ordered oldest to newest
	 * 
	 * @param userBrowsing
	 * @param userOther
	 * @return populated Conversation or null if no conversation exists or one or
	 *         other user does not exist
	 */
	public Conversation getConversationBetween(int userBrowsing, int userOther) {
		if (!userExists(userBrowsing) || !userExists(userOther)) {
			return null;
		}

		List<InternalMessage> allIMs = new ArrayList<>();
		HibernateInternalMessageDAOImpl imDAO = new HibernateInternalMessageDAOImpl();
		allIMs = imDAO.getAllMessagesBetween(userBrowsing, userOther);

		// Check to see if we found any messages between the two users
		if (allIMs.isEmpty()) {
			return null;
		}
		String otherUserName;
		String browsingUserName;
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		otherUserName = userDAO.selectByID(userOther).getUsername();
		browsingUserName = userDAO.selectByID(userBrowsing).getUsername();

		// If we are here, we have messages to put together into a conversation
		Conversation conversation = new Conversation(userBrowsing, userOther, allIMs, otherUserName, browsingUserName);
		logger.debug(String.format("Created conversation between users {%s} : {%s}", userBrowsing, userOther));
		return conversation;
	}

	/**
	 * Returns a set containing all the other users this user has been involved in a
	 * conversation with
	 * 
	 * @param userID
	 * @return set of Integers representing the UserIDs
	 */
	public Set<Integer> getAllUsersWithConversationWith(int userID) {
		Set<Integer> result = new HashSet<>();
		HibernateInternalMessageDAOImpl imDAO = new HibernateInternalMessageDAOImpl();
		result = imDAO.getAllUsersConversingWith(userID);

		logger.debug(String.format("Users found associated to user {%s} : {%s}", userID, result));
		return result;
	}

	/**
	 * Orchestration of conversation creation between two users including user
	 * validation, message content is ordered oldest to newest
	 * 
	 * @param userBrowsing
	 * @param userOther
	 * @return populated Conversation or null if no conversation exists or one or
	 *         other user does not exist
	 */
	public boolean createAndStoreMessage(String userBrowsing, String userOther, String message) {
		int origin;
		int target;

		// We need user IDs for the subsequent call to createAndStoreMessage
		// If we have numeric IDs then we can make use of that straight away
		// If we fail the parseInt, it must be alphanumeric so search for matching
		// usernames
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		try {
			origin = Integer.parseInt(userBrowsing);
		} catch (NumberFormatException e) {
			origin = userDAO.getUserIdByUsername(userBrowsing);
		}
		try {
			target = Integer.parseInt(userOther);
		} catch (NumberFormatException e) {
			target = userDAO.getUserIdByUsername(userOther);
		}
		if (target == -1 || origin == -1) {
			logger.debug("Unknown error to send message to");
			return false;
		}
		return createAndStoreMessage(origin, target, message);

	}
}
