package com.reachout.dao;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.reachout.dao.HibernateUserDAOImpl;
import com.reachout.models.User;

public class TestHibernateUserDAOImpl {

	@Test
	public void testSave() {
		User user = new User("TestUsername", "TestEmail@Email.com", "TestPassword");
		HibernateUserDAOImpl userDao = new HibernateUserDAOImpl();
		assertTrue(userDao.saveUser(user));
	}
}
