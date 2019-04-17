package com.clevergrant.request;

public class RegisterRequest {

	private String userName;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private String gender;

	/**
	 * Creates a new RegisterRequest Object
	 *
	 * @param userName  Username of User to register
	 * @param password  Password provided by User
	 * @param email     Email of User to register
	 * @param firstName First name of User
	 * @param lastName  Last name of User
	 * @param gender    Gender of User
	 */
	public RegisterRequest(String userName, String password, String email, String firstName, String lastName, String gender) {
		setUserName(userName);
		setPassword(password);
		setEmail(email);
		setFirstName(firstName);
		setLastName(lastName);
		setGender(gender);
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

	public String getEmail() {
		return email;
	}

	private void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	private void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	private void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	private void setGender(String gender) {
		this.gender = gender;
	}
}
