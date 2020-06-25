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
import com.reachout.dao.HibernateLocationDAO;
import com.reachout.dao.HibernatePasswordDAOImpl;
import com.reachout.dao.HibernateRequestDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.dao.HibernateUtil;
import com.reachout.models.InternalMessage;
import com.reachout.models.Listing;
import com.reachout.models.Location;
import com.reachout.models.Password;
import com.reachout.models.Request;
import com.reachout.models.Service;
import com.reachout.models.User;

/**
 * @author John
 *
 */
public class TestUtils {

	private static final Logger logger = LogManager.getLogger(TestUtils.class);

	/**
	 * private as all methods are static, don't need an actual instance of this
	 * class
	 */
	private TestUtils() {
	}

	/**
	 * Deletes all users from the database
	 */
	public static void clearAllUsers() {
		// Remove all users
		HibernateUserDAOImpl userDao = new HibernateUserDAOImpl();
		ArrayList<User> currentEnrolledUsers = (ArrayList<User>) userDao.getAllUsers();
		for (User user : currentEnrolledUsers) {
			logger.info("Deleting user with ID: " + user.getId());
			assertTrue(userDao.deleteUserById(user.getId()));
		}
	}

	/**
	 * Deletes all passwords from the database
	 */
	public static void clearAllPasswords() {
		// Remove all passwords
		HibernatePasswordDAOImpl passwordDao = new HibernatePasswordDAOImpl();
		ArrayList<Password> currentStoredPasswords = (ArrayList<Password>) passwordDao.getAllPasswords();
		for (Password password : currentStoredPasswords) {
			logger.info("Deleting user with ID: " + password.getPwdId());
			assertTrue(passwordDao.deletePasswordById(password.getPwdId()));
		}
	}

	/**
	 * Deletes all Listings in the database
	 */
	public static void clearAllListings() {
		// Remove all listings
		HibernateListingDAOImpl listingDAO = new HibernateRequestDAOImpl();
		ArrayList<Listing> currentStoredListings = (ArrayList<Listing>) listingDAO.getAllListings();
		for (Listing listing : currentStoredListings) {
			logger.info("Deleting request with ID: " + listing.getId());
			assertTrue(listingDAO.delete(listing));
		}
	}

	/**
	 * Deletes all InternalMessages from the database
	 */
	public static void clearAllInternalMessages() {
		HibernateInternalMessageDAOImpl imDAO = new HibernateInternalMessageDAOImpl();
		ArrayList<InternalMessage> storedIMs = (ArrayList<InternalMessage>) imDAO.getAllMessages();
		for (InternalMessage im : storedIMs) {
			logger.info("Deleting InternalMessage with ID: " + im.getId());
			assertTrue(imDAO.delete(im));
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
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			Query delQuery = session.createNativeQuery("DELETE FROM ASSIGNED_LISTINGS WHERE AS_ID like '%'");
			delQuery.executeUpdate();
			session.flush();
			session.getTransaction().commit();
		}
	}

	/**
	 * Returns a populated Request for a given user. <br>
	 * User must be persisted in the database already as this relies on the user
	 * having an assigned ID already. Defaults the location ID to -1 which will not
	 * link to any genuine locations in the db
	 *
	 * @param user
	 * @return populated Request
	 */
	public static Request makeTestRequestForUser(User user) {
		return new Request(String.format("testRequestFor%s", user.getUsername()), "Test Request", "count", "city",
				user.getId(), "Urgent", 1, -1);
	}

	/**
	 * Returns a populated service for a given user. <br>
	 * User must be persisted in the database already as this relies on the user
	 * having an assigned ID already. Defaults the location ID to -1 which will not
	 * link to any genuine locations in the db
	 *
	 * @param user
	 * @return populated Service
	 */
	public static Service makeTestServiceForUser(User user) {
		return new Service(String.format("testServiceFor%s", user.getUsername()), "Test Request", "count", "city",
				user.getId(), 0, -1);

	}

	/**
	 * Stores a new Location in the database at (52.2,-2.14) and creates a new service tied to that location
	 * Service is not persisted in the database.
	 * @param user
	 * @return populated Service
	 */
	public static Service makeTestServiceAndLocationForUser(User user) {
		Service service = makeTestServiceForUser(user);

		Location location = new Location();
		location.setLocLat(52.2);
		location.setLocLong(-2.14);

		Integer locationId = new HibernateLocationDAO().save(location);
		service.setLocationId(locationId);
		return service;
	}
}
