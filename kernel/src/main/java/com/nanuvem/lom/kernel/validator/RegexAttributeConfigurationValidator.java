package com.nanuvem.lom.kernel.validator;

import java.util.List;

import org.codehaus.jackson.JsonNode;

import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

public class RegexAttributeConfigurationValidator implements ValueValidator<String> {


	public void validate(List<ValidationError> errors,
			JsonNode configuration, String value, String regexValue) {

		if (!value.matches(regexValue)) {
			addError(errors,
					"the default value does not match regex configuration");
		}
	}

	public AttributeConfigurationValidator createFieldValidator(String field) {
		return new StringAttributeConfigurationValidator(field);
	}
}
