package com.nanuvem.lom.kernel.validator;

import java.util.List;

import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.kernel.Attribute;

public class MinimumLengthAttributeConfigurationValidator extends
		AttributeConfigurationValidatorWithDefault {

	public MinimumLengthAttributeConfigurationValidator(String field,
			String defaultField) {
		super(field, defaultField);
	}

	@Override
	protected void validateDefault(List<ValidationError> errors,
			Attribute attribute, JsonNode configuration, String defaultValue) {
		if (defaultValue.length() < configuration.get(field).asInt()) {
			addError(
					errors,
					"Invalid configuration for attribute "
							+ attribute.getName()
							+ ": the default value is smaller than minlength");
		}

	}

	@Override
	protected AttributeConfigurationValidator createFieldValidator(String field) {
		return new IntegerAttributeConfigurationValidator(field);
	}

}
