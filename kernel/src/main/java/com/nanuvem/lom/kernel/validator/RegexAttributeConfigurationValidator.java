package com.nanuvem.lom.kernel.validator;

import static com.nanuvem.lom.kernel.validator.AttributeTypeConfigurationValidator.addError;

import java.util.List;

public class RegexAttributeConfigurationValidator implements ValueValidator<String> {


	public void validate(List<ValidationError> errors,
			String value, String regexValue) {

		if (!value.matches(regexValue)) {
			addError(errors,
					"the default value does not match regex configuration");
		}
	}

	public AttributeConfigurationValidator createFieldValidator(String field) {
		return new StringAttributeConfigurationValidator(field);
	}
}
