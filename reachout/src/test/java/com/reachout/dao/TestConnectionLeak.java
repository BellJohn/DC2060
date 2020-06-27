package com.reachout.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;

import org.junit.jupiter.api.Test;

public class TestConnectionLeak {

	/**
	 * Test to see if the database connection leak returns.
	 * </br>
	 * Testing principle is to spam call different database accessors from the codebase.
	 * </br>The active thread count returned over JDBC should remain constant (offset by one as the initial connection must be made)
	 * @throws ClassNotFoundException
	 * @throws ParseException
	 */
	@Test
	public void testConnectionLeak() throws ClassNotFoundException, ParseException {
		Class.forName("com.mysql.cj.jdbc.Driver");

		String beforeTitle = "";
		int beforeValue = -2;
		String afterTitle = "";
		int afterValue = -1;
		
		// Access the database over JDBC as the problem lies in the Hibernate configuration
		// This returns the active thread count from the database. 
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/reach_out", "reach",
				"reach_pass");
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("show status like 'Threads_connected'")) {

			rs.next();
			beforeTitle = rs.getString(1);
			beforeValue = rs.getInt(2);

		} catch (Exception e) {
			System.out.println(e);
			fail(e);
		}
		// Spam connections to the database
		HibernateUserDAOImpl userDAO = new HibernateUserDAOImpl();
		for (int i = 0; i < 200; i++) {
			userDAO.getAllUsers();
		}
		//Spam some more
		HibernatePasswordDAOImpl passDAO = new HibernatePasswordDAOImpl();
		for (int i = 0; i < 200; i++) {
			passDAO.getAllPasswords();
		}
		// Fetch the new active thread count over JDBC
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/reach_out", "reach",
				"reach_pass");
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("show status like 'Threads_connected'")) {

			rs.next();
			afterTitle = rs.getString(1);
			afterValue = rs.getInt(2);
		} catch (Exception e) {
			System.out.println(e);
			fail(e);
		}
		System.out.println(beforeTitle + ", " + beforeValue);
		System.out.println(afterTitle + ", " + afterValue);

		assertEquals(beforeTitle, afterTitle);

		assertEquals(beforeValue, afterValue);
	}

}