/**
 * 
 */
package com.reachout.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.reachout.models.Request;
import com.reachout.models.User;

/**
 * @author John
 *
 */
class HibernateListingDAOImplTest {

	/**
	 * Test method for
	 * {@link com.reachout.dao.HibernateListingDAOImpl#getAllListings()}.
	 */
	@Test
	void testGetAllListings() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.reachout.dao.HibernateListingDAOImpl#delete(com.reachout.models.Listing)}.
	 */
	@Test
	void testDelete() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.reachout.dao.HibernateListingDAOImpl#assignListingToUser(com.reachout.models.Listing, com.reachout.models.User)}.
	 */
	@Test
	void testAssignListingToUser() {
		Request request = new Request("title", "description", "county", "city", 2);
		User user = new User();
		user.setId(3);

		try (HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl()) {
			assertTrue(reqDAO.save(request));
			assertTrue(reqDAO.assignListingToUser(request, user));
			assertFalse(reqDAO.assignListingToUser(request, user));
		}

	}

}