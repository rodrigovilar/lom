package com.nanuvem.lom.kernel.validator;

import java.util.List;

import org.codehaus.jackson.JsonNode;

public interface ValueValidator<T> {

	AttributeConfigurationValidator createFieldValidator(String field);

	void validate(List<ValidationError> errors, JsonNode configuration, String value, T configurationValue);

}