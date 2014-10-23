package com.nanuvem.lom.kernel.validator;

import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

import java.util.List;

public class MinimumSymbolsAttributeConfigurationValidator implements ValueValidator<Integer> {

	public void validate(List<ValidationError> errors,
			String value, Integer minSymbols) {

		int numericSymbolCounter = 0;
		for (int i = 0; i < value.length(); i++) {
			if (!Character.isLetterOrDigit(value.toCharArray()[i])) {
				numericSymbolCounter++;
			}
		}

		if (numericSymbolCounter < minSymbols) {
			String messagePlural = minSymbols > 1 ? "s"
					: "";

			addError(errors, "the default value must have at least "
					+ minSymbols + " symbol character"
					+ messagePlural);
		}
	}

	public AttributeConfigurationValidator createFieldValidator(String field) {
		return new IntegerAttributeConfigurationValidator(field);
	}

}
