package com.reachout.gui.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

class HomeControllerTest {

	@Test
	void test() {
		HomeController hc = new HomeController();
		ModelAndView result = hc.initPage(null);
		assertEquals("home", result.getViewName());
	}

}
