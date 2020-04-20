package com.reachout.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import com.reachout.models.HealthStatus;
import com.reachout.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Database accessor for a Hea object. Stored in table USER_PROFILE.
 * 
 * @author Jessica
 *
 */
public class HibernateHealthStatusDAOImpl extends HibernateDAO {

	Logger logger = LogManager.getLogger(HibernateHealthStatusDAOImpl.class);

	/**
	 * Fetches a list of all the HealthStatus objects stored in the database
	 * 
	 * @return list of health status
	 */
		
	public List<HealthStatus> getAllHealthStatuses() {
		List<HealthStatus> statuses = null;
		try (Session session = HibernateDAO.createSessionFactory().openSession())
		{
			Query q = session.createQuery("SELECT status FROM HealthStatus status", HealthStatus.class);
			statuses = q.getResultList();
		}
		return statuses;
	}
	
	public boolean save(HealthStatus hs) {
		try (Session session = this.getSessionFactory().openSession()) {
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
