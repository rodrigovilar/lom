package com.nanuvem.lom.model;

import java.util.List;

import javax.validation.ValidationException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

import com.nanuvem.lom.model.CommonCreateMethodsForTesting;
import com.nanuvem.lom.service.EntityNotFoundException;
import com.nanuvem.lom.service.EntityServiceImpl;
import com.nanuvem.lom.service.PropertyNotFoundException;
import com.nanuvem.lom.service.PropertyServiceImpl;

@RooIntegrationTest(entity = Property.class)
public class PropertyIntegrationTest {
	private Property property;
	private Entity entity;

	@Autowired
	private PropertyDataOnDemand dod;

	@Autowired
	private EntityServiceImpl entityService;

	@Autowired
	private PropertyServiceImpl propertyService;

	@Before
	public void init() {
		entity = new Entity();
		property = new Property();

	}

	@Test
	public void testCountAllPropertys() {
		entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		Assert.assertNotNull(entity);
		this.entityService.saveEntity(entity);

		property = CommonCreateMethodsForTesting.createProperty("property",
				null, PropertyType._BINARY, entity);
		propertyService.saveProperty(property);
		long count = propertyService.countAllPropertys();
		Assert.assertTrue(
				"Counter for 'Property' incorrectly reported there were no entries",
				count > 0);
	}

	@Test
	public void testCountPropertys() {
		entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		Assert.assertNotNull(entity);
		this.entityService.saveEntity(entity);

		property = CommonCreateMethodsForTesting.createProperty("property",
				null, PropertyType._BINARY, entity);
		propertyService.saveProperty(property);
		long count = Property.countPropertys();
		Assert.assertTrue(count == 1);
	}

	@Test
	public void testFindProperty() {
		entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		Assert.assertNotNull(entity);
		this.entityService.saveEntity(entity);

		property = CommonCreateMethodsForTesting.createProperty("property",
				null, PropertyType._BINARY, entity);
		propertyService.saveProperty(property);

		Property found = propertyService.findProperty(property.getId());
		Assert.assertEquals(property.getId(), found.getId());
	}

	@Test
	public void testFlush() {
		entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		this.entityService.saveEntity(entity);

		property = CommonCreateMethodsForTesting.createProperty("property",
				null, PropertyType._BINARY, entity);
		propertyService.saveProperty(property);
		Long id = property.getId();
		Assert.assertNotNull(
				"Data on demand for 'Property' failed to provide an identifier",
				id);
		property = propertyService.findProperty(id);
		Assert.assertNotNull(
				"Find method for 'Property' illegally returned null for id '"
						+ id + "'", property);

		boolean modified = dod.modifyProperty(property);

		Integer currentVersion = property.getVersion();
		property.flush();
		Assert.assertTrue(
				"Version for 'Property' failed to increment on flush directive",
				(currentVersion != null && property.getVersion() > currentVersion)
						|| !modified);
	}

	@Test
	public void testFindPropertyEntries() {
		entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		this.entityService.saveEntity(entity);

		property = CommonCreateMethodsForTesting.createProperty("property",
				null, PropertyType._BINARY, entity);
		propertyService.saveProperty(property);

		long count = Property.countPropertys();
		if (count > 20)
			count = 20;
		int firstResult = 0;
		int maxResults = (int) count;
		List<Property> result = propertyService.findPropertyEntries(
				firstResult, maxResults);
		Assert.assertNotNull(
				"Find entries method for 'Property' illegally returned null",
				result);
		Assert.assertEquals(
				"Find entries method for 'Property' returned an incorrect number of entries",
				count, result.size());
	}

	@Test
	public void testMergeUpdate() {
		entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		this.entityService.saveEntity(entity);

		property = CommonCreateMethodsForTesting.createProperty("property",
				null, PropertyType._BINARY, entity);
		propertyService.saveProperty(property);

		Long id = property.getId();
		Assert.assertNotNull(
				"Data on demand for 'Property' failed to provide an identifier",
				id);
		property = propertyService.findProperty(id);
		boolean modified = dod.modifyProperty(property);
		Integer currentVersion = property.getVersion();
		Property merged = property.merge();
		property.flush();
		Assert.assertEquals(
				"Identifier of merged object not the same as identifier of original object",
				merged.getId(), id);
		Assert.assertTrue(
				"Version for 'Property' failed to increment on merge and flush directive",
				(currentVersion != null && property.getVersion() > currentVersion)
						|| !modified);
	}

