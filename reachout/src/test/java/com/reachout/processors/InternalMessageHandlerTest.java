/**
 * 
 */
package com.reachout.processors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.Set;
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
	 * Positive case test method for
	 * {@link com.reachout.processors.InternalMessageHandler#createAndStoreMessage(int, int, java.lang.String)}.
	 */
	@Test
	void testCreateAndStoreMessage() {
		User origin = TestUtils.makeTestUser();
		origin.setUsername("origin");
		User target = TestUtils.makeTestUser();
		target.setUsername("target");
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		assertTrue(userDAO.save(origin));
		origin = null;
		origin = userDAO.selectUser("origin");
		assertNotNull(origin);

		assertTrue(userDAO.save(target));
		target = null;
		target = userDAO.selectUser("target");
		assertNotNull(target);

		InternalMessageHandler imHandler = InternalMessageHandler.getInstance();
		assertTrue(imHandler.createAndStoreMessage(origin.getId(), target.getId(), "Test Message"));
		HibernateInternalMessageDAOImpl imDAO = new HibernateInternalMessageDAOImpl();
		InternalMessage im = imDAO.selectById(1);
		assertEquals("Test Message", im.getMessage());
		assertEquals(origin.getId(), im.getOrigin());
		assertEquals(target.getId(), im.getTarget());
	}

	/**
	 * Test to show that different types of invalid data will result in no
	 * InternalMessage being created and instead return false
	 */
	@Test
	public void testCreateAndStoreMessageInvalidData() {
		InternalMessageHandler imh = InternalMessageHandler.getInstance();
		// neither user exists
		assertFalse(imh.createAndStoreMessage(1, 2, "test"));

		User user1 = TestUtils.makeTestUser();
		user1.setUsername("user1");
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		userDAO.save(user1);
		// user 2 still doesn't exist
		assertFalse(imh.createAndStoreMessage(1, 2, "test"));

		User user2 = TestUtils.makeTestUser();
		user2.setUsername("user2");
		userDAO.save(user2);
		// All data is valid
		assertTrue(imh.createAndStoreMessage(1, 2, "test"));

		// Empty Message
		assertFalse(imh.createAndStoreMessage(1, 2, ""));

		// Null Message
		assertFalse(imh.createAndStoreMessage(1, 2, null));

	}

	/**
	 * Positive case test method for
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
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		assertTrue(userDAO.save(origin));
		origin = null;
		origin = userDAO.selectUser("origin");
		assertNotNull(origin);

		assertTrue(userDAO.save(target));
		target = null;
		target = userDAO.selectUser("target");
		assertNotNull(target);

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

	/**
	 * Test case to ensure we can return all the user IDs who a given user has had a
	 * conversation with (ie, there are InternalMessages between the two)
	 */
	@Test
	public void testGetAllUsersWithConversationWith() {
		// populate db with some data
		HibernateInternalMessageDAOImpl imDAO = new HibernateInternalMessageDAOImpl();
		for (int i = 2; i < 5; i++) {
			InternalMessage im = new InternalMessage(1, i, "message1");
			imDAO.save(im);
		}

		InternalMessageHandler imh = InternalMessageHandler.getInstance();
		Set<Integer> foundUsers = imh.getAllUsersWithConversationWith(1);
		assertNotNull(foundUsers);
		assertEquals(3, foundUsers.size());
		assertTrue(foundUsers.contains(2));
		assertTrue(foundUsers.contains(3));
		assertTrue(foundUsers.contains(4));
		assertFalse(foundUsers.contains(5));
	}

	/**
	 * Positive Test case for storage of a message between two users as denoted by
	 * their usernames
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreateAndStoreByUsername() throws Exception {
		User user1 = TestUtils.makeTestUser();
		user1.setUsername("user1");

		User user2 = TestUtils.makeTestUser();
		user2.setUsername("user2");

		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		userDAO.save(user1);
		userDAO.save(user2);

		InternalMessageHandler imh = InternalMessageHandler.getInstance();

		Date dateBeforeCreate = new Date();
		assertTrue(imh.createAndStoreMessage(user1.getUsername(), user2.getUsername(), "testMessage"));
		Date dateAfterCreate = new Date();

		InternalMessage im = null;
		HibernateInternalMessageDAOImpl imDAO = new HibernateInternalMessageDAOImpl();
		im = imDAO.selectById(1);

		assertNotNull(im);
		assertTrue(dateBeforeCreate.getTime() < im.getCreatedDate());
		assertTrue(dateAfterCreate.getTime() > im.getCreatedDate());
	}

	/**
	 * Negative case test for user which doesn't exist
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreateAndStoreByUsernameDoesntExist() throws Exception {
		User user1 = TestUtils.makeTestUser();
		user1.setUsername("realUser1");

		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		userDAO.save(user1);

		InternalMessageHandler imh = InternalMessageHandler.getInstance();
		assertFalse(imh.createAndStoreMessage("realUser1", "fakeUser1", "testMessage"));
		assertFalse(imh.createAndStoreMessage("fakeUser1", "realUser1", "testMessage"));
	}
}
