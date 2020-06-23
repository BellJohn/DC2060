/**
 *
 */
package com.reachout.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.reachout.models.Request;
import com.reachout.models.User;
import com.reachout.testUtils.TestUtils;

/**
 * @author John
 *
 */
class TestHibernateListingDAOImpl {

	@BeforeEach
	@AfterEach
	public void tearDown() {
		TestUtils.clearAllAssignedListings();
	}

	/**
	 * Test method for
	 * {@link com.reachout.dao.HibernateListingDAOImpl#getAllListings()}.
	 */
	@Test
	void testGetAllListings() {
		assertTrue(true);
	}

	/**
	 * Test method for
	 * {@link com.reachout.dao.HibernateListingDAOImpl#delete(com.reachout.models.Listing)}.
	 */
	@Test
	void testDelete() {
		assertTrue(true);
	}

	/**
	 * Test method for
	 * {@link com.reachout.dao.HibernateListingDAOImpl#assignListingToUser(com.reachout.models.Listing, com.reachout.models.User)}.
	 */
	@Test
	void testAssignListingToUser() {
		Request request = new Request("title", "description", "county", "city", 2, "low", 1, -1);
		User user = new User();
		user.setId(3);

		HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl();
		assertTrue(reqDAO.save(request));
		assertTrue(reqDAO.assignListingToUser(request, user));
		assertFalse(reqDAO.assignListingToUser(request, user));
	}

}
