package com.reachout.processors;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.reachout.models.Location;

class LocationFactoryTest {

	@Test
	void buildTest() {
		LocationFactory lf = new LocationFactory();
		lf.buildLocation("Oakland Avenue", "Droitwich", "Worcestershire");
	}
	
	@Test
	void buildAndSaveTest() {
		LocationFactory lf = new LocationFactory();
		Location loc = lf.buildAndSaveLocation("Oakland Avenue", "Droitwich", "Worcestershire");
		assertNotNull(loc);
		assertTrue(loc.getLocId() != 0);
	}

}
