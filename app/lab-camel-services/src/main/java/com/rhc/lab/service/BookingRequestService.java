package com.rhc.lab.service;

import java.util.List;
import java.util.Map;

import com.rhc.lab.domain.Booking;
import com.rhc.lab.domain.BookingRequest;
import com.rhc.lab.domain.BookingResponse;
import com.rhc.lab.domain.Venue;

public interface BookingRequestService {
	/**
	 * Builds session that is needed for the rules session.
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<Object> buildSession(BookingRequest request) throws Exception;

	/**
	 * Returns the venue mentioned in the request and all bookings associated
	 * with that venue
	 * 
	 * @param request
	 * @return
	 */
	public List<Object> collectSingleVenueForSession(BookingRequest request);

	/**
	 * This method returns all venues in the collection
	 * 
	 * @return
	 */
	public List<Venue> getVenues();
	/**
	 * Get Venue by Id. Id is found in the header named id
	 * 
	 * @param id
	 */
	public Venue getVenueById(String id);
	/**
	 * This meathod will save a venue to the collection
	 * 
	 * @param id
	 */
	public String saveVenue(Venue venue);
	/**
	 * Method used to update a venue
	 * 
	 * @param id
	 * @return
	 */
	public String updateVenue(String id, Venue updatedVenue);
	/**
	 * Meathod to delete particular id
	 * 
	 * @param id
	 */
	public void deleteVenue(String id);
	/**
	 * This method returns all Bookings in the collection
	 * 
	 * @return
	 */
	public List<Booking> getBookings();
	/**
	 * Get Booking by Id. Id is found on the Camel header named id
	 * 
	 * @param id
	 * @return
	 */
	public Booking getBookingById(String id);
	/**
	 * Save new booking based on the booking response. Only saves booking if
	 * Booking Status is confirmed
	 * 
	 * @param response
	 *            - the response from the booking rules session
	 * @return id of new Booking
	 */
	public String saveBooking(BookingResponse response);
	/**
	 * Method used to delete a particular booking
	 * 
	 * @param id
	 */
	public void deleteBooking(String id);
	/**
	 * Method used to update a particular booking
	 * 
	 * @param id
	 *            of booking
	 */
	public String updateBooking(String id, Booking updatedBooking);

	/**
	 * Method used to retrieve all accommodation types
	 * 
	 * @return
	 */
	public Map<String, String> getPerformanceTypes();

}
