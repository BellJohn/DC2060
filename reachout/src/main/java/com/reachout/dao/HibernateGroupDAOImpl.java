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
import com.reachout.models.Listing;
import com.reachout.models.ListingType;
import com.reachout.models.Service;

/**
 * @author Jessica
 *
 */
public class HibernateGroupDAOImpl{

	Logger logger = LogManager.getLogger(HibernateGroupDAOImpl.class);

	/**
	 * Persists the given group into the database
	 * 
	 * @param request
	 * @return
	 */
	public synchronized boolean save(Group group) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			session.save(group);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

	/**
	 * Returns a collection of all known groups currently in the database
	 * 
	 * @return
	 */
	public List<Group> getAllGroups() {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery("SELECT group FROM Group group");
			List<Group> results = (List<Group>) query.getResultList();
			return results;
		}
	}

	/**
	 * Deletes a group object from the database that has been passed.
	 * 
	 * @param group
	 * @return
	 */
	public boolean delete(Group group) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			session.delete(group);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

	public Group selectById(int groupId) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery("SELECT group FROM Group group where id = :groupId", Group.class);
			query.setParameter("groupId", groupId);
			return (Group) query.getSingleResult();
		} catch (NoResultException | NonUniqueResultException e) {
			logger.error(String.format("Unable to find Group with ID: {%s}", groupId), e);
			return null;
		}
	}
	
	
	public Group selectByName(String name) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery("SELECT group FROM Group group where name = :groupName", Group.class);
			query.setParameter("groupName", name);
			return (Group) query.getSingleResult();
		} catch (NoResultException | NonUniqueResultException e) {
			logger.error(String.format("Unable to find Group with name: {%s}", name), e);
			return null;
		}
	}

	public boolean update(Group group) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			session.update(group);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}


}
