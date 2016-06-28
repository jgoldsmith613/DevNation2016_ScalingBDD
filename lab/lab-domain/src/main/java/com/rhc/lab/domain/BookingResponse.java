package com.rhc.lab.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rhc.lab.kie.common.KieQuery;

/**
 * 
 * This class represents the domain model for a booking response that will go
 * through our Concert Booking application for artists and venue matching. This
 * payload will be returned to the UI after rules have fired to determine
 * whether or not the venue can be booked as per the requested time(s) and
 * performance type(s)
 * 
 */
public class BookingResponse implements Serializable {
	/**
   * 
   */
	private static final long serialVersionUID = 1074544082673724792L;

	// private static final Logger logger = LoggerFactory
	// .getLogger(BookingResponse.class);

	@JsonProperty("bookingRequests")
	@KieQuery(binding = "$bookingRequest", queryName = "getBookingRequests")
	private Collection<BookingRequest> bookingRequests;

	@JsonProperty("booking")
	private Booking booking;

	// @KieQuery(binding = "$status", queryName = "getStatus")
	// private Collection<BookingStatus> bookingStatus;

	@JsonProperty("bookingStatus")
	private Collection<String> bookingStatus;

	@JsonProperty("status")
	private String status;

	public BookingResponse() {
		super();
	}

	public Booking generateBooking() {
		if (bookingRequests != null && !bookingRequests.isEmpty()) {
			booking = new Booking(bookingRequests.iterator().next());
		} else {
			// logger.error("No booking requests on response");
		}
		return booking;
	}

	public Collection<BookingRequest> getBookingRequests() {
		if (bookingRequests == null)
			bookingRequests = new ArrayList<BookingRequest>();
		return bookingRequests;
	}
	public void setBookingRequests(Collection<BookingRequest> bookingRequest) {

		this.bookingRequests = bookingRequest;
	}
	public Booking getBooking() {
		return booking;
	}
	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	// public Collection<BookingStatus> getBookingStatus() {
	// if (bookingStatus == null)
	// bookingStatus = new ArrayList<BookingStatus>();
	// return bookingStatus;
	// }
	//
	// public Collection<String> getBookingStatusUpperCase() {
	// Collection<String> upper = new ArrayList<String>();
	// for (BookingStatus str : bookingStatus) {
	// upper.add(str.toString().toUpperCase());
	// }
	// return upper;
	// }
	//
	// public void setBookingStatus(Collection<BookingStatus> bookingStatus) {
	// this.bookingStatus = bookingStatus;
	// if (this.getBookingStatusString() == null)
	// this.bookingStatusString = new ArrayList<String>();
	// for (BookingStatus status : bookingStatus) {
	// this.bookingStatusString.add(status.toString());
	// }
	// }
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Collection<String> getBookingStatus() {
		if (this.bookingStatus == null)
			this.bookingStatus = new ArrayList<String>();
		return bookingStatus;
	}

	public void setBookingStatusString(Collection<String> bookingStatusString) {

		this.bookingStatus = bookingStatusString;
	}

	public void setBookingStatus(Collection<String> bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((booking == null) ? 0 : booking.hashCode());
		result = prime * result
				+ ((bookingRequests == null) ? 0 : bookingRequests.hashCode());
		result = prime * result
				+ ((bookingStatus == null) ? 0 : bookingStatus.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookingResponse other = (BookingResponse) obj;
		if (booking == null) {
			if (other.booking != null)
				return false;
		} else if (!booking.equals(other.booking))
			return false;
		if (bookingRequests == null) {
			if (other.bookingRequests != null)
				return false;
		} else if (!bookingRequests.equals(other.bookingRequests))
			return false;
		if (bookingStatus == null) {
			if (other.bookingStatus != null)
				return false;
		} else if (!bookingStatus.equals(other.bookingStatus))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BookingResponse [bookingRequests=" + bookingRequests
				+ ", booking=" + booking + ", bookingStatus=" + bookingStatus
				+ ", status=" + status + "]";
	}

}
