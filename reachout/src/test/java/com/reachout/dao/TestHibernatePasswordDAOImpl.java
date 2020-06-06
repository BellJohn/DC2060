/**
 * 
 */
package com.reachout.dao;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.security.GeneralSecurityException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.reachout.dao.exceptions.EntityNotFoundException;
import com.reachout.models.Password;
import com.reachout.models.User;
import com.reachout.testUtils.TestUtils;

/**
 * @author John
 *
 */
class TestHibernatePasswordDAOImpl {
	Logger logger = LogManager.getLogger(HibernatePasswordDAOImpl.class);

	@BeforeEach
	@AfterEach
	public void setupAndTearDown() throws Exception {
		TestUtils.clearAllUsers();
		TestUtils.clearAllPasswords();
	}

	/**
	 * Tests that we can save a password when a relevant User has been added
	 * 
	 * @throws GeneralSecurityException
	 * @throws EntityNotFoundException
	 */
	@Test
	public void saveTest() throws GeneralSecurityException, EntityNotFoundException {
		// Setup with a test user to link to
		User userForTest = new User("first", "last", "testUser", "test@test.com", "2000/10/02");
		HibernateUserDAOImpl userDao = new HibernateUserDAOImpl();
		userDao.save(userForTest);

		Password password = new Password();

		password.setUserId(1);
		password.setCreatedDate(System.currentTimeMillis());
		password.setPasswordString("TESTPASSWORDSTRING");
		HibernatePasswordDAOImpl dao = new HibernatePasswordDAOImpl();
		assertTrue(dao.save(password));
	}

	/**
	 * Test to verify that we cannot store a password without a user existing first
	 * 
	 * @throws GeneralSecurityException
	 */
	@Test
	public void saveWithoutUserTest() throws GeneralSecurityException {
		Password password = new Password();
		password.setUserId(1);
		password.setCreatedDate(System.currentTimeMillis());
		password.setPasswordString("TESTPASSWORDSTRING");
		HibernatePasswordDAOImpl dao = new HibernatePasswordDAOImpl();
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			dao.save(password);
		});
	}

	/**
	 * Tests that we can delete an existing password
	 * 
	 * @throws GeneralSecurityException
	 * @throws EntityNotFoundException
	 */
	@Test
	public void deleteTest() throws GeneralSecurityException, EntityNotFoundException {
		// Setup with a test user
		User userForTest = new User("first", "last", "testUser", "test@test.com", "2000/10/02");
		HibernateUserDAOImpl userDao = new HibernateUserDAOImpl();
		userDao.save(userForTest);

		// And store a password so we can try the deletion
		Password password = new Password();
		password.setUserId(1);
		password.setCreatedDate(System.currentTimeMillis());
		password.setPasswordString("TESTPASSWORDSTRING");
		HibernatePasswordDAOImpl dao = new HibernatePasswordDAOImpl();
		assertTrue(dao.save(password));

		// Actual test case here
		password = dao.selectByID(1);
		assertTrue(dao.delete(password));

	}

	/**
	 * Tests that a password select by User Id where no records match returns null
	 */
	@Test
	public void testSelectByUserIDNoUser() {
		HibernatePasswordDAOImpl dao = new HibernatePasswordDAOImpl();
		Password passFound = dao.selectInUseByUserId(1);
		assertNull(passFound);

	}

}
