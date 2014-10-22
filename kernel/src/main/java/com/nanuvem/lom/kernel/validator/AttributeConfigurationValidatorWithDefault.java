package com.nanuvem.lom.kernel.validator;

import java.util.List;

import org.codehaus.jackson.JsonNode;

public class AttributeConfigurationValidatorWithDefault implements
		AttributeConfigurationValidator {

	protected ValueValidator<?> valueValidator;
	protected String defaultField;
	protected String field;

	public AttributeConfigurationValidatorWithDefault(String field,
			String defaultField, ValueValidator<?> valueValidator) {
		this.field = field;
		this.defaultField = defaultField;
		this.valueValidator = valueValidator;
	}

	@SuppressWarnings("unchecked")
	public void validate(List<ValidationError> errors, JsonNode configuration) {
		AttributeConfigurationValidator fieldValidator = valueValidator
				.createFieldValidator(field);
		fieldValidator.validate(errors, configuration);

		if (configuration.has(field)) {
			if (configuration.has(defaultField)) {

				String defaultValue = configuration.get(defaultField).asText();
				
				if (configuration.get(field).isIntegralNumber()) {
					ValueValidator<Integer> intValueValidator = (ValueValidator<Integer>) valueValidator;
					Integer configurationValue = configuration.get(field)
							.asInt();
					intValueValidator.validate(errors, configuration,
							defaultValue, configurationValue);

				} else if (configuration.get(field).isBoolean()) {
					ValueValidator<Boolean> booleanValueValidator = (ValueValidator<Boolean>) valueValidator;
					Boolean configurationValue = configuration.get(field)
							.asBoolean();
					booleanValueValidator.validate(errors, configuration,
							defaultValue, configurationValue);
				} else if (configuration.get(field).isFloatingPointNumber()) {
					ValueValidator<Double> doubleValueValidator = (ValueValidator<Double>) valueValidator;
					Double configurationValue = configuration.get(field)
							.asDouble();
					doubleValueValidator.validate(errors, configuration,
							defaultValue, configurationValue);
				} else {
					ValueValidator<String> doubleValueValidator = (ValueValidator<String>) valueValidator;
					String configurationValue = configuration.get(field)
							.asText();
					doubleValueValidator.validate(errors, configuration,
							defaultValue, configurationValue);
				}
			}
		}
	}

}