package com.rhc.lab.dao;

import java.util.List;

import com.rhc.lab.domain.Booking;

/**
 * Interface acts as the DAO interface for miscellaneous query use with the
 * MongoTemplate
 * 
 */
// @Repository
public interface LabRepository {

	/**
	 * Method returns a list of Bookings with open dates in the future
	 * 
	 * @return
	 */
	public List<Booking> findAllFutureBookings();

}
