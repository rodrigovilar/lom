package com.nanuvem.lom.kernel.validator;

import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

import java.util.List;

public class MinimumLengthAttributeConfigurationValidator implements ValueValidator<Integer> {

	
	public void validate(List<ValidationError> errors,
			String value, Integer minLength) {
		if (value.length() < minLength) {

			addError(errors, "the default value is smaller than minlength");
		}
	}

	public AttributeConfigurationValidator createFieldValidator(String field) {
		return new IntegerAttributeConfigurationValidator(field);
	}

}
