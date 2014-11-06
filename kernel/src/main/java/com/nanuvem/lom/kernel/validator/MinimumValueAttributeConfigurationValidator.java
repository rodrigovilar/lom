package com.nanuvem.lom.kernel.validator;

import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

import java.util.List;

public class MinimumValueAttributeConfigurationValidator implements ValueValidator<Integer> {

	public void validate(List<ValidationError> errors,
			String value, Integer minValue) {

		Integer intDefaultValue = Integer.parseInt(value);
		if (intDefaultValue < minValue) {

			addError(errors, "the default value is smaller than minvalue");
		}

	}

	public AttributeConfigurationValidator createFieldValidator(String field) {
		return new IntegerAttributeConfigurationValidator(field);
	}

}
