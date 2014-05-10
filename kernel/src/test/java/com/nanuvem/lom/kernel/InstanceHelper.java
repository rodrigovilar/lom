package com.nanuvem.lom.kernel;

import static org.junit.Assert.fail;
import junit.framework.Assert;

public class InstanceHelper {

	public static Instance createOneInstance(
			InstanceServiceImpl instanceService, String classFullName,
			AttributeValue... values) {

		Class clazz = AttributeHelper.newClass(classFullName);
		Instance instance = new Instance();
		instance.setClazz(clazz);

		for (AttributeValue value : values) {
			value.setInstance(instance);
			instance.getValues().add(value);
		}
		instanceService.create(instance);

		return instance;
	}

	public static void expectExceptionOnCreateInvalidInstance(
			InstanceServiceImpl instanceService, String classFullName,
			String exceptedMessage, AttributeValue... values) {

		try {
			createOneInstance(instanceService, classFullName, values);
			fail();
		} catch (MetadataException metadataException) {
			Assert.assertEquals(exceptedMessage, metadataException.getMessage());
		}
	}

	public static void createAndVerifyOneInstance(
			InstanceServiceImpl instanceService, String classFullName) {

		Instance createdInstance = createOneInstance(instanceService,
				classFullName);

		Assert.assertNotNull(createdInstance.getId());
		Assert.assertEquals(new Integer(0), createdInstance.getVersion());
		Assert.assertEquals(createdInstance,
				instanceService.findInstanceById(createdInstance.getId()));
		Assert.assertEquals(classFullName, createdInstance.getClazz()
				.getFullName());
	}

	// public static Attribute updateAttribute(
	// AttributeServiceImpl attributeService, String fullClassName,
	// Attribute oldAttribute, Integer newSequence, String newName,
	// AttributeType newType, String newConfiguration) {
	//
	// Attribute attribute = attributeService
	// .findAttributeByNameAndClassFullName(oldAttribute.getName(),
	// fullClassName);
	//
	// attribute.setSequence(newSequence);
	// attribute.setName(newName);
	// attribute.setType(newType);
	// attribute.setConfiguration(newConfiguration);
	// attribute.setId(oldAttribute.getId());
	// attribute.setVersion(oldAttribute.getVersion());
	//
	// return attributeService.update(attribute);
	// }
	//
	// public static void expectExceptionOnUpdateInvalidAttribute(
	// AttributeServiceImpl attributeService, String classFullName, Attribute
	// oldAttribute,
	// Integer newSequence, String newName,
	// AttributeType newType, String newConfiguration, String exceptedMessage) {
	//
	// try {
	// updateAttribute(attributeService, classFullName, oldAttribute,
	// newSequence, newName, newType, newConfiguration);
	// fail();
	// } catch (MetadataException metadataException) {
	// Assert.assertEquals(exceptedMessage, metadataException.getMessage());
	// }
	// }

}
