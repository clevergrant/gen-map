package com.clevergrant.exceptions;

public class NotAuthorizedException extends Exception {
	public NotAuthorizedException() {
		super("You are not authorized to view this content");
	}
}
