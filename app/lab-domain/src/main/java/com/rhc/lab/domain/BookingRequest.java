package com.rhc.lab.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * This class represents the domain model for a booking request that will go
 * through our Concert Booking application for artists and venue matching
 * 
 */
public class BookingRequest implements Serializable {

	/**
   * 
   */
	private static final long serialVersionUID = 1086652316465244736L;

	private String venueName;
	private Date open;
	private Date close;
	private Performer performer;

	public BookingRequest() {
		super();
	}

	public BookingRequest(String venueName, Date open, Date close,
			Performer performer) {
		super();
		this.venueName = venueName;
		this.open = open;
		this.close = close;
		this.performer = performer;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public Date getOpen() {
		return open;
	}

	public void setOpen(Date open) {
		this.open = open;
	}

	public Date getClose() {
		return close;
	}

	public void setClose(Date close) {
		this.close = close;
	}

	public Performer getPerformer() {
		return performer;
	}

	public void setPerformer(Performer performer) {
		this.performer = performer;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "BookingRequest [venueName=" + venueName + ", open=" + open
				+ ", close=" + close + ", performer=" + performer + "]";
	}

}
