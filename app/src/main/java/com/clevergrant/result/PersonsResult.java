package com.clevergrant.result;

import com.clevergrant.model.Person;

public class PersonsResult {
	private Person[] data;
	private String message;

	/**
	 * Creates a new PersonsResult Object to be sent to the Handler
	 *
	 * @param data    An array of Person Objects containing all of the Persons in the database
	 * @param message If there is an error, this should be used to create it. Otherwise, this should be null.
	 */
	public PersonsResult(Person[] data, String message) {
		setData(data);
		setMessage(message);
	}

	public Person[] getData() {
		return data;
	}

	private void setData(Person[] data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	private void setMessage(String message) {
		this.message = message;
	}
}
