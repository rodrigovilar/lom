package com.nanuvem.lom.kernel;

import static org.junit.Assert.fail;
import junit.framework.Assert;

public class AttributeHelper {

	public static final AttributeType TEXT = AttributeType.TEXT;
	public static final AttributeType LONGTEXT = AttributeType.LONGTEXT;
	public static final AttributeType PASSWORD = AttributeType.PASSWORD;

	public static Attribute createOneAttribute(
			AttributeServiceImpl attributeService, String classFullName,
			Integer attributeSequence, String attributeName,
			AttributeType attributeType, String attributeConfiguration) {

		Class clazz = newClass(classFullName);
		Attribute attribute = new Attribute();
		attribute.setName(attributeName);

		attribute.setClazz(clazz);
		attribute.setSequence(attributeSequence);
		attribute.setType(attributeType);
		attribute.setConfiguration(attributeConfiguration);
		attributeService.create(attribute);

		return attribute;
	}

	public static Class newClass(String classFullName) {
		String namespace = null;
		String name = null;

		if (classFullName.contains(".")) {
			namespace = classFullName.substring(0,
					classFullName.lastIndexOf("."));
			name = classFullName.substring(classFullName.lastIndexOf(".") + 1,
					classFullName.length());
		} else {
			name = classFullName;
		}

		Class clazz = new Class();
		clazz.setNamespace(ClassServiceImpl.DEFAULT_NAMESPACE.equals(namespace) ? ""
				: namespace);
		clazz.setName(name);
		return clazz;
	}

	public static void expectExceptionOnCreateInvalidAttribute(
			AttributeServiceImpl attributeService, String classFullName,
			Integer attributeSequence, String attributeName,
			AttributeType attributeType, String attributeConfiguration,
			String exceptedMessage) {

		try {
			createOneAttribute(attributeService, classFullName,
					attributeSequence, attributeName, attributeType,
					attributeConfiguration);
			fail();
		} catch (MetadataException metadataException) {
			Assert.assertEquals(exceptedMessage, metadataException.getMessage());
		}
	}

	public static void createAndVerifyOneAttribute(
			AttributeServiceImpl attributeService, String classFullName,
			Integer attributeSequence, String attributeName,
			AttributeType attributeType, String attributeConfiguration) {

		Attribute createdAttribute = createOneAttribute(attributeService,
				classFullName, attributeSequence, attributeName, attributeType,
				attributeConfiguration);

		Assert.assertNotNull(createdAttribute.getId());
		Assert.assertEquals(new Integer(0), createdAttribute.getVersion());
		Assert.assertEquals(createdAttribute,
				attributeService.findAttributeById(createdAttribute.getId()));
	}

	public static Attribute updateAttribute(
			AttributeServiceImpl attributeService, String fullClassName,
			Attribute oldAttribute, Integer newSequence, String newName,
			AttributeType newType, String newConfiguration) {

		Attribute attribute = attributeService
				.findAttributeByNameAndClassFullName(oldAttribute.getName(),
						fullClassName);

		attribute.setSequence(newSequence);
		attribute.setName(newName);
		attribute.setType(newType);
		attribute.setConfiguration(newConfiguration);
		attribute.setId(oldAttribute.getId());
		attribute.setVersion(oldAttribute.getVersion());

		return attributeService.update(attribute);
	}

	public static void expectExceptionOnUpdateInvalidAttribute(
			AttributeServiceImpl attributeService, String classFullName,
			Attribute oldAttribute, Integer newSequence, String newName,
			AttributeType newType, String newConfiguration,
			String exceptedMessage) {

		try {
			updateAttribute(attributeService, classFullName, oldAttribute,
					newSequence, newName, newType, newConfiguration);
			fail();
		} catch (MetadataException metadataException) {
			Assert.assertEquals(exceptedMessage, metadataException.getMessage());
		}
	}

}
