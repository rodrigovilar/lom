package com.nanuvem.lom.kernel.validator;

import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

import java.util.List;

public class MinimumUppersAttributeConfigurationValidator implements ValueValidator<Integer> {

	public void validate(List<ValidationError> errors,
			String value, Integer minUppers) {

		int uppercaseCharacterCounter = 0;
		for (int i = 0; i < value.length(); i++) {
			if (value.toCharArray()[i] == Character
					.toUpperCase(value.toCharArray()[i])) {
				uppercaseCharacterCounter++;
			}
		}
		if (uppercaseCharacterCounter < minUppers) {
			String messagePlural = minUppers > 1 ? "s"
					: "";

			addError(errors, "the default value must have at least "
					+ minUppers
					+ " upper case character" + messagePlural);
		}
	}

	public AttributeConfigurationValidator createFieldValidator(String field) {
		return new IntegerAttributeConfigurationValidator(field);
	}

}
