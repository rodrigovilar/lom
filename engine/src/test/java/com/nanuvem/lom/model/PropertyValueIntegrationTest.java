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

	@Test
	public void testMarkerMethod() {
	}

	@Test
	public void validInstancePropertyAndValue() {
		entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		this.entityService.saveEntity(entity);

		property = CommonCreateMethodsForTesting.createProperty("property",
				null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property);

		instance = CommonCreateMethodsForTesting.createInstance(entity);
		this.instanceService.saveInstance(instance);

		propertyValue = CommonCreateMethodsForTesting.createPropertyValue(null,
				instance, property);
		this.propertyValueService.savePropertyValue(propertyValue);

		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));
		Assert.assertEquals(1, this.propertyService.countAllPropertys());

	}

	@Test
	public void validTextPropertyValue() {
		entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		this.entityService.saveEntity(entity);

		property = CommonCreateMethodsForTesting.createProperty("property",
				null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property);

		instance = CommonCreateMethodsForTesting.createInstance(entity);
		this.instanceService.saveInstance(instance);

		propertyValue = CommonCreateMethodsForTesting.createPropertyValue(
				"TEXT", instance, property);
		this.propertyValueService.savePropertyValue(propertyValue);

		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));
		Assert.assertEquals(1, this.propertyService.countAllPropertys());
	}

	public void validLongTextPropertyValue() {
		entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		this.entityService.saveEntity(entity);

		property = CommonCreateMethodsForTesting.createProperty("property",
				null, PropertyType.LONG_TEXT, entity);
		propertyService.saveProperty(property);

		instance = CommonCreateMethodsForTesting.createInstance(entity);
		this.instanceService.saveInstance(instance);

		propertyValue = CommonCreateMethodsForTesting.createPropertyValue(
				"LOOOOOOOOOOOOOOOOOONG TEXT", instance, property);
		this.propertyValueService.savePropertyValue(propertyValue);

		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));
		Assert.assertEquals(1, this.propertyService.countAllPropertys());
	}

	@Test
	public void validPasswordPropertyValue() {
		entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		this.entityService.saveEntity(entity);

		property = CommonCreateMethodsForTesting.createProperty("property",
				null, PropertyType.PASSWORD, entity);
		propertyService.saveProperty(property);

		instance = CommonCreateMethodsForTesting.createInstance(entity);
		this.instanceService.saveInstance(instance);

		propertyValue = CommonCreateMethodsForTesting.createPropertyValue(
				"*******", instance, property);
		this.propertyValueService.savePropertyValue(propertyValue);

		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));
		Assert.assertEquals(1, this.propertyService.countAllPropertys());
	}

	@Test
	public void validIntegerPropertyValue() {
		entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		this.entityService.saveEntity(entity);

		property = CommonCreateMethodsForTesting.createProperty("property",
				null, PropertyType._INTEGER, entity);
		propertyService.saveProperty(property);

		instance = CommonCreateMethodsForTesting.createInstance(entity);
		this.instanceService.saveInstance(instance);

		propertyValue = CommonCreateMethodsForTesting.createPropertyValue(
				"12345", instance, property);
		this.propertyValueService.savePropertyValue(propertyValue);

		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));
		Assert.assertEquals(1, this.propertyService.countAllPropertys());
	}
	
	@Test
	public void validRealPropertyValue() {
		entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		this.entityService.saveEntity(entity);

		property = CommonCreateMethodsForTesting.createProperty("property",
				null, PropertyType._REAL, entity);
		propertyService.saveProperty(property);

		instance = CommonCreateMethodsForTesting.createInstance(entity);
		this.instanceService.saveInstance(instance);

		propertyValue = CommonCreateMethodsForTesting.createPropertyValue(
				"1.012355876", instance, property);
		this.propertyValueService.savePropertyValue(propertyValue);

		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));
		Assert.assertEquals(1, this.propertyService.countAllPropertys());
	}
	
	@Test
	public void validDatePropertyValue() {
		entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		this.entityService.saveEntity(entity);

		property = CommonCreateMethodsForTesting.createProperty("property",
				null, PropertyType._DATE, entity);
		propertyService.saveProperty(property);

		instance = CommonCreateMethodsForTesting.createInstance(entity);
		this.instanceService.saveInstance(instance);

		propertyValue = CommonCreateMethodsForTesting.createPropertyValue(
				"31/12/2013", instance, property);
		this.propertyValueService.savePropertyValue(propertyValue);

		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));
		Assert.assertEquals(1, this.propertyService.countAllPropertys());
	}
	
	@Test
	public void validTimePropertyValue() {
		entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		this.entityService.saveEntity(entity);

		property = CommonCreateMethodsForTesting.createProperty("property",
				null, PropertyType._TIME, entity);
		propertyService.saveProperty(property);

		instance = CommonCreateMethodsForTesting.createInstance(entity);
		this.instanceService.saveInstance(instance);

		propertyValue = CommonCreateMethodsForTesting.createPropertyValue(
				"00:00", instance, property);
		this.propertyValueService.savePropertyValue(propertyValue);

		Assert.assertEquals(propertyValue, this.propertyValueService
				.findPropertyValue(propertyValue.getId()));
		Assert.assertEquals(1, this.propertyService.countAllPropertys());
	}
	
	
}
