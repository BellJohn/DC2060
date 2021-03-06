package com.reachout.processors;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.reachout.models.ListingGUIWrapper;
import com.reachout.processors.exceptions.ListingFetchServiceConstructorException;

class ListingFetchServiceTest {

	@Test
	void testCalculateDeviationByRadius() throws ListingFetchServiceConstructorException {
		ListingFetchService lfs = new ListingFetchService("request", 52.2573741,-2.1550964, 5);
		Set<ListingGUIWrapper> results = lfs.fetchLocationsWithinRadius();
		System.out.println(results);
	}

}
