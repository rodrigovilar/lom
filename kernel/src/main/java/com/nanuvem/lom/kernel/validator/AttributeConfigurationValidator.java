package com.nanuvem.lom.kernel.validator;

import java.util.List;

import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.kernel.Attribute;

public interface AttributeConfigurationValidator {

	void validate(List<ValidationError> errors, Attribute attribute,
			JsonNode configuration);
}
