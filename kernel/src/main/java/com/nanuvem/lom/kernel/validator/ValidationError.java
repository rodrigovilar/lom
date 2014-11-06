package com.nanuvem.lom.kernel.validator;

public class ValidationError {

	private String message;

	public ValidationError(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
