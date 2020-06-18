package com.reachout.gui.controllers;

import java.util.ArrayList;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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


	//initialise the page with a list of pending requests.
	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		HibernateGroupDAOImpl groupDAO = new HibernateGroupDAOImpl();
		HibernateGroupMemberDAOImpl groupMemberDAO = new HibernateGroupMemberDAOImpl();

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
}
