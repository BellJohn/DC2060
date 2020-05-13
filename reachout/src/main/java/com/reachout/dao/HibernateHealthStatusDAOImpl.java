package com.reachout.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import com.reachout.models.HealthStatus;

/**
 * Database accessor for a Hea object. Stored in table USER_PROFILE.
 * 
 * @author Jessica
 *
 */
public class HibernateHealthStatusDAOImpl {

	Logger logger = LogManager.getLogger(HibernateHealthStatusDAOImpl.class);

	/**
	 * Fetches a list of all the HealthStatus objects stored in the database
	 * 
	 * @return list of health status
	 */
		
	public List<String> getAllHealthStatuses() {
		List<String> statuses = null;
		try (Session session = HibernateUtil.getInstance().getSession())
		{
			Query q = session.createQuery("SELECT status.status FROM HealthStatus status");
			statuses = q.getResultList();
		}
		return statuses;
	}
	
	public boolean save(HealthStatus hs) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			session.save(hs);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException | ConstraintViolationException e) {
			return false;
		}
		return true;
	}


}
