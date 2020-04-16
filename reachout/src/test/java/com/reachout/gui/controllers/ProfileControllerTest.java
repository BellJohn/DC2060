package com.reachout.gui.controllers;

import static org.junit.jupiter.api.Assertions.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;

class ProfileControllerTest {

	@Test
	void test() {
		Authentication auth = new UsernamePasswordAuthenticationToken("username", "passwod");
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
		SecurityContextHolder.setContext(securityContext);
		HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);

		ProfileController pc = new ProfileController();
		ModelAndView result = pc.initPage(mockedRequest);
		assertNotNull(result);
		assertEquals("profile", result.getViewName());
		assertEquals("username", result.getModel().get("user"));
		assertEquals("profile", result.getModel().get("currentPage"));
	}
	
	

}
