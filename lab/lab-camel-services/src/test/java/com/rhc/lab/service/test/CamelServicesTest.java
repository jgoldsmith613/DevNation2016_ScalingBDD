package com.rhc.lab.service.test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.restlet.engine.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

import com.rhc.lab.dao.BookingRepository;
import com.rhc.lab.dao.LabRepository;
import com.rhc.lab.dao.VenueRepository;
import com.rhc.lab.domain.Booking;
import com.rhc.lab.domain.BookingRequest;
import com.rhc.lab.domain.PerformanceType;
import com.rhc.lab.domain.Performer;
import com.rhc.lab.domain.Venue;

@SuppressWarnings("deprecation")
@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ActiveProfiles({ "test", "remote" })
@ContextConfiguration(locations = {"classpath:camel-context.xml"})
public class CamelServicesTest {

	private static final Logger logger = LoggerFactory
			.getLogger(CamelServicesTest.class);

	@Autowired
	protected CamelContext camelContext;

	@Resource(name = "bookingDao")
	BookingRepository bookingDao;
	@Resource(name = "venueDao")
	VenueRepository venueDao;
	@Resource(name = "labDao")
	LabRepository labDao;

	@EndpointInject(uri = "mock:end")
	protected MockEndpoint mockEnd;

	@Produce(uri = "direct:getBookings")
	protected ProducerTemplate template1;

	Performer performer1 = new Performer("The Doors", "a band",
			PerformanceType.BAND.name());
	Performer performer2 = new Performer("New York City Ballet",
			"fancy dancers", PerformanceType.BALLET.name());

	Venue venue1 = new Venue("Madison Square Garden", "New York City", 10000,
			PerformanceType.BAND.name(), PerformanceType.COMIC.name(),
			PerformanceType.ORCHESTRA.name());
	Venue venue2 = new Venue("Lincoln Square", "New York City", 5000,
			PerformanceType.BALLET.name());

	BookingRequest request1 = new BookingRequest(venue1.getName(),
			Date.valueOf("2016-06-01"), Date.valueOf("2016-06-02"), performer1);
	BookingRequest request2 = new BookingRequest(venue2.getName(),
			Date.valueOf("2016-06-01"), Date.valueOf("2016-06-02"), performer2);
	BookingRequest request3 = new BookingRequest(venue1.getName(),
			Date.valueOf("2016-06-01"), Date.valueOf("2016-06-02"), performer2);

	Booking book1 = new Booking(request1);
	Booking book2 = new Booking(request2);

	RouteBuilder testRoutes;

	@Before
	public void setUp() {
		bookingDao.deleteAll();
		venueDao.deleteAll();
		MockEndpoint.resetMocks(camelContext);
	}

	@Test
	public void shouldSaveVenue() throws InterruptedException {
		// Given 1 venue
		mockEnd.expectedMessageCount(1);

		// when I send a venue
		template1.sendBody("direct:newVenue", venue1);

		// then I expect the id of that venue to return
		mockEnd.assertIsSatisfied();
		Exchange exchange = mockEnd.getExchanges().get(0);
		Message message = exchange.getIn();
		String venueId = message.getBody(String.class);
		Assert.assertEquals("Did not return expect values", "0", venueId);
	}

	@Test
	public void shouldRetrieveVenues() throws InterruptedException {
		// given 2 venues saved
		venueDao.save(venue1);
		venueDao.save(venue2);

		// expect 1 response
		mockEnd.expectedMessageCount(1);

		// when I send something to get Venuew
		template1.sendBody("direct:getVenues", "Bookings");

		// Then I expect a message returned
		mockEnd.assertIsSatisfied();

		// And I expect both venues to be returned
		Exchange exchange = mockEnd.getExchanges().get(0);
		Message message = exchange.getIn();
		ArrayList<?> venues = message.getBody(ArrayList.class);
		Assert.assertEquals("Did not return expect values", 2, venues.size());
		Assert.assertTrue("Does not contain venue1", venues.contains(venue1));
		Assert.assertTrue("Does not contain venue2", venues.contains(venue2));
	}

	@Ignore
	@Test
	public void shouldUpdateVenue() throws InterruptedException {
		// TODO: write unit test for happy path on updating
	}

	@Test
	public void shouldDeleteVenue() throws InterruptedException {
		// given two saved venues
		venue1 = venueDao.save(venue1);
		venue2 = venueDao.save(venue2);

		// expect 1 response
		mockEnd.expectedMessageCount(1);

		// when I send a message to delete a venue
		template1.sendBodyAndHeader("direct:deleteVenue", "", "id",
				venue1.getId());

		// Then I expect a message returned
		mockEnd.assertIsSatisfied();

		Collection<Venue> venues = (Collection<Venue>) venueDao.findAll();
		// And I expect there to only be 1 venue in db
		Assert.assertEquals("Venues saved differnt than expected", 1,
				venues.size());
		// And that venue is venue2
		Assert.assertTrue("Wrong Venue deleted", venues.contains(venue2));

	}

