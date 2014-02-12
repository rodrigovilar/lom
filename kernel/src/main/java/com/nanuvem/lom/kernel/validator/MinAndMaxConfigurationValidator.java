package com.nanuvem.lom.kernel.validator;

import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

import java.util.List;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.kernel.Attribute;

public class MinAndMaxConfigurationValidator implements
		AttributeConfigurationValidator {

	private String maxField;
	private String minField;

	public MinAndMaxConfigurationValidator(String maxField, String minField) {
		this.maxField = maxField;
		this.minField = minField;
	}

	public void validate(List<ValidationError> errors, Attribute attribute,
			JsonNode configuration) {

		if (configuration.has(maxField) && configuration.has(minField)) {

			int minLengthValue = configuration.get(minField).getIntValue();
			int maxLengthValue = configuration.get(maxField).getIntValue();

			if (minLengthValue > maxLengthValue) {
				addError(errors, minField + " is greater than " + maxField);
			}
		}

	}

}
