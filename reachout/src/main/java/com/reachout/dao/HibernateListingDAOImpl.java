/**
 * 
 */
package com.reachout.dao;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.reachout.models.Listing;
import com.reachout.models.ListingType;
import com.reachout.models.Request;
import com.reachout.models.Service;
import com.reachout.models.User;

/**
 * @author John
 *
 */
public abstract class HibernateListingDAOImpl {
	private Logger logger = LogManager.getLogger(HibernateListingDAOImpl.class);

	/**
	 * Returns all the possible listings in the system
	 * 
	 * @return
	 */
	public abstract List<Listing> getAllListings();

	/**
	 * Deletes a request object from the database that has been passed.
	 * 
	 * @param request
	 * @return
	 */
	public boolean delete(Listing listing) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			session.delete(listing);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

	/**
	 * Attempts to update the ASSIGNED_LISTINGS table with a new entry and update
	 * the relevant listing status simultaneously. </br>
	 * Errors in this result in rollback </br>
	 * Returns true/false success indicator
	 * 
	 * @param dao
	 * 
	 * @param listing
	 * @param user
	 * @return
	 */
	public synchronized boolean assignListingToUser(Listing listing, User user) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			Query query = session.createSQLQuery(
					"INSERT INTO ASSIGNED_LISTINGS (AS_LISTING_ID, AS_USER_ID) VALUES (:listingID, :userID)");
			query.setParameter("listingID", listing.getId());
			query.setParameter("userID", user.getId());
			// Ensure both the insert happens and the update happens
			if (query.executeUpdate() != 1) {
				session.getTransaction().rollback();
				throw new PersistenceException(
						"Unable to save assignedListing mapping, preventing Listing object updating in database");
			}
			if (listing instanceof Request) {
				session.saveOrUpdate((Request) listing);
			}
			if (listing instanceof Service) {
				session.saveOrUpdate((Service) listing);
			}
			session.flush();
			session.getTransaction().commit();
		} catch (PersistenceException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	/**
	 * Returns either a <b>Service</b> or <b>Request</b> by ID
	 * @param listingID
	 * @return Listing
	 */
	public Listing selectListingByIDofUnknownType(int listingID) {
		Listing listingToReturn = null;
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			Query query = session.createSQLQuery("SELECT LST_TYPE FROM LISTINGS WHERE LST_ID = :lst_id");
			query.setParameter("lst_id", listingID);
			Integer listingType = (Integer) query.getSingleResult();

			HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl();
			HibernateServiceDAOImpl serDAO = new HibernateServiceDAOImpl();
			if (ListingType.REQUEST.ordinal() == listingType) {
				listingToReturn = reqDAO.selectById(listingID);
			}
			else {
				listingToReturn = serDAO.selectById(listingID);
			}
		}
		return listingToReturn;
	}
}