	@Test
	public void testSaveProperty() {
		entity = CommonCreateMethodsForTesting.createEntity("EntityName",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("PropertyName",
				null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property);
		property.flush();
		Assert.assertNotNull(
				"Expected 'Property' identifier to no longer be null",
				property.getId());
	}

	@Test
	public void testDeleteProperty() {
		entity = CommonCreateMethodsForTesting.createEntity("EntityName",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("PropertyName",
				null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property);
		long id = property.getId();
		propertyService.deleteProperty(property);
		property.flush();
		propertyService.findProperty(id);
	}

	@Test
	public void testUpdatePropertyUpdate() {
		entity = CommonCreateMethodsForTesting.createEntity("EntityName",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("PropertyName",
				null, PropertyType.TEXT, entity);
		this.propertyService.saveProperty(property);
		Assert.assertNotNull(
				"Data on demand for 'Property' failed to initialize correctly",
				property);
		Long id = property.getId();
		Assert.assertNotNull(
				"Data on demand for 'Property' failed to provide an identifier",
				id);
		property = propertyService.findProperty(id);
		boolean modified = dod.modifyProperty(property);
		Integer currentVersion = property.getVersion();
		Property merged = propertyService.updateProperty(property);
		property.flush();
		Assert.assertEquals(
				"Identifier of merged object not the same as identifier of original object",
				merged.getId(), id);
		Assert.assertTrue(
				"Version for 'Property' failed to increment on merge and flush directive",
				(currentVersion != null && property.getVersion() > currentVersion)
						|| !modified);
	}

	/* CREATE PROPERTY */

	@Test
	public void validEntityPropertyTypeAndName() {
		entity = CommonCreateMethodsForTesting.createEntity("EntityName",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("PropertyName",
				null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property);
		Assert.assertEquals(propertyService.findProperty(property.getId()),
				property);
	}

	@Test
	public void twoPropertiesWithSameNameInDiferentEntities() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("SameName",
				null, PropertyType.TEXT, entity);
		propertyService.saveProperty(property);
		Entity entity_2 = CommonCreateMethodsForTesting.createEntity(
				"Entity_2", "EntityNamespace");
		entity_2.persist();
		Property property_2 = CommonCreateMethodsForTesting.createProperty(
				"SameName", null, PropertyType.TEXT, entity_2);
		propertyService.saveProperty(property_2);

		Assert.assertEquals(propertyService.findProperty(property_2.getId()),
				property_2);
		Assert.assertEquals(entity_2, property_2.getEntity());
	}

	@Test
	public void propertyNameWithSpaces() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
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
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty(
				"Name with spaces", "{\"mandatory\":true}", PropertyType.TEXT,
				entity);
		propertyService.saveProperty(property);

	}

	@Test
	public void configurePropertyWithDefaultValue() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty(
				"Name with spaces", "{\"default\":\"abc\"}", PropertyType.TEXT,
				entity);

		propertyService.saveProperty(property);
	}

	@Test(expected = ValidationException.class)
	public void propertyWithoutName() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
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
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
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
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
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
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("@#$%", null,
				PropertyType.TEXT, entity);
		propertyService.saveProperty(property);
	}

	@Test(expected = ValidationException.class)
	public void caseInsensitiveNames() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
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
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("SameName",
				"{\"regex\":\"a-zA-Z\"}", PropertyType.TEXT, entity);
		propertyService.saveProperty(property);

	}

	@Test
	public void textPropertyConfigurationMaxValidation() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("SameName",
				"{\"maxsize\":100}", PropertyType.TEXT, entity);
		propertyService.saveProperty(property);
	}

	@Test
	public void textPropertyConfigurationMinValidation() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("SameName",
				"{\"minsize\":1}", PropertyType.TEXT, entity);
		propertyService.saveProperty(property);
	}

	@Test(expected = ValidationException.class)
	public void forceAwrongConfigurationInRegexValue() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
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
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
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
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
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
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
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
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
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
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
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
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
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
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
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
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
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
		List<Property> properties = propertyService.findAllPropertys();
		Assert.assertEquals(properties.size(), 0);
	}

	@Test
	public void getPropertyByID() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
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
		Assert.assertNull(propertyService.findProperty((long) 10));
	}

	@Test(expected = EntityNotFoundException.class)
	public void listPropertiesOfAnUnknownEntity() {
		Property.findPropertysByEntity(entity);
	}

	/* DELETE PROPERTIES */

	@Test(expected = PropertyNotFoundException.class)
	public void deletePropretyWithAnUnknowId() {
		entity = CommonCreateMethodsForTesting.createEntity("Entity_1",
				"EntityNamespace");
		this.entityService.saveEntity(entity);
		property = CommonCreateMethodsForTesting.createProperty("Property 1",
				null, PropertyType.TEXT, entity);
		propertyService.deleteProperty(property);
	}

}
