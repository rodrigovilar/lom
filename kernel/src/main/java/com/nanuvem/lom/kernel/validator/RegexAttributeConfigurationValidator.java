package com.nanuvem.lom.kernel.validator;

import java.util.List;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.kernel.Attribute;

public class RegexAttributeConfigurationValidator extends
		AttributeConfigurationValidatorWithDefault {

	public RegexAttributeConfigurationValidator(String field,
			String defaultField) {
		super(field, defaultField);
	}

	@Override
	protected void validateDefault(List<ValidationError> errors,
			Attribute attribute, JsonNode configuration, String defaultValue) {
		
		String regexValue = configuration.get(field).asText();
		if (!defaultValue.matches(regexValue)) {
			super.addError(
					errors,
					"Invalid configuration for attribute "
							+ attribute.getName()
							+ ": the default value does not match regex configuration");
		}
	}
}
