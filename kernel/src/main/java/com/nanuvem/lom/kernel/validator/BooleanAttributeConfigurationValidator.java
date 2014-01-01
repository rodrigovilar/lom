package com.nanuvem.lom.kernel.validator;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.kernel.Attribute;

public class BooleanAttributeConfigurationValidator extends
		AbstractAttributeConfigurationValidator {
	

	public BooleanAttributeConfigurationValidator(String field){
		super(field, "true or false literals");
	}

	@Override
	public boolean validate(Attribute attribute, JsonNode configuration) {
		if (configuration.has(super.field)) {
			return configuration.get(field).isBoolean();
		}
		return true;
	}
}
