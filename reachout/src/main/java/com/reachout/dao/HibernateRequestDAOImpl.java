/**
 * 
 */
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

import com.reachout.models.Listing;
import com.reachout.models.ListingType;
import com.reachout.models.Request;

/**
 * @author John
 *
 */
public class HibernateRequestDAOImpl extends HibernateListingDAOImpl {

	Logger logger = LogManager.getLogger(HibernateRequestDAOImpl.class);

	/**
	 * Persists the given request into the database
	 * 
	 * @param request
	 * @return
	 */
	public boolean save(Request request) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.save(request);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

	/**
	 * Returns a collection of all known requests currently in the database
	 * 
	 * @return
	 */
	public List<Request> getAllRequests() {
		try (Session session = this.getSessionFactory().openSession()) {
			Query query = session.createQuery("SELECT request FROM Request request where LST_TYPE = :lstType",
					Request.class);
			query.setParameter("lstType", ListingType.REQUEST.getOrdindal());
			List<?> results = query.getResultList();
			ArrayList<Request> returnList = new ArrayList<>();
			for (Object obj : results) {
				if (obj instanceof Request) {
					returnList.add((Request) obj);
				}
			}
			return returnList;
		}
	}

	/**
	 * Deletes a request object from the database that has been passed.
	 * 
	 * @param request
	 * @return
	 */
	public boolean delete(Request request) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.delete(request);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

	public Request selectById(int reqId) {
		try (Session session = this.getSessionFactory().openSession()) {
			Query query = session.createQuery("SELECT request FROM Request request where id = :reqId", Request.class);
			query.setParameter("reqId", reqId);
			return (Request) query.getSingleResult();
		} catch (NoResultException | NonUniqueResultException e) {
			logger.error(String.format("Unable to find Request with ID: {%s}", reqId), e);
			return null;
		}
	}

	public boolean update(Request request) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.update(request);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

	@Override
	public List<Listing> getAllListings() {
		ArrayList<Listing> allResults = new ArrayList<>();
		allResults.addAll(getAllRequests());
		try(HibernateServiceDAOImpl serDAO = new HibernateServiceDAOImpl()){
			allResults.addAll(serDAO.getAllServices());
		}
		return allResults;
	}

	/**
	 * Returns all requests made by a specific user based on their ID
	 * 
	 * @param userId The users ID
	 * @return List of requests made by a specific user
	 */
	public List<Request> getAllRequestsForUser(int userId) {
		ArrayList<Request> returnList = new ArrayList<>();
		try (Session session = this.getSessionFactory().openSession()) {
			Query query = session.createQuery("SELECT request FROM Request request where LST_TYPE = :lstType AND LST_USER_ID = :userId",
					Request.class);
			query.setParameter("lstType", ListingType.REQUEST.getOrdindal());
			query.setParameter("userId", userId);
			List<?> results = query.getResultList();
			for (Object obj : results) {
				if (obj instanceof Request) {
					returnList.add((Request) obj);
				}
			}
		}
		return returnList;
	}

	public int getNumRequestsForUser(int userId) {
		return getAllRequestsForUser(userId).size();
	}
}
