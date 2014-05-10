package com.nanuvem.lom.kernel.validator.deployer;

import java.util.ArrayList;
import java.util.List;

import com.nanuvem.lom.kernel.validator.AttributeConfigurationValidator;
import com.nanuvem.lom.kernel.validator.BooleanAttributeConfigurationValidator;
import com.nanuvem.lom.kernel.validator.MaximumLengthAttributeConfigurationValidator;
import com.nanuvem.lom.kernel.validator.MinAndMaxConfigurationValidator;
import com.nanuvem.lom.kernel.validator.MinimumLengthAttributeConfigurationValidator;
import com.nanuvem.lom.kernel.validator.StringAttributeConfigurationValidator;

public class LongTextAttributeTypeDeployer implements AttributeTypeDeployer {

	public List<AttributeConfigurationValidator> getValidators() {
		List<AttributeConfigurationValidator> validators = new ArrayList<AttributeConfigurationValidator>();

		validators.add(new StringAttributeConfigurationValidator(
				DEFAULT_CONFIGURATION_NAME));
		validators.add(new MinimumLengthAttributeConfigurationValidator(
				MINLENGTH_CONFIGURATION_NAME, DEFAULT_CONFIGURATION_NAME));
		validators.add(new MaximumLengthAttributeConfigurationValidator(
				MAXLENGTH_CONFIGURATION_NAME, DEFAULT_CONFIGURATION_NAME));
		validators.add(new MinAndMaxConfigurationValidator(
				MAXLENGTH_CONFIGURATION_NAME, MINLENGTH_CONFIGURATION_NAME));

		validators.add(new BooleanAttributeConfigurationValidator(
				MANDATORY_CONFIGURATION_NAME));

		return validators;
	}

	public boolean containsConfigurationField(String fieldName) {
		return MANDATORY_CONFIGURATION_NAME.equals(fieldName)
				|| DEFAULT_CONFIGURATION_NAME.equals(fieldName)
				|| MINLENGTH_CONFIGURATION_NAME.equals(fieldName)
				|| MAXLENGTH_CONFIGURATION_NAME.equals(fieldName);
	}

}
