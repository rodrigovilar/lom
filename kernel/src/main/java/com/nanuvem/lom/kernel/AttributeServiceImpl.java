package com.nanuvem.lom.kernel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;

import com.nanuvem.lom.kernel.dao.AttributeDao;
import com.nanuvem.lom.kernel.dao.DaoFactory;
import com.nanuvem.lom.kernel.util.JsonNodeUtil;
import com.nanuvem.lom.kernel.validator.AttributeConfigurationValidator;
import com.nanuvem.lom.kernel.validator.ValidationError;
import com.nanuvem.lom.kernel.validator.deployer.AttributeTypeDeployer;
import com.nanuvem.lom.kernel.validator.deployer.Deployers;

public class AttributeServiceImpl {

	private AttributeDao attributeDao;

	private ClassServiceImpl classService;

	private final Integer MINIMUM_VALUE_FOR_THE_ATTRIBUTE_SEQUENCE = 1;

	private final String PREFIX_EXCEPTION_MESSAGE_CONFIGURATION = "Invalid configuration for attribute";

	private Deployers deployers; 
	

	AttributeServiceImpl(DaoFactory dao, ClassServiceImpl classService, Deployers deployers) {
		this.classService = classService;
		this.deployers = deployers;
		this.attributeDao = dao.createAttributeDao();

	}

	private void validateCreate(Attribute attribute) {
		this.validateExistingAttributeNotInClass(attribute);
		defineAttributeSequenceNumber(attribute);

		this.validateNameAttribute(attribute);

		if (attribute.getType() == null) {
			throw new MetadataException("The type of a Attribute is mandatory");
		}
		this.validateAttributeConfiguration(attribute);
	}

	private void defineAttributeSequenceNumber(Attribute attribute) {
		int currentNumberOfAttributes = attribute.getClazz().getAttributes()
				.size();
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
	}

	private void validateNameAttribute(Attribute attribute) {
		if (attribute.getName() == null || attribute.getName().isEmpty()) {
			throw new MetadataException("The name of an Attribute is mandatory");
		}

		if (!Pattern.matches("[a-zA-Z1-9]{1,}", attribute.getName())) {
			throw new MetadataException("Invalid value for Attribute name: "
					+ attribute.getName());
		}
	}

	private List<Attribute> findAllAttributesForClass(Class clazz) {
		if (clazz != null && !clazz.getFullName().isEmpty()) {
			Class foundClass = this.classService.readClass(clazz.getFullName());
			if (foundClass != null && foundClass.getAttributes() != null
					&& foundClass.getAttributes().size() > 0) {
				return foundClass.getAttributes();
			}
		}
		return null;
	}

	private void validateAttributeConfiguration(Attribute attribute) {
		String configuration = attribute.getConfiguration();
		if (configuration != null && !configuration.isEmpty()) {
			JsonNode jsonNode = JsonNodeUtil.validate(configuration,
					"Invalid value for Attribute configuration: "
							+ configuration);
			validateFieldNames(attribute, jsonNode);
			validateFieldValues(attribute, jsonNode);
		}
	}

	private void validateFieldNames(Attribute attribute, JsonNode jsonNode) {
		Iterator<String> fieldNames = jsonNode.getFieldNames();
		while (fieldNames.hasNext()) {
			String fieldName = fieldNames.next();
			if (!this.deployers.get(attribute.getType().name())
					.containsConfigurationField(fieldName)) {

				throw new MetadataException(
						"Invalid configuration for attribute "
								+ attribute.getName() + ": the " + fieldName
								+ " configuration attribute is unknown");
			}
		}
	}

	private void validateFieldValues(Attribute attribute, JsonNode jsonNode) {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		AttributeTypeDeployer deployer = this.deployers.get(attribute.getType()
				.name());
		for (AttributeConfigurationValidator validator : deployer
				.getValidators()) {
			validator.validate(errors, jsonNode);
		}

		if (!errors.isEmpty()) {
			String errorMessage = "";
			for (ValidationError error : errors) {
				if (errorMessage.isEmpty()) {
					errorMessage += PREFIX_EXCEPTION_MESSAGE_CONFIGURATION
							+ " " + attribute.getName() + ": "
							+ error.getMessage();
				} else {
					errorMessage += ", " + error.getMessage();
				}
			}
			throw new MetadataException(errorMessage);
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

	private void validateExistingAttributeNotInClass(Attribute attribute) {
		List<Attribute> foundAttributes = this
				.findAllAttributesForClass(attribute.getClazz());
		if (foundAttributes != null) {
			for (Attribute at : foundAttributes) {
				if (at.getName().equalsIgnoreCase(attribute.getName())) {
					this.throwMetadataExceptionOnAttributeDuplication(attribute);
				}
			}
		}
	}

	private void validateExistingAttributeNotInClassOnUpdate(Attribute attribute) {
		if (attribute.getId() != null) {
			Attribute foundAttributes = this
					.findAttributeByNameAndClassFullName(attribute.getName(),
							attribute.getClazz().getFullName());
			if (foundAttributes != null) {
				if (!attribute.getId().equals(foundAttributes.getId())) {
					this.throwMetadataExceptionOnAttributeDuplication(attribute);
				}
			}
		}
	}

	private void throwMetadataExceptionOnAttributeDuplication(
			Attribute attribute) {
		throw new MetadataException("Attribute duplication on "
				+ attribute.getClazz().getFullName()
				+ " Class. It already has an attribute "
				+ StringUtils.lowerCase(attribute.getName() + "."));
	}

	public void create(Attribute attribute) {
		Class clazz = validateExistingClassForAttribute(attribute);
		attribute.setClazz(clazz);
		this.validateCreate(attribute);
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
				classFullName = ClassServiceImpl.DEFAULT_NAMESPACE + "."
						+ classFullName;
			}
			return this.attributeDao.findAttributeByNameAndClassFullName(
					nameAttribute, classFullName);
		}
		return null;
	}

	public Attribute update(Attribute attribute) {
		this.validateNameAttribute(attribute);
		this.validateUpdateSequence(attribute);
		this.validateUpdateType(attribute);
		this.validateExistingAttributeNotInClassOnUpdate(attribute);
		this.validateAttributeConfiguration(attribute);

		return this.attributeDao.update(attribute);
	}

	private void validateUpdateType(Attribute attribute) {
		Attribute attributeFound = this.findAttributeById(attribute.getId());

		if (!attributeFound.getType().equals(attribute.getType())) {
			throw new MetadataException(
					"Can not change the type of an attribute");
		}
	}

	private void validateUpdateSequence(Attribute attribute) {
		Class clazz = this.classService.readClass(attribute.getClazz()
				.getFullName());
		int currentNumberOfAttributes = clazz.getAttributes()
				.get(clazz.getAttributes().size() - 1).getSequence();

		if (attribute.getSequence() != null) {
			boolean minValueForSequence = attribute.getSequence() < MINIMUM_VALUE_FOR_THE_ATTRIBUTE_SEQUENCE;
			boolean maxValueForSequence = currentNumberOfAttributes < attribute
					.getSequence();

			if (!(minValueForSequence || maxValueForSequence)) {
				return;
			}
		}
		throw new MetadataException("Invalid value for Attribute sequence: "
				+ attribute.getSequence());
	}
}