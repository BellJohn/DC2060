/**
 * 
 */
package com.reachout.maps;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author John
 *
 */
class LocationUtilsTest {

	@Test
	void testGetDistanceInMiles() {
		LatLng pointA = new LatLng(51.5045, 0.0865);
		LatLng pointB = new LatLng(52.4869, 1.8882);
		assertEquals(108.16, LocationUtils.getDistanceInMiles(pointA, pointB), 0.5);
	}

}
