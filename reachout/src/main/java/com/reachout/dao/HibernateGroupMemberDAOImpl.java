/**
 * 
 */
package com.reachout.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.reachout.models.Group;
import com.reachout.models.GroupMember;
import com.reachout.models.Listing;
import com.reachout.models.ListingType;
import com.reachout.models.Service;

/**
 * @author Jessica
 *
 */
public class HibernateGroupMemberDAOImpl{

	Logger logger = LogManager.getLogger(HibernateGroupMemberDAOImpl.class);

	/**
	 * Persists group members into the database
	 * 
	 * @param request
	 * @return
	 */
	public synchronized boolean save(GroupMember groupMember) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			session.save(groupMember);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

	/**
	 * Returns a collection of all known groups that this user is a member of
	 * 
	 * @return
	 */
	public List<Group> getUserGroups(int userId) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery("SELECT groupId FROM GroupMember groupMember WHERE GM_G_UID = :userId AND NOT GM_U_STATUS_ID = 0");
			query.setParameter("userId", userId);
			List<Integer> groupIds = (List<Integer>) query.getResultList();

			List<Group> results = new ArrayList<Group>();
			HibernateGroupDAOImpl groupDAO = new HibernateGroupDAOImpl();
			for (int id : groupIds) {
				results.add(groupDAO.selectById(id)); 
			}		
			return results;
		}
	}


	/**
	 * Returns a collection of all known groups  the user is not a member of
	 * 
	 * @return
	 */
	public Set<Group> getNonUserGroups(int userId) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery("SELECT DISTINCT groupMember FROM GroupMember groupMember WHERE NOT GM_G_UID = :userId");
			query.setParameter("userId", userId);
			List<GroupMember> groupMembers = (List<GroupMember>) query.getResultList();

			//remove any groups the user is a member of
			List<GroupMember> userGroups = new ArrayList<GroupMember>();
			for (GroupMember gm : groupMembers) {
				for (Group g : getUserGroups(userId)) {
					if ( gm.getGroupId() == g.getId()) {
						userGroups.add(gm);
					}
				}
			}
				groupMembers.removeAll(userGroups);


				//make sure no duplicates
				Set<Group> results = new HashSet<Group>();
				HibernateGroupDAOImpl groupDAO = new HibernateGroupDAOImpl();
				for (GroupMember grp : groupMembers) {
					results.add(groupDAO.selectById(grp.getGroupId()));
				}		


				return results;
			
		}
	}

	/**
	 * Deletes a group member object from the database that has been passed.
	 * 
	 * @param groupMember
	 * @return
	 */
	public boolean delete(GroupMember groupMember) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			session.delete(groupMember);
			Query query = session.createNativeQuery("DELETE FROM GROUPS WHERE GM_ID = :gm_id");
			query.setParameter("gm_id", groupMember.getId());
			query.executeUpdate();
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

	public GroupMember selectById(int userId) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery("SELECT groupMember FROM GroupMember groupMember where id = :userId", GroupMember.class);
			query.setParameter("userId", userId);
			return (GroupMember) query.getSingleResult();
		} catch (NoResultException | NonUniqueResultException e) {
			logger.error(String.format("Unable to find Group member with ID: {%s}", userId), e);
			return null;
		}
	}

	public boolean update(GroupMember groupMember) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			session.update(groupMember);
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

	public GroupMember checkIfGroupMember(int userId, int groupId) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery("SELECT groupMember FROM GroupMember groupMember where userId = :userId AND groupId = :groupId", GroupMember.class);
			query.setParameter("userId", userId);
			query.setParameter("groupId", groupId);

			ArrayList<GroupMember> results = (ArrayList<GroupMember>) query.getResultList();
			if (results.size() == 0) {
				return null;
			}
			else {
				return results.get(0);
			}

		}
	}

	public boolean groupDelete(int groupID) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			session.beginTransaction();
			Query query = session.createNativeQuery("DELETE FROM GROUP_MEMBER WHERE GM_G_ID = :group_id");
			query.setParameter("group_id", groupID);
			query.executeUpdate();
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			return false;
		}
		return true;
	}

	//Get all requests with a value of 0 (pending) for a specific group
	public ArrayList<GroupMember> getPendingMembers(int groupID){
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery("SELECT groupMember FROM GroupMember groupMember WHERE GM_G_ID = :group_id AND GM_U_STATUS_ID = 0");
			query.setParameter("group_id", groupID);
			ArrayList<GroupMember> pendingUsers = (ArrayList<GroupMember>) query.getResultList();	
			return pendingUsers;

		} catch (NoResultException e) {
			logger.error(String.format("No pending requests for this group: " + groupID), e);
			return null;
		}
	}




}
