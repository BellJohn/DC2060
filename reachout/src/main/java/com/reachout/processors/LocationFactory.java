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
	private static final String KEY = "&key=AIzaSyAacgfRpZuxnVNV4x7DoZ01yrY_jcSmJLc";

	public LocationFactory() {
		// Needs no constructor body
	}

	/**
	 * Builds a new location object with the correct lat/long found by google api with the data provided.
	 * Does NOT save the location to the database on creation so the location object will be missing an ID.
	 * @param address
	 * @param city
	 * @param county
	 * @return
	 */
	public Location buildLocation(String address, String city, String county) {
		Location location = null;

		try {
			String locationData = fetchData(address, city, county);

			JSONObject jObj = new JSONObject(locationData);

			JSONArray jarray = jObj.getJSONArray("results");
			JSONObject locationObject = null;
			if(jarray.get(0) instanceof JSONObject) {
				locationObject = ((JSONObject) jarray.get(0)).getJSONObject("geometry").getJSONObject("location");
			}
			if(locationObject == null) {
				throw new JSONException("Unable to find location object within returned data");
			}
			double lat = locationObject.getDouble("lat");
			double lng = locationObject.getDouble("lng");
			location = new Location();
			location.setLocLat(lat);
			location.setLocLong(lng);
		} catch (IOException | JSONException e) {
			logger.error("Unable to fetch location data", e);
			location = null;
		}

		return location;
	}

	
	public Location buildAndSaveLocation(String address, String city, String county) {
		Location location = buildLocation(address, city, county);
		
		if(location == null) {
			return location;
		}
		
		HibernateLocationDAO locationDAO = new HibernateLocationDAO();
		Integer result = locationDAO.save(location);
		if(result != null) {
			location.setLocId(result);
		}
		
		return location;
	}
	
	
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
