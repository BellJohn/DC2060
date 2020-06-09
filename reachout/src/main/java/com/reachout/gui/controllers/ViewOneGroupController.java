package com.reachout.gui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.auth.SystemUser;
import com.reachout.dao.HibernateGroupDAOImpl;
import com.reachout.dao.HibernateGroupMemberDAOImpl;
import com.reachout.dao.HibernateRequestDAOImpl;
import com.reachout.dao.HibernateServiceDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.ListingStatus;
import com.reachout.models.Group;
import com.reachout.models.Listing;
import com.reachout.models.ListingType;
import com.reachout.models.User;
import com.reachout.models.exceptions.ListingTypeNotMatchedException;
import com.reachout.processors.ListingHandler;

@Controller
@RequestMapping("/viewOneGroup")
public class ViewOneGroupController {
	public final Logger logger = LogManager.getLogger(ViewOneGroupController.class);

	private static final String VIEW_NAME = "viewOneGroup";

	/**
	 * Method for populating the page with details of the group and all requests that
	 * have been made within the group.
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping
	public ModelAndView initPage(HttpServletRequest request) {
		logger.debug("Reached viewOneGroup Controller");
					
		Integer groupID = null;
		String username = null;
		try {
			groupID = Integer.parseInt(request.getParameter("groupID"));
			username = request.getParameter("username");
		} catch (NumberFormatException e) {
			logger.error("Request contained non-numeric groupID, returning user to error page");
			return getErrorPage();
		}

		Group group = getGroupDetails(groupID);
		if (group == null) {
			logger.error("No group found with ID" + groupID);
			return getErrorPage();
		}

		logger.debug(
				String.format("Found group " + group.getName() + group.getDescription()));


		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		int userID = userDAO.getUserIdByUsername(username);

		//To find out whether user is admin and if they should be able to delete/authorise requests

		boolean isAdmin = checkIfAdmin(userID, groupID);
		boolean enableDeleteButton = isAdmin;
		boolean enableAuthoriseRequests = isAdmin;

		//TO DO - display requests for this group
		
		
		
		
		
		
		
		
		
		ModelAndView mv;
		mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("ListingObj", group);
		mv.addObject("enableDeleteButton", enableDeleteButton);
		mv.addObject("enableAuthoriseRequests", enableAuthoriseRequests);


		return mv;
	}

	private ModelAndView getErrorPage() {
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("Something went wrong, please try again");
		return mv;
	}


	/**
	 * Finds the group requested based on the groupID provided from the previous page
	 * 
	 * @param groupID
	 * @return the group found
	 */
	private Group getGroupDetails(int groupID) {
		Group result;
		HibernateGroupDAOImpl groupDAO = new HibernateGroupDAOImpl();
		result = groupDAO.selectById(groupID);

		return result;
	}


	/**
	 * Finds out whether the user had admin privileges for the selected group
	 * 
	 * @param groupID
	 * @param userID
	 * @return boolean for if user is admin
	 */
	private boolean checkIfAdmin(int userId, int groupId) {
		HibernateGroupMemberDAOImpl groupMemberDAO = new HibernateGroupMemberDAOImpl();
		if ( groupMemberDAO.checkIfAdmin(userId, groupId) == 1 ){
			return true;
		}
		else return false;

	}
}
