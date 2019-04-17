package com.clevergrant.result;

public class PersonResult {
	private String descendant;
	private String personID;
	private String firstName;
	private String lastName;
	private String gender;
	private String father;
	private String mother;
	private String spouse;
	private String message;

	/**
	 * Creates a new PersonResult Object to be sent to the Handler
	 *
	 * @param descendant Name of user account this person belongs to
	 * @param personID   Person's unique ID
	 * @param firstName  Person's first name
	 * @param lastName   Person's last name
	 * @param gender     Person's gender ("m" or "f")
	 * @param father     ID of person's father (Optional)
	 * @param mother     ID of person's mother (Optional)
	 * @param spouse     ID of person's spouse (Optional)
	 * @param message    If there is an error, this should be used to create it. Otherwise, this should be null.
	 */
	public PersonResult(String descendant, String personID, String firstName, String lastName, String gender, String father, String mother, String spouse, String message) {
		setDescendant(descendant);
		setPersonID(personID);
		setFirstName(firstName);
		setLastName(lastName);
		setGender(gender);
		setFather(father);
		setMother(mother);
		setSpouse(spouse);
		setMessage(message);
	}

	public String getDescendant() {
		return descendant;
	}

	private void setDescendant(String descendant) {
		this.descendant = descendant;
	}

	public String getPersonID() {
		return personID;
	}

	private void setPersonID(String personID) {
		this.personID = personID;
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

	public String getFather() {
		return father;
	}

	private void setFather(String father) {
		this.father = father;
	}

	public String getMother() {
		return mother;
	}

	private void setMother(String mother) {
		this.mother = mother;
	}

	public String getSpouse() {
		return spouse;
	}

	private void setSpouse(String spouse) {
		this.spouse = spouse;
	}

	public String getMessage() {
		return message;
	}

	private void setMessage(String message) {
		this.message = message;
	}
}
