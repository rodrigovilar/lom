package com.nanuvem.lom.kernel.validator.deployer;

import java.util.List;

import com.nanuvem.lom.kernel.validator.AttributeConfigurationValidator;

public interface AttributeTypeDeployer {

	final String MANDATORY_CONFIGURATION_NAME = "mandatory";
	final String DEFAULT_CONFIGURATION_NAME = "default";
	final String REGEX_CONFIGURATION_NAME = "regex";
	final String MINLENGTH_CONFIGURATION_NAME = "minlength";
	final String MAXLENGTH_CONFIGURATION_NAME = "maxlength";

	final String MINUPPERS_CONFIGURATION_NAME = "minUppers";
	final String MINNUMBERS_CONFIGURATION_NAME = "minNumbers";
	final String MINSYMBOLS_CONFIGURATION_NAME = "minSymbols";
	final String MAXREPEAT_CONFIGURATION_NAME = "maxRepeat";

	final String MINVALUE_CONFIGURATION_NAME = "minvalue";
	final String MAXVALUE_CONFIGURATION_NAME = "maxvalue";

	List<AttributeConfigurationValidator> getValidators();

	/**
	 * Returns true when this attribute type deployer accepts
	 * the 'fieldName' configuration field 
	 */
	boolean containsConfigurationField(String fieldName);
	
	Class<?> getAttributeClass();
}
