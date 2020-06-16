/**
 * 
 */
package com.reachout.processors;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.reachout.dao.JDBCLocationDAO;
import com.reachout.maps.LatLng;
import com.reachout.maps.LocationUtils;
import com.reachout.models.ListingType;
import com.reachout.models.LocationListingWrapper;
import com.reachout.processors.exceptions.ListingFetchServiceConstructorException;

/**
 * @author John
 *
 */
public class ListingFetchService {
	private static final Logger logger = LogManager.getLogger(ListingFetchService.class);

	private final ListingType lt;
	private final double latitude;
	private final double longitude;

	private final LatLng centre;
	private final double radius;

	/**
	 * Creates a new ListingFetchService centred on a specified latitude and
	 * longitude, designed to fetch specific listing types within a given radius (in
	 * miles) from the database
	 * 
	 * @param type
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @throws ListingFetchServiceConstructorException
	 */
	public ListingFetchService(String type, Double latitude, Double longitude, double radius)
			throws ListingFetchServiceConstructorException {
		this.lt = ListingType.getByValue(type.toUpperCase());
		this.latitude = latitude;
		this.longitude = longitude;
		this.centre = new LatLng(latitude, longitude);
		this.radius = radius;
		logger.debug("Centre:" + centre);
		if (!checkValidConstruction()) {
			throw new ListingFetchServiceConstructorException(
					"One or more values used in constructor were not usable: [Type:%s] [Lattitude:%s] [Longitude:%s], [Radius:%s]");
		}
	}

	/**
	 * Primary orchestration method for collecting the data representing listings of
	 * a given type within a given radius from a given central location
	 * 
	 * @return
	 */
	public Set<LocationListingWrapper> fetchLocationsWithinRadius() {

		// Build a square with a width of 2 * our radius and our starting lat/long at
		// the centre
		LatLng north = calculateDeviationExtremesByBearing(0);
		LatLng south = calculateDeviationExtremesByBearing(180);
		LatLng east = calculateDeviationExtremesByBearing(90);
		LatLng west = calculateDeviationExtremesByBearing(270);

		logger.trace(String.format("Starting location: %s,%s", latitude, longitude));
		logger.trace(String.format("North %s mile: %s,%s", radius, north.getlatitude(), north.getLongitude()));
		logger.trace(String.format("South %s mile: %s,%s", radius, south.getlatitude(), south.getLongitude()));
		logger.trace(String.format("East %s mile: %s,%s", radius, east.getlatitude(), east.getLongitude()));
		logger.trace(String.format("West %s mile: %s,%s", radius, west.getlatitude(), west.getLongitude()));

		// Fetch all locations within that square

		JDBCLocationDAO listingDAO = new JDBCLocationDAO();
		Set<LocationListingWrapper> results = listingDAO.selectLocationsWithinRegion(south.getlatitude(),
				north.getlatitude(), west.getLongitude(), east.getLongitude(), lt);
		results = removeErroneousResults(results);

		return results;
	}

	/**
	 * As the system returns everything from the database within a square area, we
	 * need to trim off the results which aren't truely within our circular radius
	 * 
	 * @param results
	 * @return
	 */
	private Set<LocationListingWrapper> removeErroneousResults(Set<LocationListingWrapper> results) {
		Set<LocationListingWrapper> clean = new HashSet<>();
		for (LocationListingWrapper llw : results) {
			LatLng pointB = new LatLng(llw.getLocation().getLocLat(), llw.getLocation().getLocLong());

			if (LocationUtils.getDistanceInMiles(centre, pointB) <= radius) {
				clean.add(llw);
			}
		}

		return clean;
	}

	/**
	 * Trust the math.
	 * 
	 * This method will take a bearing and the radius specified for the
	 * ListingFetchService instance and construct a LatLng found by travelling along
	 * that bearing for the length of the radius in miles.
	 * 
	 * @param bearing
	 * @return LatLng built at the extreme end of the radius along a specific
	 *         bearing
	 */
	private LatLng calculateDeviationExtremesByBearing(double bearing) {
		double dist = radius * 1.60934;
		dist = dist / 6371;
		double brng = Math.toRadians(bearing);
		double lat1 = Math.toRadians(latitude);
		double lon1 = Math.toRadians(longitude);

		double lat2 = Math.asin(Math.sin(lat1) * Math.cos(dist) + Math.cos(lat1) * Math.sin(dist) * Math.cos(brng));

		double lon2 = lon1 + Math.atan2(Math.sin(brng) * Math.sin(dist) * Math.cos(lat1),
				Math.cos(dist) - Math.sin(lat1) * Math.sin(lat2));

		return new LatLng(Math.toDegrees(lat2), Math.toDegrees(lon2));

	}

	public ListingType getLt() {
		return lt;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getRadius() {
		return radius;
	}

	private boolean checkValidConstruction() {
		return (lt != null && radius >= 0);
	}

}
