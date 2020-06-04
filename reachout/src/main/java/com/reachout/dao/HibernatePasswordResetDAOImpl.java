package com.reachout.dao;

import com.reachout.models.PasswordReset;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class HibernatePasswordResetDAOImpl {

    /**
	 * Persists the given password reset into the database
	 * 
	 * @param pr
	 * @return
	 */
	public synchronized boolean save(PasswordReset pr) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			session.save(pr);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}
    
}