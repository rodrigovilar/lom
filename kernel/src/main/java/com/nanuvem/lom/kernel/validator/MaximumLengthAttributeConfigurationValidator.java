package com.nanuvem.lom.kernel.validator;

import java.util.List;

import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.kernel.Attribute;

public class MaximumLengthAttributeConfigurationValidator extends
		AttributeConfigurationValidatorWithDefault {

	public MaximumLengthAttributeConfigurationValidator(String field,
			String defaultField) {
		super(field, defaultField);
	}

	@Override
	protected void validateDefault(List<ValidationError> errors,
			Attribute attribute, JsonNode configuration, String defaultValue) {
		if (defaultValue.length() > configuration.get(field).asInt()) {
			addError(
					errors,
					"Invalid configuration for attribute "
							+ attribute.getName()
							+ ": the default value is greater than maxlength");
		}

	}

	@Override
	protected AttributeConfigurationValidator createFieldValidator(String field) {
		return new IntegerAttributeConfigurationValidator(field);
	}

}
