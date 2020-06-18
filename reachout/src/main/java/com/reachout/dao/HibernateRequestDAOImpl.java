package com.reachout.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
	public synchronized boolean save(Request request) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
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
		try (Session session = HibernateUtil.getInstance().getSession()) {
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
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			session.delete(request);
			Query query = session.createNativeQuery("DELETE FROM ASSIGNED_LISTINGS WHERE AS_LISTING_ID = :lst_id");
			query.setParameter("lst_id", request.getId());
			query.executeUpdate();
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

	public Request selectById(int reqId) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery("SELECT request FROM Request request where id = :reqId", Request.class);
			query.setParameter("reqId", reqId);
			return (Request) query.getSingleResult();
		} catch (NoResultException | NonUniqueResultException e) {
			logger.error(String.format("Unable to find Request with ID: {%s}", reqId), e);
			return null;
		}
	}

	public boolean update(Request request) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
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
		HibernateServiceDAOImpl serDAO = new HibernateServiceDAOImpl();
		allResults.addAll(serDAO.getAllServices());
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
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery(
					"SELECT request FROM Request request where LST_TYPE = :lstType AND LST_USER_ID = :userId",
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

	/**
	 * Returns all open requests made by anyone other than the current user
	 * 
	 * @param userId the users ID
	 * @return List of requests made by all other users
	 */
	public List<Request> getAllRequestsForDisplay(int userId) {
		ArrayList<Request> returnList = new ArrayList<>();
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery(
					"SELECT request FROM Request request where LST_TYPE = :lstType AND LST_USER_ID != :userId AND LST_STATUS = :status",
					Request.class);
			query.setParameter("lstType", ListingType.REQUEST.getOrdindal());
			query.setParameter("userId", userId);
			query.setParameter("status", 0);
			List<?> results = query.getResultList();
			for (Object obj : results) {
				if (obj instanceof Request) {
					returnList.add((Request) obj);
				}
			}
		}
		return returnList;
	}

	/**
	 * Returns all requests that a user has offered to help on
	 * 
	 * @param userId The ID of the user to get the requests for
	 * @return List of the requests a user has offered to help on
	 */
	public List<Request> getAcceptedRequestsForUser(int userId) {
		ArrayList<Request> returnList = new ArrayList<>();

		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/reach_out", "reach",
				"reach_pass");
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT LST_ID, LST_TITLE, LST_DESCRIPTION, LST_COUNTY, LST_CITY, LST_USER_ID, LST_STATUS, LST_PRIORITY, LST_LOC_ID FROM LISTINGS l JOIN ASSIGNED_LISTINGS al ON l.LST_ID = al.AS_LISTING_ID where l.LST_TYPE = "
								+ ListingType.REQUEST.getOrdindal() + " AND al.AS_USER_ID = " + userId)) {
			while (rs.next()) {

				Request r = new Request(rs.getString("LST_TITLE"), rs.getString("LST_DESCRIPTION"),
						rs.getString("LST_COUNTY"), rs.getString("LST_CITY"), rs.getInt("LST_USER_ID"),
						rs.getString("LST_PRIORITY"), rs.getInt("LST_LOC_ID"));
				r.setStatus(rs.getInt("LST_STATUS"));
				r.setId(rs.getInt("LST_ID"));
				returnList.add(r);
			}
		} catch (Exception e) {
			logger.error("Failed to fetch requests for user",e);
		}

		return returnList;
	}
}
