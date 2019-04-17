package com.clevergrant.result;

public class EventResult {
	private String descendant;
	private String eventID;
	private String personID;
	private float longitude;
	private float latitude;
	private String country;
	private String city;
	private String eventType;
	private int year;
	private String message;

	/**
	 * Creates a new EventResult Object to be sent to the Handler
	 *
	 * @param descendant Name of user account this event belongs to
	 * @param eventID    Event's unique ID
	 * @param personID   ID of the person this event belongs to
	 * @param longitude  Longitude of the event's location
	 * @param latitude   Latitude of the event's location
	 * @param country    Name of country where event occurred
	 * @param city       Name of city where event occurred
	 * @param eventType  Type of event (birth, baptism, etc)
	 * @param year       Year the event occurred
	 * @param message    If there is an error, this should be used to create it. Otherwise, this should be null.
	 */
	public EventResult(String descendant, String eventID, String personID, float longitude, float latitude, String country, String city, String eventType, int year, String message) {
		setDescendant(descendant);
		setEventID(eventID);
		setPersonID(personID);
		setLongitude(longitude);
		setLatitude(latitude);
		setCountry(country);
		setCity(city);
		setEventType(eventType);
		setYear(year);
		setMessage(message);
	}

	public String getDescendant() {
		return descendant;
	}

	private void setDescendant(String descendant) {
		this.descendant = descendant;
	}

	public String getEventID() {
		return eventID;
	}

	private void setEventID(String eventID) {
		this.eventID = eventID;
	}

	public String getPersonID() {
		return personID;
	}

	private void setPersonID(String personID) {
		this.personID = personID;
	}

	public float getLongitude() {
		return longitude;
	}

	private void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	private void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public String getCountry() {
		return country;
	}

	private void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	private void setCity(String city) {
		this.city = city;
	}

	public String getEventType() {
		return eventType;
	}

	private void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public int getYear() {
		return year;
	}

	private void setYear(int year) {
		this.year = year;
	}

	public String getMessage() {
		return message;
	}

	private void setMessage(String message) {
		this.message = message;
	}
}
