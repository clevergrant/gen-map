package com.clevergrant.request;

public class EventsRequest {
	private String token;

	/**
	 * Creates a new EventsRequest Object to be sent to the Service
	 *
	 * @param token The AuthToken string associated with the user making the com.clevergrant.request
	 */
	public EventsRequest(String token) {
		setToken(token);
	}

	public String getToken() {
		return token;
	}

	private void setToken(String token) {
		this.token = token;
	}
}
