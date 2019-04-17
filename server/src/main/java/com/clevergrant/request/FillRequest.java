package com.clevergrant.request;

public class FillRequest {
	private String userName;
	private int generations;

	/**
	 * Creates a new FillRequest Object to be sent to the FillService com.clevergrant.service
	 *
	 * @param userName    Username to fill with ancestors
	 * @param generations Number of generations to add (default is 4, if there are was no number specified)
	 */
	public FillRequest(String userName, int generations) {
		setUserName(userName);
		setGenerations(generations);
	}

	public FillRequest(String userName) {
		this(userName, 4);
	}

	public String getUserName() {
		return userName;
	}

	private void setUserName(String userName) {
		this.userName = userName;
	}

	public int getGenerations() {
		return generations;
	}

	private void setGenerations(int generations) {
		this.generations = generations;
	}
}
