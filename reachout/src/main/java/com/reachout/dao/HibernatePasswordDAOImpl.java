/**
 * 
 */
package com.reachout.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import com.reachout.dao.exceptions.EntityNotFoundException;
import com.reachout.models.Password;
import com.reachout.models.User;

/**
 * @author John
 *
 */
public class HibernatePasswordDAOImpl extends HibernateDAO {

	Logger logger = LogManager.getLogger(HibernatePasswordDAOImpl.class);

	public Password selectByID(int id) {
		Password password = null;
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			password = session.get(Password.class, id);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException | ConstraintViolationException e) {
			return password;
		}
		return password;
	}

	public Password selectByUserID(int userId) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			Query query = session.createQuery("SELECT password FROM Password password WHERE PWD_USER_ID = :userId");
			query.setParameter("userId", userId);
			return (Password) query.getSingleResult();
		} catch (NoResultException e) {
			logger.debug("Searched for password with pwd_USER_ID [" + userId + "]. Found none");
		}
		return null;
	}

	public boolean save(Password password) throws EntityNotFoundException {
		// Check that there is actually a user attributed to that userId to start with
		try (HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl()) {
			User user = userDAO.selectByID(password.getUserId());
			if (user == null) {
				throw new EntityNotFoundException("Cannot store password as no User found to map to");
			}
		}

		// Now try to store the password. Return true/false after this point
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.save(password);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException | ConstraintViolationException e) {
			return false;
		}
		return true;
	}

	public boolean delete(Password password) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.delete(password);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException | ConstraintViolationException e) {
			return false;
		}
		return true;
	}

	public boolean deletePasswordById(int pwdId) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			Password pwdToDelete = session.get(Password.class, pwdId);
			session.delete(pwdToDelete);
			if (session.get(Password.class, pwdId) == null) {
				session.getTransaction().commit();
				return true;
			}
			session.getTransaction().rollback();
			return false;
		}

	}

	public List<Password> getAllPasswords() {
		try (Session session = this.getSessionFactory().openSession()) {
			return session.createQuery("SELECT password FROM Password password", Password.class).getResultList();
		}
	}
}
