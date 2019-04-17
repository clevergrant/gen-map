package com.clevergrant.result;

public class FillResult {
	private String message;

	/**
	 * Creates a new FillResult Object to be sent to the Handler
	 *
	 * @param personsAdded Number of persons added to the database
	 * @param eventsAdded  Number of events added to the database
	 * @param message      If there is an error, this should be used to create it. Otherwise, this should be null.
	 */
	public FillResult(int personsAdded, int eventsAdded, String message) {
		if (message == null)
			setMessage("Successfully added " + String.valueOf(personsAdded) + " persons and " + String.valueOf(eventsAdded) + " events to the database.");
		else
			setMessage(message);
	}

	private void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
