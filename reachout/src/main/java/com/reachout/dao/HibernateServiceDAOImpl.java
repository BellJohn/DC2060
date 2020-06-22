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
	public synchronized boolean save(Service service) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
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
		try (Session session = HibernateUtil.getInstance().getSession()) {
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
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			session.delete(service);
			Query query = session.createNativeQuery("DELETE FROM ASSIGNED_LISTINGS WHERE AS_LISTING_ID = :lst_id");
			query.setParameter("lst_id", service.getId());
			query.executeUpdate();
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

	public Service selectById(int serId) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery("SELECT service FROM Service service where id = :serId", Service.class);
			query.setParameter("serId", serId);
			return (Service) query.getSingleResult();
		} catch (NoResultException | NonUniqueResultException e) {
			logger.error(String.format("Unable to find Service with ID: {%s}", serId), e);
			return null;
		}
	}

	public boolean deleteById(int serId) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createNativeQuery("DELETE FROM LISTINGS WHERE LST_ID = :serID");
			query.setParameter("serId", serId);
			query.executeUpdate();
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;

	}

	public boolean update(Service service) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
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
		HibernateRequestDAOImpl reqDAO = new HibernateRequestDAOImpl();
		allResults.addAll(reqDAO.getAllRequests());

		return allResults;
	}

	/**
	 * Returns all services made by a specific user based on their ID
	 * 
	 * @param userId The users ID
	 * @return List of services made by a specific user
	 */
	public List<Service> getAllServicesForUser(int userId) {
		ArrayList<Service> returnList = new ArrayList<>();
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery(
					"SELECT service FROM Service service where LST_TYPE = :lstType AND LST_USER_ID = :userId",
					Service.class);
			query.setParameter("lstType", ListingType.SERVICE.getOrdindal());
			query.setParameter("userId", userId);
			List<?> results = query.getResultList();
			for (Object obj : results) {
				if (obj instanceof Service) {
					returnList.add((Service) obj);
				}
			}
		}
		return returnList;
	}

	public int getNumServicesForUser(int userId) {
		return getAllServicesForUser(userId).size();
	}

	/**
	 * Returns all public open services made by anyone other than the current user
	 * 
	 * @param userId the users ID
	 * @return List of services made by all other users
	 */
	public List<Service> getAllServicesForDisplay(int userId) {
		ArrayList<Service> returnList = new ArrayList<>();
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery(
					"SELECT service FROM Service service where LST_TYPE = :lstType AND LST_USER_ID != :userId AND LST_STATUS = :status AND LST_VISIBILITY = 1",
					Service.class);
			query.setParameter("lstType", ListingType.SERVICE.getOrdindal());
			query.setParameter("userId", userId);
			query.setParameter("status", 0);
			List<?> results = query.getResultList();
			for (Object obj : results) {
				if (obj instanceof Service) {
					returnList.add((Service) obj);
				}
			}
		}
		return returnList;
	}

	public int getNewServiceId(int userId) {
		Integer intFound = -1;
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			Query query = session.createNativeQuery("SELECT LST_ID FROM LISTINGS WHERE LST_USER_ID = :userId ORDER BY LST_ID DESC LIMIT 1");
			query.setParameter("userId", userId);
			intFound = (Integer) (query.getSingleResult());
		} catch (NoResultException e) {
			logger.debug(String.format("No service found for user: " + userId));
		}
		return intFound;
	}
}
