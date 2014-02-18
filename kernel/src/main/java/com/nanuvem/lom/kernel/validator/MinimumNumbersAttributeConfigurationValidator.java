package com.nanuvem.lom.kernel.validator;

import java.util.List;

import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.kernel.Attribute;

public class MinimumNumbersAttributeConfigurationValidator extends
		AttributeConfigurationValidatorWithDefault {

	public MinimumNumbersAttributeConfigurationValidator(String field,
			String defaultField) {
		super(field, defaultField);
	}

	@Override
	protected void validateDefault(List<ValidationError> errors,
			Attribute attribute, JsonNode configuration, String defaultValue) {

		int numericCharacterCounter = 0;
		for (int i = 0; i < defaultValue.length(); i++) {
			if (Character.isDigit(defaultValue.toCharArray()[i])) {
				numericCharacterCounter++;
			}
		}
		if (numericCharacterCounter < configuration.get(field).asInt()) {
			String messagePlural = configuration.get(field).asInt() > 1 ? "s"
					: "";

			addError(errors, "the default value must have at least "
					+ configuration.get(field).asInt() + " numerical character"
					+ messagePlural);
		}
	}

	@Override
	protected AttributeConfigurationValidator createFieldValidator(String field) {
		return new IntegerAttributeConfigurationValidator(field);
	}

}
