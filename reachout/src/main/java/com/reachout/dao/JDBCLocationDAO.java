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
import com.reachout.models.ListingStatus;
import com.reachout.models.ListingType;
import com.reachout.models.Location;
import com.reachout.models.LocationListingWrapper;
import com.reachout.models.Request;
import com.reachout.models.Service;
import com.reachout.processors.exceptions.SystemPropertiesFunctionException;

public class JDBCLocationDAO {
	private Logger logger = LogManager.getLogger(JDBCLocationDAO.class);

	public Set<LocationListingWrapper> selectLocationsWithinRegion(double latMin, double latMax, double longMin,
			double longMax, ListingType type) {

		Set<LocationListingWrapper> results = new HashSet<>();
		String queryString = "SELECT * FROM listings JOIN locations ON listings.LST_LOC_ID = locations.locId WHERE listings.LST_TYPE = ? AND (locations.locLat >= ? AND locations.locLat <= ?) AND (locations.locLong >= ? AND locations.locLong <= ?)";
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/reach_out", "reach",
				"reach_pass"); PreparedStatement ps = con.prepareStatement(queryString);) {

			ps.setInt(1, type.getOrdindal());
			ps.setDouble(2, latMin);
			ps.setDouble(3, latMax);
			ps.setDouble(4, longMin);
			ps.setDouble(5, longMax);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					int listingId = rs.getInt(1);
					int userId = rs.getInt(2);
					String title = rs.getString(3);
					String description = rs.getString(4);
					String county = rs.getString(5);
					String city = rs.getString(6);
					int status = rs.getInt(7);
					long createDate = rs.getLong(9);
					String priority = rs.getString(10);
					int locId = rs.getInt(11);
					double locLat = rs.getDouble(13);
					double locLong = rs.getDouble(14);

					LocationListingWrapper result = marshalResult(listingId, userId, title, description, county, city,
							status, createDate, priority, locId, locLat, locLong, type);
					System.out.println(String.format("Made a new locationListingWrapper: [%s]", result.toString()));
					results.add(result);
				}
			} catch (SQLException e) {
				logger.error("Failed to populate the SystemProperties map fully", e);
				throw new SystemPropertiesFunctionException(e);
			}
		} catch (SQLException e1) {
			logger.error("Unable to retrieve requested listings",e1);
		}
		return results;
	}

	private LocationListingWrapper marshalResult(int listingId, int userId, String title, String description,
			String county, String city, int status, long createdDate, String priority, int locId, double locLat,
			double locLong, ListingType type) throws SQLException {

		Location location = new Location(locId, locLat, locLong);
		System.out.println("new location: " + location);
		Listing listing = null;
		switch (type) {
		case REQUEST:
			// String title, String description, String county, String city, int userId,
			// ListingStatus status,String priority
			listing = new Request(title, description, county, city, userId, ListingStatus.getByOrdinal(status),
					priority);
			break;
		case SERVICE:
			// String title, String description, String county, String city, int userId,
			// ListingStatus status
			listing = new Service(title, description, county, city, userId, ListingStatus.getByOrdinal(status));
			break;
		default:
			throw new SQLException("Unable to parse returned Listing data");
		}
		listing.setId(listingId);
		listing.setCreatedDate(createdDate);
		return new LocationListingWrapper(location, listing);
	}
}
