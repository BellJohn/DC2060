/**
 * 
 */
package com.reachout.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.reachout.models.ListingStatus;
import com.reachout.models.Request;
import com.reachout.testUtils.TestUtils;

/**
 * @author John
 *
 */
class TestHibernateRequestDAOImpl {

	private final String title = "TEST_TITLE";
	private final String description = "TEST_DESCRIPTION";
	private final String county = "TEST_COUNTY";
	private final String city = "TEST_CITY";
	private final int userId = 1;
	private final String priority = "Low";

	/**
	 * Guarantee a clean test bed each time
	 * 
	 * @throws Exception
	 */
	@BeforeEach
	@AfterEach
	public void setupAndTearDown() throws Exception {
		TestUtils.clearAllListings();
	}

	/**
	 * Tests the save functionality on a Listing DAO
	 */
	@Test
	void testSave() {
		Request request = new Request(title, description, county, city, userId, priority, -1);
		System.out.println(request);
		HibernateRequestDAOImpl listingDAO = new HibernateRequestDAOImpl();
		assertTrue(listingDAO.save(request));

	}

	/**
	 * Tests the delete functionality on a Listing DAO
	 */
	@Test
	void testDelete() {
		Request request = new Request(title, description, county, city, userId, priority, -1);
		System.out.println(request);
		HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl();
		assertTrue(reqDAO.save(request));
		assertTrue(reqDAO.delete(request));

	}

	/**
	 * Tests that we can accurately update a Listing record
	 */
	@Test
	void testUpdate() {
		Request request = new Request(title, description, county, city, userId, priority, -1);
		String newCityData = "NOT THE TEST DATA";
		System.out.println(request);
		HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl();
		assertTrue(reqDAO.save(request));
		// Capture the ID of the saved request so we can pull the entity from the
		// database before updating it
		int reqId = request.getId();
		// Clear out knowledge of the request
		request = null;
		// Re populate ready for updates
		request = (Request) reqDAO.selectById(reqId);

		request.setCity(newCityData);
		request.setStatus(ListingStatus.PENDING);
		// TEST the method returns what we expect
		assertTrue(reqDAO.update(request));

		// Clear out the knowledge of the request again
		request = null;

		// Get it from the database so we can compare the change
		request = (Request) reqDAO.selectById(reqId);

		// TEST that the returned entity actually has the updated fields
		assertEquals(newCityData, request.getCity());
		assertEquals(ListingStatus.PENDING, request.getStatus());
	}
}
