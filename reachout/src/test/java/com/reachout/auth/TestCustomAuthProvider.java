package com.reachout.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.User;

public class TestCustomAuthProvider {

	private static User user;
	private static final String username = "testUser";
	private static final String password = "password";
	private static final String email = "email@fake.com";
	
	@BeforeAll
	public static void setup() {
		user = new User(username, email, password);
		try(HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl()){
			if(!userDAO.saveUser(user)) {
				fail("Unable to save user, cannot proceed with tests in TestCustomAuthProvider");
			}
		}
	}
	
	@AfterAll
	public static void tearDown() {
		try(HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl()){
			if(!userDAO.deleteUser(user)) {
				fail("Unable to delete user, cannot guarantee clean test bed for future tests");
			}
		}
	}
	
	@Test 
	public void testAuthenticateExistingUser(){
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
		CustomAuthProvider cap = new CustomAuthProvider();
		Authentication result = cap.authenticate(token);
		assertNotNull(result);
		assertTrue(result instanceof UsernamePasswordAuthenticationToken);
		assertEquals(1, result.getAuthorities().size());
		assertEquals( "[USER]", result.getAuthorities().toString());
	}
	
	@Test 
	public void testAuthenticateWrongUserDetails(){
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("WRONG", password, new ArrayList<>());
		CustomAuthProvider cap = new CustomAuthProvider();
		Authentication result = cap.authenticate(token);
		assertNull(result);
	}
	
	@Test 
	public void testAuthenticateMissingUserDetails(){
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("", password, new ArrayList<>());
		CustomAuthProvider cap = new CustomAuthProvider();
		Authentication result = cap.authenticate(token);
		assertNull(result);
	}

	@Test
	public void testSupports() {
		assertTrue(new CustomAuthProvider().supports(UsernamePasswordAuthenticationToken.class));
	}
}
