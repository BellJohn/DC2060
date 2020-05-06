/**
 * 
 */
package com.reachout.testUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.reachout.dao.HibernateInternalMessageDAOImpl;
import com.reachout.dao.HibernateListingDAOImpl;
import com.reachout.dao.HibernatePasswordDAOImpl;
import com.reachout.dao.HibernateRequestDAOImpl;
import com.reachout.dao.HibernateServiceDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.InternalMessage;
import com.reachout.models.Listing;
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

	public static void clearAllListings() {
		// Remove all listings
		try (HibernateListingDAOImpl listingDAO = new HibernateRequestDAOImpl()) {
			ArrayList<Listing> currentStoredListings = (ArrayList<Listing>) listingDAO.getAllListings();
			for (Listing listing : currentStoredListings) {
				logger.info("Deleting request with ID: " + listing.getId());
				assertTrue(listingDAO.delete(listing));
			}
		}
	}

	public static void clearAllInternalMessages() {
		try (HibernateInternalMessageDAOImpl imDAO = new HibernateInternalMessageDAOImpl()) {
			ArrayList<InternalMessage> storedIMs = (ArrayList<InternalMessage>) imDAO.getAllMessages();
			for (InternalMessage im : storedIMs) {
				logger.info("Deleting InternalMessage with ID: " + im.getId());
				assertTrue(imDAO.delete(im));
			}
		}
	}

	/**
	 * Makes a test user in memory with default data of:
	 * <ul>
	 * <li>First Name: firstName</li>
	 * <li>Last Name: lastName</li>
	 * <li>Username: username</li>
	 * <li>Email: email@email.com</li>
	 * <li>DOB: 01/01/1970</li>
	 * </ul>
	 * 
	 * @return test User
	 */
	public static User makeTestUser() {
		return new User("firstName", "lastName", "username", "email@email.com", "01/01/1970");
	}

	/**
	 * Blanket deletes every assigned listing in the database
	 */
	public static void clearAllAssignedListings() {
		try (HibernateServiceDAOImpl serDAO = new HibernateServiceDAOImpl(); Session session = serDAO.getSessionFactory().openSession()) {
			session.beginTransaction();
			Query delQuery = session.createNativeQuery("DELETE FROM ASSIGNED_LISTINGS WHERE AS_ID like '%'");
			delQuery.executeUpdate();
			session.flush();
			session.getTransaction().commit();
		}
	}
}
