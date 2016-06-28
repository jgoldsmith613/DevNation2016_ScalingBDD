package com.rhc.lab.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rhc.lab.dao.BookingRepository;
import com.rhc.lab.dao.LabRepository;
import com.rhc.lab.dao.VenueRepository;
import com.rhc.lab.domain.Booking;
import com.rhc.lab.domain.BookingRequest;
import com.rhc.lab.domain.BookingResponse;
import com.rhc.lab.domain.PerformanceType;
import com.rhc.lab.domain.Venue;
import com.rhc.lab.service.BookingRequestService;

/**
 * 
 * This service is used by Camel to execute rules and call saves in the various
 * DAO repositories.
 * 
 */
@Service("requestService")
public class BookingRequestServiceImpl implements BookingRequestService {

	private static final Logger logger = LoggerFactory
			.getLogger(BookingRequestServiceImpl.class);
	@Resource(name = "bookingDao")
	BookingRepository bookingRepo;
	@Resource(name = "venueDao")
	VenueRepository venueRepo;
	@Resource(name = "labDao")
	LabRepository labRepo;

	public BookingRequestServiceImpl() {

	}

	public BookingRequestServiceImpl(BookingRepository bookingRepo,
			VenueRepository venueRepo) {
		this.bookingRepo = bookingRepo;
		this.venueRepo = venueRepo;
	}

	/**
	 * Builds session that is needed for the rules session.
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Object> buildSession(@Body BookingRequest request)
			throws Exception {

		// collect all the relevent data for the ksession
		List<Object> facts = collectSingleVenueForSession(request);
		// post the facts to the body of the exchange
		// put on the in so we do not loose the headers
		return facts;
	}

	/**
	 * Save new booking based on the booking response. Only saves booking if
	 * Booking Status is confirmed
	 * 
	 * @param response
	 *            - the response from the booking rules session
	 * @return id of new Booking
	 */
	@Override
	public String saveBooking(@Body BookingResponse response) {

		// Attempt to implement logger
		logger.info("Session returned: " + response.toString());

		Booking booking = response.generateBooking();
		try {
			// attempting to save the bookings returned
			if (response.getBookingStatus().iterator().next()
					.equalsIgnoreCase("CONFRIMED")) {

				logger.info("Attempting to save booking: " + booking.toString());
				booking = bookingRepo.save(booking);
			}
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return booking.getId();
	}

	/**
	 * Returns the venue mentioned in the request and all bookings associated
	 * with that venue
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public List<Object> collectSingleVenueForSession(BookingRequest request) {

		logger.info("collect venue: " + request.getVenueName());
		List<Venue> venue = venueRepo.findByName(request.getVenueName());

		logger.info("venue found:" + venue);
		List<Booking> bookings = bookingRepo.findByVenueName(request
				.getVenueName());

		List<Object> facts = new ArrayList<Object>();
		facts.addAll(venue);
		facts.addAll(bookings);
		facts.add(request);
		return facts;
	}

	/**
	 * This method returns all Bookings in the collection
	 * 
	 * @return
	 */
	@Override
	public List<Booking> getBookings() {
		List<Booking> bookings = new ArrayList<Booking>();
		Iterable<Booking> iterable = bookingRepo.findAll();
		bookings = IteratorUtils.toList(iterable.iterator());
		return bookings;
	}

	/**
	 * This method returns all venues in the collection
	 * 
	 * @return
	 */
	@Override
	public List<Venue> getVenues() {
		return IteratorUtils.toList(venueRepo.findAll().iterator());
	}

	/**
	 * This meathod will save a venue to the collection
	 * 
	 * @param id
	 */
	@Override
	public String saveVenue(@Body Venue venue) {
		try {
			venue = venueRepo.save(venue);
			return venue.getId();
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		}
	}

	/**
	 * Get Venue by Id. Id is found in the header named id
	 * 
	 * @param id
	 */
	@Override
	public Venue getVenueById(@Header(value = "id") String id) {
		return venueRepo.findOne(id);
	}

	/**
	 * Get Booking by Id. Id is found on the Camel header named id
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Booking getBookingById(@Header(value = "id") String id) {
		return bookingRepo.findOne(id);
	}

	/**
	 * Method used to delete a particular booking
	 * 
	 * @param id
	 */
	@Override
	public void deleteBooking(@Header(value = "id") String id) {
		bookingRepo.delete(id);
	}

	/**
	 * Method used to update a particular booking
	 * 
	 * @param id
	 *            of booking
	 */
	@Override
	public String updateBooking(@Header(value = "id") String id,
			@Body Booking updatedBooking) {
		// TODO: update the booking
		return "booking not updated";

	}

	@Override
	public String updateVenue(@Header(value = "id") String id,
			@Body Venue updatedVenue) {
		// TODO Auto-generated method stub
		return "Venue not updated";
	}

	@Override
	public void deleteVenue(@Header(value = "id") String id) {
		venueRepo.delete(id);
	}

	@Override
	public Map<String, String> getPerformanceTypes() {
		Map<String, String> types = new HashMap<String, String>();
		for (PerformanceType p : PerformanceType.values()) {
			types.put(p.name(), p.getLabel());
		}
		return types;
	}

}
