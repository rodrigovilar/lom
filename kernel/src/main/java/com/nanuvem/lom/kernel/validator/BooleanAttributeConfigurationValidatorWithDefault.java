package com.nanuvem.lom.kernel.validator;

import org.codehaus.jackson.JsonNode;

public class BooleanAttributeConfigurationValidatorWithDefault extends
		AttributeConfigurationValidatorWithDefault<Boolean> {

	public BooleanAttributeConfigurationValidatorWithDefault(String field,
			String defaultField, ValueValidator<Boolean> valueValidator) {
		super(field, defaultField, valueValidator);
	}

	protected Boolean getConfigurationValue(JsonNode configuration) {
		return configuration.get(field).asBoolean();
	}

}