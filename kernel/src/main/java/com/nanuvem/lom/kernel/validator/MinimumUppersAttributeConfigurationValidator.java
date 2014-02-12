package com.nanuvem.lom.kernel.validator;

import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

import java.util.List;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.kernel.Attribute;

public class MinimumUppersAttributeConfigurationValidator extends
		AttributeConfigurationValidatorWithDefault {

	public MinimumUppersAttributeConfigurationValidator(String field,
			String defaultField) {
		super(field, defaultField);
	}

	@Override
	protected void validateDefault(List<ValidationError> errors,
			Attribute attribute, JsonNode configuration, String defaultValue) {

		int uppercaseCharacterCounter = 0;
		for (int i = 0; i < defaultValue.length(); i++) {
			if (defaultValue.toCharArray()[i] == Character
					.toUpperCase(defaultValue.toCharArray()[i])) {
				uppercaseCharacterCounter++;
			}
		}
		if (uppercaseCharacterCounter < configuration.get(field).asInt()) {
			String messagePlural = configuration.get(field).asInt() > 1 ? "s"
					: "";

			addError(errors, "the default value must have at least "
					+ configuration.get(field).asInt()
					+ " upper case character" + messagePlural);
		}
	}

	@Override
	protected AttributeConfigurationValidator createFieldValidator(String field) {
		return new IntegerAttributeConfigurationValidator(field);
	}

}