	@Test
	public void shouldRetriveSingleVenue() throws InterruptedException {
		// given 2 venues saved in DB
		venue1 = venueDao.save(venue1);
		venue2 = venueDao.save(venue2);

		// expect 1 response
		mockEnd.expectedMessageCount(1);

		// when I send a message to get venue1
		template1.sendBodyAndHeader("direct:getVenueById", "", "id",
				venue1.getId());

		mockEnd.assertIsSatisfied();

		Exchange exchange = mockEnd.getExchanges().get(0);
		Message message = exchange.getIn();
		Venue venue = message.getBody(Venue.class);
		Assert.assertEquals("Did not return expect values", venue1, venue);

	}

	@Test
	public void shouldSaveBooking() throws InterruptedException {
		// given a saved venue
		venue1 = venueDao.save(venue1);
		// and a booking request for that venue
		// request1
		// expect 1 response
		mockEnd.expectedMessageCount(1);

		// when I send a message with the booking request
		template1.sendBody("direct:newBooking", request1);

		// then I expect to recieve a message back with a string id
		mockEnd.assertIsSatisfied();
		String bookingId = mockEnd.getExchanges().get(0).getIn()
				.getBody(String.class);
		Assert.assertFalse("booking Id not returned",
				StringUtils.isNullOrEmpty(bookingId));
		// Assert booking is saved to database
		Booking booking = bookingDao.findOne(bookingId);
		Assert.assertNotNull(booking);
		Assert.assertEquals("not the right booking", book1, booking);

	}

	@Test
	public void shouldRetrieveAllBookings() throws InterruptedException {
		// given a saved booking and corresponding venue
		venue1 = venueDao.save(venue1);
		venue2 = venueDao.save(venue2);
		book1 = bookingDao.save(book1);
		book2 = bookingDao.save(book2);

		// I expect only one reponse
		mockEnd.expectedMessageCount(1);

		// When I call a request get all bookings
		template1.sendBody("direct:getBookings", "");

		// then I expect to recieve both bookings back
		mockEnd.assertIsSatisfied();

		Exchange exchange = mockEnd.getExchanges().get(0);
		Message message = exchange.getIn();
		ArrayList<?> bookings = message.getBody(ArrayList.class);
		Assert.assertEquals("Did not return expect values", 2, bookings.size());
		Assert.assertTrue("Does not contain venue1", bookings.contains(book1));
		Assert.assertTrue("Does not contain venue2", bookings.contains(book2));

	}

	@Test
	public void shouldRetriveSingleBookingById() throws InterruptedException {
		// give two saved bookings
		book1 = bookingDao.save(book1);
		book2 = bookingDao.save(book2);

		// I only expect 1 response
		mockEnd.expectedMessageCount(1);

		// when I call a request to get bookings by Id
		template1.sendBodyAndHeader("direct:getBookingById", "", "id",
				book1.getId());

		// then I expet to recive a message with only 1 booking
		mockEnd.assertIsSatisfied();
		Booking returnedBooking = mockEnd.getExchanges().get(0).getIn()
				.getBody(Booking.class);
		Assert.assertEquals("returned wrong booking", book1, returnedBooking);
	}

	@Test
	public void shouldDeleteSingleBooking() throws InterruptedException {
		// given 2 saved bookings
		book1 = bookingDao.save(book1);
		book2 = bookingDao.save(book2);

		// expect 1 response
		mockEnd.expectedMessageCount(1);

		// when I request to delete a particular booking
		template1.sendBodyAndHeader("direct:deleteBooking", "", "id",
				book1.getId());

		// then I expect no body returned
		mockEnd.assertIsSatisfied();
		Assert.assertTrue(StringUtils.isNullOrEmpty(mockEnd.getExchanges()
				.get(0).getIn().getBody(String.class)));

		// and I expect the booking to be deleted in the dao
		Collection<Booking> books = (Collection<Booking>) bookingDao.findAll();
		Assert.assertEquals(1, books.size());
		Assert.assertTrue("Booking did not return book2", books.contains(book2));
	}

	@Ignore
	@Test
	public void shouldUpdateABooking() {
		// TODO: Write test for updating bookings
	}

	@Test
	public void shouldRetrievePerformanceTypes() throws InterruptedException {
		PerformanceType[] pt = PerformanceType.values();

		mockEnd.expectedMessageCount(1);

		template1.sendBody("direct:getPerformanceTypes", "");

		mockEnd.assertIsSatisfied();

		Map<?, ?> result = mockEnd.getExchanges().get(0).getIn()
				.getBody(HashMap.class);
		Assert.assertNotNull(result);
		logger.info("returned value" + result);
		for (PerformanceType p : pt) {
			Assert.assertTrue("Performance Type not included",
					result.containsValue(p.toString()));
		}
	}

}