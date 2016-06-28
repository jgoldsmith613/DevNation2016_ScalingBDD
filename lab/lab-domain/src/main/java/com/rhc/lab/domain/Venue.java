package com.rhc.lab.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This class represents the domain model for a Concert Venue
 * 
 */
// @Document(collection = "venues")
public class Venue implements Serializable {
	/**
   * 
   */
	private static final long serialVersionUID = -6165217833968313884L;

	// @Id
	private String id;
	private String name;
	private String city;
	private Integer capacity;
	private List<String> accomodations;

	public Venue() {
	}

	public Venue(String name, String city, Integer capacity,
			List<String> accomodations) {
		super();
		this.name = name;
		this.city = city;
		this.capacity = capacity;
		this.accomodations = accomodations;
	}

	public Venue(String name, String city, Integer capacity,
			String... accomodations) {
		super();
		this.name = name;
		this.city = city;
		this.capacity = capacity;
		this.accomodations = new ArrayList<String>();
		for (String type : accomodations) {
			this.accomodations.add(type);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public List<String> getAccomodations() {
		return accomodations;
	}

	public void setAccomodations(List<String> accomodations) {
		this.accomodations = accomodations;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Cleans up the leading "The" in band name for more accurate sorting
	 */
	// @Override
	// public int compareTo(Venue o) {
	// String thisName = "";
	// String otherName = "";
	//
	// if (StringUtils.startsWithIgnoreCase(this.getName(), "the")) {
	// thisName = this.getName().substring(3).trim();
	// } else {
	// thisName = this.getName();
	// }
	// if (StringUtils.startsWithIgnoreCase(o.getName(), "the")) {
	// otherName = o.getName().substring(3).trim();
	// } else {
	// otherName = o.getName();
	// }
	//
	// return thisName.compareTo(otherName);
	// }

}
