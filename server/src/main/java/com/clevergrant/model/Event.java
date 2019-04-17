package com.clevergrant.model;

public class Event {
	private String eventID;
	private String descendant;
	private String personID;
	private float latitude;
	private float longitude;
	private String country;
	private String city;
	private String eventType;
	private int year;

	/**
	 * Creates a new Event Object
	 *
	 * @param eventID    ID of this event
	 * @param descendant User (Username) to which this person belongs
	 * @param personID   ID of person to which this event belongs
	 * @param latitude   Latitude of event’s location
	 * @param longitude  Longitude of event’s location
	 * @param country    Country in which event occurred
	 * @param city       City in which event occurred
	 * @param eventType  Type of event (birth, baptism, christening, marriage, death, etc.)
	 * @param year       Year in which event occurred
	 */
	public Event(String eventID, String descendant, String personID, float latitude, float longitude, String country, String city, String eventType, int year) {
		setEventID(eventID);
		setDescendant(descendant);
		setPersonID(personID);
		setLatitude(latitude);
		setLongitude(longitude);
		setCountry(country);
		setCity(city);
		setEventType(eventType);
		setYear(year);
	}

	public String getEventID() {
		return eventID;
	}

	private void setEventID(String eventID) {
		this.eventID = eventID;
	}

	public String getDescendant() {
		return descendant;
	}

	private void setDescendant(String descendant) {
		this.descendant = descendant;
	}

	public String getPersonID() {
		return personID;
	}

	private void setPersonID(String personID) {
		this.personID = personID;
	}

	public float getLatitude() {
		return latitude;
	}

	private void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	private void setLongitude(float longitude) {
		this.longitude = longitude;
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

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o instanceof Event) {
			Event oEvent = (Event) o;
			return oEvent.getEventID().equals(getEventID()) &&
					oEvent.getDescendant().equals(getDescendant()) &&
					oEvent.getPersonID().equals(getPersonID()) &&
					oEvent.getLatitude() == (getLatitude()) &&
					oEvent.getLongitude() == (getLongitude()) &&
					oEvent.getCountry().equals(getCountry()) &&
					oEvent.getCity().equals(getCity()) &&
					oEvent.getEventType().equals(getEventType()) &&
					oEvent.getYear() == (getYear());
		}
		return false;
	}
}
