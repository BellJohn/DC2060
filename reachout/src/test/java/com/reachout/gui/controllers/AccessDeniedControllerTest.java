package com.reachout.gui.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

class AccessDeniedControllerTest {

	@Test
	void test() {
		AccessDeniedController adc = new AccessDeniedController();
		ModelAndView result = adc.initPage(null);
		assertEquals("home",result.getViewName());
	}

}
