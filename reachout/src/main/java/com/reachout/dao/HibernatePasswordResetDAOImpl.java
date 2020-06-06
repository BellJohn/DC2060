package com.reachout.dao;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.reachout.models.PasswordReset;

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
	 * 
	 * @param code to search from
	 * @return the database entry if one exists or null
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
	 * 
	 * @param code to check is still valid
	 * @return whether the code is valid or not
	 */
    public Boolean checkIfCodeValid(String code) {
		PasswordReset pr = selectByCode(code);
		if (pr != null) {
			Long expiryTime = pr.getCreateDate() + 900000;
			Long currentTime = System.currentTimeMillis();
			if(expiryTime > currentTime) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Delete a password reset code based on entered code.
	 * 
	 * @param code to delete entry for
	 */
	public void deletePasswordResetByCode(String code) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			Query query = session.createNativeQuery("DELETE FROM PASSWORD_RESET WHERE PR_CODE = :code");
			query.setParameter("code", code);
			query.executeUpdate();
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
		}
	}

}