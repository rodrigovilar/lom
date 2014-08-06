package com.nanuvem.lom.kernel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;

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

	@Test
	public void instanceWithInvalidValuesForTheConfigurationOfAttributesText() {
		ClassHelper.createClass(this.classService, "abc", "a");

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameA", AttributeType.TEXT, "{\"mandatory\" : true}");
		AttributeValue valueA = InstanceHelper.newAttributeValue(
				this.attributeService, "nameA", "abc.a", null);
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for the 'nameA' attribute is mandatory",
						valueA);

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameB", AttributeType.TEXT, "{\"minlength\" : 5}");
		AttributeValue valueB = InstanceHelper.newAttributeValue(
				this.attributeService, "nameB", "abc.a", "abcd");
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for 'nameB' must have a minimum length of 5 characters",
						valueB);

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameBA", AttributeType.TEXT,
				"{\"mandatory\" : true, \"minlength\" : 5}");
		AttributeValue valueBA = InstanceHelper.newAttributeValue(
				this.attributeService, "nameBA", "abc.a", "");
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for the 'nameBA' attribute is mandatory, "
								+ "the value for 'nameBA' must have a minimum length of 5 characters",
						valueBA);

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameC", AttributeType.TEXT, "{\"maxlength\" : 5}");
		AttributeValue valueC = InstanceHelper.newAttributeValue(
				this.attributeService, "nameC", "abc.a", "abcdef");
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for 'nameC' must have a maximum length of 5 characters",
						valueC);

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameCA", AttributeType.TEXT, "{\"maxlength\" : 5}");
		AttributeValue valueCA = InstanceHelper.newAttributeValue(
				this.attributeService, "nameCA", "abc.a", "abcdef");
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for the 'nameCA' attribute is mandatory, "
								+ "the value for 'nameCA' must have a maximum length of 5 characters",
						valueCA);

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameD", AttributeType.TEXT,
				"{\"minlength\" : 5, \"maxlength\" : 5}");
		AttributeValue valueD = InstanceHelper.newAttributeValue(
				this.attributeService, "nameD", "abc.a", "abcdef");
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for 'nameD' should have maximum length and minimum length of 5 characters",
						valueD);

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameDA", AttributeType.TEXT,
				"{\"mandatory\" : true, \"minlength\" : 5, \"maxlength\" : 5}");
		AttributeValue valueDA = InstanceHelper.newAttributeValue(
				this.attributeService, "nameDA", "abc.a", "");
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for the 'nameDA' attribute is mandatory, "
								+ "the value for 'nameDA' should have maximum length and minimum length of 5 characters",
						valueDA);

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameE", AttributeType.TEXT,
				"{\"regex\" :\"\\d\\d\\d([,\\s])?\\d\\d\\d\\d\"}");
		AttributeValue valueE = InstanceHelper.newAttributeValue(
				this.attributeService, "nameE", "abc.a", "12345678");
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value of the 'nameE' attribute does not meet the defined regular expression",
						valueE);

		AttributeHelper
				.createOneAttribute(
						this.attributeService,
						"abc.a",
						null,
						"nameF",
						AttributeType.TEXT,
						"{\"mandatory\" : true, \"minlength\" : 4, \"maxlength\" : 4, \"regex\" : \"\\d\\d\\d\\s\\d\\d\\d\\s\\d\\d\\d\\s\\d\\d\"}");
		AttributeValue valueF = InstanceHelper.newAttributeValue(
				this.attributeService, "nameF", "abc.a", "123 456 789");
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for 'nameF' should have maximum length and minimum length of 4 characters, "
								+ "the value of the 'nameF' attribute does not meet the defined regular expression",
						valueF);

	}

	@Test
	public void instanceWithInvalidValuesForTheConfigurationOfAttributesLongText() {
		ClassHelper.createClass(this.classService, "abc", "a");

		AttributeHelper
				.createOneAttribute(this.attributeService, "abc.a", null,
						"nameA", AttributeType.LONGTEXT,
						"{\"mandatory\" : true}");
		AttributeValue valueA = InstanceHelper.newAttributeValue(
				this.attributeService, "nameA", "abc.a", null);
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for the 'nameA' attribute is mandatory",
						valueA);

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameB", AttributeType.LONGTEXT, "{\"minlength\" : 5}");
		AttributeValue valueB = InstanceHelper.newAttributeValue(
				this.attributeService, "nameB", "abc.a", "abcd");
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for 'nameB' must have a minimum length of 5 characters",
						valueB);

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameBA", AttributeType.LONGTEXT,
				"{\"mandatory\" : true, \"minlength\" : 5}");
		AttributeValue valueBA = InstanceHelper.newAttributeValue(
				this.attributeService, "nameBA", "abc.a", "");
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for the 'nameBA' attribute is mandatory, "
								+ "the value for 'nameBA' must have a minimum length of 5 characters",
						valueBA);

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameC", AttributeType.LONGTEXT, "{\"maxlength\" : 5}");
		AttributeValue valueC = InstanceHelper.newAttributeValue(
				this.attributeService, "nameC", "abc.a", "abcdef");
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for 'nameC' must have a maximum length of 5 characters",
						valueC);

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameCA", AttributeType.LONGTEXT, "{\"maxlength\" : 5}");
		AttributeValue valueCA = InstanceHelper.newAttributeValue(
				this.attributeService, "nameCA", "abc.a", "abcdef");
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for the 'nameCA' attribute is mandatory, "
								+ "the value for 'nameCA' must have a maximum length of 5 characters",
						valueCA);

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameD", AttributeType.LONGTEXT,
				"{\"minlength\" : 5, \"maxlength\" : 5}");
		AttributeValue valueD = InstanceHelper.newAttributeValue(
				this.attributeService, "nameD", "abc.a", "abcdef");
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for 'nameD' should have maximum length and minimum length of 5 characters",
						valueD);

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameE", AttributeType.LONGTEXT,
				"{\"mandatory\" : true, \"minlength\" : 5, \"maxlength\" : 5}");
		AttributeValue valueE = InstanceHelper.newAttributeValue(
				this.attributeService, "nameE", "abc.a", "");
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for the 'nameE' attribute is mandatory, "
								+ "the value for 'nameE' should have maximum length and minimum length of 5 characters",
						valueE);
	}

	@Test
	public void instanceWithInvalidValuesForTheConfigurationOfAttributesInteger() {
		ClassHelper.createClass(this.classService, "abc", "a");

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameA", AttributeType.INTEGER, "{\"mandatory\" : true}");
		AttributeValue valueA = InstanceHelper.newAttributeValue(
				this.attributeService, "nameA", "abc.a", null);
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for the 'nameA' attribute is mandatory",
						valueA);

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameB", AttributeType.INTEGER, "{\"minvalue\" : 3}");
		AttributeValue valueB = InstanceHelper.newAttributeValue(
				this.attributeService, "nameB", "abc.a", 2);
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for 'nomeB' must be greater than or equal to 3",
						valueB);

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameC", AttributeType.INTEGER,
				"{\"mandatory\" : true, \"minvalue\" : 3}");
		AttributeValue valueC = InstanceHelper.newAttributeValue(
				this.attributeService, "nameC", "abc.a", null);
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for the 'nameC' attribute is mandatory, "
								+ "the value for 'nomeC' must be greater than or equal to 3",
						valueC);

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameD", AttributeType.INTEGER, "{\"maxvalue\" : 3}");
		AttributeValue valueD = InstanceHelper.newAttributeValue(
				this.attributeService, "nameD", "abc.a", 4);
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for 'nomeD' must be smaller or equal to 3",
						valueD);

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameE", AttributeType.INTEGER,
				"{\"mandatory\" : true, \"maxvalue\" : 3}");
		AttributeValue valueE = InstanceHelper.newAttributeValue(
				this.attributeService, "nameC", "abc.a", null);
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for the 'nameC' attribute is mandatory, "
								+ "the value for 'nomeE' must be smaller or equal to 3",
						valueE);

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameF", AttributeType.INTEGER,
				"{\"minvalue\" : 3, \"maxvalue\" : 3}");
		AttributeValue valueF = InstanceHelper.newAttributeValue(
				this.attributeService, "nameF", "abc.a", 4);
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for 'nomeF' must be smaller or equal to and greater than or equal to 3 ",
						valueF);

		AttributeHelper.createOneAttribute(this.attributeService, "abc.a",
				null, "nameG", AttributeType.INTEGER,
				"{\"mandatory\" : true, \"minvalue\" : 3, \"maxvalue\" : 3}");
		AttributeValue valueG = InstanceHelper.newAttributeValue(
				this.attributeService, "nameG", "abc.a", null);
		InstanceHelper
				.expectExceptionOnCreateInvalidInstance(
						this.instanceService,
						"abc.a",
						"Invalid value for the Instance. The value for the 'nameG' attribute is mandatory, "
								+ "the value for 'nomeG' must be smaller or equal to and greater than or equal to 3 ",
						valueG);
	}
}