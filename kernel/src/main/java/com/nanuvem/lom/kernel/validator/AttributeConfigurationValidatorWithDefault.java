package com.nanuvem.lom.kernel.validator;

import java.util.List;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.kernel.Attribute;

public abstract class AttributeConfigurationValidatorWithDefault extends
		StringAttributeConfigurationValidator {

	protected String defaultField;

	public AttributeConfigurationValidatorWithDefault(String field,
			String defaultField) {
		super(field);

		this.defaultField = defaultField;
	}

	public void validate(List<ValidationError> errors, Attribute attribute,
			JsonNode configuration) {

		super.validate(errors, attribute, configuration);

		if (configuration.has(field)) {
			if (configuration.has(defaultField)) {
				String defaultValue = configuration.get(defaultField).asText();
				validateDefault(errors, attribute, configuration, defaultValue);
			}
		}

	}

	protected abstract void validateDefault(List<ValidationError> errors,
			Attribute attribute, JsonNode configuration, String defaultValue);
}
