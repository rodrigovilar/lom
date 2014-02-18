package com.nanuvem.lom.kernel.validator;

import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

import java.util.List;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.kernel.Attribute;

public class MaximumRepeatAttributeConfigurationValidator extends
		AttributeConfigurationValidatorWithDefault {

	public MaximumRepeatAttributeConfigurationValidator(String field,
			String defaultField) {
		super(field, defaultField);
	}

	@Override
	protected void validateDefault(List<ValidationError> errors,
			Attribute attribute, JsonNode configuration, String defaultValue) {

		int repeatedCharacterCounter = 0;
		for (int i = 0; i < defaultValue.length(); i++) {
			for (int j = i + 1; j < defaultValue.length(); j++) {
				if (defaultValue.toCharArray()[i] == defaultValue.toCharArray()[j]) {
					repeatedCharacterCounter++;
				}
			}
		}

		int maxRepeat = configuration.get(field).asInt();
		if (repeatedCharacterCounter > maxRepeat) {
			String messagePlural = repeatedCharacterCounter > 1 ? " more than "
					+ (maxRepeat + 1) + " " : " ";

			addError(errors, "the default value must not have" + messagePlural
					+ "repeated characters");
		}
	}

	@Override
	protected AttributeConfigurationValidator createFieldValidator(String field) {
		return new IntegerAttributeConfigurationValidator(field);
	}

}
