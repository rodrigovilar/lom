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

		this.classService = serviceFactory.createClassService();
		this.attributeService = serviceFactory.createAttributeService();
		this.instanceService = serviceFactory.createInstanceService();
	}

	@Test
	public void unknownClass() {
		InstanceHelper.expectExceptionOnCreateInvalidInstance(instanceService,
				"a", "Unknown class: a",
				InstanceHelper.attributeValue("age", 30));

		InstanceHelper.expectExceptionOnCreateInvalidInstance(instanceService,
				"abc.a", "Unknown class: abc.a",
				InstanceHelper.attributeValue("age", 30));
	}

	@Test
	public void nullClass() {
		InstanceHelper.expectExceptionOnCreateInvalidInstance(instanceService,
				null,
				"Invalid value for Instance class: The class is mandatory",
				InstanceHelper.attributeValue("age", 30));
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
				InstanceHelper.attributeValue("age", 30));

		InstanceHelper.expectExceptionOnCreateInvalidInstance(instanceService,
				"system.Client", "Unknown attribute for system.Client: height",
				InstanceHelper.attributeValue("height", 1.85));

		InstanceHelper.expectExceptionOnCreateInvalidInstance(instanceService,
				"system.Client", "Unknown attribute for system.Client: name",
				InstanceHelper.attributeValue("name", "John"));

		InstanceHelper.expectExceptionOnCreateInvalidInstance(instanceService,
				"system.Client", "Unknown attribute for system.Client: active",
				InstanceHelper.attributeValue("active", true));
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
		AttributeValue value1 = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.a", "Jose");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.a", value1);

		ClassHelper.createClass(this.classService, "abc", "b");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.b",
				null, "name", AttributeType.TEXT,
				"{\"mandatory\": true, \"default\": \"Michael\"}");
		AttributeValue value2 = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.b", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.b", value2);

		ClassHelper.createClass(classService, "abc", "c");
		AttributeHelper
				.createOneAttribute(this.attributeService, "abc.c", null,
						"name", AttributeType.TEXT,
						"{\"mandatory\": true, \"default\": \"Johson\", \"minlength\": 6}");
		AttributeValue value3 = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.c", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.c", value3);

		ClassHelper.createClass(classService, "abc", "d");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.d",
				null, "name", AttributeType.TEXT,
				"{\"mandatory\": true, \"minlength\": 6}");
		AttributeValue value4 = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.d", "Johson");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.d", value4);

		ClassHelper.createClass(classService, "abc", "e");
		AttributeHelper
				.createOneAttribute(this.attributeService, "abc.e", null,
						"name", AttributeType.TEXT,
						"{\"mandatory\": true, \"default\": \"Johson\", \"maxlength\" : 6}");
		AttributeValue value5 = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.e", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.e", value5);

		ClassHelper.createClass(classService, "abc", "f");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.f",
				null, "name", AttributeType.TEXT,
				"{\"mandatory\": true, \"maxlength\" : 6}");
		AttributeValue value6 = InstanceHelper.newAttributeValue(
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
		AttributeValue value7 = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.g", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.g", value7);

		ClassHelper.createClass(classService, "abc", "i");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.i",
				null, "name", AttributeType.TEXT,
				"{\"mandatory\": true, \"minlength\": 6, \"maxlength\" : 6}");
		AttributeValue value8 = InstanceHelper.newAttributeValue(
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
		AttributeValue value9 = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.j", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.j", value9);

		ClassHelper.createClass(classService, "abc", "k");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.k",
				null, "name", AttributeType.TEXT,
				"{\"mandatory\": true, \"minlength\": 3, \"maxlength\" : 8}");
		AttributeValue value10 = InstanceHelper.newAttributeValue(
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
		AttributeValue value11 = InstanceHelper.newAttributeValue(
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
		AttributeValue value12 = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.m", "abc@abc.com");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.m", value12);
	}

	@Test
	public void instanceWithValidValuesForTheConfigurationOfAttributesLongText() {
		ClassHelper.createClass(this.classService, "abc", "a");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "name", AttributeType.LONGTEXT, "{\"mandatory\": true}");
		AttributeValue value1 = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.a", "Jose");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.a", value1);

		ClassHelper.createClass(this.classService, "abc", "b");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.b",
				null, "name", AttributeType.LONGTEXT,
				"{\"default\" : \"default longtext\"}");
		AttributeValue value2 = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.b", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.b", value2);

		ClassHelper.createClass(this.classService, "abc", "c");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.c",
				null, "name", AttributeType.LONGTEXT,
				"{\"default\" : \"default longtext\"}");
		AttributeValue value3 = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.c", "Jose");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.c", value3);

		ClassHelper.createClass(this.classService, "abc", "d");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.d",
				null, "name", AttributeType.LONGTEXT, "{\"minlength\" : 6}");
		AttributeValue value4 = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.d", "Johson");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.d", value4);

		ClassHelper.createClass(this.classService, "abc", "e");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.e",
				null, "name", AttributeType.LONGTEXT,
				"{\"mandatory\" : true, \"minlength\" : 6}");
		AttributeValue value5 = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.e", "Johson");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.e", value5);

		ClassHelper.createClass(this.classService, "abc", "f");
		AttributeHelper
				.createOneAttribute(this.attributeService, "abc.f", null,
						"name", AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"default\":\"Johson\", \"minlength\" : 6}");
		AttributeValue value6 = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.f", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.f", value6);

		ClassHelper.createClass(this.classService, "abc", "g");
		AttributeHelper
				.createOneAttribute(this.attributeService, "abc.g", null,
						"name", AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"default\":\"Johson\", \"minlength\" : 6}");
		AttributeValue value7 = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.g", "abccab");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.g", value7);

		ClassHelper.createClass(this.classService, "abc", "h");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.h",
				null, "name", AttributeType.LONGTEXT, "{\"maxlength\" : 6}");
		AttributeValue value8 = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.h", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.h", value8);

		ClassHelper.createClass(this.classService, "abc", "i");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.i",
				null, "name", AttributeType.LONGTEXT,
				"{\"mandatory\" : true, \"maxlength\" : 6}");
		AttributeValue value9 = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.i", "Johson");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.i", value9);

		ClassHelper.createClass(this.classService, "abc", "j");
		AttributeHelper
				.createOneAttribute(this.attributeService, "abc.j", null,
						"name", AttributeType.LONGTEXT,
						"{\"mandatory\" : true, \"default\" : \"Johson\", \"maxlength\" : 6}");
		AttributeValue value10 = InstanceHelper.newAttributeValue(
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
		AttributeValue value11 = InstanceHelper.newAttributeValue(
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
		AttributeValue value12 = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.l", "abccba");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.l", value12);
	}

	public void instanceWithValidValuesForTheConfigurationOfAttributesInteger() {
		ClassHelper.createClass(this.classService, "abc", "a");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "number", AttributeType.INTEGER, "{\"default\": 1}");
		AttributeValue value1 = InstanceHelper.newAttributeValue(
				this.attributeService, "number", "abc.a", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.a", value1);

		ClassHelper.createClass(this.classService, "abc", "b");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.b",
				null, "number", AttributeType.INTEGER, "{\"minvalue\": 1}");
		AttributeValue value2 = InstanceHelper.newAttributeValue(
				this.attributeService, "number", "abc.b", 1);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.b", value2);

		ClassHelper.createClass(this.classService, "abc", "c");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.c",
				null, "number", AttributeType.INTEGER, "{\"maxvalue\": 1}");
		AttributeValue value3 = InstanceHelper.newAttributeValue(
				this.attributeService, "number", "abc.c", 1);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.c", value3);

		ClassHelper.createClass(this.classService, "abc", "d");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.d",
				null, "number", AttributeType.INTEGER,
				"{\"minvalue\": 1, \"maxvalue\": 1}");
		AttributeValue value4 = InstanceHelper.newAttributeValue(
				this.attributeService, "number", "abc.d", 1);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.d", value4);

		ClassHelper.createClass(this.classService, "abc", "e");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.e",
				null, "number", AttributeType.INTEGER,
				"{\"default\": 1, \"minvalue\": 1, \"maxvalue\": 1}");
		AttributeValue value5 = InstanceHelper.newAttributeValue(
				this.attributeService, "number", "abc.e", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.e", value5);

		ClassHelper.createClass(this.classService, "abc", "f");
		AttributeHelper
				.createOneAttribute(this.attributeService, "abc.f", null,
						"number", AttributeType.INTEGER,
						"{\"mandatory\": true,\"default\": 1, \"minvalue\": 1, \"maxvalue\": 1}");
		AttributeValue value6 = InstanceHelper.newAttributeValue(
				this.attributeService, "number", "abc.f", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.f", value6);
	}

	public void instanceWithValidValuesForTheConfigurationOfAttributesPassword() {
		ClassHelper.createClass(this.classService, "abc", "a");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "secretKey", AttributeType.PASSWORD,
				"{\"default\": \"password\"}");
		AttributeValue value1 = InstanceHelper.newAttributeValue(
				this.attributeService, "secretKey", "abc.a", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.a", value1);

		ClassHelper.createClass(this.classService, "abc", "b");
		AttributeHelper
				.createOneAttribute(this.attributeService, "abc.b", null,
						"secretKey", AttributeType.PASSWORD,
						"{\"minUppers\": 2}");
		AttributeValue value2 = InstanceHelper.newAttributeValue(
				this.attributeService, "secretKey", "abc.b", "SecretKey");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.b", value2);

		ClassHelper.createClass(this.classService, "abc", "c");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.c",
				null, "secretKey", AttributeType.PASSWORD,
				"{\"minNumbers\": 2}");
		AttributeValue value3 = InstanceHelper.newAttributeValue(
				this.attributeService, "secretKey", "abc.c", "1secretkey2");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.c", value3);

		ClassHelper.createClass(this.classService, "abc", "d");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.d",
				null, "secretKey", AttributeType.PASSWORD,
				"{\"minSymbols\": 2}");
		AttributeValue value4 = InstanceHelper.newAttributeValue(
				this.attributeService, "secretKey", "abc.d", "&secretkey%");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.d", value4);

		ClassHelper.createClass(this.classService, "abc", "e");
		AttributeHelper
				.createOneAttribute(this.attributeService, "abc.e", null,
						"secretKey", AttributeType.PASSWORD,
						"{\"maxRepeat\": 2}");
		AttributeValue value5 = InstanceHelper.newAttributeValue(
				this.attributeService, "secretKey", "abc.e", "seecretkey");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.e", value5);

		ClassHelper.createClass(this.classService, "abc", "f");
		AttributeHelper
				.createOneAttribute(this.attributeService, "abc.f", null,
						"secretKey", AttributeType.PASSWORD,
						"{\"minlength\": 6}");
		AttributeValue value6 = InstanceHelper.newAttributeValue(
				this.attributeService, "secretKey", "abc.f", "secret");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.f", value6);

		ClassHelper.createClass(this.classService, "abc", "g");
		AttributeHelper
				.createOneAttribute(this.attributeService, "abc.g", null,
						"secretKey", AttributeType.PASSWORD,
						"{\"maxlength\": 6}");
		AttributeValue value7 = InstanceHelper.newAttributeValue(
				this.attributeService, "secretKey", "abc.g", "secret");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.g", value7);

		ClassHelper.createClass(this.classService, "abc", "h");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.h",
				null, "secretKey", AttributeType.PASSWORD,
				"{\"mandatory\": true}");
		AttributeValue value8 = InstanceHelper.newAttributeValue(
				this.attributeService, "secretKey", "abc.h", "secret");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.h", value8);

		ClassHelper.createClass(this.classService, "abc", "i");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.i",
				null, "secretKey", AttributeType.PASSWORD,
				"{\"mandatory\": false}");
		AttributeValue value9 = InstanceHelper.newAttributeValue(
				this.attributeService, "secretKey", "abc.i", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.h", value9);

		ClassHelper.createClass(this.classService, "abc", "j");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.j",
				null, "secretKey", AttributeType.PASSWORD,
				"{\"mandatory\": true, \"default\": \"mypassword\"}");
		AttributeValue value10 = InstanceHelper.newAttributeValue(
				this.attributeService, "secretKey", "abc.j", null);
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.j", value10);

		ClassHelper.createClass(this.classService, "abc", "k");
		AttributeHelper
				.createOneAttribute(
						this.attributeService,
						"abc.k",
						null,
						"secretKey",
						AttributeType.PASSWORD,
						"{\"mandatory\": true, \"default\": \"P@ssW04\", \"minlength\": 6, "
								+ "\"maxlength\": 9, \"minUppers\": 2, \"minNumbers\": 2, "
								+ "\"minSymbols\": 2, \"maxRepeat\": 2}");
		AttributeValue value11 = InstanceHelper.newAttributeValue(
				this.attributeService, "secretKey", "abc.k", "0tH3r@ss");
		InstanceHelper.createAndVerifyOneInstance(this.instanceService,
				"abc.k", value11);
	}

	@Test
	public void instanceWithInvalidValuesForTextTypeAttributeWithoutConfiguration() {
		String messageException = "Invalid value for the Instance. The 'name' attribute can only get values ​​of type TEXT";

		ClassHelper.createClass(this.classService, "abc", "a");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "name", AttributeType.TEXT, "");

		AttributeValue value = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.a", false);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", true);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", 1);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", 0);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", -1);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", 2.1);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", 0.1);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", -2.1);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", 12L);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);
	}

	@Test
	public void instanceWithInvalidValuesForLongTextTypeAttributeWithoutConfiguration() {
		String messageException = "Invalid value for the Instance. The 'name' attribute can only get values ​​of type LONGTEXT";

		ClassHelper.createClass(this.classService, "abc", "a");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "name", AttributeType.LONGTEXT, "");

		AttributeValue value = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.a", false);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", true);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", 1);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", 0);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", -1);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", 2.1);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", 0.1);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", -2.1);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", 12L);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);
	}

	@Test
	public void instanceWithInvalidValuesForIntegerTypeAttributeWithoutConfiguration() {
		String messageException = "Invalid value for the Instance. The 'name' attribute can only get values ​​of type INTEGER";

		ClassHelper.createClass(this.classService, "abc", "a");
		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "name", AttributeType.INTEGER, "");

		AttributeValue value = InstanceHelper.newAttributeValue(
				this.attributeService, "name", "abc.a", false);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", true);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", "pa");
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", 3.2);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", 0.75);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", -3.2);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);

		value = InstanceHelper.newAttributeValue(this.attributeService, "name",
				"abc.a", 5L);
		InstanceHelper.expectExceptionOnCreateInvalidInstance(
				this.instanceService, "abc.a", messageException, value);
	}
}