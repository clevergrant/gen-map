package com.clevergrant.result;

public class ClearResult {
	private String message;

	/**
	 * Creates a new ClearResult Object to be sent to the Handler
	 *
	 * @param message If there is an error, this should be used to create it. Otherwise, this should be null.
	 */
	public ClearResult(String message) {
		if (message == null)
			setMessage("ClearService succeeded.");
		else
			setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	private void setMessage(String message) {
		this.message = message;
	}
}
