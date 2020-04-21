package com.reachout.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import com.reachout.models.User;
import com.reachout.models.UserProfile;

/**
 * Database accesser for a User Profile object. Stored in table USER_PROFILE.
 * 
 * @author Jessica
 *
 */
public class HibernateUserProfileDAOImpl extends HibernateDAO {

	Logger logger = LogManager.getLogger(HibernateUserProfileDAOImpl.class);

	/**
	 * <p>
	 * Attempts to persist a new user profile in the database.
	 * </p>
	 * 
	 * @param user
	 * @return true if successful, false otherwise
	 */

	public boolean save(UserProfile userProfile) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.save(userProfile);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException | ConstraintViolationException e) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * Fetches a profile from a user ID in the database
	 * 
	 * @return
	 */
	public UserProfile getProfileById(int userID) {
		try (Session session = this.getSessionFactory().openSession()) {
			Query query  = session.createQuery("SELECT profile FROM UserProfile profile where USER_ID = :userID ");
			query.setParameter("userID", userID);
			return (UserProfile) query.getSingleResult();
		}
	}


	/**
	 * Deletes from the database where the user has a given ID
	 * 
	 * @param userID
	 * @return
	 */
	public boolean deleteUserById(int userID) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			User userToDelete = session.get(User.class, userID);
			session.delete(userToDelete);
			if (session.get(User.class, userID) == null) {
				session.getTransaction().commit();
				return true;
			}
			session.getTransaction().rollback();
			return false;
		}
	}
	
	public List<UserProfile> getAllProfiles(){
		List<UserProfile> profiles = new ArrayList<UserProfile>();
		try( Session session = this.getSessionFactory().openSession()){
			Query query = session.createQuery("SELECT userprofile from UserProfile userprofile", UserProfile.class);
			profiles = query.getResultList();
			return profiles;
		}
	}

	/**
	 * Fetches a profile picture from a user ID in the database
	 * 
	 * @return
	 */
	public String getProfilePicById(int userID) {
		try (Session session = this.getSessionFactory().openSession()) {
			Query query  = session.createQuery("SELECT profile.profilePic FROM UserProfile profile where USER_ID = :userID ");
			query.setParameter("userId", userID);
			return (String) query.getSingleResult();
		}
	}

	/**
	 * Attempts to update the user profile. Returns true if successful
	 * 
	 * @param userProfile
	 * @return
	 */
	public boolean updateUserProfile(UserProfile userProfile) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.update(userProfile);
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

}
