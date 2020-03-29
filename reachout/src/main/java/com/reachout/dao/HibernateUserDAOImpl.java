package com.reachout.dao;

import javax.persistence.RollbackException;

import org.hibernate.Session;

import com.reachout.models.User;

public class HibernateUserDAOImpl extends HibernateDAO {

	/**
	 * <p>
	 * Attempts to persist a user in the database.
	 * </p>
	 * 
	 * @param user
	 * @return true if successful, false otherwise
	 */
	public boolean saveUser(User user) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.save(user);
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}
}
