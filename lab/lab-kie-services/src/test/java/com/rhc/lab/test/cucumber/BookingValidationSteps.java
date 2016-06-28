package com.rhc.lab.test.cucumber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.rhc.lab.domain.Booking;
import com.rhc.lab.domain.BookingRequest;
import com.rhc.lab.domain.BookingResponse;
import com.rhc.lab.domain.Performer;
import com.rhc.lab.domain.Venue;
import com.rhc.lab.kie.api.StatelessDecisionService;
import com.rhc.lab.test.repository.BookingCucumberRepository;
import com.rhc.lab.test.repository.VenueCucumberRepository;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({"classpath*:cucumber.xml", "classpath:kie-context.xml"})
@ActiveProfiles({"remote", "test"})
public class BookingValidationSteps {
	@Resource(name = "decisionService")
	private StatelessDecisionService decisionService;

	protected static final Logger logger = LoggerFactory
			.getLogger(BookingValidationSteps.class);

	protected VenueCucumberRepository venueRepo = new VenueCucumberRepository();
	protected BookingCucumberRepository bookingRepo = new BookingCucumberRepository();
	protected Venue venue = new Venue();
	protected Booking booking = new Booking();
	protected BookingRequest request = new BookingRequest();
	protected BookingResponse response = new BookingResponse();
	protected List<Object> facts;

	protected static final String processId = "bookingProcess";

	@Given("^a venue \"(.*?)\" with an occupancy of \"(.*?)\"$")
	public void a_venue_with_an_occupancy_of(String venueName, String occupancy)
			throws Throwable {
		// Set properties regarding venueName/occupancy
		venue.setName(venueName);
		venue.setCapacity(Integer.parseInt(occupancy));

		// (Test repo-maps) Add venue to venueRepo
		if (venueRepo.findByName(venueName).isEmpty()) {
			venueRepo.save(venue);
		}

		request.setVenueName(venueName);

		logger.info("Given step: " + venueName + " " + occupancy);
	}

	@Given("^an existing \"(.*?)\" performance by \"(.*?)\" from \"(.*?)\" to \"(.*?)\"$")
	public void an_existing_performance_by_from_to(String performanceType,
			String performanceName, String open, String close) throws Throwable {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
		Performer performer = new Performer();
		performer.setName(performanceName);
		performer.setType(performanceType);

		booking = new Booking();
		booking.setPerformer(performer);
		Date dOpen = sdf.parse(open);
		Date dClose = sdf.parse(close);
		Assert.assertNotNull(dOpen);
		Assert.assertNotNull(dClose);
		booking.setOpen(dOpen);
		booking.setClose(dClose);
		booking.setVenueName(venue.getName());

		logger.info("saving previous booking " + booking.toString());
		bookingRepo.save(booking);

	}

	@Given("^the venue accomodates performances by a$")
	public void the_venue_accomodates_performances_by_a(List<String> artistTypes)
			throws Throwable {
		// List<PerformanceType> accomodations = new ArrayList<String>();
		// for (String artistType : artistTypes) {
		// accomodations.add(PerformanceType.valueOf(artistType));
		// }

		venue.setAccomodations(artistTypes);

		logger.info("And first step: " + artistTypes);

	}

	@And("^a request for a \"(.*?)\" performance by \"(.*?)\"$")
	public void a_request_for_a_performance_by(String type, String artistName)
			throws Throwable {
		// Set properties regarding performance requests
		Performer performer = new Performer();
		performer.setName(artistName);
		try {
			performer.setType(type);
		} catch (Exception e) {
			throw new Exception("Type '" + type + "' does not exist");
		}
		request.setPerformer(performer);

		logger.info("And second step: " + type + " " + artistName);
	}

	@And("^a dated request for a \"(.*?)\" performance by \"(.*?)\" from \"(.*?)\" to \"(.*?)\"$")
	public void a_request_for_a_performance_by_from(String type,
			String artistName, String open, String close) throws Throwable {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
		// Set properties regarding performance requests
		Performer performer = new Performer();
		performer.setName(artistName);
		try {
			performer.setType(type);
		} catch (Exception e) {
			throw new Exception("Type '" + type + "' does not exist");
		}
		request.setPerformer(performer);
		Date dOpen = sdf.parse(open);
		Date dClose = sdf.parse(close);
		Assert.assertNotNull(dOpen);
		Assert.assertNotNull(dClose);
		request.setOpen(dOpen);
		request.setClose(dClose);
		request.setVenueName(venue.getName());

		logger.info("And second step: " + request.toString());
	}

	@Then("^the booking should be \"(.*?)\"$")
	public void the_booking_should_be(String bookingStatus) throws Throwable {
		// XXX-Instructions
		logger.info("expected : " + response);
		if (response.getBookingStatus() != null
				&& !response.getBookingStatus().isEmpty()) {

			Assert.assertTrue("Expected : " + bookingStatus + " , Got : "
					+ response.getBookingStatus(), response.getBookingStatus()
					.contains(bookingStatus));
		} else {
			Assert.fail("No booking status returned from the knowledge session.");
		}

	}

	@When("^validating the booking$")
	public void validating_the_booking() throws Throwable {
		// Run rules
		facts = buildSession(request);
		Object response = decisionService.execute(facts, processId,
				BookingResponse.class);
		System.out.println(response.toString());
		Assert.assertTrue(response.getClass() == BookingResponse.class);
		saveBooking((BookingResponse) response);
		this.response = (BookingResponse) response;

		logger.info("When step");
	}

	private List<Object> buildSession(BookingRequest request) {
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

	private boolean saveBooking(BookingResponse response) {

		// Attempt to implement logger
		logger.info("Session returned: " + response.toString());

		Booking booking = response.generateBooking();
		try {
			// attempting to save the bookings returned
			if (response.getBookingStatus().iterator().hasNext()) {
				if (response.getBookingStatus().contains("Confirmed")) {

					logger.info("Attempting to save booking: "
							+ booking.toString());
					bookingRepo.save(booking);
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Then("^wait (\\d+) ms$")
	public void wait_ms(int arg1) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		Thread.sleep(arg1 * 3);
	}
}
