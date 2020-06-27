/**
 * 
 */
package com.reachout.processors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.reachout.dao.HibernateRequestDAOImpl;
import com.reachout.dao.HibernateServiceDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.Request;
import com.reachout.models.Service;
import com.reachout.models.User;
import com.reachout.models.exceptions.ListingTypeNotMatchedException;
import com.reachout.testUtils.TestUtils;

/**
 * @author John
 *
 */
class ListingHandlerTest {

	@BeforeEach
	@AfterEach
	public void tearDown() {
		TestUtils.clearAllUsers();
		TestUtils.clearAllListings();
		TestUtils.clearAllAssignedListings();
	}

	/**
	 * Test method for
	 * {@link com.reachout.processors.ListingHandler#acceptRequest(com.reachout.models.User, java.lang.Integer, java.lang.String)}.
	 * 
	 * @throws ListingTypeNotMatchedException
	 */
	@Test
	void testAcceptRequest() throws ListingTypeNotMatchedException {

		User userBrowsing = TestUtils.makeTestUser();
		userBrowsing.setUsername("userBrowsing");

		User userOwner = TestUtils.makeTestUser();
		userOwner.setUsername("userOwner");

		Request request = null;
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl();

		userDAO.save(userBrowsing);
		userDAO.save(userOwner);

		userBrowsing = userDAO.selectUser(userBrowsing.getUsername());
		userOwner = userDAO.selectUser(userOwner.getUsername());

		request = TestUtils.makeTestRequestForUser(userOwner);
		reqDAO.save(request);
		List<Request> allReqs =  reqDAO.getAllRequests();
		request = allReqs.get(allReqs.size()-1);

		ListingHandler lh = ListingHandler.getInstance();
		assertTrue(lh.acceptRequest(userBrowsing, request.getId(), request.getListingType().getName()));
	}

	/**
	 * Test method for
	 * {@link com.reachout.processors.ListingHandler#acceptRequest(com.reachout.models.User, java.lang.Integer, java.lang.String)}.
	 * 
	 * @throws ListingTypeNotMatchedException
	 */
	@Test
	void testAcceptService() throws ListingTypeNotMatchedException {

		User userBrowsing = TestUtils.makeTestUser();
		userBrowsing.setUsername("userBrowsing");

		User userOwner = TestUtils.makeTestUser();
		userOwner.setUsername("userOwner");

		Service service = null;
		HibernateServiceDAOImpl serDAO = new HibernateServiceDAOImpl();
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		userDAO.save(userBrowsing);
		userDAO.save(userOwner);

		userBrowsing = userDAO.selectUser(userBrowsing.getUsername());
		userOwner = userDAO.selectUser(userOwner.getUsername());

		service = TestUtils.makeTestServiceForUser(userOwner);
		serDAO.save(service);
		service = serDAO.selectById(1);

		ListingHandler lh = ListingHandler.getInstance();
		assertTrue(lh.acceptRequest(userBrowsing, service.getId(), service.getListingType().getName()));
	}

	/**
	 * A test to ensure a listing acceptance fails and throws an exception when the
	 * listing type is neither <b>service</b> or <b>request</b>
	 */
	@Test
	public void testNoSuchListingType() {
		ListingHandler lh = ListingHandler.getInstance();
		Assertions.assertThrows(ListingTypeNotMatchedException.class, () -> {
			lh.acceptRequest(null, null, "not a request or service");
		});
	}

	/**
	 * A test to ensure an attempt to accept a listing which doesn't exist fails.
	 * 
	 * @throws ListingTypeNotMatchedException
	 */
	@Test
	public void testNoSuchListingFound() throws ListingTypeNotMatchedException {
		ListingHandler lh = ListingHandler.getInstance();
		assertFalse(lh.acceptRequest(null, 1, "service"));
	}

	/**
	 * A test to ensure an attempt to accept a listing fails when the user browsing
	 * is the user who owns a request
	 * 
	 * @throws ListingTypeNotMatchedException
	 */
	@Test
	public void userBrowsingOwnsRequest() throws ListingTypeNotMatchedException {
		User user = TestUtils.makeTestUser();

		HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl();
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		userDAO.save(user);
		user = userDAO.selectUser(user.getUsername());
		Request request = TestUtils.makeTestRequestForUser(user);

		reqDAO.save(request);

		assertFalse(ListingHandler.getInstance().acceptRequest(user, 1, "request"));
	}
}
