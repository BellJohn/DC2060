package com.reachout.gui.controllers;

import java.util.ArrayList;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.auth.SystemUser;
import com.reachout.dao.HibernateGroupDAOImpl;
import com.reachout.dao.HibernateGroupMemberDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.Group;
import com.reachout.models.GroupMember;
import com.reachout.models.GroupRequestGUIWrapper;

import com.reachout.models.User;


/**
 * 
 * Controller to view pending user requests to join a group 
 * and for an admin to approve/deny the request.
 * @author Jessica
 *
 */

@Controller
@RequestMapping("/pendingGroupRequests")
public class GroupRequestsController {
	public final Logger logger = LogManager.getLogger(GroupRequestsController.class);
	private HibernateGroupMemberDAOImpl groupMemberDAO;


	//initialise the page with a list of pending requests.
	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		HibernateGroupDAOImpl groupDAO = new HibernateGroupDAOImpl();
		groupMemberDAO = new HibernateGroupMemberDAOImpl();

		logger.debug("Reached pending group requests Controller");

		int groupID = Integer.parseInt(request.getParameter("groupID"));

		List<GroupMember> pendingUsers = groupMemberDAO.getPendingMembers(groupID);
		List<GroupRequestGUIWrapper> pendingRequests = new ArrayList<>();

		for(GroupMember gm : pendingUsers) {
			User user = userDAO.selectByID(gm.getUserId());
			Group group = groupDAO.selectById(groupID);	
			GroupRequestGUIWrapper reqGui = new GroupRequestGUIWrapper(group, user, gm.getId());
			pendingRequests.add(reqGui);
		}

		ModelAndView mv = new ModelAndView("pendingGroupRequests");
		mv.addObject("currentPage", "pendingGroupRequests");
		mv.addObject("pendingRequests", pendingRequests);


		return mv;
	}
	
	
	@PostMapping
	public ModelAndView approveUser(HttpServletRequest request) {
		
		String intent = request.getParameter("submit");
		if (StringUtils.isEmpty(intent)) {
			return new ProfilePageController().initPage(request);
		}

		if ("accept".equals(intent)) {
			return handleAccept(request);
		} else if ("reject".equals(intent)) {
			return handleReject(request);
		} else {
			return new GroupRequestsController().initPage(request);
		}		
	}
	
	
	public ModelAndView handleAccept(HttpServletRequest request) {
		
		int groupMemberID = Integer.parseInt(request.getParameter("groupMemberID"));
		boolean success = false;
		
		GroupMember gm = groupMemberDAO.selectById(groupMemberID);
		//set user status to 1 so they are accepted as a member into the group
		gm.setUserStatus(1);
		
		try {
		success = groupMemberDAO.update(gm);
		logger.debug("Group membership updated, has been accepted into group id: ." + gm.getGroupId());
		}
		catch (Exception e) {
			logger.error("Unable to update group membership");
		}
		
		return initPage(request);
	}
	
	
	
	
	public ModelAndView handleReject(HttpServletRequest request) {
		
		int groupMemberID = Integer.parseInt(request.getParameter("groupMemberID"));
		
		boolean success = false;
		
		try {
		success = groupMemberDAO.delete(groupMemberDAO.selectById(groupMemberID));
		logger.debug("User request rejected and delete from database for groupMemberID" + groupMemberID);
		}
		catch (Exception e) {
			logger.error("Unable to delete request");
		}
		
		return initPage(request);
	}
}
