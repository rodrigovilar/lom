package com.nanuvem.lom.dao.typesquare;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

import com.nanuvem.lom.dao.typesquare.CommonCreateMethodsForTesting;
import com.nanuvem.lom.dao.typesquare.Entity;
import com.nanuvem.lom.dao.typesquare.Property;
import com.nanuvem.lom.dao.typesquare.PropertyType;
import com.nanuvem.lom.service.EntityNotFoundException;
import com.nanuvem.lom.service.EntityServiceImpl;
import com.nanuvem.lom.service.PropertyNotFoundException;
import com.nanuvem.lom.service.PropertyServiceImpl;

@RooIntegrationTest(entity = Property.class, count = false, find = false, findAll = false, findEntries = false, flush = false, merge = false, persist = false, remove = false)
public class PropertyIntegrationTest {
	private Property property;
	private Entity entity;

	@Autowired
	private EntityServiceImpl entityService;

	@Autowired
	private PropertyServiceImpl propertyService;

	@Before
	public void init() {
		entity = new Entity();
		property = new Property();

	}

	@After
	public void cleanDatabase() {
		Entity.entityManager().flush();
		Entity.entityManager().clear();
		List<Entity> allEntitys = entityService.findAllEntitys();
		List<Entity> copy = new ArrayList<Entity>(allEntitys);
		for (Entity entity : copy) {
			entityService.deleteEntity(entity);
		}
	}
	
	@Test
	public void testCountPropertys() {
		int countOfPropertiesBeforeThisTest = propertyService
				.findAllPropertys().size();
		entity = CommonCreateMethodsForTesting.createEntity("entity2",
				"namespace2");
		Assert.assertNotNull(entity);
		this.entityService.saveEntity(entity);

		property = CommonCreateMethodsForTesting.createProperty("property2",
				null, PropertyType._BINARY, entity);
		propertyService.saveProperty(property);
		long count = Property.countPropertys();
		Assert.assertTrue(count == countOfPropertiesBeforeThisTest + 1);
	}

	@Test
	public void testFindProperty() {
		entity = CommonCreateMethodsForTesting.createEntity("entity3",
				"namespace3");
		Assert.assertNotNull(entity);
		this.entityService.saveEntity(entity);

		property = CommonCreateMethodsForTesting.createProperty("property3",
				null, PropertyType._BINARY, entity);
		propertyService.saveProperty(property);

		Property found = propertyService.findProperty(property.getId());
		Assert.assertEquals(property.getId(), found.getId());
	}

	/* CREATE PROPERTY */

	@Test
	public void validEntityPropertyTypeAndName() {
		entity = CommonCreateMethodsForTesting.createEntity("EntityName4",
				"EntityNamespace4");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty(
				"PropertyName4", null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property);
		Assert.assertEquals(propertyService.findProperty(property.getId()),
				property);
	}

