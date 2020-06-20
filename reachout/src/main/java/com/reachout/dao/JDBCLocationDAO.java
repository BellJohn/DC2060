package com.reachout.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.reachout.models.Listing;
import com.reachout.models.ListingGUIWrapper;
import com.reachout.models.ListingStatus;
import com.reachout.models.ListingType;
import com.reachout.models.Location;
import com.reachout.models.Request;
import com.reachout.models.Service;
import com.reachout.models.User;

public class JDBCLocationDAO {
	private Logger logger = LogManager.getLogger(JDBCLocationDAO.class);

	
	public synchronized Set<ListingGUIWrapper> selectLocationsWithinRegion(double latMin, double latMax, double longMin,
			double longMax, ListingType type) {

		Set<ListingGUIWrapper> results = new HashSet<>();
		String queryString = "SELECT * FROM listings JOIN locations ON listings.LST_LOC_ID = locations.LOC_ID JOIN users on users.USERS_ID = listings.LST_USER_ID WHERE listings.LST_TYPE = ? AND (locations.LOC_LAT >= ? AND locations.LOC_LAT <= ?) AND (locations.LOC_LONG >= ? AND locations.LOC_LONG <= ?)";
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/reach_out", "reach",
				"reach_pass"); PreparedStatement ps = con.prepareStatement(queryString);) {

			ps.setInt(1, type.getOrdindal());
			ps.setDouble(2, latMin);
			ps.setDouble(3, latMax);
			ps.setDouble(4, longMin);
			ps.setDouble(5, longMax);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ListingGUIWrapper result = marshalResult(rs, type);
					results.add(result);
				}
			}
		} catch (SQLException e1) {
			logger.error("Unable to retrieve requested listings", e1);
		}
		return results;
	}

	private ListingGUIWrapper marshalResult(ResultSet rs, ListingType type) throws SQLException {
		int listingId = rs.getInt("LST_ID");
		int userId = rs.getInt("USERS_ID");
		String title = rs.getString("LST_TITLE");
		String description = rs.getString("LST_DESCRIPTION");
		String county = rs.getString("LST_COUNTY");
		String city = rs.getString("LST_CITY");
		int status = rs.getInt("LST_STATUS");
		long createdDate = rs.getLong("LST_CREATE_DATE");
		String priority = rs.getString("LST_PRIORITY");
		int locId = rs.getInt("LOC_ID");
		double locLat = rs.getDouble("LOC_LAT");
		double locLong = rs.getDouble("LOC_LONG");
		String username = rs.getString("USERS_USERNAME");
		String email = rs.getString("USERS_EMAIL");
		String first = rs.getString("USERS_FIRSTNAME");
		String last = rs.getString("USERS_LASTNAME");
		String dob = rs.getString("USERS_DOB");

		Location location = new Location(locId, locLat, locLong);
		System.out.println("new location: " + location);
		Listing listing = null;
		switch (type) {
		case REQUEST:
			// String title, String description, String county, String city, int userId,
			// ListingStatus status,String priority
			listing = new Request(title, description, county, city, userId, ListingStatus.getByOrdinal(status),
					priority, listingId);
			break;
		case SERVICE:
			// String title, String description, String county, String city, int userId,
			// ListingStatus status
			listing = new Service(title, description, county, city, userId, ListingStatus.getByOrdinal(status),
					listingId);
			break;
		default:
			throw new SQLException("Unable to parse returned Listing data");
		}
		listing.setId(listingId);
		listing.setCreatedDate(createdDate);
		// String firstName, String lastName, String username, String email, String dob
		User user = new User(first, last, username, email, dob);
		user.setId(userId);
		return new ListingGUIWrapper(listing, user, location);
	}
}
