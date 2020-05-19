/**
 * 
 */
package com.reachout.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.reachout.models.Image;
import com.reachout.models.Listing;
import com.reachout.models.Request;
import com.reachout.models.Service;
import com.reachout.models.User;

/**
 * @author John
 *
 */
public class HibernateImageDAOImpl extends HibernateDAO {
	private Logger logger = LogManager.getLogger(HibernateImageDAOImpl.class);


	/**
	 * Deletes a request object from the database that has been passed.
	 * 
	 * @param request
	 * @return
	 */
	public boolean saveImage(Image image) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.save(image);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

	public Image getImageById(int imageID) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			Query query = session.createQuery("SELECT image.id FROM Image image WHERE IMAGE_ID = :imageID");
			query.setParameter("imageID", imageID);
			return (Image)(query.getSingleResult());
		} catch (NoResultException e) {
			return null;
		}
	}

	public boolean updateImage(Image image) {
		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.update(image);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

	public Image getImagebyUserID(int userID) {

		try (Session session = this.getSessionFactory().openSession()) {
			session.beginTransaction();
			Query query = session.createQuery("SELECT user.id FROM Image image WHERE USER_ID = :userID");
			query.setParameter("userID", userID);
			return (Image)(query.getSingleResult());
		} catch (NoResultException e) {
			return null;
		}

	}
}


