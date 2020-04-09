package com.reachout.gui.controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

class LoginControllerTest {

	@Test
	void test() {
		LoginController lc = new LoginController();
		ModelAndView result = lc.initPage(null);
		assertEquals("login", result.getViewName());
	}

}
