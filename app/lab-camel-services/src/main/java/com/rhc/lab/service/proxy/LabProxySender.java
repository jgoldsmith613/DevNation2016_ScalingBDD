package com.rhc.lab.service.proxy;

import org.springframework.stereotype.Service;

import com.rhc.lab.domain.BookingRequest;

/**
 * 
 * This interface is used to kick off a Camel route via proxy, intended to be
 * executed by the MVC layer
 * 
 */
@Service(value = "labProxySender")
public interface LabProxySender {

	/**
	 * This method takes in a BookingRequest object and kick off a Camel Route
	 * which will run rules and return a BookingResponse to declare the status
	 * of the venue booking
	 * 
	 * @param request
	 */
	public void submit(BookingRequest request);

}
