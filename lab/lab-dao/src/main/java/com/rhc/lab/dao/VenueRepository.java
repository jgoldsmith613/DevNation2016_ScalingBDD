package com.rhc.lab.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.rhc.lab.domain.Venue;

/**
 * 
 * This interface extends the Spring-Data-MongoDB interface, exposing base
 * MongoDB operations and additional field level queries for the venues
 * collection
 * 
 */
// @Repository(value = "venueDao")
public interface VenueRepository
		extends
			PagingAndSortingRepository<Venue, String> {

	/**
	 * Function returns a list of Venues by attribute "name" (expected return is
	 * list size of 1)
	 * 
	 * @param name
	 * @return
	 */
	public List<Venue> findByName(String name);

}
