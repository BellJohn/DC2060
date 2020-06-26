package com.reachout.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import com.reachout.models.User;

/**
 * Database accesser for a User object. Stored in table USERS.
 * 
 * @author John
 *
 */
public class HibernateUserDAOImpl {

	Logger logger = LogManager.getLogger(HibernateUserDAOImpl.class);

	/**
	 * <p>
	 * Attempts to persist a new user in the database.
	 * </p>
	 * 
	 * @param user
	 * @return true if successful, false otherwise
	 */

	public boolean save(User user) {
		boolean success = false;
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			session.save(user);
			session.flush();
			session.getTransaction().commit();
			success = true;
		} catch (IllegalStateException | RollbackException | ConstraintViolationException e) {
			success = false;
		}
		return success;
	}

	/**
	 * Deletes a specified user from the database </br>
	 * 
	 * @param user being deleted
	 * @return true if deletion succeeded. False otherwise
	 */
	public boolean delete(User user) {
		boolean success = false;
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			session.delete(user);
			session.getTransaction().commit();
			success = true;
		} catch (IllegalStateException | RollbackException e) {
			success = false;
		}
		return success;
	}

	/**
	 * Deletes from the database where the user has a given ID
	 * 
	 * @param userID
	 * @return
	 */
	public boolean deleteUserById(int userID) {
		boolean success = false;
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			User userToDelete = session.get(User.class, userID);
			session.delete(userToDelete);
			if (session.get(User.class, userID) == null) {
				session.getTransaction().commit();
				success = true;
			} else {
				session.getTransaction().rollback();
				success = false;
			}
		}

		return success;
	}

	/**
	 * Fetches a list of all the user objects stored in the database
	 * 
	 * @return
	 */
	public List<User> getAllUsers() {
		List<User> usersFound = new ArrayList<>();
		try (Session session = HibernateUtil.getInstance().getSession()) {
			usersFound = session.createQuery("SELECT user FROM User user", User.class).getResultList();
		}
		return usersFound;
	}

	/**
	 * Attempts to update the user. Returns true if successful
	 * 
	 * @param user
	 * @return
	 */
	public boolean updateUser(User user) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
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
	 * @return the User found or null if nothing matches
	 */
	public User selectUser(String username) {
		User userFound = null;
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery("SELECT user FROM User user WHERE USERS_USERNAME = :username",
					User.class);
			query.setParameter("username", username);
			userFound = (User) query.getSingleResult();
		} catch (NoResultException e) {
			logger.debug(String.format("Searched for user with username [%s]. Found none", username), e);
		}
		return userFound;
	}

	public User selectByID(int userId) {
		User userFound = null;
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			Query query = session.createQuery("SELECT user FROM User user WHERE USERS_ID = :userId");
			query.setParameter("userId", userId);
			userFound = (User) query.getSingleResult();
		} catch (NoResultException e) {
			logger.debug(String.format("Searched for user with username [%s]. Found none", userId), e);
		}
		return userFound;
	}

	public int getUserIdByUsername(String username) {
		Integer intFound = -1;
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			Query query = session.createQuery("SELECT user.id FROM User user WHERE USERS_USERNAME = :username");
			query.setParameter("username", username);
			intFound = (Integer) (query.getSingleResult());
		} catch (NoResultException e) {
			logger.debug(String.format("No user found for username %s", username));
		}
		return intFound;
	}

	/**
	 * Get a user by their email address
	 * 
	 * @param email address to search for
	 * @return the User found or null if nothing matches
	 */
	public User getUserByEmail(String email) {
		User userFound = null;
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			Query query = session.createQuery("SELECT user FROM User user WHERE USERS_EMAIL = :email");
			query.setParameter("email", email);
			userFound = (User) query.getSingleResult();
		} catch (NoResultException e) {
			logger.debug("Searched for user with email [" + email + "]. Found none");
		} catch(NonUniqueResultException e) {
			logger.debug("Searched for user with email [" + email + "]. Found multiple");
		}
		
		return userFound;
	}

}