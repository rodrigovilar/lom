package com.nanuvem.lom.kernel;

import org.junit.Before;
import org.junit.Test;

import com.nanuvem.lom.kernel.dao.memory.MemoryDaoFactory;

public class InstanceServiceTest {

	private ClassServiceImpl classService;
	private AttributeServiceImpl attributeService;
	private InstanceServiceImpl instanceService;

	@Before
	public void init() {
		MemoryDaoFactory daoFactory = new MemoryDaoFactory();
		ServiceFactory serviceFactory = new ServiceFactory(daoFactory);
		
		this.classService = serviceFactory.getClassService();
		this.attributeService = serviceFactory.getAttributeService();
		this.instanceService = serviceFactory.getInstanceService();
	}

	@Test
	public void unknownClass() {
		InstanceHelper.expectExceptionOnCreateInvalidInstance(instanceService,
				"a", "Unknown class: default.a", attributeValue("age", 30));

		InstanceHelper.expectExceptionOnCreateInvalidInstance(instanceService,
				"abc.a", "Unknown class: abc.a", attributeValue("age", 30));
	}

	@Test
	public void nullClass() {
		InstanceHelper.expectExceptionOnCreateInvalidInstance(instanceService,
				null,
				"Invalid value for Instance class: The class is mandatory",
				attributeValue("age", 30));
	}

	@Test
	public void classWithoutAttributes() {
		ClassHelper.createClass(classService, "abc", "a");
		InstanceHelper.createAndVerifyOneInstance(instanceService, "abc.a");

		ClassHelper.createClass(classService, "abc", "b");
		InstanceHelper.createAndVerifyOneInstance(instanceService, "abc.b");

		ClassHelper.createClass(classService, "", "a");
		InstanceHelper.createAndVerifyOneInstance(instanceService, "a");

		ClassHelper.createClass(classService, "", "b");
		InstanceHelper.createAndVerifyOneInstance(instanceService, "b");
	}

	@Test
	public void classWithoutAttributesUnknownAttributes() {
		ClassHelper.createClass(classService, "system", "Client");

		InstanceHelper.expectExceptionOnCreateInvalidInstance(instanceService,
				"system.Client", "Unknown attribute for system.Client: age",
				attributeValue("age", 30));

		InstanceHelper.expectExceptionOnCreateInvalidInstance(instanceService,
				"system.Client", "Unknown attribute for system.Client: height",
				attributeValue("height", 1.85));

		InstanceHelper.expectExceptionOnCreateInvalidInstance(instanceService,
				"system.Client", "Unknown attribute for system.Client: name",
				attributeValue("name", "John"));

		InstanceHelper.expectExceptionOnCreateInvalidInstance(instanceService,
				"system.Client", "Unknown attribute for system.Client: active",
				attributeValue("active", true));
	}

	@Test
	public void classWithKnownAttributesAndWithoutConfiguration() {
		ClassHelper.createClass(classService, "system", "Client");
		AttributeHelper.createOneAttribute(this.attributeService,
				"system.Client", null, "pa", AttributeType.TEXT, null);

		ClassHelper.createClass(classService, "system", "User");
		AttributeHelper.createOneAttribute(this.attributeService,
				"system.User", null, "pa", AttributeType.TEXT, null);
		AttributeHelper.createOneAttribute(this.attributeService,
				"system.User", null, "pb", AttributeType.LONGTEXT, null);

		ClassHelper.createClass(classService, "system", "Organization");
		AttributeHelper.createOneAttribute(this.attributeService,
				"system.Organization", null, "pa", AttributeType.TEXT, null);
		AttributeHelper
				.createOneAttribute(this.attributeService,
						"system.Organization", null, "pb",
						AttributeType.LONGTEXT, null);
		AttributeHelper.createOneAttribute(this.attributeService,
				"system.Organization", null, "pc", AttributeType.INTEGER, null);

		ClassHelper.createClass(classService, "system", "Category");
		AttributeHelper.createOneAttribute(this.attributeService,
				"system.Category", null, "pa", AttributeType.TEXT, null);
		AttributeHelper.createOneAttribute(this.attributeService,
				"system.Category", null, "pb", AttributeType.LONGTEXT, null);
		AttributeHelper.createOneAttribute(this.attributeService,
				"system.Category", null, "pc", AttributeType.INTEGER, null);
		AttributeHelper.createOneAttribute(this.attributeService,
				"system.Category", null, "pd", AttributeType.PASSWORD, null);

		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"system.Client");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"system.User");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"system.Organization");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"system.Category");
	}

	private static AttributeValue attributeValue(String attributeName,
			Object objValue) {
		Attribute attribute = new Attribute();
		attribute.setName(attributeName);
		AttributeValue value = new AttributeValue();
		value.setValue(objValue);
		value.setAttribute(attribute);
		return value;
	}

}