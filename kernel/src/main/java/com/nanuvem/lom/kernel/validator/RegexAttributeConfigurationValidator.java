package com.nanuvem.lom.kernel.validator;

import java.util.List;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.kernel.Attribute;
import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

public class RegexAttributeConfigurationValidator extends
		AttributeConfigurationValidatorWithDefault {

	public RegexAttributeConfigurationValidator(String field,
			String defaultField) {
		super(field, defaultField);
	}

	@Override
	protected void validateDefault(List<ValidationError> errors,
			Attribute attribute, JsonNode configuration, String defaultValue) {

		String regexValue = configuration.get(field).asText();
		if (!defaultValue.matches(regexValue)) {
			addError(errors,
					"the default value does not match regex configuration");
		}
	}

	@Override
	protected AttributeConfigurationValidator createFieldValidator(String field) {
		return new StringAttributeConfigurationValidator(field);
	}
}
