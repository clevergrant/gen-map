package com.clevergrant.request;

public class LoginRequest {
	private String userName;
	private String password;

	/**
	 * Creates a new LoginRequest Object to be sent to the login com.clevergrant.service
	 *
	 * @param userName Username of User to log in
	 * @param password Password of User to log in
	 */
	public LoginRequest(String userName, String password) {
		setUserName(userName);
		setPassword(password);
	}

	public String getUserName() {
		return userName;
	}

	private void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	private void setPassword(String password) {
		this.password = password;
	}
}
