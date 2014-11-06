package com.nanuvem.lom.kernel.validator;

import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

import java.util.List;

public class MaximumValueAttributeConfigurationValidator implements ValueValidator<Integer> {


	public void validate(List<ValidationError> errors,
			String value, Integer maxValue) {
		Integer intDefaultValue = Integer.parseInt(value);
		if (intDefaultValue > maxValue) {
			addError(errors, "the default value is greater than maxvalue");
		}
	}

	public AttributeConfigurationValidator createFieldValidator(String field) {
		return new IntegerAttributeConfigurationValidator(field);
	}
}
