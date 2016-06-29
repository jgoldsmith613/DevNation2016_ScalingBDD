package com.rhc.lab.test.cucumber.camel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhc.lab.domain.Booking;
import com.rhc.lab.domain.BookingRequest;
import com.rhc.lab.domain.Performer;
import com.rhc.lab.domain.Venue;
import com.rhc.lab.kie.api.StatelessDecisionService;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({"classpath*:cucumber.xml", "classpath:kie-context.xml"})
@ActiveProfiles({"remote", "test"})
public class CamelBookingValidationSteps {
	@Resource(name = "decisionService")
	private StatelessDecisionService decisionService;

	public static final MediaType JSON = MediaType
			.parse("application/json; charset=utf-8");

	protected static final Logger logger = LoggerFactory
			.getLogger(CamelBookingValidationSteps.class);

	protected Venue venue = new Venue();
	protected BookingRequest request = new BookingRequest();

	private OkHttpClient client = new OkHttpClient();

	private ObjectMapper mapper = new ObjectMapper();

	private static String serverUrl = "http://localhost:8081";

	private static String bookingPath = "/bookings";
	private static String venuePath = "/venues";
	private static Response restResponse;

	@Given("^a venue \"(.*?)\" with an occupancy of \"(.*?)\"$")
	public void a_venue_with_an_occupancy_of(String venueName, String occupancy)
			throws Throwable {
		// Set properties regarding venueName/occupancy
		venue.setName(venueName);
		venue.setCapacity(Integer.parseInt(occupancy));
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

		Booking booking = new Booking();
		booking.setPerformer(performer);
		Date dOpen = sdf.parse(open);
		Date dClose = sdf.parse(close);
		Assert.assertNotNull(dOpen);
		Assert.assertNotNull(dClose);
		booking.setOpen(dOpen);
		booking.setClose(dClose);
		booking.setVenueName(venue.getName());

		logger.info("saving previous booking " + booking.toString());
		// bookingRepo.save(booking);

		BookingRequest request = new BookingRequest(venue.getName(), dOpen,
				dClose, performer);
		String requestString = mapper.writeValueAsString(request);
		logger.info("" + requestString);
		RequestBody body = RequestBody.create(JSON, requestString);
		Request restRequest = new Request.Builder()
				.url(serverUrl + bookingPath).post(body).build();
		Response response = client.newCall(restRequest).execute();
		Assert.assertEquals(response.code(), 200);
		// restResponse = response;

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

		if (bookingStatus.equalsIgnoreCase("Confirmed")) {
			i_expect_a_confirmed_request();
		}

		if (bookingStatus.equalsIgnoreCase("Revoked")) {
			i_expect_a_denied_request();
		}
	}

	@When("^validating the booking$")
	public void validating_the_booking() throws Throwable {
		// Run rules
		String requestString = mapper.writeValueAsString(request);
		logger.info("" + requestString);
		RequestBody body = RequestBody.create(JSON, requestString);
		Request request = new Request.Builder().url(serverUrl + bookingPath)
				.post(body).build();
		Response response = client.newCall(request).execute();
		restResponse = response;

	}

	@Then("^wait (\\d+) ms$")
	public void wait_ms(int arg1) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		Thread.sleep(arg1 * 3);
	}

	@When("^request to schedule the booking$")
	public void request_to_schedule_the_booking() throws Throwable {
		String requestString = mapper.writeValueAsString(request);
		logger.info("" + requestString);
		RequestBody body = RequestBody.create(JSON, requestString);
		Request request = new Request.Builder().url(serverUrl + bookingPath)
				.post(body).build();
		Response response = client.newCall(request).execute();
		restResponse = response;

	}

	@Then("^I expect a denied request$")
	public void i_expect_a_denied_request() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		Assert.assertEquals(417, restResponse.code());
	}

	@Then("^I expect a confirmed request$")
	public void i_expect_a_confirmed_request() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		Assert.assertEquals(200, restResponse.code());
		Assert.assertNotNull(restResponse.body().string());
	}

	@Given("^the venue is saved$")
	public void the_venue_is_saved() throws Throwable {
		String venueString = mapper.writeValueAsString(venue);
		RequestBody body = RequestBody.create(JSON, venueString);
		Request request = new Request.Builder().url(serverUrl + venuePath)
				.post(body).build();
		Response response = client.newCall(request).execute();
		venue.setId(response.body().string());

	}

	@Given("^all respositories are clear$")
	public void all_respositories_are_clear() throws Throwable {
		Request request = new Request.Builder().url(serverUrl + bookingPath)
				.delete().build();
		Response response = client.newCall(request).execute();
		System.out.println(response.body());
		Assert.assertEquals("\"All bookings deleted\"", response.body()
				.string());

		request = new Request.Builder().url(serverUrl + venuePath).delete()
				.build();
		response = client.newCall(request).execute();
		Assert.assertEquals("\"All venues deleted\"", response.body().string());

	}
}
