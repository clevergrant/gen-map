package com.clevergrant.request;

public class PersonRequest {
	private String personID;
	private String token;

	/**
	 * Creates a new PersonRequest Object to be sent to the Service
	 *
	 * @param personID ID of the person object to be returned by the com.clevergrant.service
	 * @param token    The AuthToken string associated with the User making the com.clevergrant.request
	 */
	public PersonRequest(String personID, String token) {
		setPersonID(personID);
		setToken(token);
	}

	public String getPersonID() {
		return personID;
	}

	private void setPersonID(String personID) {
		this.personID = personID;
	}

	public String getToken() {
		return token;
	}

	private void setToken(String token) {
		this.token = token;
	}
}
