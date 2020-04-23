/**
 * 
 */
package com.reachout.dao;

import java.util.List;

import javax.persistence.RollbackException;

import org.hibernate.Session;

import com.reachout.models.Listing;

/**
 * @author John
 *
 */
public abstract class HibernateListingDAOImpl extends HibernateDAO {

	/**
	 * Returns all the possible listings in the system
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
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.delete(listing);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}
	 
}
