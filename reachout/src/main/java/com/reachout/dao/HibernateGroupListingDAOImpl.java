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

import com.reachout.models.Group;
import com.reachout.models.GroupListing;
import com.reachout.models.GroupMember;
import com.reachout.models.Listing;
import com.reachout.models.ListingType;
import com.reachout.models.Service;

/**
 * @author Jessica
 *
 */
public class HibernateGroupListingDAOImpl{

	Logger logger = LogManager.getLogger(HibernateGroupListingDAOImpl.class);

	/**
	 * Persists group listings into the database
	 * 
	 * @param request
	 * @return
	 */
	public synchronized boolean save(GroupListing groupListing) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			session.save(groupListing);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

	/**
	 * Returns a collection of listings to display for a certain group
	 * 
	 * @return
	 */
	public List<Listing> getGroupListings(int groupId) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery("SELECT groupListing FROM GroupListing groupListing WHERE GL_GRP_ID = :groupId");
			query.setParameter("groupId", groupId);
			List<GroupListing> groupListings = (List<GroupListing>) query.getResultList();

			List<Listing> listings = new ArrayList<>();
			HibernateServiceDAOImpl serviceDAO = new HibernateServiceDAOImpl();

			for (GroupListing gl : groupListings) {
				listings.add(serviceDAO.selectListingByIDofUnknownType(gl.getListingId()));
			}		
			return listings;
		}
	}


	public boolean groupListingDelete(int listingId) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			Query query = session.createNativeQuery("DELETE FROM GROUP_LISTING WHERE GL_LST_ID = :listing_id");
			query.setParameter("listing_id", listingId);
			query.executeUpdate();
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}




}
