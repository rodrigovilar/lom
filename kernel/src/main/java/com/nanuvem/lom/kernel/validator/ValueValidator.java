package com.nanuvem.lom.kernel.validator;

import java.util.List;

public interface ValueValidator<T> {

	AttributeConfigurationValidator createFieldValidator(String field);

	void validate(List<ValidationError> errors, String value, T configurationValue);

}