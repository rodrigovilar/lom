package com.nanuvem.lom.kernel.validator;

import java.util.List;

import org.codehaus.jackson.JsonNode;

public abstract class AttributeConfigurationValidatorWithDefault<T> implements
		AttributeConfigurationValidator {

	protected ValueValidator<T> valueValidator;
	protected String defaultField;
	protected String field;

	public AttributeConfigurationValidatorWithDefault(String field,
			String defaultField, ValueValidator<T> valueValidator) {
		this.field = field;
		this.defaultField = defaultField;
		this.valueValidator = valueValidator;
	}

	public void validate(List<ValidationError> errors, JsonNode configuration) {
		AttributeConfigurationValidator fieldValidator = valueValidator
				.createFieldValidator(field);
		fieldValidator.validate(errors, configuration);

		if (configuration.has(field)) {
			if (configuration.has(defaultField)) {

				String defaultValue = configuration.get(defaultField).asText();

				T configurationValue = getConfigurationValue(configuration);
				valueValidator.validate(errors, defaultValue,
						configurationValue);
			}
		}
	}

	protected abstract T getConfigurationValue(JsonNode configuration);

}