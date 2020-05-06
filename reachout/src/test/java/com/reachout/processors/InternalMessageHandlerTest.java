/**
 * 
 */
package com.reachout.processors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.reachout.dao.HibernateInternalMessageDAOImpl;
import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.im.Conversation;
import com.reachout.models.InternalMessage;
import com.reachout.models.User;
import com.reachout.testUtils.TestUtils;

/**
 * @author John
 *
 */
class InternalMessageHandlerTest {

	@BeforeEach
	@AfterEach
	public void tearDown() {
		TestUtils.clearAllUsers();
		TestUtils.clearAllInternalMessages();
	}

	/**
	 * Test method for
	 * {@link com.reachout.processors.InternalMessageHandler#createAndStoreMessage(int, int, java.lang.String)}.
	 */
	@Test
	void testCreateAndStoreMessage() {
		User origin = TestUtils.makeTestUser();
		origin.setUsername("origin");
		User target = TestUtils.makeTestUser();
		target.setUsername("target");
		try (HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl()) {
			assertTrue(userDAO.save(origin));
			origin = null;
			origin = userDAO.selectUser("origin");
			assertNotNull(origin);

			assertTrue(userDAO.save(target));
			target = null;
			target = userDAO.selectUser("target");
			assertNotNull(target);
		}

		InternalMessageHandler imHandler = InternalMessageHandler.getInstance();
		assertTrue(imHandler.createAndStoreMessage(origin.getId(), target.getId(), "Test Message"));
		try (HibernateInternalMessageDAOImpl imDAO = new HibernateInternalMessageDAOImpl()) {
			InternalMessage im = imDAO.selectById(1);
			assertEquals("Test Message", im.getMessage());
			assertEquals(origin.getId(), im.getOrigin());
			assertEquals(target.getId(), im.getTarget());
		}
	}

	/**
	 * Test method for
	 * {@link com.reachout.processors.InternalMessageHandler#getConversationBetween(int, int)}
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testGetConversationBetween() throws InterruptedException {
		User origin = TestUtils.makeTestUser();
		origin.setUsername("origin");
		User target = TestUtils.makeTestUser();
		target.setUsername("target");
		try (HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl()) {
			assertTrue(userDAO.save(origin));
			origin = null;
			origin = userDAO.selectUser("origin");
			assertNotNull(origin);

			assertTrue(userDAO.save(target));
			target = null;
			target = userDAO.selectUser("target");
			assertNotNull(target);
		}

		// Set up a conversation of 4 messages with clearly separate time stamps
		InternalMessageHandler imHandler = InternalMessageHandler.getInstance();
		assertTrue(imHandler.createAndStoreMessage(origin.getId(), target.getId(), "Test Message 1"));
		TimeUnit.MILLISECONDS.sleep(100);
		assertTrue(imHandler.createAndStoreMessage(target.getId(), origin.getId(), "Test Message 2"));
		TimeUnit.MILLISECONDS.sleep(100);
		assertTrue(imHandler.createAndStoreMessage(origin.getId(), target.getId(), "Test Message 3"));
		TimeUnit.MILLISECONDS.sleep(100);
		assertTrue(imHandler.createAndStoreMessage(target.getId(), origin.getId(), "Test Message 4"));
		
		
		Conversation conversation = imHandler.getConversationBetween(origin.getId(), target.getId());
		System.out.println(conversation.getAllIMsAsList().toString());
		assertEquals("Test Message 1", conversation.getAllIMsAsList().get(0).getMessage());
		assertEquals("Test Message 2", conversation.getAllIMsAsList().get(1).getMessage());
		assertEquals("Test Message 3", conversation.getAllIMsAsList().get(2).getMessage());
		assertEquals("Test Message 4", conversation.getAllIMsAsList().get(3).getMessage());
		
		assertEquals(origin.getId(), conversation.getAllIMsAsList().get(0).getOrigin());
		assertEquals(target.getId(), conversation.getAllIMsAsList().get(1).getOrigin());
		assertEquals(origin.getId(), conversation.getAllIMsAsList().get(2).getOrigin());
		assertEquals(target.getId(), conversation.getAllIMsAsList().get(3).getOrigin());

	}

}
