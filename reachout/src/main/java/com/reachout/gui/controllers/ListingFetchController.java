package com.reachout.gui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ListingFetchController")
public class ListingFetchController {

	private static final Logger logger = LogManager.getLogger(ListingFetchController.class);

	/**
	 * Primary entry point for this class. Works to collate all listings of a given
	 * type within a given radius of a central location and returns as a JSON string
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping
	public String dataFetch(@RequestParam(name = "type", required = true) String type,
			@RequestParam(name = "lat", required = true) String lat,
			@RequestParam(name = "lng", required = true) String lng,
			@RequestParam(name = "radius", required = true) String radius, HttpServletRequest request) {

		logger.debug("Reached Listing FetchService with fields: "+ type + "," + lat +"," + lng +","+ radius);
		
		
		return "DONE";
	}
}
