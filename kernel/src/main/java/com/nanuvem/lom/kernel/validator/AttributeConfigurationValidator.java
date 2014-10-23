package com.nanuvem.lom.kernel.validator;

import java.util.List;

import org.codehaus.jackson.JsonNode;

public interface AttributeConfigurationValidator {

	void validate(List<ValidationError> errors, JsonNode configuration);
}