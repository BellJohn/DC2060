package com.reachout.gui.controllers;

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
import com.reachout.utils.ROUtils;


/**
 * Controller for editing/deleting a group.
 * 
 * @author Jessica
 * 
 */

@Controller
@RequestMapping("/editGroup")
public class EditGroupController {

	public final Logger logger = LogManager.getLogger(EditGroupController.class);

	private static final String VIEW_NAME = "editGroup";
	private static final String ERROR_STRING = "error";

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {

		// Make sure the user attempting to edit this actually owns the listing
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		}
		logger.debug("Reached editGroup Controller");

		String groupIDFromRequest = request.getParameter("groupID");
		if (StringUtils.isEmpty(groupIDFromRequest) || !ROUtils.isNumericInt(groupIDFromRequest)) {
			// Wont be able to find the group so return to the group's page
			return new ViewGroupsController().initPage(request);
		}

		// Safe to do this as it will have been checked in the condition above
		int groupID = Integer.parseInt(groupIDFromRequest);

		Group groupRequested;
		HibernateGroupDAOImpl groupDAO = new HibernateGroupDAOImpl();
		groupRequested = groupDAO.selectById(groupID);

		// if it's null, something went wrong so return to the groups page
		if (groupRequested == null) {
			return new ViewGroupsController().initPage(request);
		}

		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("group", groupRequested);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("user", username);
		return mv;

	}

	/**
	 * Update group information
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping()
	public ModelAndView postRequest(HttpServletRequest request) {
		String intent = request.getParameter("submit");
		if (StringUtils.isEmpty(intent)) {
			return new ViewOneGroupController().initPage(request);
		}

		if ("update".equals(intent)) {
			return handleUpdate(request);
		} else if ("delete".equals(intent)) {
			return handleDelete(request);
		} else {
			return new ViewOneGroupController().initPage(request);
		}
	}

	private ModelAndView handleUpdate(HttpServletRequest request) {
		logger.debug("Attempting listing update");
		String groupIDFromRequest = request.getParameter("groupID");
		String newName = request.getParameter("Name");
		String newDescription = request.getParameter("Description");
		String newPicture = request.getParameter("Picture");


		if (StringUtils.isEmpty(newName) || StringUtils.isEmpty(newDescription)) {
			logger.error("A field was empty in the update request");
			ModelAndView returnView = initPage(request);
			returnView.addObject(ERROR_STRING, "Something went wrong, try again and ensure all fields are populated");
			return returnView;
		}

		if (!ROUtils.isNumericInt(groupIDFromRequest)) {
			logger.error("Group ID was not a numeric in in the update request");
			return new ViewOneGroupController().initPage(request);
		}

		int groupID = Integer.parseInt(groupIDFromRequest);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();

		} else {
			username = (String) auth.getPrincipal();
		}
		int userId = -1;
		Group groupToUpdate;
		HibernateGroupDAOImpl groupDAO = new HibernateGroupDAOImpl();
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		HibernateGroupMemberDAOImpl groupMemberDAO = new HibernateGroupMemberDAOImpl();
		userId = userDAO.getUserIdByUsername(username);
		groupToUpdate = groupDAO.selectById(groupID);

		if ((groupMemberDAO.checkIfGroupMember(userId, groupID)).getUserStatus() == 0) {
			// This user should not be able to update this group, they are not admin. Something has gone
			// wrong, return them to the group with an error message
			logger.info("A user attempted to update a group which was not theirs");
			ModelAndView errorReturn = new ViewSingleListingController().initPage(request);
			errorReturn.addObject(ERROR_STRING, "You are not authorised to edit this group");
			return errorReturn;
		}

		// If we have made it this far, lets update the record.
		groupToUpdate.setName(newName);
		groupToUpdate.setDescription(newDescription);
		groupToUpdate.setPicture(newPicture);


		boolean changeSuccess = false;

		changeSuccess = groupDAO.update((Group) groupToUpdate);


		logger.info(String.format("Update success [%s]", changeSuccess));
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("postSent", "TRUE");
		mv.addObject("changeSuccess", changeSuccess);
		if (!changeSuccess) {
			mv.addObject(ERROR_STRING, "Something went wrong with the update. Try again");
		}
		mv.addObject("changeType", "updated");
		return mv;
	}

	private ModelAndView handleDelete(HttpServletRequest request) {
		logger.debug("Attempting group delete");
		String groupIDFromRequest = request.getParameter("groupID");

		if (!ROUtils.isNumericInt(groupIDFromRequest)) {
			logger.error("Group ID was not a numeric in in the update request");
			return new ViewGroupsController().initPage(request);
		}

		int groupID = Integer.parseInt(groupIDFromRequest);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username;
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();

		} else {
			username = (String) auth.getPrincipal();
		}

		Group groupToDelete;
		HibernateGroupDAOImpl groupDAO = new HibernateGroupDAOImpl();
		HibernateGroupMemberDAOImpl groupMemberDAO = new HibernateGroupMemberDAOImpl();
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		groupToDelete = groupDAO.selectById(groupID);
		int userId = userDAO.getUserIdByUsername(username);

		if (groupToDelete == null) {
			logger.error("No group found to delete");
			ModelAndView returnView = initPage(request);
			returnView.addObject(ERROR_STRING, "Something went wrong, try again.");
			return returnView;
		}

		if ((groupMemberDAO.checkIfGroupMember(userId, groupID)).getUserStatus() == 0) {
			// This user should not be able to update this group, they are not admin. Something has gone
			// wrong, return them to the group with an error message
			logger.info("A user attempted to update a group which was not theirs");
			ModelAndView errorReturn = new ViewSingleListingController().initPage(request);
			errorReturn.addObject(ERROR_STRING, "You are not authorised to edit this group");
			return errorReturn;
		}

		boolean changeSuccess = false;
		changeSuccess = groupDAO.delete( (Group) groupToDelete);
		changeSuccess = groupMemberDAO.groupDelete(groupToDelete.getId());


		logger.info(String.format("Delete success [%s]", changeSuccess));
		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);
		mv.addObject("postSent", "TRUE");
		mv.addObject("changeSuccess", changeSuccess);
		if (!changeSuccess) {
			mv.addObject(ERROR_STRING, "Something went wrong with the deletion. Try again");
		}
		mv.addObject("changeType", "deleted");
		return mv;
	}
}
