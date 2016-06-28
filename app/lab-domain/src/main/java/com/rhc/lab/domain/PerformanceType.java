package com.rhc.lab.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * This enumerated type represents the domain model for performance types to be
 * used in Booking Requests
 * 
 */
public enum PerformanceType
		implements
			Serializable,
			Comparable<PerformanceType> {

	BALLET("Ballet"), BAND("Band"), COMIC("Comic"), MUSICAL("Musical"), ORCHESTRA(
			"Orchestra"), PLAY("Play"), RALLY("Rally"), SCREENING(
			"Film Screening"), SPORTING_EVENT("Sporting Event");

	@JsonProperty("label")
	private String label;

	PerformanceType(String performanceType) {
		this.label = performanceType;
	}

	@Override
	public String toString() {
		return label;
	}

	public String getLabel() {
		return label;
	}

}
