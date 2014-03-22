package com.nanuvem.lom.kernel.validator;

import java.util.List;

import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.kernel.Attribute;

public class MaximumValueAttributeConfigurationValidator extends
		AttributeConfigurationValidatorWithDefault {

	public MaximumValueAttributeConfigurationValidator(String field,
			String defaultField) {
		super(field, defaultField);
	}

	@Override
	protected void validateDefault(List<ValidationError> errors,
			Attribute attribute, JsonNode configuration, String defaultValue) {
		Integer intDefaultValue = Integer.parseInt(defaultValue);
		if (intDefaultValue > configuration.get(field).asInt()) {
			addError(errors, "the default value is greater than maxvalue");
		}
	}

	@Override
	protected AttributeConfigurationValidator createFieldValidator(String field) {
		return new IntegerAttributeConfigurationValidator(field);
	}
}
