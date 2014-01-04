package com.nanuvem.lom.kernel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.nanuvem.lom.kernel.dao.AttributeDao;
import com.nanuvem.lom.kernel.dao.DaoFactory;
import com.nanuvem.lom.kernel.validator.AttributeConfigurationValidator;
import com.nanuvem.lom.kernel.validator.BooleanAttributeConfigurationValidator;
import com.nanuvem.lom.kernel.validator.RegexAttributeConfigurationValidator;
import com.nanuvem.lom.kernel.validator.StringAttributeConfigurationValidator;
import com.nanuvem.lom.kernel.validator.ValidationError;

public class AttributeServiceImpl {

	private AttributeDao attributeDao;

	private ClassServiceImpl classService;

	private final Integer MINIMUM_VALUE_FOR_THE_ATTRIBUTE_SEQUENCE = 1;

	private final String MANDATORY_CONFIGURATION_NAME = "mandatory";
	private final String DEFAULT_CONFIGURATION_NAME = "default";
	private final String REGEX_CONFIGURATION_NAME = "regex";
	private final String MINLENGTH_CONFIGURATION_NAME = "minlength";
	private final String MAXLENGTH_CONFIGURATION_NAME = "maxlength";

	private List<AttributeConfigurationValidator> validators = new ArrayList<AttributeConfigurationValidator>();

	public AttributeServiceImpl(DaoFactory dao) {
		this.classService = new ClassServiceImpl(dao);
		this.attributeDao = dao.createAttributeDao();

		validators.add(new BooleanAttributeConfigurationValidator(
				MANDATORY_CONFIGURATION_NAME));
		
		validators.add(new StringAttributeConfigurationValidator(
				DEFAULT_CONFIGURATION_NAME));
		
		validators.add(new RegexAttributeConfigurationValidator(
				REGEX_CONFIGURATION_NAME, DEFAULT_CONFIGURATION_NAME));
	}

	private void validate(Attribute attribute) {
		this.validateConfigurationAttribute(attribute);

		int currentNumberOfAttributes = attribute.getClazz().getAttributes()
				.size();
		this.validateExistingAttributeNotInClass(attribute.getClazz(),
				attribute);

		if (attribute.getSequence() != null) {
			boolean minValueForSequence = attribute.getSequence() < MINIMUM_VALUE_FOR_THE_ATTRIBUTE_SEQUENCE;
			boolean maxValueForSequence = currentNumberOfAttributes + 1 < attribute
					.getSequence();

			if (minValueForSequence || maxValueForSequence) {
				throw new MetadataException(
						"Invalid value for Attribute sequence: "
								+ attribute.getSequence());
			}
		} else {
			attribute.setSequence(currentNumberOfAttributes + 1);
		}

		if (attribute.getName() == null || attribute.getName().isEmpty()) {
			throw new MetadataException("The name of a Attribute is mandatory");
		}

		if (!Pattern.matches("[a-zA-Z1-9]{1,}", attribute.getName())) {
			throw new MetadataException("Invalid value for Attribute name: "
					+ attribute.getName());
		}

		if (attribute.getType() == null) {
			throw new MetadataException("The type of a Attribute is mandatory");
		}
	}

