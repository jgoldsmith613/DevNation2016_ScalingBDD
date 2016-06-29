package com.rhc.lab.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * This class represents the persistent data model for a booking confirmed by
 * the lab business rules
 * 
 */
// @Document(collection = "bookings")
public class Booking implements Serializable, Comparable<Booking> {

	private static final long serialVersionUID = -3194593190599248428L;

	// @Id
	private String id;
	private String venueName;
	private Performer performer;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private Date open;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private Date close;

	public Booking() {
	}

	public Booking(String venueName, Performer performer, Date open, Date close) {
		this.venueName = venueName;
		this.performer = performer;
		this.open = open;
		this.close = close;
	}

	public Booking(BookingRequest bookingRequest) {
		this.venueName = bookingRequest.getVenueName();
		this.performer = bookingRequest.getPerformer();
		this.open = bookingRequest.getOpen();
		this.close = bookingRequest.getClose();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	public int compareTo(Booking o) {
		if (this.getOpen() == null && o.getClose() == null)
			return 0;
		else if (o.getOpen() == null)
			return -1;
		else if (this.getOpen() == null)
			return 1;
		else
			return this.getOpen().compareTo(o.getOpen());

	}

	@Override
	public String toString() {
		return "Booking [id=" + id + ", venueName=" + venueName
				+ ", performer=" + performer + ", open=" + open + ", close="
				+ close + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((close == null) ? 0 : close.hashCode());
		result = prime * result + ((open == null) ? 0 : open.hashCode());
		result = prime * result
				+ ((performer == null) ? 0 : performer.hashCode());
		result = prime * result
				+ ((venueName == null) ? 0 : venueName.hashCode());
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
		Booking other = (Booking) obj;
		if (close == null) {
			if (other.close != null)
				return false;
		} else if (!close.equals(other.close))
			return false;
		if (open == null) {
			if (other.open != null)
				return false;
		} else if (!open.equals(other.open))
			return false;
		if (performer == null) {
			if (other.performer != null)
				return false;
		} else if (!performer.equals(other.performer))
			return false;
		if (venueName == null) {
			if (other.venueName != null)
				return false;
		} else if (!venueName.equals(other.venueName))
			return false;
		return true;
	}

}
