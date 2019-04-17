package com.clevergrant.request;

public class PersonsRequest {
	private String token;

	/**
	 * Creates a new PersonsRequest Object to be sent to the Service
	 *
	 * @param token The AuthToken string associated with the user who's requesting the com.clevergrant.data
	 */
	public PersonsRequest(String token) {
		setToken(token);
	}

	public String getToken() {
		return token;
	}

	private void setToken(String token) {
		this.token = token;
	}
}
