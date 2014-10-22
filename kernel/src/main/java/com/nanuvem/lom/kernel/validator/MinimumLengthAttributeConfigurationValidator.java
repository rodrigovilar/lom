package com.nanuvem.lom.kernel.validator;

import java.util.List;

import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

import org.codehaus.jackson.JsonNode;

public class MinimumLengthAttributeConfigurationValidator implements ValueValidator<Integer> {

	
	public void validate(List<ValidationError> errors,
			JsonNode configuration, String value, Integer minLength) {
		if (value.length() < minLength) {

			addError(errors, "the default value is smaller than minlength");
		}
	}

	public AttributeConfigurationValidator createFieldValidator(String field) {
		return new IntegerAttributeConfigurationValidator(field);
	}

}
