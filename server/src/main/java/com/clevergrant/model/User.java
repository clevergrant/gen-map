package com.clevergrant.model;

public class User {

	private String userName;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private String gender;
	private String personID;

	/**
	 * Creates a new User Object
	 *
	 * @param userName  Unique user name
	 * @param password  User’s password
	 * @param email     User’s email address
	 * @param firstName User’s first name
	 * @param lastName  User's last name
	 * @param gender    User's gender (string: "m" or "f")
	 * @param personID  Unique Person ID assigned to this user’s generated Person object
	 */
	public User(String userName, String password, String email, String firstName, String lastName, String gender, String personID) {
		setUserName(userName);
		setPassword(password);
		setEmail(email);
		setFirstName(firstName);
		setLastName(lastName);
		setGender(gender);
		setPersonID(personID);
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

	public String getPersonID() {
		return personID;
	}

	private void setPersonID(String personID) {
		this.personID = personID;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o instanceof User) {
			User oUser = (User) o;
			return oUser.getUserName().equals(getUserName()) &&
					oUser.getPassword().equals(getPassword()) &&
					oUser.getEmail().equals(getEmail()) &&
					oUser.getFirstName().equals(getFirstName()) &&
					oUser.getLastName().equals(getLastName()) &&
					oUser.getGender().equals(getGender()) &&
					oUser.getPersonID().equals(getPersonID());
		}
		return false;
	}
}
