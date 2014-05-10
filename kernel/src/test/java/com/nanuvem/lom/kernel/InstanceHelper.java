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
			InstanceServiceImpl instanceService, String classFullName,
			AttributeValue... values) {

		Instance createdInstance = createOneInstance(instanceService,
				classFullName, values);

		Assert.assertNotNull(createdInstance.getId());
		Assert.assertEquals(new Integer(0), createdInstance.getVersion());
		Assert.assertEquals(createdInstance,
				instanceService.findInstanceById(createdInstance.getId()));

		Assert.assertEquals(applyStandardNamingTheClassFullName(classFullName),
				createdInstance.getClazz().getFullName());

		if (values != null && values.length > 0) {
			boolean containsAllAttributesValues = false;
			for (AttributeValue value : createdInstance.getValues()) {
				containsAllAttributesValues = false;
				for (int i = 0; i < values.length; i++) {
					if (value.getAttribute().equals(values[i].getAttribute())
							&& value.getValue().equals(values[i].getValue())) {
						containsAllAttributesValues = true;
						break;
					}
				}
				Assert.assertTrue(containsAllAttributesValues);
			}
		}
	}

	private static String applyStandardNamingTheClassFullName(
			String classFullName) {
		try {
			classFullName.substring(0, classFullName.lastIndexOf("."));
			classFullName.substring(classFullName.lastIndexOf(".") + 1,
					classFullName.length());
		} catch (StringIndexOutOfBoundsException exception) {
			classFullName = ClassServiceImpl.PREVIOUS_NAME_DEFAULT_OF_THE_CLASSFULLNAME
					+ "." + classFullName;
		}
		return classFullName;
	}
}