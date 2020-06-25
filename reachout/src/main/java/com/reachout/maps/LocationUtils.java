/**
 * 
 */
package com.reachout.maps;

/**
 * @author John
 *
 */
public class LocationUtils {

	public static final int EARTH_RADIUS = 6731;
	private LocationUtils() {
	}

	public static double getDistanceInMiles(LatLng pointA, LatLng pointB) {
		//dist = arccos(sin(lat1) · sin(lat2) + cos(lat1) · cos(lat2) · cos(lon1 - lon2)) · R
		double lat1 = Math.toRadians(pointA.getlatitude());
		double long1 = Math.toRadians(pointA.getLongitude());
		double lat2 = Math.toRadians(pointB.getlatitude());
		double long2 = Math.toRadians(pointB.getLongitude());
				
		double distanceKM = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(long1 - long2)) * EARTH_RADIUS;
		return distanceKM * 0.621371; 
	}
}
