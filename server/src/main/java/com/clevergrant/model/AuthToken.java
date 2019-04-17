package com.clevergrant.model;

import java.util.UUID;

public class AuthToken {
	private String userName;
	private String token;

	/**
	 * Creates a new AuthToken Object, and automatically generates a token (type 4 UUID)
	 *
	 * @param userName The userName associated with the token
	 */
	public AuthToken(String userName) {
		setUserName(userName);
		setToken();
	}

	/**
	 * Creates a Java Object representation of an AuthToken found in the database
	 *
	 * @param userName The userName associated with the token found in the database
	 * @param token    The token found in the database
	 */
	public AuthToken(String userName, String token) {
		setUserName(userName);
		setToken(token);
	}

	public String getUserName() {
		return userName;
	}

	private void setUserName(String userName) {
		this.userName = userName;
	}

	public String getToken() {
		return token;
	}

	/**
	 * Used for creating Java Object representations of a token that was created elsewhere
	 *
	 * @param token String representation of the token you want to make a Java Object for
	 */
	private void setToken(String token) {
		this.token = token;
	}

	/**
	 * Automatically generates the token (type 4 UUID)
	 */
	private void setToken() {
		setToken(UUID.randomUUID().toString());
	}
}
