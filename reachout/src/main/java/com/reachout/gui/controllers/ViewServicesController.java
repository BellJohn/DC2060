package com.reachout.gui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.dao.HibernateServiceDAOImpl;

@Controller
@RequestMapping("/viewServices")
public class ViewServicesController {
	public final Logger logger = LogManager.getLogger(ViewServicesController.class);

	@GetMapping
	public ModelAndView initPage(HttpServletRequest request) {

		logger.debug("Reached viewServices Controller");
		ModelAndView mv = new ModelAndView("viewServices");
		mv.addObject("currentPage", "viewServices");

		try (HibernateServiceDAOImpl serDAO = new HibernateServiceDAOImpl()) {
			mv.addObject("liveServices", serDAO.getAllServices());
		}
		return mv;
	}
}
