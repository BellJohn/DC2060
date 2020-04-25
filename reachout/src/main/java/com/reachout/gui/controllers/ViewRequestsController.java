package com.reachout.gui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.dao.HibernateRequestDAOImpl;

@Controller
@RequestMapping("/viewRequests")
public class ViewRequestsController {
	public final Logger logger = LogManager.getLogger(ViewRequestsController.class);

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {

		logger.debug("Reached viewRequests Controller");
		ModelAndView mv = new ModelAndView("viewRequests");
		mv.addObject("currentPage", "viewRequests");

		try (HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl()) {
			mv.addObject("liveRequests", reqDAO.getAllRequests());
		}
		return mv;
	}
}
