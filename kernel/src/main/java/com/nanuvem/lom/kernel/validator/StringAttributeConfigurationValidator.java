package com.nanuvem.lom.kernel.validator;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.kernel.Attribute;

public class StringAttributeConfigurationValidator extends
		AbstractAttributeConfigurationValidator {
	

	public StringAttributeConfigurationValidator(String field){
		super(field, "a string");
	}

	@Override
	public boolean validate(Attribute attribute, JsonNode configuration) {
		if (configuration.has(super.field)) {
			return configuration.get(field).isTextual();
		}
		return true;
	}
}
