package com.nanuvem.lom.kernel.validator;

import java.util.List;

import org.codehaus.jackson.JsonNode;

public abstract class AttributeTypeConfigurationValidator implements
		AttributeConfigurationValidator {

	protected String field;
	private String suffixExceptionMessage;

	public AttributeTypeConfigurationValidator(String field,
			String suffixExceptionMessage) {
		this.field = field;
		this.suffixExceptionMessage = suffixExceptionMessage;
	}

	public void validate(List<ValidationError> errors, JsonNode configuration) {

		if (!this.validate(configuration)) {
			addError(errors, "the " + this.field + " value must be "
					+ this.suffixExceptionMessage);
		}
	}

	protected static void addError(List<ValidationError> errors, String message) {
		ValidationError validationError = new ValidationError(message);
		errors.add(validationError);
	}

	public abstract boolean validate(JsonNode configuration);
}
