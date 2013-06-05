package com.nanuvem.lom.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

import com.nanuvem.lom.dao.typesquare.Entity;
import com.nanuvem.lom.dao.typesquare.Instance;
import com.nanuvem.lom.dao.typesquare.Property;
import com.nanuvem.lom.dao.typesquare.PropertyType;
import com.nanuvem.lom.dao.typesquare.PropertyValue;
import com.nanuvem.lom.service.EntityServiceImpl;
import com.nanuvem.lom.service.InstanceNotFoundException;
import com.nanuvem.lom.service.InstanceServiceImpl;
import com.nanuvem.lom.service.PropertyServiceImpl;
import com.nanuvem.lom.service.PropertyValueNotFoundException;

@RooIntegrationTest(entity = PropertyValue.class)
public class PropertyValueIntegrationTest {

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

	@Test(expected = PropertyValueNotFoundException.class)
	public void testDeletePropertyValue() {
		PropertyValue obj = dod.getRandomPropertyValue();
		Assert.assertNotNull(
				"Data on demand for 'PropertyValue' failed to initialize correctly",
				obj);
		Long id = obj.getId();
		Assert.assertNotNull(
				"Data on demand for 'PropertyValue' failed to provide an identifier",
				id);
		obj = propertyValueService.findPropertyValue(id);
		propertyValueService.deletePropertyValue(obj);
		obj.flush();
		propertyValueService.findPropertyValue(id);
	}

	@Test
	public void validInstancePropertyAndNullValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType.TEXT, null);

		this.verifyPropertyValue(propertyValue);
	}

	@Test
	public void validTextPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType.TEXT, "TEXT");

		this.verifyPropertyValue(propertyValue);

	}

	public void validLongTextPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType.LONG_TEXT,
				"LOOOOOOOOOOOOOOOOOONG TEXT");
		this.verifyPropertyValue(propertyValue);
	}

	@Test
	public void validPasswordPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType.PASSWORD, "******");
		this.verifyPropertyValue(propertyValue);

	}

	@Test
	public void validIntegerPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType._INTEGER, "12345");
		this.verifyPropertyValue(propertyValue);
	}

	@Test
	public void validRealPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType._REAL, "1.0123456");
		this.verifyPropertyValue(propertyValue);
	}

	@Test
	public void validDatePropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType._DATE,
				"31/12/2013 TEXT");
		this.verifyPropertyValue(propertyValue);
	}

	@Test
	public void validTimePropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType._TIME, "00:00");
		this.verifyPropertyValue(propertyValue);
	}

	@Test
	public void updateValidTextPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType.TEXT, "TEXT1");
		propertyValue.flush();
		propertyValue.clear();
		propertyValue.set_value("NEW TEXT VALUE");
		propertyValue.merge();
		propertyValue.flush();
		this.verifyPropertyValue(propertyValue);

	}

	@Test
	public void updateValidLongTextPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType.LONG_TEXT,
				"LOOOOOOOOOOOOOOOOOONG TEXT");
		propertyValue.flush();
		propertyValue.clear();
		propertyValue.set_value("NEW LONG TEXT");
		propertyValue.merge();
		propertyValue.flush();
		this.verifyPropertyValue(propertyValue);
	}

	@Test
	public void updateValidPasswordPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType.PASSWORD, "******");
		propertyValue.flush();
		propertyValue.clear();
		propertyValue.set_value("newpassword***");
		propertyValue.merge();
		propertyValue.flush();
		this.verifyPropertyValue(propertyValue);
	}

	@Test
	public void updateValidIntegerPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType._INTEGER, "12345");
		propertyValue.flush();
		propertyValue.clear();
		propertyValue.set_value("100000");
		propertyValue.merge();
		propertyValue.flush();
		this.verifyPropertyValue(propertyValue);

	}

	@Test
	public void updateValidRealPropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType._REAL, "1.0123456");
		propertyValue.flush();
		propertyValue.clear();
		propertyValue.set_value("1000.1826634949");
		propertyValue.merge();
		propertyValue.flush();
		this.verifyPropertyValue(propertyValue);

	}

	@Test
	public void updateValidDatePropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType._DATE, "31/12/2013");
		propertyValue.flush();
		propertyValue.clear();
		propertyValue.set_value("01/01/2014");
		propertyValue.merge();
		propertyValue.flush();
		this.verifyPropertyValue(propertyValue);
	}

	@Test
	public void updateValidTimePropertyValue() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType._TIME, "00:00");
		propertyValue.flush();
		propertyValue.clear();
		propertyValue.set_value("11:11");
		propertyValue.merge();
		propertyValue.flush();
		this.verifyPropertyValue(propertyValue);

	}

	@Test
	public void listAllValueOfAnInstance() {
		Entity entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		this.entityService.saveEntity(entity);

		Property property = CommonCreateMethodsForTesting.createProperty(
				"property", "", PropertyType.TEXT, entity);
		this.propertyService.saveProperty(property);

		Instance instance = CommonCreateMethodsForTesting
				.createInstance(entity);
		this.instanceService.saveInstance(instance);

		PropertyValue propertyValue1 = CommonCreateMethodsForTesting
				.createPropertyValue("propertyValue1", instance, property);
		this.propertyValueService.savePropertyValue(propertyValue);

		PropertyValue propertyValue2 = CommonCreateMethodsForTesting
				.createPropertyValue("propertyValue2", instance, property);
		this.propertyValueService.savePropertyValue(propertyValue);

		PropertyValue propertyValue3 = CommonCreateMethodsForTesting
				.createPropertyValue("propertyValue3", instance, property);
		this.propertyValueService.savePropertyValue(propertyValue);

		List<PropertyValue> listOfPropertyValues = this.propertyValueService
				.findAllPropertyValues();

		Assert.assertTrue(listOfPropertyValues.contains(propertyValue1));
		Assert.assertTrue(listOfPropertyValues.contains(propertyValue2));
		Assert.assertTrue(listOfPropertyValues.contains(propertyValue3));

	}

	@Test
	public void listValuesWhenThereIsNoValues() {
		List<PropertyValue> findAllPropertyValues = this.propertyValueService
				.findAllPropertyValues();
		Assert.assertEquals(findAllPropertyValues.size(), 0);
	}

	@Test
	public void getPropertyValueById() {
		propertyValue = createPropertyValueAndDependecies("entity",
				"namespace", "property", null, PropertyType._TIME, "00:00");
		this.verifyPropertyValue(propertyValue);
	}

	@Test(expected = PropertyValueNotFoundException.class)
	public void getPropertyValueWithAnUnknownId() {
		this.propertyValueService.findPropertyValue((long) 10000);
	}

	@Test(expected = PropertyValueNotFoundException.class)
	public void getPropertyValueWithAnUnknownInstance() {
		this.propertyValueService.findPropertyValuesByInstance(instance);
	}

	@Test(expected = PropertyValueNotFoundException.class)
	public void deletePropertyValueWithAnUnknownId() {
		propertyValue = new PropertyValue();
		this.propertyValueService.deletePropertyValue(propertyValue);
	}
}
