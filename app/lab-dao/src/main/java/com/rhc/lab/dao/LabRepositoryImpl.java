package com.rhc.lab.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.rhc.lab.domain.Booking;

/**
 * Class acts as the DAO for miscellaneous query use with the MongoTemplate
 * 
 */
// @Repository(value = "labDao")
public class LabRepositoryImpl implements LabRepository {

	@Resource(name = "labDao")
	private LabRepository labDao;

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	@Override
	public List<Booking> findAllFutureBookings() {
		Query query = new Query(Criteria.where("open").gte(new Date()));
		List<Booking> bookings = mongoTemplate.find(query, Booking.class);
		return bookings;
	}

}
