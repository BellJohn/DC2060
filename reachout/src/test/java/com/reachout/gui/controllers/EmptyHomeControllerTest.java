package com.reachout.gui.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

class EmptyHomeControllerTest {

	@Test
	void test() {
		EmptyHomeController ehc = new EmptyHomeController();
		ModelAndView result = ehc.initPageRedirect(null);
		assertEquals("home", result.getViewName());
	}

}
