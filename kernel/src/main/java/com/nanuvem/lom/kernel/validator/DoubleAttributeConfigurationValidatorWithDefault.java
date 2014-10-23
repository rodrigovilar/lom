package com.nanuvem.lom.kernel.validator;

import org.codehaus.jackson.JsonNode;

public class DoubleAttributeConfigurationValidatorWithDefault extends
		AttributeConfigurationValidatorWithDefault<Double> {

	public DoubleAttributeConfigurationValidatorWithDefault(String field,
			String defaultField, ValueValidator<Double> valueValidator) {
		super(field, defaultField, valueValidator);
	}

	protected Double getConfigurationValue(JsonNode configuration) {
		return configuration.get(field).asDouble();
	}

}