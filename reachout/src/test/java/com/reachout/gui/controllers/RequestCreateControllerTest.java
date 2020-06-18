/**
 * 
 */
package com.reachout.gui.controllers;

import static org.junit.jupiter.api.Assertions.fail;

import java.security.GeneralSecurityException;

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

import com.reachout.dao.HibernatePasswordDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.dao.exceptions.EntityNotFoundException;
import com.reachout.models.Password;
import com.reachout.models.User;

/**
 * @author John
 *
 */
class RequestCreateControllerTest {

	private User user = new User("firstname", "lastname", "username", "fake@email.com", "01/01/2000");
	Password password = new Password();

	@BeforeEach
	@AfterEach
	public void setup() throws GeneralSecurityException {

		HibernatePasswordDAOImpl passDao = new HibernatePasswordDAOImpl();
		HibernateUserDAOImpl dao = new HibernateUserDAOImpl();
		User userFound = dao.selectUser(user.getUsername());
		if (userFound != null) {
			if (!dao.delete(userFound)) {
				fail("Unable to delete user. Cannot guarantee clean test bed for future tests");
			}
			password = passDao.selectInUseByUserId(userFound.getId());
			passDao.deletePasswordById(password.getPwdId());
		}
	}

	@Test
	void test() throws EntityNotFoundException, GeneralSecurityException {
		HibernatePasswordDAOImpl passDao = new HibernatePasswordDAOImpl();
		HibernateUserDAOImpl userDao = new HibernateUserDAOImpl();
		userDao.save(user);
		password.setCreatedDate(System.currentTimeMillis());
		password.setHashedPasswordString("password");
		password.setUserId(user.getId());
		passDao.save(password);

		Authentication auth = new UsernamePasswordAuthenticationToken("user", "password");
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
		SecurityContextHolder.setContext(securityContext);

		HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
		Mockito.when(mockedRequest.getParameter("title")).thenReturn(user.getUsername());
		Mockito.when(mockedRequest.getParameter("description")).thenReturn(user.getEmail());
		Mockito.when(mockedRequest.getParameter("county")).thenReturn("password");
		Mockito.when(mockedRequest.getParameter("request")).thenReturn("password");

		RequestCreateController rcc = new RequestCreateController();
		String title = "testTitle";
		String description = "THIS IS A DESCRIPTION";
		String county = "worcestershire";
		String city = "worcester";
		String priority = "Medium";
		String visibility = "visible";
		ModelAndView result = rcc.submitForm(title, description, county, city, priority, visibility, visibility, mockedRequest);
	} 

}
