package com.reachout.gui.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.reachout.dao.HibernateHealthStatusDAOImpl;
import com.reachout.dao.HibernatePasswordDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.dao.HibernateUserProfileDAOImpl;
import com.reachout.models.Password;
import com.reachout.models.User;
import com.reachout.models.UserProfile;

class ProfileControllerTest {

	private static User user = new User("first", "last", "User2", "test2@test.com", "20/10/2000");
	public final static Logger logger = LogManager.getLogger(UpdateProfileController.class);

	@BeforeAll
	public static void setup() throws MessagingException {
		HibernateHealthStatusDAOImpl dao = new HibernateHealthStatusDAOImpl();
		List<String> list = dao.getAllHealthStatuses();
		if (list == null) {
			fail("Retrieve health statuses");
		}

		// set up a user account
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
		try {
			mockedRequest.login(user.getUsername(), "password");
		} catch (ServletException e) {
			logger.error(e.getStackTrace());
		}
	}

	@BeforeAll
	@AfterAll
	public static void tearDown() {

		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		User userFound = userDAO.selectUser(user.getUsername());
		if (userFound != null) {
			userDAO.delete(userFound);
		}
		HibernatePasswordDAOImpl dao = new HibernatePasswordDAOImpl();
		List<Password> passwords = dao.getAllPasswords();
		if (passwords != null) {
			for (Password p : passwords) {
				dao.delete(p);
			}
		}
		
		HibernateUserProfileDAOImpl profileDAO = new HibernateUserProfileDAOImpl();
		List<UserProfile> allProfiles = profileDAO.getAllProfiles();
		for (UserProfile userProfile : allProfiles) {
			assertTrue(profileDAO.delete(userProfile));
		}
		
	}

	@Test
	void initPageTest() throws ServletException {

		Authentication auth = new UsernamePasswordAuthenticationToken("User2", "password");
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
		SecurityContextHolder.setContext(securityContext);
		HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
		UpdateProfileController pc = new UpdateProfileController();
		ModelAndView result = pc.initPage(mockedRequest);
		assertNotNull(result);
		assertEquals("updateProfile", result.getViewName());
		assertEquals("updateProfile", result.getModel().get("currentPage"));
	}

	@Test
	void updateProfileTest() throws IOException, ServletException {

		HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
		Mockito.when(mockedRequest.getParameter("profilePic")).thenReturn("picture.png");
		Mockito.when(mockedRequest.getParameter("userBio")).thenReturn("Hi I am user 1, I want to help");
		Mockito.when(mockedRequest.getParameter("healthStatus")).thenReturn("In quarantine");

		UpdateProfileController pc = new UpdateProfileController();
		pc.initPage(mockedRequest);
		MultipartFile firstFile = new MockMultipartFile("data", "filename.png", "text/plain", "some xml".getBytes());
		ModelAndView result = pc.saveOrUpdate(mockedRequest, firstFile);
		assertNotNull(result);

		assertEquals("redirect:/profile", result.getViewName());
		HibernateUserProfileDAOImpl dao = new HibernateUserProfileDAOImpl();
		assertEquals(1, dao.getAllProfiles().size());

	}

}
