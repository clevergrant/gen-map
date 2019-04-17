package com.clevergrant.request;

import com.clevergrant.model.Event;
import com.clevergrant.model.Person;
import com.clevergrant.model.User;

public class LoadRequest {
	private User[] users;
	private Person[] persons;
	private Event[] events;

	/**
	 * Creates a new LoadRequest Object to be sent to the Load com.clevergrant.service
	 *
	 * @param users   An array of User objects
	 * @param persons An array of Person objects
	 * @param events  An array of Event objects
	 */
	public LoadRequest(User[] users, Person[] persons, Event[] events) {
		setUsers(users);
		setPersons(persons);
		setEvents(events);
	}

	public User[] getUsers() {
		return users;
	}

	private void setUsers(User[] users) {
		this.users = users;
	}

	public Person[] getPersons() {
		return persons;
	}

	private void setPersons(Person[] persons) {
		this.persons = persons;
	}

	public Event[] getEvents() {
		return events;
	}

	private void setEvents(Event[] events) {
		this.events = events;
	}
}
