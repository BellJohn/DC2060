package com.reachout.processors;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.reachout.processors.exceptions.SystemPropertiesFunctionException;

@Singleton
@Startup
public class SystemPropertiesService {
	private static final Logger logger = LogManager.getLogger(SystemPropertiesService.class);
	private static final String EMPTY_STRING = "";

	private static SystemPropertiesService spsInstance;
	private Map<String, String> systemProperties;

	@PostConstruct
	public void init() {
		logger.info("SystemPropertiesService Startup begin");
		String appLocation = new File(".").getAbsolutePath();
		logger.info(String.format("THIS APPLICATION IS RUNNING WITH A ROOT PATH OF: {%s}", appLocation));
		getInstance();
		logger.info("SystemPropertiesService Startup end");
	}

	private SystemPropertiesService() {
		systemProperties = new HashMap<>();
	}

	public static SystemPropertiesService getInstance() {
		if (spsInstance == null) {
			spsInstance = new SystemPropertiesService();
			try {
				spsInstance.updateMap();
			} catch (ClassNotFoundException e) {
				logger.error(
						"Attempted retrieval of new SystemPropertiesService. If this message is seen during runtime after boot something has gone very wrong");
			}
		}

		return spsInstance;
	}

	/**
	 * Attempt to fetch a value from the system properties map. If the map does not
	 * contain the value, it will return an empty string
	 * 
	 * @param key
	 * @return found value or empty string
	 */
	public String getProperty(String key) {
		if (systemProperties.containsKey(key)) {
			return systemProperties.get(key);
		}
		logger.debug("No property found for key " + key);
		logger.debug("Map content: " + systemProperties.toString());
		return EMPTY_STRING;
	}

	/**
	 * Provides access to the contains key method to keep system properties map
	 * private
	 * 
	 * @param key
	 * @return
	 */
	public boolean validKey(String key) {
		return systemProperties.containsKey(key);
	}

	/**
	 * Contains the logic for populating the System Properties map
	 * 
	 * @throws ClassNotFoundException
	 * @throws SystemPropertiesFunctionException
	 */
	private void updateMap() throws ClassNotFoundException {
		logger.info("Map update start");

		Class.forName("com.mysql.cj.jdbc.Driver");

		// Access the database over JDBC as the hibernate mapping may not exist on
		// startup
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/reach_out", "reach",
				"reach_pass");
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM SYSTEM_PROPERTIES")) {
			while (rs.next()) {
				String key = rs.getString(2);
				String value = rs.getString(3);
				systemProperties.put(key, value);
			}
		} catch (SQLException e) {
			logger.error("Failed to populate the SystemProperties map fully", e);
			throw new SystemPropertiesFunctionException(e);
		}

		logger.info("Map update complete");
		if (logger.isTraceEnabled()) {
			logger.trace("Map content: " + systemProperties.toString());
		}
	}
}
