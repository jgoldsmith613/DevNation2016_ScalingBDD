package com.rhc.lab.test.repository;

import java.util.List;

import javax.annotation.Resource;

import com.rhc.lab.dao.BookingRepository;
import com.rhc.lab.dao.LabRepository;
import com.rhc.lab.domain.Booking;

public class LabCucumberRepository implements LabRepository {

	@Resource(name = "bookingDao")
	private BookingRepository bookingRepo;

	@Override
	public List<Booking> findAllFutureBookings() {
		// TODO Auto-generated method stub
		return null;
	}

}
