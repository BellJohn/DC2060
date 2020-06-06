package com.reachout.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import javax.persistence.PersistenceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.reachout.models.User;

public class TestHibernateUserDAOImpl {

	static final Logger logger = LogManager.getLogger(TestHibernateUserDAOImpl.class);
	User user = new User("first", "last", "testUser", "test@test.com", "2000/10/02");

	@BeforeEach
	@AfterEach
	public void setupAndTearDown() throws Exception {
		HibernateUserDAOImpl userDao = new HibernateUserDAOImpl();
		ArrayList<User> currentEnrolledUsers = (ArrayList<User>) userDao.getAllUsers();
		for (User user : currentEnrolledUsers) {
			logger.info("Deleting user with ID: " + user.getId());
			assertTrue(userDao.deleteUserById(user.getId()));
		}
	}

	@Test
	public void testSave() throws Exception {
		HibernateUserDAOImpl userDao = new HibernateUserDAOImpl();
		assertTrue(userDao.save(user));

		// Try and put the same user in again. Should fail for a CVE
		Assertions.assertThrows(PersistenceException.class, () -> {
			userDao.save(user);
		});
	}

	@Test
	public void testUpdate() throws Exception {
		HibernateUserDAOImpl userDao = new HibernateUserDAOImpl();
		assertTrue(userDao.save(user));
		user.setEmail("notthesameemail@test.com");
		assertTrue(userDao.updateUser(user));
	}

	@Test
	public void testSelectExists() {
		HibernateUserDAOImpl userDao = new HibernateUserDAOImpl();
		assertTrue(userDao.save(user));
		User userFound = userDao.selectUser(user.getUsername());
		assertNotNull(userFound);
		assertEquals(userFound.getEmail(), user.getEmail());
	}

	@Test
	public void testSelectDoesNotExist() {
		HibernateUserDAOImpl userDao = new HibernateUserDAOImpl();
		User userFound = userDao.selectUser(user.getUsername());
		assertNull(userFound);
	}
}
