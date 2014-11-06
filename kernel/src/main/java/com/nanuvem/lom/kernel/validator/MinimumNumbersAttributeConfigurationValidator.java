package com.nanuvem.lom.kernel.validator;

import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

import java.util.List;

public class MinimumNumbersAttributeConfigurationValidator implements ValueValidator<Integer> {

	public void validate(List<ValidationError> errors,
			String value, Integer minNumbers){

		int numericCharacterCounter = 0;
		for (int i = 0; i < value.length(); i++) {
			if (Character.isDigit(value.toCharArray()[i])) {
				numericCharacterCounter++;
			}
		}
		if (numericCharacterCounter < minNumbers) {
			String messagePlural = minNumbers > 1 ? "s"
					: "";

			addError(errors, "the default value must have at least "
					+ minNumbers + " numerical character"
					+ messagePlural);
		}
	}

	public AttributeConfigurationValidator createFieldValidator(String field) {
		return new IntegerAttributeConfigurationValidator(field);
	}

}
