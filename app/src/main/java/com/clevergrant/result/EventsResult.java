package com.clevergrant.result;

import com.clevergrant.model.Event;

public class EventsResult {
	private Event[] data;
	private String message;

	/**
	 * Creates a new EventsResult Object to be sent to the Handler
	 *
	 * @param data    An array of Event objects containing all the events in the database
	 * @param message If there is an error, this should be used to create it. Otherwise, this should be null.
	 */
	public EventsResult(Event[] data, String message) {
		setData(data);
		setMessage(message);
	}

	public Event[] getData() {
		return data;
	}

	private void setData(Event[] data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	private void setMessage(String message) {
		this.message = message;
	}
}
