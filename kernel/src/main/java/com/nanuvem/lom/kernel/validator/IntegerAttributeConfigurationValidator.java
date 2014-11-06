package com.nanuvem.lom.kernel.validator;

import org.codehaus.jackson.JsonNode;

public class IntegerAttributeConfigurationValidator extends
		AttributeTypeConfigurationValidator {

	public IntegerAttributeConfigurationValidator(String field) {
		super(field, "an integer number");
	}

	@Override
	public boolean validate(JsonNode configuration) {
		if (configuration.has(super.field)) {
			return configuration.get(field).isIntegralNumber();
		}
		return true;
	}

}