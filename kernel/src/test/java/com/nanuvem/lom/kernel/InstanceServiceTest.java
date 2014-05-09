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
				"default.a", "Unknown class: a",
				attributeValue("age", 30));

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

	@Test
	public void instanceWithValidValuesForTheConfigurationOfAttributesText() {
		ClassHelper.createClass(this.classService, "abc", "a");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "name", AttributeType.TEXT, "{\"mandatory\": true}");
		AttributeValue value1 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.a", "Jose");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.a", value1);

		ClassHelper.createClass(this.classService, "abc", "b");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.b",
				null, "name", AttributeType.TEXT,
				"{\"mandatory\": true, \"default\": \"Michael\"}");
		AttributeValue value2 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.b", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.b", value2);

		ClassHelper.createClass(classService, "abc", "c");
		AttributeHelper
				.createOneAttribute(this.attributeService, "abc.c", null,
						"name", AttributeType.TEXT,
						"{\"mandatory\": true, \"default\": \"Johson\", \"minlength\": 6}");
		AttributeValue value3 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.c", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.c", value3);

		ClassHelper.createClass(classService, "abc", "d");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.d",
				null, "name", AttributeType.TEXT,
				"{\"mandatory\": true, \"minlength\": 6}");
		AttributeValue value4 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.d", "Johson");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.d", value4);

		ClassHelper.createClass(classService, "abc", "e");
		AttributeHelper
				.createOneAttribute(this.attributeService, "abc.e", null,
						"name", AttributeType.TEXT,
						"{\"mandatory\": true, \"default\": \"Johson\", \"maxlength\" : 6}");
		AttributeValue value5 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.e", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.e", value5);

		ClassHelper.createClass(classService, "abc", "f");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.f",
				null, "name", AttributeType.TEXT,
				"{\"mandatory\": true, \"maxlength\" : 6}");
		AttributeValue value6 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.f", "Johnson");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.f", value6);

		ClassHelper.createClass(classService, "abc", "g");
		AttributeHelper
				.createOneAttribute(
						this.attributeService,
						"abc.g",
						null,
						"name",
						AttributeType.TEXT,
						"{\"mandatory\": true, \"default\": \"Johson\", \"minlength\": 6, \"maxlength\" : 6}");
		AttributeValue value7 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.g", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.g", value7);

		ClassHelper.createClass(classService, "abc", "i");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.i",
				null, "name", AttributeType.TEXT,
				"{\"mandatory\": true, \"minlength\": 6, \"maxlength\" : 6}");
		AttributeValue value8 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.i", "Johson");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.i", value8);

		ClassHelper.createClass(classService, "abc", "j");
		AttributeHelper
				.createOneAttribute(
						this.attributeService,
						"abc.j",
						null,
						"name",
						AttributeType.TEXT,
						"{\"mandatory\": true, \"default\": \"Johson\", \"minlength\": 3, \"maxlength\" : 8}");
		AttributeValue value9 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.j", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.j", value9);

		ClassHelper.createClass(classService, "abc", "k");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.k",
				null, "name", AttributeType.TEXT,
				"{\"mandatory\": true, \"minlength\": 3, \"maxlength\" : 8}");
		AttributeValue value10 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.k", "Johnson");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.k", value10);

		ClassHelper.createClass(classService, "abc", "l");
		AttributeHelper
				.createOneAttribute(
						this.attributeService,
						"abc.l",
						null,
						"name",
						AttributeType.TEXT,
						"{\"mandatory\": true, \"default\": \"abc@abc.com\", \"minlength\": 3, \"maxlength\" : 15, \"regex\": \"(\\\\w[-.\\\\w]\\\\w@\\\\w[-._\\\\w]\\\\w.\\\\w{2,3})\"}");
		AttributeValue value11 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.l", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.l", value11);

		ClassHelper.createClass(classService, "abc", "m");
		AttributeHelper
				.createOneAttribute(
						this.attributeService,
						"abc.m",
						null,
						"name",
						AttributeType.TEXT,
						"{\"mandatory\": true, \"minlength\": 3, \"maxlength\" : 15, \"regex\": \"(\\\\w[-.\\\\w]\\\\w@\\\\w[-._\\\\w]\\\\w.\\\\w{2,3})\"}");
		AttributeValue value12 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.m", "abc@abc.com");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.m", value12);
	}

	@Test
	public void instanceWithValidValuesForTheConfigurationOfAttributesLongText() {
		ClassHelper.createClass(this.classService, "abc", "a");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "name", AttributeType.LONGTEXT, "{\"mandatory\": true}");
		AttributeValue value1 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.a", "Jose");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.a", value1);

		ClassHelper.createClass(this.classService, "abc", "b");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.b",
				null, "name", AttributeType.LONGTEXT,
				"{\"default\" : \"default longtext\"}");
		AttributeValue value2 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.b", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.b", value2);

		ClassHelper.createClass(this.classService, "abc", "c");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.c",
				null, "name", AttributeType.LONGTEXT,
				"{\"default\" : \"default longtext\"}");
		AttributeValue value3 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.c", "Jose");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.c", value3);

		ClassHelper.createClass(this.classService, "abc", "d");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.d",
				null, "name", AttributeType.LONGTEXT, "{\"minlength\" : 6}");
		AttributeValue value4 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.d", "Johson");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.d", value4);

		ClassHelper.createClass(this.classService, "abc", "e");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.e",
				null, "name", AttributeType.LONGTEXT,
				"{\"mandatory\" : true, \"minlength\" : 6}");
		AttributeValue value5 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.e", "Johson");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.e", value5);

		ClassHelper.createClass(this.classService, "abc", "f");
		AttributeHelper
				.createOneAttribute(this.attributeService, "abc.f", null,
						"name", AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"default\":\"Johson\", \"minlength\" : 6}");
		AttributeValue value6 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.f", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.f", value6);

		ClassHelper.createClass(this.classService, "abc", "g");
		AttributeHelper
				.createOneAttribute(this.attributeService, "abc.g", null,
						"name", AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"default\":\"Johson\", \"minlength\" : 6}");
		AttributeValue value7 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.g", "abccab");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.g", value7);

		ClassHelper.createClass(this.classService, "abc", "h");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.h",
				null, "name", AttributeType.LONGTEXT, "{\"maxlength\" : 6}");
		AttributeValue value8 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.h", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.h", value8);

		ClassHelper.createClass(this.classService, "abc", "i");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.i",
				null, "name", AttributeType.LONGTEXT,
				"{\"mandatory\" : true, \"maxlength\" : 6}");
		AttributeValue value9 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.i", "Johson");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.i", value9);

		ClassHelper.createClass(this.classService, "abc", "j");
		AttributeHelper
				.createOneAttribute(this.attributeService, "abc.j", null,
						"name", AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"default\" : \"Johson\", \"maxlength\" : 6}");
		AttributeValue value10 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.j", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.j", value10);

		ClassHelper.createClass(this.classService, "abc", "k");
		AttributeHelper
				.createOneAttribute(
						this.attributeService,
						"abc.k",
						null,
						"name",
						AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"default\" : \"Johson\", \"minlength\" : 6, \"maxlength\" : 6}");
		AttributeValue value11 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.k", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.k", value11);

		ClassHelper.createClass(this.classService, "abc", "l");
		AttributeHelper
				.createOneAttribute(
						this.attributeService,
						"abc.l",
						null,
						"name",
						AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"default\" : \"Johson\", \"minlength\" : 6, \"maxlength\" : 6}");
		AttributeValue value12 = InstanceHelper.createOneAttributeValue(
				this.attributeService, "name", "abc.l", "abccba");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.l", value12);
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