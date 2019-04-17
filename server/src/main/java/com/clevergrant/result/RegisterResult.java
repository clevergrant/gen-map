package com.clevergrant.result;

public class RegisterResult {
	private String token;
	private String userName;
	private String personID;
	private String message;

	/**
	 * Creates a new RegisterResult Object to be sent to the Handler
	 *
	 * @param token    The AuthToken string generated for the user who was registered
	 * @param userName The userName of the person who was registered
	 * @param personID The ID of the Person Object generated when the person was registered
	 * @param message  If there is an error, this should be used to create it. Otherwise, this should be null.
	 */
	public RegisterResult(String token, String userName, String personID, String message) {
		setToken(token);
		setUserName(userName);
		setPersonID(personID);
		setMessage(message);
	}

	public String getToken() {
		return token;
	}

	private void setToken(String token) {
		this.token = token;
	}

	public String getUserName() {
		return userName;
	}

	private void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPersonID() {
		return personID;
	}

	private void setPersonID(String personID) {
		this.personID = personID;
	}

	public String getMessage() {
		return message;
	}

	private void setMessage(String message) {
		this.message = message;
	}
}
