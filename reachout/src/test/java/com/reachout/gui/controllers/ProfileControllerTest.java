package com.reachout.gui.controllers;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.dao.HibernateHealthStatusDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.dao.HibernateUserProfileDAOImpl;
import com.reachout.models.HealthStatus;
import com.reachout.models.UserProfile;

class ProfileControllerTest {
	
	
	@BeforeEach
	@AfterEach
	public void setup() {
		try (HibernateHealthStatusDAOImpl dao = new HibernateHealthStatusDAOImpl()) {
			List<String> list = dao.getAllHealthStatuses();
			System.out.println(list);
			if (list == null) {
					fail("Retrieve health statuses");
				}
			}
		}
	
	@Test
	void initPageTest(){
		
		Authentication auth = new UsernamePasswordAuthenticationToken("username", "password");
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
		SecurityContextHolder.setContext(securityContext);
		HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
		ProfileController pc = new ProfileController();
		ModelAndView result = pc.initPage(mockedRequest);
		assertNotNull(result);
		assertEquals("updateProfile", result.getViewName());
		assertEquals("updateProfile", result.getModel().get("currentPage"));
	}
	
	/**
	
	@Test
	void updateProfileTest() {
		
	}
	
	
	@Test
	void invalidImageTypeTest() {
		
	}
	
*/

}