	@Test
	public void twoPropertiesWithSameNameInDiferentEntities() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
				"EntityNamespace");
		entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("SameName",
				null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property);
		Entity entity_2 = CommonCreateMethodsForTesting.createEntity(
				"Entity_2", "EntityNamespace");
		entityService.saveEntity(entity_2);
		Property property_2 = CommonCreateMethodsForTesting.createProperty(
				"SameName", null, PropertyType.TEXT, entity_2);
		propertyService.saveProperty(property_2);

		Assert.assertEquals(propertyService.findProperty(property_2.getId()),
				property_2);
		Assert.assertEquals(entity_2, property_2.getEntity());
	}

	@Test
	public void propertyNameWithSpaces() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity2",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty(
				"Name with spaces", null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property);
		Assert.assertEquals(propertyService.findProperty(property.getId()),
				property);
	}

	@Test
	public void configureMandatoryProperty() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_3",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty(
				"Name with spaces", "{\"mandatory\":true}", PropertyType.TEXT,
				entity);
		propertyService.saveProperty(property);

	}

	@Test
	public void configurePropertyWithDefaultValue() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_4",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty(
				"Name with spaces", "{\"default\":\"abc\"}", PropertyType.TEXT,
				entity);

		propertyService.saveProperty(property);
	}

	@Test(expected = ValidationException.class)
	public void propertyWithoutName() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_5",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("", null,
				PropertyType.TEXT, entity);
		propertyService.saveProperty(property);
	}

	@Test(expected = ValidationException.class)
	public void propertyWithoutEntity() {
		Entity nullEntity = null;
		property = CommonCreateMethodsForTesting.createProperty("", null,
				PropertyType.TEXT, nullEntity);
		propertyService.saveProperty(property);
	}

	@Test(expected = ValidationException.class)
	public void twoDifferentTypePropertiesWithSameNameInSameEntity() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_6",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("SameName",
				null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property);

		Property property_2 = CommonCreateMethodsForTesting.createProperty(
				"SameName", null, PropertyType.PASSWORD, entity);
		propertyService.saveProperty(property_2);
	}

	@Test(expected = ValidationException.class)
	public void twoSameTypePropertiesWithSameNameInSameEntity() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_7",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("SameName",
				null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property);
		Property property_2 = CommonCreateMethodsForTesting.createProperty(
				"SameName", null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property_2);
	}

	@Test(expected = ValidationException.class)
	public void nameWithInvalidChars() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_8",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("@#$%", null,
				PropertyType.TEXT, entity);
		propertyService.saveProperty(property);
	}

	@Test(expected = ValidationException.class)
	public void caseInsensitiveNames() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_9",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("SameName",
				null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property);
		Property property_2 = CommonCreateMethodsForTesting.createProperty(
				"samename", null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property_2);
	}

	@Test
	public void textPropertyConfigurationRegexValidation() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_10",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("SameName",
				"{\"regex\":\"a-zA-Z\"}", PropertyType.TEXT, entity);
		propertyService.saveProperty(property);

	}

	@Test
	public void textPropertyConfigurationMaxValidation() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_11",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("SameName",
				"{\"maxsize\":100}", PropertyType.TEXT, entity);
		propertyService.saveProperty(property);
	}

	@Test
	public void textPropertyConfigurationMinValidation() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_12",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("SameName",
				"{\"minsize\":1}", PropertyType.TEXT, entity);
		propertyService.saveProperty(property);
	}

	@Test(expected = ValidationException.class)
	public void forceAwrongConfigurationInRegexValue() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_13",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting
				.createProperty(
						"SameName",
						"{\"regex\":10, "
								+ "\"minsize\": 0, \"maxsize\": 30, \"mandatory\":true}",
						PropertyType.TEXT, entity);
		propertyService.saveProperty(property);

	}

	@Test(expected = ValidationException.class)
	public void forceAwrongConfigurationInMinsizeValue() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_14",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting
				.createProperty(
						"SameName",
						"{\"regex\":\"a-zA-Z\", "
								+ "\"minsize\": notInt, \"maxsize\": 30, \"mandatory\":true}",
						PropertyType.TEXT, entity);
		propertyService.saveProperty(property);

	}

	@Test(expected = ValidationException.class)
	public void forceAwrongConfigurationInMaxsizeValue() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_15",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting
				.createProperty(
						"SameName",
						"{\"regex\":\"a-zA-Z\", "
								+ "\"minsize\": 0, \"maxsize\": notInt, \"mandatory\":true}",
						PropertyType.TEXT, entity);
		propertyService.saveProperty(property);

	}

	@Test(expected = ValidationException.class)
	public void forceAwrongConfigurationInMandatoryValue() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_16",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting
				.createProperty(
						"SameName",
						"{\"regex\":\"a-zA-Z\", "
								+ "\"minsize\": 0, \"maxsize\": 30, \"mandatory\":\"notBoolean\"}",
						PropertyType.TEXT, entity);
		propertyService.saveProperty(property);

	}

	/* READ PROPERTIES */

	@Test
	public void testFindAllPropertys() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_17",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("Property_1",
				null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property);

		Property property_2 = CommonCreateMethodsForTesting.createProperty(
				"Property_2", null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property_2);
		long count = Property.countPropertys();
		List<Property> result = propertyService.findAllPropertys();
		Assert.assertNotNull(
				"Find all method for 'Property' illegally returned null",
				result);
		Assert.assertTrue(
				"Find all method for 'Property' failed to return any data",
				result.size() == count);
	}

	@Test
	public void listAllPropertiesOfAnEntity() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_18",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("Property_1",
				null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property);

		Property property_2 = CommonCreateMethodsForTesting.createProperty(
				"Property_2", null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property_2);

		List<Property> properties = Property.findPropertysByEntity(entity)
				.getResultList();

		Assert.assertTrue(properties.contains(property));
		Assert.assertTrue(properties.contains(property_2));
		Assert.assertTrue(properties.size() == 2);
	}

	@Test
	public void listAllPropertiesOfAnEntityByValidFragmentOfName() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_19",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("Property_1",
				null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property);

		Property property_2 = CommonCreateMethodsForTesting.createProperty(
				"Property_2", null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property_2);

		List<Property> propertiesByFragment = entity.findPropertiesByName("_2");

		Assert.assertTrue(propertiesByFragment.contains(property_2));
		Assert.assertEquals(propertiesByFragment.size(), 1);

	}

	@Test
	public void listAllPropertiesOfAnEntityByEmptyName() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_20",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("Property 1",
				null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property);

		Property property_2 = CommonCreateMethodsForTesting.createProperty(
				"Property_2", null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property_2);

		List<Property> propertiesByFragment = entity.findPropertiesByName("");

		Assert.assertTrue(propertiesByFragment.contains(property));
		Assert.assertTrue(propertiesByFragment.contains(property_2));
		Assert.assertEquals(propertiesByFragment.size(), 2);

	}

	@Test
	public void listAllPropertiesOfAnEntityByFragmentOfNameWithSpaces() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_21",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("Property 1",
				null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property);

		Property property_2 = CommonCreateMethodsForTesting.createProperty(
				"Property_2", null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property_2);

		List<Property> propertiesByFragment = entity
				.findPropertiesByName("y 1");

		Assert.assertTrue(propertiesByFragment.contains(property));
		Assert.assertEquals(propertiesByFragment.size(), 1);
	}

	@Test
	public void listPropertiesWhenThereIsNoProperties() {
		int countOfPropertiesBeforeThisTest = propertyService
				.findAllPropertys().size();
		List<Property> properties = propertyService.findAllPropertys();
		Assert.assertEquals(properties.size(), countOfPropertiesBeforeThisTest);
	}

	@Test
	public void getPropertyByID() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_22",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("Property 1",
				null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property);
		Assert.assertEquals(property,
				propertyService.findProperty(property.getId()));
	}

	@Test(expected = PropertyNotFoundException.class)
	public void getPropertyWithUnknownId() {
		Assert.assertNull(propertyService.findProperty((long) 1000));
	}

	@Test(expected = EntityNotFoundException.class)
	public void listPropertiesOfAnUnknownEntity() {
		Property.findPropertysByEntity(entity);
	}

	/* DELETE PROPERTIES */

	@Test(expected = PropertyNotFoundException.class)
	public void deletePropretyWithAnUnknowId() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_23",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("Property 1",
				null, PropertyType.TEXT, entity);
		propertyService.deleteProperty(property);
	}

}
