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
import com.reachout.models.Service;

/**
 * @author John
 *
 */
public class HibernateServiceDAOImpl extends HibernateListingDAOImpl {

	Logger logger = LogManager.getLogger(HibernateServiceDAOImpl.class);

	/**
	 * Persists the given service into the database
	 * 
	 * @param request
	 * @return
	 */
	public boolean save(Service service) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.save(service);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

	/**
	 * Returns a collection of all known services currently in the database
	 * 
	 * @return
	 */
	public List<Service> getAllServices() {
		try (Session session = this.getSessionFactory().openSession()) {
			Query query = session.createQuery("SELECT service FROM Service service where LST_TYPE = :lstType",
					Service.class);
			query.setParameter("lstType", ListingType.SERVICE.getOrdindal());
			List<?> results = query.getResultList();
			ArrayList<Service> returnList = new ArrayList<>();
			for (Object obj : results) {
				if (obj instanceof Service) {
					returnList.add((Service) obj);
				}
			}
			return returnList;
		}
	}

	/**
	 * Deletes a service object from the database that has been passed.
	 * 
	 * @param request
	 * @return
	 */
	public boolean delete(Service service) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.delete(service);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

	public Service selectById(int serId) {
		try (Session session = this.getSessionFactory().openSession()) {
			Query query = session.createQuery("SELECT service FROM Service service where id = :serId", Service.class);
			query.setParameter("serId", serId);
			return (Service) query.getSingleResult();
		} catch (NoResultException | NonUniqueResultException e) {
			logger.error(String.format("Unable to find Service with ID: {%s}", serId), e);
			return null;
		}
	}

	public boolean update(Service service) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.update(service);
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
		allResults.addAll(getAllServices());
		try(HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl()){
			allResults.addAll(reqDAO.getAllRequests());
		}
		return allResults;
	}
}
