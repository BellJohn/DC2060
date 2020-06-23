package com.reachout.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.reachout.models.User;
import com.reachout.models.UserProfile;

/**
 * Database accesser for a User Profile object. Stored in table USER_PROFILE.
 *
 * @author Jessica
 *
 */
public class HibernateUserProfileDAOImpl {

	Logger logger = LogManager.getLogger(HibernateUserProfileDAOImpl.class);

	/**
	 * Attempts to persist a new user profile in the database.
	 *
	 * @param user
	 * @return true if successful, false otherwise
	 */

	public boolean saveOrUpdateProfile(UserProfile userProfile) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			session.saveOrUpdate(userProfile);
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
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
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery("SELECT profile FROM UserProfile profile where USER_ID = :userID ");
			query.setParameter("userID", userID);
			return (UserProfile) query.getSingleResult();
		} catch (NoResultException e) {
			logger.debug("No user profile found for user id: " + userID);
			return null;
		}
	}

	/**
	 * Deletes from the database where the user has a given ID
	 *
	 * @param userID
	 * @return
	 */
	public boolean deleteUserById(int userID) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
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

	public List<UserProfile> getAllProfiles() {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery("SELECT userprofile from UserProfile userprofile", UserProfile.class);
			return query.getResultList();
		}
	}

	/**
	 * Fetches a profile picture from a user ID in the database
	 * Failure to find a profile pic returns the default "no-profile-pic.png"
	 * @return
	 */
	public String getProfilePicById(int userID) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session
					.createQuery("SELECT profile.profilePic FROM UserProfile profile where USER_ID = :userID ");
			query.setParameter("userID", userID);
			return (String) query.getSingleResult();
		} catch (NoResultException e) {
			logger.debug(String.format("No profile pic found for user id {%s}", userID));
			return "no-profile-pic.png";
		}
	}

	/**
	 * Attempts to update the user profile. Returns true if successful
	 *
	 * @param userProfile
	 * @return
	 */
	public boolean updateUserProfile(UserProfile userProfile) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			session.update(userProfile);
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

}
