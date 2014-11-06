package com.nanuvem.lom.kernel.validator;

import org.codehaus.jackson.JsonNode;

public class StringAttributeConfigurationValidatorWithDefault extends
		AttributeConfigurationValidatorWithDefault<String> {

	public StringAttributeConfigurationValidatorWithDefault(String field,
			String defaultField, ValueValidator<String> valueValidator) {
		super(field, defaultField, valueValidator);
	}

	protected String getConfigurationValue(JsonNode configuration) {
		return configuration.get(field).asText();
	}

}