	private void validateConfigurationAttribute(Attribute attribute) {
		String configuration = attribute.getConfiguration();
		if (configuration != null && !configuration.isEmpty()) {

			ObjectMapper objectMapper = new ObjectMapper();
			JsonFactory factory = objectMapper.getJsonFactory();
			JsonNode jsonNode = null;

			try {
				jsonNode = objectMapper.readTree(factory
						.createJsonParser(configuration));
			} catch (Exception e) {
				throw new MetadataException(
						"Invalid value for Attribute configuration: "
								+ configuration);
			}

			String attributeName = attribute.getName();
			List<ValidationError> errors = new ArrayList<ValidationError>();
			for (AttributeConfigurationValidator validator : this.validators) {
				validator.validate(errors, attribute, jsonNode);
			}

			if (!errors.isEmpty()) {
				String errorMessage = "";
				for (ValidationError error : errors) {
					errorMessage += error.getMessage();
				}
				throw new MetadataException(errorMessage);
			}

			// if (jsonNode.has(REGEX_CONFIGURATION_NAME)) {
			// this.validateConfigurationAndTypeOfConfiguration(attributeName,
			// REGEX_CONFIGURATION_NAME,
			// jsonNode.get(REGEX_CONFIGURATION_NAME).isTextual(),
			// "a string");
			//
			// String regexValue = jsonNode.get(REGEX_CONFIGURATION_NAME)
			// .asText();
			// if (jsonNode.has(DEFAULT_CONFIGURATION_NAME)) {
			// String defaultValue = jsonNode.get(
			// DEFAULT_CONFIGURATION_NAME).asText();
			// if (!defaultValue.matches(regexValue)) {
			// throw new MetadataException(
			// "Invalid configuration for attribute "
			// + attributeName
			// + ": the default value does not match regex configuration");
			// }
			// }
			// }

			if (jsonNode.has(MINLENGTH_CONFIGURATION_NAME)) {
				this.validateConfigurationAndTypeOfConfiguration(attributeName,
						MINLENGTH_CONFIGURATION_NAME,
						jsonNode.get(MINLENGTH_CONFIGURATION_NAME)
								.isIntegralNumber(), "an integer number");

				if (jsonNode.has(DEFAULT_CONFIGURATION_NAME)) {
					int minLengthValue = jsonNode.get(
							MINLENGTH_CONFIGURATION_NAME).getIntValue();
					String defaultValue = jsonNode.get(
							DEFAULT_CONFIGURATION_NAME).asText();

					if (defaultValue.length() < minLengthValue) {
						throw new MetadataException(
								"Invalid configuration for attribute "
										+ attributeName
										+ ": the default value is smaller than minlength");
					}
				}
			}

			if (jsonNode.has(MAXLENGTH_CONFIGURATION_NAME)) {
				this.validateConfigurationAndTypeOfConfiguration(attributeName,
						MAXLENGTH_CONFIGURATION_NAME,
						jsonNode.get(MAXLENGTH_CONFIGURATION_NAME)
								.isIntegralNumber(), "an integer number");

				if (jsonNode.has(DEFAULT_CONFIGURATION_NAME)) {
					int maxLengthValue = jsonNode.get(
							MAXLENGTH_CONFIGURATION_NAME).getIntValue();
					String defaultValue = jsonNode.get(
							DEFAULT_CONFIGURATION_NAME).asText();

					if (defaultValue.length() > maxLengthValue) {
						throw new MetadataException(
								"Invalid configuration for attribute "
										+ attributeName
										+ ": the default value is greater than maxlength");
					}
				}
			}

			if (jsonNode.has(MINLENGTH_CONFIGURATION_NAME)
					&& jsonNode.has(MAXLENGTH_CONFIGURATION_NAME)) {
				int minLengthValue = jsonNode.get(MINLENGTH_CONFIGURATION_NAME)
						.getIntValue();
				int maxLengthValue = jsonNode.get(MAXLENGTH_CONFIGURATION_NAME)
						.getIntValue();

				if (minLengthValue > maxLengthValue) {
					throw new MetadataException(
							"Invalid configuration for attribute "
									+ attributeName
									+ ": minlength is greater than maxlength");
				}
			}
		}
	}

	private void validateConfigurationAndTypeOfConfiguration(
			String attributeName, String configurationName, boolean validation,
			String suffixExceptionMessage) {

		if (!validation) {
			throw new MetadataException("Invalid configuration for attribute "
					+ attributeName + ": the " + configurationName
					+ " value must be " + suffixExceptionMessage);
		}
	}

	private Class validateExistingClassForAttribute(Attribute attribute) {
		Class clazz = null;
		try {
			clazz = classService.readClass(attribute.getClazz().getFullName());
		} catch (MetadataException e) {
			throw new MetadataException("Invalid Class: "
					+ attribute.getClazz().getFullName());
		}
		return clazz;
	}

	private void validateExistingAttributeNotInClass(Class clazz,
			Attribute attribute) {

		for (Attribute attrib : clazz.getAttributes()) {
			if (attrib.getName().equalsIgnoreCase(attribute.getName())) {
				throw new MetadataException("Attribute duplication on "
						+ clazz.getFullName()
						+ " Class. It already has an attribute "
						+ attrib.getName() + ".");
			}
		}
	}

	public void create(Attribute attribute) {
		Class clazz = validateExistingClassForAttribute(attribute);
		attribute.setClazz(clazz);
		this.validate(attribute);
		this.attributeDao.create(attribute);
	}

	public List<Attribute> listAllAttributes(String classFullName) {
		return this.attributeDao.listAllAttributes(classFullName);
	}

	public Attribute findAttributeById(Long id) {
		if (id != null) {
			return this.attributeDao.findAttributeById(id);
		} else {
			return null;
		}
	}

	public Attribute findAttributeByNameAndClassFullName(String nameAttribute,
			String classFullName) {

		if ((nameAttribute != null && !nameAttribute.isEmpty())
				&& (classFullName != null && !classFullName.isEmpty())) {
			if (!classFullName.contains(".")) {
				classFullName = ClassServiceImpl.PREVIOUS_NAME_DEFAULT_OF_THE_CLASSFULLNAME
						+ "." + classFullName;
			}

			return this.attributeDao.findAttributeByNameAndClassFullName(
					nameAttribute, classFullName);
		}
		return null;
	}

	public Attribute update(Attribute attribute) {
		return null;
	}
}