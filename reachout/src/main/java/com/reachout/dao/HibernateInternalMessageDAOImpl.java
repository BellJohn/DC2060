/**
 * 
 */
package com.reachout.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import com.reachout.models.InternalMessage;

/**
 * @author John
 *
 */
public class HibernateInternalMessageDAOImpl extends HibernateDAO {

	Logger logger = LogManager.getLogger(HibernateInternalMessageDAOImpl.class);

	/**
	 * Persists a NEW InternalMessage in the database
	 * 
	 * @param im
	 * @return true if success, false otherwise
	 */
	public synchronized boolean save(InternalMessage im) {
		boolean result = false;
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			result = ((Integer) session.save(im) > 0);
			session.flush();
			session.getTransaction().commit();
		}
		return result;
	}

	/**
	 * Fetches every InternalMessage in the system
	 * 
	 * @return all InternalMessages
	 */
	public List<InternalMessage> getAllMessages() {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			return session
					.createQuery("SELECT internalMessage FROM InternalMessage internalMessage", InternalMessage.class)
					.getResultList();
		}
	}

	/**
	 * Evicts the provided InternalMessage from the database
	 * 
	 * @param im
	 * @return
	 */
	public boolean delete(InternalMessage im) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.delete(im);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException | ConstraintViolationException e) {
			return false;
		}
		return true;
	}

	/**
	 * Selects an InternalMessage by it's ID
	 * 
	 * @param imID
	 * @return
	 */
	public InternalMessage selectById(int imID) {
		try (Session session = this.getSessionFactory().getCurrentSession()) {
			session.beginTransaction();
			Query query = session.createQuery(
					"SELECT internalMessage FROM InternalMessage internalMessage where id = :imID",
					InternalMessage.class);
			query.setParameter("imID", imID);
			return (InternalMessage) query.getSingleResult();
		} catch (NoResultException | NonUniqueResultException e) {
			logger.error(String.format("Unable to find InternalMessage with ID: {%s}", imID), e);
			return null;
		}
	}

	/**
	 * Fetches all InternalMessages between the two given user's in order of oldest
	 * to newest
	 * 
	 * @param userBrowsing
	 * @param userOther
	 * @return
	 */
	public List<InternalMessage> getAllMessagesBetween(int userBrowsing, int userOther) {
		try (Session session = this.getSessionFactory().openSession()) {
			Query query = session.createQuery(
					"SELECT internalMessage FROM InternalMessage internalMessage WHERE (IM_ORIG_ID = :id_browse AND IM_TARGET_ID = :id_other ) OR (IM_ORIG_ID = :id_other AND IM_TARGET_ID = :id_browse) ORDER BY IM_CREATE_DATE ASC",
					InternalMessage.class);
			query.setParameter("id_browse", userBrowsing);
			query.setParameter("id_other", userOther);

			List<?> results = query.getResultList();
			ArrayList<InternalMessage> returnList = new ArrayList<>();
			for (Object obj : results) {
				if (obj instanceof InternalMessage) {
					returnList.add((InternalMessage) obj);
				}
			}
			return returnList;
		}
	}

	public Set<Integer> getAllUsersConversingWith(int userBrowsing) {
		Set<Integer> usersFound = new HashSet<>();
		try (Session session = this.getSessionFactory().openSession()) {
			String queryStringMessagesReceived = "SELECT DISTINCT IM_ORIG_ID FROM INTERNAL_MESSAGES WHERE IM_TARGET_ID = :userID";
			String queryStringMessagesSent = "SELECT DISTINCT IM_TARGET_ID FROM INTERNAL_MESSAGES WHERE IM_ORIG_ID = :userID";
			
			//Fetch all users this user has received messages from
			Query query = session.createNativeQuery(queryStringMessagesReceived);
			query.setParameter("userID", userBrowsing);
			List<?> results = query.getResultList();
			usersFound.addAll(marshalAsIntegersSet(results));
			
			//Fetch all users this user has sent messages to
			query = session.createNativeQuery(queryStringMessagesSent);
			query.setParameter("userID", userBrowsing);
			results = query.getResultList();
			usersFound.addAll(marshalAsIntegersSet(results));
		}
		return usersFound;
	}

	private Set<Integer> marshalAsIntegersSet(List<?> results) {
		Set<Integer> returnVal = new HashSet<>();
		for (Object result : results) {
			if (result instanceof Integer) {
				returnVal.add((Integer) result);
			}
		}
		return returnVal;
	}
}
