package com.reachout.gui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.dao.HibernateRequestDAOImpl;
import com.reachout.dao.HibernateServiceDAOImpl;
import com.reachout.models.Listing;
import com.reachout.models.ListingType;

@Controller
@RequestMapping("/viewListing")
public class ViewSingleListingController {
	public final Logger logger = LogManager.getLogger(ViewSingleListingController.class);

	@PostMapping
	public ModelAndView initPage(HttpServletRequest request) {
		logger.debug("Reached viewSingleListing Controller");
		ModelAndView mv;
		String returnURL = request.getHeader("referer");
		if (StringUtils.isEmpty(returnURL)) {
			returnURL = "home";
		}

		int listingID = Integer.parseInt(request.getParameter("listingID"));
		System.out.println("LISTING ID: " + listingID);
		String listingType = request.getParameter("listingType");
		System.out.println("LISTING TYPE: " + listingType);

		Listing result;
		if (ListingType.REQUEST.getName().equalsIgnoreCase(listingType)) {
			try (HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl()) {
				result = reqDAO.selectById(listingID);
			}
		} else if (ListingType.SERVICE.getName().equalsIgnoreCase(listingType)) {
			try (HibernateServiceDAOImpl serDAO = new HibernateServiceDAOImpl()) {
				result = serDAO.selectById(listingID);
			}
		} else {
			logger.error("Received neither Request nor Service specifier, returning error page");
			return getErrorPage();
		}

		if (result == null) {
			logger.error("Found neither Request nor Service with provided ID");
			return getErrorPage();
		}

		mv = new ModelAndView("viewListing");
		mv.addObject("currentPage", "viewListing");
		mv.addObject("ListingObj", result);

		try (HibernateServiceDAOImpl serDAO = new HibernateServiceDAOImpl()) {
			mv.addObject("liveServices", serDAO.getAllServices());
		}
		return mv;
	}

	private ModelAndView getErrorPage() {
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("Something went wrong, please try again");
		return mv;
	}
}
