package com.nanuvem.lom.kernel.validator;

import org.codehaus.jackson.JsonNode;

public class StringAttributeConfigurationValidator extends
		AttributeTypeConfigurationValidator {

	public StringAttributeConfigurationValidator(String field) {
		super(field, "a string");
	}

	@Override
	public boolean validate(JsonNode configuration) {
		if (configuration.has(super.field)) {
			return configuration.get(field).isTextual();
		}
		return true;
	}
}
