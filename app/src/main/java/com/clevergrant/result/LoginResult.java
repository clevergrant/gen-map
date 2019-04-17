package com.clevergrant.result;

public class LoginResult {
	private String token;
	private String userName;
	private String personID;
	private String message;

	/**
	 * Creates a new LoginResult Object to be sent to the Handler
	 *
	 * @param token    The newly created AuthToken string to be sent to the client
	 * @param userName The userName associated with the AuthToken
	 * @param personID The personID of the User who logged in
	 * @param message  If there is an error, this should be used to create it. Otherwise, this should be null.
	 */
	public LoginResult(String token, String userName, String personID, String message) {
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
