package com.nanuvem.lom.model;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

import com.nanuvem.lom.service.EntityServiceImpl;
import com.nanuvem.lom.service.InstanceServiceImpl;
import com.nanuvem.lom.service.PropertyServiceImpl;

@RooIntegrationTest(entity = PropertyValue.class)
public class PropertyValueIntegrationTest {
	private Property property;
	private Entity entity;
	private Instance instance;
	private PropertyValue propertyValue;

	@Autowired
	private EntityServiceImpl entityService;

	@Autowired
	private PropertyServiceImpl propertyService;

	@Autowired
	private InstanceServiceImpl instanceService;

	@Before
	public void init() {
		entity = new Entity();
		property = new Property();
		instance = new Instance();
		propertyValue = new PropertyValue();
	}

	private PropertyValue createPropertyValueAndDependecies(String entityName,
			String namespace, String configuration, String defaultValue,
			PropertyType propertyType, String value) {

		Entity entity = CommonCreateMethodsForTesting.createEntity(entityName,
				namespace);
		this.entityService.saveEntity(entity);

		Property property = CommonCreateMethodsForTesting.createProperty(
				configuration, defaultValue, propertyType, entity);
		this.propertyService.saveProperty(property);

		Instance instance = CommonCreateMethodsForTesting
				.createInstance(entity);
		this.instanceService.saveInstance(instance);

		propertyValue = CommonCreateMethodsForTesting.createPropertyValue(
				value, instance, property);
		this.propertyValueService.savePropertyValue(propertyValue);

		return propertyValue;
	}

	private void verifyPropertyValue(PropertyValue pv) {
		Assert.assertEquals(pv,
				this.propertyValueService.findPropertyValue(pv.getId()));
	}

	@Test
	public void validInstancePropertyAndNullValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType.TEXT, null);

		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));
	}

	@Test
	public void validTextPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType.TEXT, "TEXT");

		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));

	}

	public void validLongTextPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType.LONG_TEXT,
				"LOOOOOOOOOOOOOOOOOONG TEXT");

		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));
	}

	@Test
	public void validPasswordPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType.PASSWORD, "******");

		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));

	}

	@Test
	public void validIntegerPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType._INTEGER, "12345");
		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));
	}

	@Test
	public void validRealPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType._REAL, "1.0123456");
		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));
	}

	@Test
	public void validDatePropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType._DATE,
				"31/12/2013 TEXT");
		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));
	}

	@Test
	public void validTimePropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType._TIME, "00:00");
		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));
	}

	@Test
	public void updateValidTextPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType.TEXT, "TEXT");

		propertyValue.set_value("NEW TEXT VALUE");

		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));

	}

	@Test
	public void updateValidLongTextPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType.LONG_TEXT,
				"LOOOOOOOOOOOOOOOOOONG TEXT");

		propertyValue.set_value("NEW LONG TEXT");

		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));
	}

	@Test
	public void updateValidPasswordPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType.PASSWORD, "******");

		propertyValue.set_value("newpassword***");

		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));
	}

	@Test
	public void updateValidIntegerPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType._INTEGER, "12345");

		propertyValue.set_value("100000");

		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));

	}

	@Test
	public void updateValidRealPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType._REAL, "1.0123456");

		propertyValue.set_value("1000.1826634949");

		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));

	}

	@Test
	public void updateValidDatePropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType._DATE, "31/12/2013");

		propertyValue.set_value("01/01/2014");

		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));

	}

	@Test
	public void updateValidTimePropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType._TIME, "00:00");

		propertyValue.set_value("11:11");

		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));

	}

	@Test
	public void read() {

	}

}
