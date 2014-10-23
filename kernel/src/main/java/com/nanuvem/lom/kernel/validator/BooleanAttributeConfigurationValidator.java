package com.nanuvem.lom.kernel.validator;

import org.codehaus.jackson.JsonNode;

public class BooleanAttributeConfigurationValidator extends
		AttributeTypeConfigurationValidator {

	public BooleanAttributeConfigurationValidator(String field) {
		super(field, "true or false literals");
	}

	@Override
	public boolean validate(JsonNode configuration) {
		if (configuration.has(super.field)) {
			return configuration.get(field).isBoolean();
		}
		return true;
	}
}
