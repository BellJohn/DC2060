package com.reachout.gui.controllers;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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
import com.reachout.models.User;
import com.reachout.models.UserProfile;

class ProfileControllerTest {
	
	private static User user = new User("first", "last", "User2", "test2@test.com", "20/10/2000");
	
	@BeforeAll
	public static void setup() {
		try (HibernateHealthStatusDAOImpl dao = new HibernateHealthStatusDAOImpl()) {
			List<String> list = dao.getAllHealthStatuses();
			if (list == null) {
					fail("Retrieve health statuses");
				}
			}
		//set up a user account
		HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
		Mockito.when(mockedRequest.getParameter("firstName")).thenReturn(user.getFirstName());
		Mockito.when(mockedRequest.getParameter("lastName")).thenReturn(user.getLastName());
		Mockito.when(mockedRequest.getParameter("username")).thenReturn(user.getUsername());
		Mockito.when(mockedRequest.getParameter("email")).thenReturn(user.getEmail());
		Mockito.when(mockedRequest.getParameter("dob")).thenReturn(user.getDob());
		Mockito.when(mockedRequest.getParameter("password")).thenReturn("password");
		Mockito.when(mockedRequest.getParameter("password_confirm")).thenReturn("password");
		
		SignupController sc = new SignupController();
		ModelAndView result = sc.signup(mockedRequest);
		assertNotNull(result);
		assertEquals("signup", result.getViewName());
		assertTrue((boolean) result.getModel().get("postSent"));
		assertEquals(user.getEmail(), result.getModel().get("emailAddress"));
		Object resultObj = result.getModel().get("validationErrors");
		if (resultObj instanceof Map<?, ?>) {
			Map<?, ?> errors = (Map<?, ?>) resultObj;
			for (Object s : errors.keySet()) {
				System.out.println("ERROR IN POSITIVE TEST CASE: " + errors.get(s));
			}
			assertTrue(errors.isEmpty());
		}
		
		/**
		try (HibernateUserDAOImpl dao = new HibernateUserDAOImpl()){
		User userFound = dao.selectUser(user.getUsername());
		if (userFound != null) {
			dao.delete(userFound);
			}
		}
		*/
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
	
	
	@Test
	void updateProfileTest() {
		
		HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
		Mockito.when(mockedRequest.getParameter("profilePic")).thenReturn("picture.png");
		Mockito.when(mockedRequest.getParameter("userBio")).thenReturn("Hi I am user 1, I want to help");
		Mockito.when(mockedRequest.getParameter("healthStatus")).thenReturn("In quarantine");
		
		ProfileController pc = new ProfileController();
		pc.initPage(mockedRequest);
		ModelAndView result = pc.update(mockedRequest);
		assertNotNull(result);
		
		assertEquals("profile", result.getViewName());
	//	assertTrue((boolean) result.getModel().get("postSent"));
		try(HibernateUserProfileDAOImpl dao = new HibernateUserProfileDAOImpl()){
			assertEquals(1, dao.getAllProfiles().size());
		}
	}
	/**
	
	@Test
	void invalidImageTypeTest() {
		
	}
	
*/

}
