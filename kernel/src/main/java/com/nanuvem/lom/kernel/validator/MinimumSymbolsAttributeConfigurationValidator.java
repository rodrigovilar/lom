package com.nanuvem.lom.kernel.validator;

import java.util.List;

import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.kernel.Attribute;

public class MinimumSymbolsAttributeConfigurationValidator extends
		AttributeConfigurationValidatorWithDefault {

	public MinimumSymbolsAttributeConfigurationValidator(String field,
			String defaultField) {
		super(field, defaultField);
	}

	@Override
	protected void validateDefault(List<ValidationError> errors,
			Attribute attribute, JsonNode configuration, String defaultValue) {

		int numericSymbolCounter = 0;
		for (int i = 0; i < defaultValue.length(); i++) {
			if (!Character.isLetterOrDigit(defaultValue.toCharArray()[i])) {
				numericSymbolCounter++;
			}
		}

		if (numericSymbolCounter < configuration.get(field).asInt()) {
			String messagePlural = configuration.get(field).asInt() > 1 ? "s"
					: "";

			addError(errors, "the default value must have at least "
					+ configuration.get(field).asInt() + " symbol character"
					+ messagePlural);
		}
	}

	@Override
	protected AttributeConfigurationValidator createFieldValidator(String field) {
		return new IntegerAttributeConfigurationValidator(field);
	}

}
