/**
 * 
 */
package com.reachout.processors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.reachout.dao.HibernateGroupDAOImpl;
import com.reachout.dao.HibernateGroupMemberDAOImpl;
import com.reachout.models.Group;
import com.reachout.models.GroupMember;
import com.reachout.models.exceptions.ListingTypeNotMatchedException;

/**
 * @author Jess
 *
 */
public class GroupHandler {

	private static GroupHandler groupHandlerInstance;
	public final Logger logger = LogManager.getLogger(GroupHandler.class);

	private GroupHandler() {
	}

	public static GroupHandler getInstance() {
		if (groupHandlerInstance == null) {
			groupHandlerInstance = new GroupHandler();
		}
		return groupHandlerInstance;
	}

	/**
	 * Method for handling the logic of requesting to join a group
	 * 
	 * @param userId
	 * @param groupID
	 * @return
	 * @throws ListingTypeNotMatchedException
	 */
	public boolean requestToJoin(Integer userId, Integer groupId) {
		Group groupFound = null;
		HibernateGroupDAOImpl groupDAO = new HibernateGroupDAOImpl();
		groupFound = groupDAO.selectById(groupId);


		if (groupFound == null) {
			logger.error(String.format("Unable to find a group with id {%s}", groupId));
			return false;
		}

		// check if user is already in group
		HibernateGroupMemberDAOImpl groupMem = new HibernateGroupMemberDAOImpl();

		if( (groupMem.checkIfGroupMember(userId, groupId)) != null) {
			logger.error(String.format(
					"User has already requested to join the group"));
			return false;
		}


		// If we reached here, this user is valid to accept this request and we are
		// confident we have appropriate objects for both
		boolean success = false;
		GroupMember pendingGroupMember = new GroupMember(groupId, userId, 0);
		success = groupMem.save(pendingGroupMember);

		return success;

	}

}
