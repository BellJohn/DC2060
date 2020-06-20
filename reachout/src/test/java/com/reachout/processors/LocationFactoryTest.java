package com.reachout.processors;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.reachout.models.Location;
import com.reachout.processors.exceptions.MappingAPICallException;

class LocationFactoryTest {

	@Test
	void buildTest() throws MappingAPICallException {
		LocationFactory lf = new LocationFactory();
		lf.buildLocation("Oakland Avenue", "Droitwich", "Worcestershire");
	}
	
	@Test
	void buildAndSaveTest() throws MappingAPICallException {
		LocationFactory lf = new LocationFactory();
		Location loc = lf.buildAndSaveLocation("Oakland Avenue", "Droitwich", "Worcestershire");
		assertNotNull(loc);
		assertTrue(loc.getLocId() != 0);
	}

}
