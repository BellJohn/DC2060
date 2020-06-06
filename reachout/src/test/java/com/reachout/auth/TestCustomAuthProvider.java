package com.reachout.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.GeneralSecurityException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.reachout.dao.HibernatePasswordDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.dao.exceptions.EntityNotFoundException;
import com.reachout.models.Password;
import com.reachout.models.User;
import com.reachout.testUtils.TestUtils;

public class TestCustomAuthProvider {

	private static final String firstName = "first";
	private static final String lastName = "last";
	private static final String username = "testUser";
	private static final String passwordString = "password";
	private static final String email = "email@fake.com";
	private static final String dob = "1987/07/20";

	@BeforeAll
	@AfterAll
	public static void setupAndTearDown() {
		TestUtils.clearAllUsers();
		TestUtils.clearAllPasswords();
	}

	/**
	 * Tests to see if we can log in as a user which we are already aware of.
	 * 
	 * @throws GeneralSecurityException
	 * @throws EntityNotFoundException 
	 */
	@Test
	public void testAuthenticateExistingUser() throws GeneralSecurityException, EntityNotFoundException {
		// Prep the test by building and storing a User and a Password
		User user = new User(firstName, lastName, username, email, dob);
		user.setId(1);
		Password password = new Password();
		password.setHashedPasswordString(passwordString);
		password.setUserId(user.getId());

		HibernatePasswordDAOImpl passDAO = new HibernatePasswordDAOImpl();
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		userDAO.save(user);
		passDAO.save(password);

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, passwordString,
				new ArrayList<>());
		CustomAuthProvider cap = new CustomAuthProvider();
		Authentication result = cap.authenticate(token);
		assertNotNull(result);
		assertTrue(result instanceof UsernamePasswordAuthenticationToken);
		assertEquals(1, result.getAuthorities().size());
		assertEquals("[USER]", result.getAuthorities().toString());
	}

	@Test
	public void testAuthenticateWrongUserDetails() {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("WRONG", passwordString,
				new ArrayList<>());
		CustomAuthProvider cap = new CustomAuthProvider();
		BadCredentialsException bce = assertThrows(BadCredentialsException.class, () -> {
			cap.authenticate(token);
		});
		assertEquals("Unable to login with credentials provided", bce.getMessage());

	}

	@Test
	public void testAuthenticateMissingUserDetails() {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("", passwordString,
				new ArrayList<>());
		CustomAuthProvider cap = new CustomAuthProvider();
		BadCredentialsException bce = assertThrows(BadCredentialsException.class, () -> {
			cap.authenticate(token);
		});
		assertEquals("Missing either username or password, cannot log in", bce.getMessage());

	}

	@Test
	public void testSupports() {
		assertTrue(new CustomAuthProvider().supports(UsernamePasswordAuthenticationToken.class));
	}
}
