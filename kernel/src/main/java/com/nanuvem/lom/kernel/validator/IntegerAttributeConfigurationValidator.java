package com.nanuvem.lom.kernel.validator;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.kernel.Attribute;

public class IntegerAttributeConfigurationValidator extends	AttributeTypeConfigurationValidator {

	public IntegerAttributeConfigurationValidator(String field) {
		super(field, "an integer number");
	}

	@Override
	public boolean validate(Attribute attribute, JsonNode configuration) {
		if (configuration.has(super.field)) {
			return configuration.get(field).isIntegralNumber();
		}
		return true;
	}

}