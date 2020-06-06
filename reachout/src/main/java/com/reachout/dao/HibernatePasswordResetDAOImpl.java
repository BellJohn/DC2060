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

	Logger logger = LogManager.getLogger(HibernatePasswordResetDAOImpl.class);

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

	/**
	 * Check if a password reset code exists within the database
	 */
	public PasswordReset selectByCode(String code) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery("SELECT pr FROM PasswordReset pr where code = :code", PasswordReset.class);
			query.setParameter("code", code);
			return (PasswordReset) query.getSingleResult();
		} catch (NoResultException | NonUniqueResultException e) {
			logger.error(String.format("Unable to find PasswordReset with CODE: {%s}", code), e);
			return null;
		}
	}

	/** 
	 * Check if a password reset code is still valid (exists and is not older than 15 mins)
	 */
    public Boolean checkIfCodeValid(String code) {
		PasswordReset pr = selectByCode(code);
		// If the entry exists
		if (pr != null) {
			Long expiryTime = pr.getCreateDate() + 900000;
			Long currentTime = System.currentTimeMillis();
			// And the expiry time is less than the current time
			if(expiryTime > currentTime) {
				return true;
			}
		}
		return false;
	}

}