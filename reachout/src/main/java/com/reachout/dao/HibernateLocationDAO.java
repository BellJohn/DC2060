package com.reachout.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.reachout.models.Location;

public class HibernateLocationDAO {

	Logger logger = LogManager.getLogger(HibernateLocationDAO.class);

	/**
	 * Persists the given location into the database
	 * 
	 * @param location
	 * @return
	 */
	public synchronized Integer save(Location location) {
		Integer id = null;
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			id = (Integer) session.save(location);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return null;
		}
		return id;
	}

	/**
	 * Returns a collection of all known locations currently in the database
	 * 
	 * @return
	 */
	public List<Location> getAllLocations() {
		List<Location> locations = new ArrayList<>();
		try (Session session = HibernateUtil.getInstance().getSession()) {
			locations = session.createQuery("SELECT location FROM Location location", Location.class).getResultList();
		}
		return locations;
	}

	public Location selectLocationById(int locationId) {
		Location location = null;
		try(Session session = HibernateUtil.getInstance().getSession()){
			Query query = session.createQuery("SELECT location FROM Location location WHERE LOC_ID = :locId", Location.class);
			query.setParameter("locId", locationId);
			location = (Location) query.getSingleResult();
		} catch (NoResultException e) {
			logger.debug(String.format("No location found with id [%s]", locationId), e);
		}
		return location;
	}
	
}
