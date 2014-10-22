package com.nanuvem.lom.kernel.validator;

import java.util.List;

import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

import org.codehaus.jackson.JsonNode;

public class MaximumLengthAttributeConfigurationValidator implements ValueValidator<Integer> {

	public void validate(List<ValidationError> errors,
			JsonNode configuration, String value, Integer maxLength) {
		if (value.length() > maxLength) {
			addError(errors, "the default value is greater than maxlength");
		}
	}

	public AttributeConfigurationValidator createFieldValidator(String field) {
		return new IntegerAttributeConfigurationValidator(field);
	}
}
