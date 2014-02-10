package com.nanuvem.lom.kernel.validator;

import java.util.List;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.kernel.Attribute;

public abstract class AttributeConfigurationValidatorWithDefault implements
		AttributeConfigurationValidator {

	protected AttributeConfigurationValidator validator;
	protected String defaultField;
	protected String field;

	public AttributeConfigurationValidatorWithDefault(String field,
			String defaultField) {
		this.field = field;
		this.defaultField = defaultField;
		this.validator = createFieldValidator(field);
	}

	protected abstract AttributeConfigurationValidator createFieldValidator(
			String field);

	public void validate(List<ValidationError> errors, Attribute attribute,
			JsonNode configuration) {

		this.validator.validate(errors, attribute, configuration);
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
