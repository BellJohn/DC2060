package com.reachout.gui.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.auth.SystemUser;
import com.reachout.dao.HibernateGroupDAOImpl;
import com.reachout.dao.HibernateGroupListingDAOImpl;
import com.reachout.dao.HibernateGroupMemberDAOImpl;
import com.reachout.dao.HibernateRequestDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.Group;
import com.reachout.models.GroupListing;
import com.reachout.models.Request;
import com.reachout.models.Service;
import com.reachout.models.User;

@Controller
@RequestMapping("/createRequest")
public class RequestCreateController {

	public final Logger logger = LogManager.getLogger(RequestCreateController.class);

	private static final String VIEW_NAME = "createRequest";
	private int userId;

	/**
	 * Arrival on page will trigger this
	 * 
	 * @param request
	 * @return The create request view
	 */
	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {

		logger.debug("Reached Request Create Controller");

		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		HibernateGroupMemberDAOImpl groupMemberDAO = new HibernateGroupMemberDAOImpl();
		List<Group> userGroups = null;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username; 
		if (auth.getPrincipal() instanceof SystemUser) {
			username = ((SystemUser) auth.getPrincipal()).getUsername();
		} else {
			username = (String) auth.getPrincipal();
		}
		userId = userDAO.getUserIdByUsername(username);


		ModelAndView mv = new ModelAndView(VIEW_NAME);
		mv.addObject("currentPage", VIEW_NAME);

		if ( (userGroups = groupMemberDAO.getUserGroups(userId)) != null) {
			ArrayList<String> groupNames = new ArrayList<String>();

			for (Group g : userGroups) {
				groupNames.add(g.getName());
				logger.debug("Groups for the user are : " + groupNames.toString());
				mv.addObject("userGroups", groupNames);
			}
		}

		return mv;
	}

	/**
	 * Submission of the create new request form will trigger this
	 * 
	 * @param title
	 * @param description
	 * @param county
	 * @param city
	 * @param request
	 * @param priority
	 * @param visibility
	 * @return View representing success or failure
	 */
	@PostMapping
	public ModelAndView submitForm(@RequestParam(name = "reqTitle") String title,
			@RequestParam(name = "reqDesc", required = false) String description,
			@RequestParam(name = "reqCounty") String county,
			@RequestParam(name = "reqPriority") String priority,
			@RequestParam(name = "group", required = false) String groupVisibility,
			@RequestParam(name = "reqCity", required = false) String city, HttpServletRequest request) {

		boolean createSuccess = false;
		int publicVisibility = 0;
		Request newRequest = new Request();
		
		//check if user is in any groups, other wise make request public
		HibernateGroupMemberDAOImpl groupMemDAO = new HibernateGroupMemberDAOImpl();
		if (groupMemDAO.getUserGroups(userId).isEmpty()) {
			publicVisibility = 1;
			newRequest = new Request(title, description, county, city, userId, priority, publicVisibility);
			logger.info("Public request created");
		}
		else {
			List<String> visibility= Arrays.asList(request.getParameterValues("reqVisibility")) ;
			if (visibility.contains("public")){
				publicVisibility = 1;
				newRequest = new Request(title, description, county, city, userId, priority, publicVisibility);
				logger.info("Public request created");
			}
			//check if a group request has been made

			if(visibility.contains("group")) {
				newRequest = new Request(title, description, county, city, userId, priority, publicVisibility);
				logger.info("Group service created");
				HibernateGroupDAOImpl groupDAO = new HibernateGroupDAOImpl();
				HibernateGroupListingDAOImpl glDAO = new HibernateGroupListingDAOImpl();
				try {
					Group group = groupDAO.selectByName(groupVisibility);
					GroupListing gl = new GroupListing(group.getId(), newRequest.getId());
					createSuccess = glDAO.save(gl);
				}
				catch (Exception e) {
					logger.error("Could not find group: " + groupVisibility) ;
				}
				if(createSuccess = false) {
					logger.error("Group listing was not added");
				}
			}
		}

			HibernateRequestDAOImpl requestDAO = new HibernateRequestDAOImpl();
			createSuccess = requestDAO.save(newRequest);

			ModelAndView mv = new ModelAndView(VIEW_NAME);
			if (createSuccess) {
				logger.debug(String.format("Built new request as %s", newRequest.toString()));
			}
			mv.addObject("postSent", true);
			mv.addObject("createSuccess", createSuccess);
			mv.addObject("currentPage", VIEW_NAME);
			return mv;
		}
	}
