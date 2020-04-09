package com.reachout.gui.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class LogoutControllerTest {

	@Test
	void test() {
		Authentication auth = new UsernamePasswordAuthenticationToken("username", "passwod");
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
		SecurityContextHolder.setContext(securityContext);

		HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
		HttpServletResponse mockedResponse = Mockito.mock(HttpServletResponse.class);
		LogoutController lc = new LogoutController();
		String result = lc.initPage(mockedRequest, mockedResponse);
		assertEquals("redirect:/login?logout", result);
	}

}
