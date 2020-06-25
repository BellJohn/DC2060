/**
 * 
 */
package com.reachout.maps;

import java.text.DecimalFormat;

/**
 * @author John
 *
 */
public class LatLng {

	private Double latitude;
	private Double longitude;

	public LatLng(Double latitude, Double longitude) {
		DecimalFormat df = new DecimalFormat("#.#######");

		this.latitude = Double.valueOf(df.format(latitude));
		this.longitude = Double.valueOf(df.format(longitude));

	}

	public Double getlatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return String.format("LatLng: [%s,%s]", latitude, longitude);
	}
}
