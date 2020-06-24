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

/**
 * @author Jessica
 *
 */
public class HibernateGroupMemberDAOImpl {

	private static final Logger logger = LogManager.getLogger(HibernateGroupMemberDAOImpl.class);
	private static final String USER_ID = "userId";

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
			logger.error("Failed to save groupMember", e);
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
			Query query = session.createQuery(
					"SELECT groupId FROM GroupMember groupMember WHERE GM_G_UID = :userId AND NOT GM_U_STATUS_ID = 0");
			query.setParameter(USER_ID, userId);
			List<?> returnedData = query.getResultList();
			ArrayList<Integer> castedData = new ArrayList<>();
			for (Object result : returnedData) {
				if (result instanceof Integer) {
					castedData.add((Integer) result);
				}
			}
			List<Group> results = new ArrayList<>();
			HibernateGroupDAOImpl groupDAO = new HibernateGroupDAOImpl();
			for (int id : castedData) {
				results.add(groupDAO.selectById(id));
			}
			return results;
		}
	}

	/**
	 * Returns a collection of all known groups the user is not a member of
	 * 
	 * @return
	 */
	public Set<Integer> getNonUserGroups(int userId) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery(
					"SELECT groupMember.groupId FROM GroupMember groupMember WHERE NOT GM_G_UID = :userId AND NOT GM_U_STATUS_ID = 0");
			query.setParameter(USER_ID, userId);
			List<Integer> groupIds = new ArrayList<>();
			List<?> returnedData = query.getResultList();
			for (Object result : returnedData) {
				if (result instanceof Integer) {
					groupIds.add((Integer) result);
				}
			}
			// remove any groups the user is a member of
			List<Integer> userGroupIds = new ArrayList<>();
			for (int id : groupIds) {
				for (Group g : getUserGroups(userId)) {
					if (id == g.getId()) {
						userGroupIds.add(id);
					}
				}
			}
			// remove any pending groups
			List<Integer> pendingGroupIds = new ArrayList<>();
			for (int id : groupIds) {
				for (int i : getPendingGroups(userId)) {
					if (i == id) {
						pendingGroupIds.add(i);
					}
				}
			}
			groupIds.removeAll(userGroupIds);
			groupIds.removeAll(pendingGroupIds);

			// make sure no duplicates
			Set<Integer> results = new HashSet<>();
			for (int id : groupIds) {
				results.add(id);
			}
			return results;
		}
	}

	public List<Integer> getPendingGroups(int userId) {
		List<Integer> returnVal = new ArrayList<>();
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery(
					"SELECT groupMember.groupId FROM GroupMember groupMember WHERE GM_G_UID = :userId AND GM_U_STATUS_ID = 0");
			query.setParameter(USER_ID, userId);
			List<?> results = query.getResultList();
			for (Object result : results) {
				if (result instanceof Integer) {
					returnVal.add((Integer) result);
				}
			}
		}
		return returnVal;
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
			Query query = session.createNativeQuery("DELETE FROM ALL_GROUPS WHERE GM_ID = :gm_id");
			query.setParameter("gm_id", groupMember.getId());
			query.executeUpdate();
			session.flush();
			session.getTransaction().commit();
		} catch (IllegalStateException | RollbackException e) {
			logger.error("Failed to delete groupMember", e);
			return false;
		}
		return true;
	}

	public GroupMember selectById(int userId) {
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery("SELECT groupMember FROM GroupMember groupMember where id = :userId",
					GroupMember.class);
			query.setParameter(USER_ID, userId);
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
			logger.error("Failed to update groupMember", e);
			return false;
		}
		return true;
	}

	public GroupMember checkIfGroupMember(int userId, int groupId) {
		GroupMember groupMember = null;
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery(
					"SELECT groupMember FROM GroupMember groupMember where userId = :userId AND groupId = :groupId",
					GroupMember.class);
			query.setParameter(USER_ID, userId);
			query.setParameter("groupId", groupId);
			List<?> results = query.getResultList();
			if (!results.isEmpty() && (results.get(0) instanceof GroupMember)) {
				groupMember = (GroupMember) results.get(0);
			}
		}
		return groupMember;
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
			logger.error("Failed to remove associated members from group", e);
			return false;
		}
		return true;
	}

	// Get all requests with a value of 0 (pending) for a specific group
	public List<GroupMember> getPendingMembers(int groupID) {
		List<GroupMember> returnVal = new ArrayList<>();
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery(
					"SELECT groupMember FROM GroupMember groupMember WHERE GM_G_ID = :group_id AND GM_U_STATUS_ID = 0");
			query.setParameter("group_id", groupID);
			List<?> results = query.getResultList();
			for (Object result : results) {
				if (result instanceof GroupMember) {
					returnVal.add((GroupMember) result);
				}
			}
		} catch (NoResultException e) {
			logger.error(String.format("No pending requests for this group: {%s}", groupID), e);
		}
		return returnVal;
	}

	public List<Integer> getUserGroupIDs(int userId) {
		List<Integer> returnVal = new ArrayList<>();
		try (Session session = HibernateUtil.getInstance().getSession()) {
			Query query = session.createQuery(
					"SELECT groupId FROM GroupMember groupMember WHERE GM_G_UID = :userId AND NOT GM_U_STATUS_ID = 0");
			query.setParameter(USER_ID, userId);
			List<?> results = query.getResultList();
			for (Object result : results) {
				if (result instanceof Integer) {
					returnVal.add((Integer) result);
				}
			}
		}
		return returnVal;
	}

}
