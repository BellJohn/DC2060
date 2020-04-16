package com.reachout.gui.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.text.ParseException;
import java.util.Map;

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

import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.User;

class SignupControllerTest {

	private static User user = new User("first", "last", "testUser", "test@test.com", "2000/10/02");

	@BeforeEach
	@AfterEach
	public void setup() {

		try (HibernateUserDAOImpl dao = new HibernateUserDAOImpl()) {
			User userFound = dao.selectUser(user.getUsername());
			if (userFound != null) {
				if (!dao.delete(userFound)) {
					fail("Unable to delete user. Cannot guarantee clean test bed for future tests");
				}
			}
		}
	}

	@Test
	void initPageTest() throws ParseException{

		Authentication auth = new UsernamePasswordAuthenticationToken("username", "passwod");
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
		SecurityContextHolder.setContext(securityContext);

		HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
		SignupController sc = new SignupController();
		ModelAndView result = sc.initPage(mockedRequest);
		assertNotNull(result);
		assertEquals("signup", result.getViewName());
		assertEquals("signup", result.getModel().get("currentPage"));
	}

	@Test
	void signUpValidUserTest() throws ParseException {
		HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
		Mockito.when(mockedRequest.getParameter("firstName")).thenReturn(user.getFirstName());
		Mockito.when(mockedRequest.getParameter("lastName")).thenReturn(user.getLastName());
		Mockito.when(mockedRequest.getParameter("username")).thenReturn(user.getUsername());
		Mockito.when(mockedRequest.getParameter("email")).thenReturn(user.getEmail());
		Mockito.when(mockedRequest.getParameter("dob")).thenReturn("20/09/1993");
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
	}

	@Test
	void signUpDuplicateUserTest() throws ParseException {
		HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
		Mockito.when(mockedRequest.getParameter("firstName")).thenReturn("first");
		Mockito.when(mockedRequest.getParameter("lastName")).thenReturn("last");
		Mockito.when(mockedRequest.getParameter("username")).thenReturn(user.getUsername());
		Mockito.when(mockedRequest.getParameter("email")).thenReturn(user.getEmail());
		Mockito.when(mockedRequest.getParameter("dob")).thenReturn("20/09/1993");
		Mockito.when(mockedRequest.getParameter("password")).thenReturn("password");
		Mockito.when(mockedRequest.getParameter("password_confirm")).thenReturn("password");
		SignupController sc = new SignupController();
		ModelAndView firstResult = sc.signup(mockedRequest);
		Object firstResultObj = firstResult.getModel().get("validationErrors");
		if (firstResultObj instanceof Map<?, ?>) {
			Map<?, ?> errors = (Map<?, ?>) firstResultObj;
			for (Object s : errors.keySet()) {
				System.out.println("ERROR IN POSITIVE TEST CASE: " + errors.get(s));
			}
			assertTrue(errors.isEmpty());
		}
		// Call it again so the duplicate user generation is attempted
		ModelAndView result = sc.signup(mockedRequest);

		assertNotNull(result);
		assertEquals("signup", result.getViewName());
		assertTrue((boolean) result.getModel().get("postSent"));
		assertEquals(user.getEmail(), result.getModel().get("emailAddress"));
		Object resultObj = result.getModel().get("validationErrors");
		if (resultObj instanceof Map<?, ?>) {
			Map<?, ?> errors = (Map<?, ?>) resultObj;
			assertFalse(errors.isEmpty());
			assertEquals("This username is already taken", errors.get("Duplicate Username"));
		}
	}

	@Test
	void signUpInvalidUserTest() throws ParseException{
		String badEmail = "NOT_A_GOOD_EMAIL.com";

		HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
		Mockito.when(mockedRequest.getParameter("firstName")).thenReturn("first");
		Mockito.when(mockedRequest.getParameter("lastName")).thenReturn("last");
		Mockito.when(mockedRequest.getParameter("username")).thenReturn(user.getUsername());
		Mockito.when(mockedRequest.getParameter("email")).thenReturn(badEmail);
		Mockito.when(mockedRequest.getParameter("dob")).thenReturn("20/09/1993");
		Mockito.when(mockedRequest.getParameter("password")).thenReturn("password");
		Mockito.when(mockedRequest.getParameter("password_confirm")).thenReturn("DIFFERENT_PASSWORD");

		SignupController sc = new SignupController();
		sc.signup(mockedRequest);
		// Call it again so the duplicate user generation is attempted
		ModelAndView result = sc.signup(mockedRequest);

		assertNotNull(result);
		assertEquals("signup", result.getViewName());
		assertTrue((boolean) result.getModel().get("postSent"));
		assertEquals(badEmail, result.getModel().get("emailAddress"));
		Object resultObj = result.getModel().get("validationErrors");
		if (resultObj instanceof Map<?, ?>) {
			Map<?, ?> errors = (Map<?, ?>) resultObj;
			assertFalse(errors.isEmpty());
			for (Object s : errors.keySet()) {
				System.out.println("ERROR IN POSITIVE TEST CASE: " + s + ":" + errors.get(s));
			}
			assertEquals(badEmail, errors.get("Email is not of valid form"));
			assertTrue(errors.keySet().contains("Passwords do not match"));
		}
	}

	@Test
	void signUpUnderageUserTest() throws ParseException{
		String dobString = ("20/09/2010");
		HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
		Mockito.when(mockedRequest.getParameter("firstName")).thenReturn("first");
		Mockito.when(mockedRequest.getParameter("lastName")).thenReturn("last");
		Mockito.when(mockedRequest.getParameter("username")).thenReturn(user.getUsername());
		Mockito.when(mockedRequest.getParameter("email")).thenReturn("test@test.com");
		Mockito.when(mockedRequest.getParameter("dob")).thenReturn(dobString);
		Mockito.when(mockedRequest.getParameter("password")).thenReturn("password");
		Mockito.when(mockedRequest.getParameter("password_confirm")).thenReturn("password");

		SignupController sc = new SignupController();
		ModelAndView result = sc.signup(mockedRequest);
		
		assertNotNull(result);
		assertEquals("signup", result.getViewName());
		assertTrue((boolean) result.getModel().get("postSent"));
		Object resultObj = result.getModel().get("validationErrors");
		if (resultObj instanceof Map<?, ?>) {
			Map<?, ?> errors = (Map<?, ?>) resultObj;
			assertFalse(errors.isEmpty());
			for (Object s : errors.keySet()) {
				System.out.println("ERROR IN POSITIVE TEST CASE: " + s + ":" + errors.get(s));
			}
			assertEquals(dobString, errors.get("You must be over 18 to sign up"));
		}
	}
		
}
