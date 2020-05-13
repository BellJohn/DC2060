package com.reachout.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Class responsible for Hibernate configuration and management.
 * </br>
 * Singleton instance so all connections are managed from a single source
 * @author John
 *
 */
public final class HibernateUtil {

	private static HibernateUtil util;
	private SessionFactory sessionFactory;
	
	/**
	 * Hidden private constructor responsible for setting up the connection requirements for the system to the database
	 */
	private  HibernateUtil() {
		// configures settings from hibernate.cfg.xml
		sessionFactory = new MetadataSources(new StandardServiceRegistryBuilder().configure().build()).buildMetadata().buildSessionFactory();
	}
	
	/**
	 * Get the HibernateUtil Instance
	 * @return
	 */
	public static HibernateUtil getInstance() {
		if(util == null) {
			util = new HibernateUtil();
		}
		return util;
	}
	
	/**
	 * Returns an open session for connectivity to the database
	 * </br>
	 * <b>Make sure this session object is closed or used within a try-with-resources block
	 * @return
	 */
	public Session getSession() {
		return sessionFactory.openSession();
	}

}
