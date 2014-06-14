package com.nanuvem.lom.kernel;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.nanuvem.lom.kernel.validator.deployer.AttributeTypeDeployer;

public class InstanceHelper {

	public static Instance createOneInstance(
			InstanceServiceImpl instanceService, String classFullName,
			AttributeValue... values) {

		Class clazz = null;
		if (classFullName != null) {
			clazz = AttributeHelper.newClass(classFullName);
		}

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
		Assert.assertEquals(
				classFullName,
				AttributeHelper.newClass(
						createdInstance.getClazz().getFullName()).getFullName());

		verifyAllAttributesValues(createdInstance, values);
	}

	private static void verifyAllAttributesValues(Instance createdInstance,
			AttributeValue... values) {

		boolean allWereValidatedAttributesValues = values == null
				|| values.length == 0 ? true : false;

		for (AttributeValue attributeValue : values) {
			boolean valueParameterOfTheInteractionWasValidated = false;

			for (AttributeValue valueCreated : createdInstance.getValues()) {
				try {
					boolean theAttributeValueIsEqualInAttributesCompared = valueCreated
							.getId().equals(attributeValue.getId());

					if (existsDefaultConfiguration(attributeValue)
							&& theAttributeValueIsEqualInAttributesCompared) {

						valueParameterOfTheInteractionWasValidated = validateThatDefaultConfigurationWasAppliedToValue(valueCreated);
						break;

					} else if (theAttributeValueIsEqualInAttributesCompared) {
						valueParameterOfTheInteractionWasValidated = valueCreated
								.equals(attributeValue);
						break;
					}

				} catch (Exception e) {
					fail();
				}
				allWereValidatedAttributesValues = allWereValidatedAttributesValues
						&& valueParameterOfTheInteractionWasValidated;
			}

		}
		Assert.assertTrue("There has been no validated AttributeValue",
				allWereValidatedAttributesValues);
	}

	private static boolean existsDefaultConfiguration(
			AttributeValue attributeValue) {

		return (attributeValue.getValue() == null)
				&& (attributeValue.getAttribute().getConfiguration() != null)
				&& (attributeValue.getAttribute().getConfiguration()
						.contains(AttributeTypeDeployer.DEFAULT_CONFIGURATION_NAME));
	}

	public static AttributeValue createOneAttributeValue(
			AttributeServiceImpl attributeService, String attributeName,
			String classFullName, Object value) {

		AttributeValue attributeValue = new AttributeValue();
		attributeValue.setAttribute(attributeService
				.findAttributeByNameAndClassFullName(attributeName,
						classFullName));
		attributeValue.setValue(value);
		return attributeValue;
	}

	private static boolean validateThatDefaultConfigurationWasAppliedToValue(
			AttributeValue attributeValue) {
		JsonNode jsonNode = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonFactory factory = objectMapper.getJsonFactory();
			jsonNode = objectMapper.readTree(factory
					.createJsonParser(attributeValue.getAttribute()
							.getConfiguration()));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
			return false;
		}
		String defaultField = jsonNode.get(
				AttributeTypeDeployer.DEFAULT_CONFIGURATION_NAME).asText();
		return attributeValue.getValue().equals(defaultField);

	}
}