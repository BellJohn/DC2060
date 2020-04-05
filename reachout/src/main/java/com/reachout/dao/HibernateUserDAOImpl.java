package com.reachout.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import com.reachout.models.User;

public class HibernateUserDAOImpl extends HibernateDAO {

	Logger logger = LogManager.getLogger(HibernateUserDAOImpl.class);

	/**
	 * <p>
	 * Attempts to persist a new user in the database.
	 * </p>
	 * 
	 * @param user
	 * @return true if successful, false otherwise
	 */

	public boolean saveUser(User user) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.save(user);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException | ConstraintViolationException e) {
			return false;
		}
		return true;
	}

	/**
	 * Deletes a specified user from the database
	 * 
	 * @param user
	 * @return
	 */
	public boolean deleteUser(User user) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.delete(user);
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

	/**
	 * Deletes from the database where the user has a given ID
	 * 
	 * @param userID
	 * @return
	 */
	public boolean deleteUserById(int userID) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			User userToDelete = session.get(User.class, userID);
			session.delete(userToDelete);
			if (session.get(User.class, userID) == null) {
				session.getTransaction().commit();
				return true;
			}
			session.getTransaction().rollback();
			return false;
		}

	}

	/**
	 * Fetches a list of all the user objects stored in the database
	 * 
	 * @return
	 */
	public List<User> getAllUsers() {
		try (Session session = this.getSessionFactory().openSession()) {
			return session.createQuery("SELECT user FROM User user", User.class).getResultList();
		}
	}

	/**
	 * Attempts to update the user. Returns true if successful
	 * 
	 * @param user
	 * @return
	 */
	public boolean updateUser(User user) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.update(user);
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;

	}

	/**
	 * Search the database for a user with a given username
	 * 
	 * @param username
	 * @return
	 */
	public User selectUser(String username) {
		try (Session session = this.getSessionFactory().openSession()) {
			Query query = session.createQuery("SELECT user FROM User user WHERE USERS_USERNAME = :username",
					User.class);
			query.setParameter("username", username);
			return (User) query.getSingleResult();
		} catch (NoResultException e) {
			logger.debug("Searched for user with username [" + username + "]. Found none");
		}
		return null;
	}

}
