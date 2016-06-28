package com.rhc.lab.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.rhc.lab.domain.Booking;

/**
 * 
 * This interface extends the Spring-Data-MongoDB interface, exposing base
 * MongoDB operations and additional field level queries for the bookings
 * collection
 * 
 */
// @Repository(value = "bookingDao")
public interface BookingRepository
		extends
			PagingAndSortingRepository<Booking, String> {

	/**
	 * Function returns a list of Bookings by attribute "venueName" (expected
	 * return is list size of 1)
	 * 
	 * @param name
	 * @return
	 */
	public List<Booking> findByVenueName(String name);
}
