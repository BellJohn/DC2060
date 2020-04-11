/**
 * 
 */
package com.reachout.testUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.reachout.dao.HibernatePasswordDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.Password;
import com.reachout.models.User;

/**
 * @author John
 *
 */
public class TestUtils {

	private static final Logger logger = LogManager.getLogger(TestUtils.class);

	/**
	 * 
	 */
	private TestUtils() {
	}

	public static void clearAllUsers() {
		// Remove all users
		try (HibernateUserDAOImpl userDao = new HibernateUserDAOImpl()) {
			ArrayList<User> currentEnrolledUsers = (ArrayList<User>) userDao.getAllUsers();
			for (User user : currentEnrolledUsers) {
				logger.info("Deleting user with ID: " + user.getId());
				assertTrue(userDao.deleteUserById(user.getId()));
			}
		}
	}

	public static void clearAllPasswords() {
		// Remove all passwords
		try (HibernatePasswordDAOImpl passwordDao = new HibernatePasswordDAOImpl()) {
			ArrayList<Password> currentStoredPasswords = (ArrayList<Password>) passwordDao.getAllPasswords();
			for (Password password : currentStoredPasswords) {
				logger.info("Deleting user with ID: " + password.getPwdId());
				assertTrue(passwordDao.deletePasswordById(password.getPwdId()));
			}
		}
	}
}
