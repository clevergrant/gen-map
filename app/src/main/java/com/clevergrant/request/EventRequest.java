package com.clevergrant.request;

public class EventRequest {
	private String eventID;
	private String token;

	/**
	 * Creates a new EventRequest Object to be sent to the Service
	 *
	 * @param eventID The ID of the event being requested
	 * @param token   The AuthToken string associated with the user making the com.clevergrant.request
	 */
	public EventRequest(String eventID, String token) {
		setEventID(eventID);
		setToken(token);
	}

	public String getEventID() {
		return eventID;
	}

	private void setEventID(String eventID) {
		this.eventID = eventID;
	}

	public String getToken() {
		return token;
	}

	private void setToken(String token) {
		this.token = token;
	}
}
