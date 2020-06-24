/**
 * 
 */
package com.reachout.processors;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.reachout.dao.HibernateLocationDAO;
import com.reachout.models.Location;
import com.reachout.processors.exceptions.MappingAPICallException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * @author John
 *
 */
public class LocationFactory {
	public final Logger logger = LogManager.getLogger(LocationFactory.class);
	private static final String GOOGLE_URL = "https://maps.googleapis.com/maps/api/geocode/json";
	private static final String KEY = "&key=" + SystemPropertiesService.getInstance().getProperty("API_KEY");

	public LocationFactory() {
		// Needs no constructor body
	}

	/**
	 * Builds a new location object with the correct lat/long found by google api
	 * with the data provided. Does NOT save the location to the database on
	 * creation so the location object will be missing an ID.
	 * 
	 * @param address
	 * @param city
	 * @param county
	 * @return New location object (non-persisted) or null on failure
	 * @throws MappingAPICallException
	 */
	public Location buildLocation(String address, String city, String county) throws MappingAPICallException {
		Location location = null;

		try {
			String locationData = fetchData(address, city, county);
			JSONObject jObj = new JSONObject(locationData);
			String status = jObj.getString("status");
			if ("OK".equalsIgnoreCase(status)) {
				JSONArray jarray = jObj.getJSONArray("results");
				JSONObject locationObject = null;
				if (jarray.get(0) instanceof JSONObject) {
					locationObject = ((JSONObject) jarray.get(0)).getJSONObject("geometry").getJSONObject("location");
				}
				if (locationObject == null) {
					throw new JSONException("Unable to find location object within returned data");
				}
				double lat = locationObject.getDouble("lat");
				double lng = locationObject.getDouble("lng");
				location = new Location();
				location.setLocLat(lat);
				location.setLocLong(lng);
				logger.debug("Constructed new location for [%s,%s,%s] : [%s,%s]", address, city, county, lat, lng);
			} else {
				throw new MappingAPICallException(String.format(
						"Unable to fetch data requested. Response was [%s]. Full content retrieved was [%s]", status,
						locationData));
			}
		} catch (IOException | JSONException e) {
			logger.error("Unable to fetch location data", e);
			location = null;
		}

		return location;
	}

	/**
	 * Attempts to create a new Location and persist in the database
	 * 
	 * @param address
	 * @param city
	 * @param county
	 * @return Persisted Location or Null on failure
	 * @throws MappingAPICallException
	 */
	public Location buildAndSaveLocation(String address, String city, String county) throws MappingAPICallException {
		Location location = buildLocation(address, city, county);

		if (location == null) {
			return location;
		}

		HibernateLocationDAO locationDAO = new HibernateLocationDAO();
		Integer result = locationDAO.save(location);
		if (result != null) {
			location.setLocId(result);
		}

		return location;
	}

	/**
	 * Worker method for figuring out the actual geographic location of the address
	 * details provided
	 * 
	 * @param address
	 * @param city
	 * @param county
	 * @return JSON location data
	 * @throws IOException
	 */
	private String fetchData(String address, String city, String county) throws IOException {
		OkHttpClient client = new OkHttpClient();
		String locationRequest = String.format("?address=%s,%s,%s,UK", address, city, county);
		locationRequest = locationRequest.replaceAll(" ", "+");
		String completeURL = String.format("%s%s%s", GOOGLE_URL, locationRequest, KEY);
		Request request = new Request.Builder().url(completeURL).build();
		try (ResponseBody responseBody = client.newCall(request).execute().body()) {
			return responseBody.string();
		}
	}

}
