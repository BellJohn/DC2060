/**
 * 
 */
package com.reachout.dao;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.reachout.models.InternalMessage;
import com.reachout.testUtils.TestUtils;

/**
 * @author John
 *
 */
class TestHibernateInternalMessageDAOImpl {

	@BeforeEach
	@AfterEach
	public void tearDown() {
		TestUtils.clearAllInternalMessages();
	}

	/**
	 * Test method for
	 * {@link com.reachout.dao.HibernateInternalMessageDAOImpl#save(com.reachout.models.InternalMessage)}.
	 */
	@Test
	void testSave() {
		InternalMessage message = new InternalMessage(1, 2, "TEST");
		HibernateInternalMessageDAOImpl imDAO = new HibernateInternalMessageDAOImpl();
		assertTrue(imDAO.save(message));
	}

}
