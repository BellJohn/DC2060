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
	private static Logger logger = LogManager.getLogger(HibernateListingDAOImpl.class);

	protected static synchronized boolean syncronizedSave(Listing listing) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			if (listing instanceof Request) {
				session.save((Request) listing);
			} else {
				session.save((Service) listing);
			}
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			logger.error("Error saving listing", e);
			return false;
		}
		return true;
	}

	protected static synchronized boolean synchronizedDelete(Listing listing) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			if (listing instanceof Request) {
				session.delete((Request) listing);
			} else {
				session.delete((Service) listing);
			}
			Query assingedListingsQuery = session
					.createNativeQuery("DELETE FROM ASSIGNED_LISTINGS WHERE AS_LISTING_ID = :lst_id");
			assingedListingsQuery.setParameter("lst_id", listing.getId());
			assingedListingsQuery.executeUpdate();
			new HibernateGroupListingDAOImpl().groupListingDelete(listing.getId());
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			logger.error("Error deleting listing", e);
			return false;
		}
		return true;
	}

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
	public boolean nonSynchListingDelete(Listing listing) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			session.delete(listing);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			logger.error("Failed to delete listing", e);
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
	 * Returns the id of the user who accepted a particular listing
	 * 
	 * @param listing
	 * @return int id of user
	 */
	public synchronized Integer getUserIdWhoAcceptedListing(Listing listing) {
		Integer userID = null;
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createSQLQuery(
					"SELECT AS_USER_ID FROM ASSIGNED_LISTINGS WHERE AS_LISTING_ID = :lstID ORDER BY AS_ID DESC LIMIT 1");
			query.setParameter("lstID", listing.getId());
			Object result = query.getSingleResult();
			if (result instanceof Integer) {
				userID = (Integer) result;
			}
		}
		return userID;
	}

	/**
	 * Returns either a <b>Service</b> or <b>Request</b> by ID
	 * 
	 * @param listingID
	 * @return Listing
	 */
	public synchronized Listing selectListingByIDofUnknownType(int listingID) {
		Listing listingToReturn = null;
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createSQLQuery("SELECT LST_TYPE FROM LISTINGS WHERE LST_ID = :lst_id");
			query.setParameter("lst_id", listingID);
			Integer listingType = (Integer) query.getSingleResult();

			HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl();
			HibernateServiceDAOImpl serDAO = new HibernateServiceDAOImpl();
			if (ListingType.REQUEST.ordinal() == listingType) {
				listingToReturn = reqDAO.selectById(listingID);
			} else {
				listingToReturn = serDAO.selectById(listingID);
			}
		}
		return listingToReturn;
	}
}
