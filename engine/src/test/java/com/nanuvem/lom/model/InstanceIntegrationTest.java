package com.nanuvem.lom.model;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

import com.nanuvem.lom.model.CommonCreateMethodsForTesting;
import com.nanuvem.lom.service.EntityServiceImpl;
import com.nanuvem.lom.service.InstanceNotFoundException;
import com.nanuvem.lom.service.InstanceService;
import com.nanuvem.lom.service.PropertyServiceImpl;

@RooIntegrationTest(entity = Instance.class)
public class InstanceIntegrationTest {

	private Entity entity;
	private Property property;
	private Instance instance;

	@Autowired
	private EntityServiceImpl entityService;

	@Autowired
	private PropertyServiceImpl propertyService;

	@Autowired
	InstanceService instanceService;

	@Before
	public void init() {
		entity = new Entity();
		property = new Property();
		instance = new Instance();
	}

	@Test
	public void testMarkerMethod() {
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testDeleteInstance() {
		this.entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		this.instance = CommonCreateMethodsForTesting.createInstance(entity);
		entityService.saveEntity(entity);
		instanceService.saveInstance(instance);
		Long id = instance.getId();
		Assert.assertNotNull(
				"Data on demand for 'Instance' failed to provide an identifier",
				id);
		instanceService.deleteInstance(instance);
		instance.flush();
		instanceService.findInstance(id);
	}

	@Test
	public void entityWithoutProperties() throws Exception {
		this.entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		this.instance = CommonCreateMethodsForTesting.createInstance(entity);
		entityService.saveEntity(entity);
		instanceService.saveInstance(instance);
		Assert.assertEquals(instance,
				instanceService.findInstance(instance.getId()));
	}

	@Test
	public void entityWithNonMandatoryProperties() throws Exception {
		this.entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		entityService.saveEntity(entity);

		this.property = CommonCreateMethodsForTesting.createProperty(
				"property", "{\"mandatory\":false}", PropertyType.TEXT, entity);
		propertyService.saveProperty(property);

		Property property2 = CommonCreateMethodsForTesting.createProperty(
				"property2", "{}", PropertyType.TEXT, entity);
		propertyService.saveProperty(property2);

		this.instance = CommonCreateMethodsForTesting.createInstance(entity);
		instanceService.saveInstance(instance);

		Assert.assertEquals(instance,
				instanceService.findInstance(instance.getId()));
	}

	@Test
	public void entityWithMandatoryPropertiesWithDefaultValue() {
		// TODO default value configuration in Property

	}

	// TODO
	@Test(expected = Exception.class)
	public void validEntityWithMandatoryPropertiesWithoutDefaultValue()
			throws Exception {
		throw new Exception("");
	}

	@Test
	public void listAllInstancesOfAnEntity() {
		this.entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		entityService.saveEntity(entity);

		this.property = CommonCreateMethodsForTesting.createProperty(
				"property", "{\"mandatory\":false}", PropertyType.TEXT, entity);
		propertyService.saveProperty(property);

		this.instance = CommonCreateMethodsForTesting.createInstance(entity);
		instanceService.saveInstance(instance);

		List<Instance> allInstancesByEntity = instanceService
				.findInstancesByEntity(entity);

		Assert.assertTrue(allInstancesByEntity.contains(instance));
		Assert.assertEquals(1, allInstancesByEntity.size());
	}

	@Test
	public void listAllInstancesWhenThereIsNoInstances() {
		List<Instance> allInstances = instanceService.findAllInstances();
		Assert.assertEquals(0, allInstances.size());
	}

	@Test
	public void getInstanceById() throws Exception {
		this.entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		entityService.saveEntity(entity);
		this.instance = CommonCreateMethodsForTesting.createInstance(entity);
		instanceService.saveInstance(instance);

		Assert.assertEquals(instance,
				instanceService.findInstance(instance.getId()));

		Instance instance2 = CommonCreateMethodsForTesting
				.createInstance(entity);
		instanceService.saveInstance(instance2);

		Assert.assertEquals(instance2,
				instanceService.findInstance(instance2.getId()));
	}

	@Test(expected = InstanceNotFoundException.class)
	public void getInstanceWithAnUnknownId() throws Exception {
		instanceService.findInstance((long) 10);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void listInstancesIfAnUnknownEntity() {
		Entity entityWithUnknowId = CommonCreateMethodsForTesting.createEntity(
				"entity", "namespace");
		entityService.saveEntity(entityWithUnknowId);
		instanceService.findInstancesByEntity(entityWithUnknowId);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void deleteInstanceWithAnUnknownId() {
		Instance unknownInstance = new Instance();
		instanceService.deleteInstance(unknownInstance);
	}

}
