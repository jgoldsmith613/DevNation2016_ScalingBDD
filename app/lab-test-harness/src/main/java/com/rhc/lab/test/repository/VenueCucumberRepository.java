package com.rhc.lab.test.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import com.rhc.lab.dao.VenueRepository;
import com.rhc.lab.domain.Venue;

public class VenueCucumberRepository implements VenueRepository {

	private Map<String, Venue> venueRepo = new HashMap<String, Venue>();

	@Override
	public Iterable<Venue> findAll(Sort arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Venue> findAll(Pageable arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		return venueRepo.size();
	}

	@Override
	public void delete(String arg0) {
		venueRepo.remove(arg0);
	}

	@Override
	public void delete(Venue arg0) {
		venueRepo.remove(arg0.getId());
	}

	@Override
	public void delete(Iterable<? extends Venue> venues) {
		for (Venue v : venues) {
			delete(v);
		}

	}

	@Override
	public void deleteAll() {
		venueRepo.clear();
	}

	@Override
	public boolean exists(String arg0) {
		return venueRepo.containsKey(arg0);
	}

	@Override
	public Iterable<Venue> findAll() {
		return venueRepo.values();
	}

	@Override
	public Iterable<Venue> findAll(Iterable<String> arg0) {
		Collection<Venue> venues = new ArrayList<Venue>();
		for (String id : arg0) {
			venues.add(findOne(id));
		}
		return venues;
	}

	@Override
	public Venue findOne(String arg0) {
		return venueRepo.get(arg0);
	}

	@Override
	public <S extends Venue> S save(S venue) {
		if (StringUtils.isEmpty(venue.getId())) {
			venue.setId("" + venueRepo.size());
		}
		venueRepo.put(venue.getId(), venue);
		return venue;
	}

	@Override
	public <S extends Venue> Iterable<S> save(Iterable<S> arg0) {
		Collection<S> venues = new ArrayList<S>();
		for (S obj : arg0) {
			venues.add(save(obj));
		}
		return venues;
	}

	@Override
	public List<Venue> findByName(String name) {
		List<Venue> venues = new ArrayList<Venue>();
		for (Venue venue : venueRepo.values()) {
			if (venue.getName().equals(name)) {
				venues.add(venue);
			}
		}
		return venues;
	}

	public void clear() {
		venueRepo = new HashMap<String, Venue>();
	}
}
