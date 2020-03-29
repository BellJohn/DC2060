package com.reachout.dao;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public abstract class HibernateDAO {

	private SessionFactory sessionFactory;

	public HibernateDAO() {
		// configures settings from hibernate.cfg.xml
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();

		sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
