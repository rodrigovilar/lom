package com.nanuvem.lom.kernel;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.nanuvem.lom.kernel.dao.memory.MemoryDaoFactory;

public class AttributeServiceTest {

	private ClassServiceImpl classService;
	private AttributeServiceImpl attributeService;

	@Before
	public void init() {
		MemoryDaoFactory daoFactory = new MemoryDaoFactory();
		this.classService = new ClassServiceImpl(daoFactory);
		this.attributeService = new AttributeServiceImpl(daoFactory);
	}

	@Test
	public void validAttributeData() {
		ClassHelper.createClass(classService, "abc", "a");
		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pa", AttributeType.TEXT, "{\"mandatory\":true}");

		ClassHelper.createClass(classService, "abc", "b");
		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.b",
				1, "pe", AttributeType.LONGTEXT, "{\"mandatory\":false}");

		ClassHelper.createClass(classService, "abc", "c");
		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.c",
				1, "pa", AttributeType.TEXT, "");

		ClassHelper.createClass(classService, "abc", "d");
		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.d",
				1, "pa", AttributeType.TEXT, null);
	}

	@Test
	public void invalidAttributeData() {
		ClassHelper.createClass(classService, "abc", "a");
		AttributeHelper.expectExceptionOnCreateInvalidAttribute(
				attributeService, "abc.a", 0, "pa", AttributeType.TEXT,
				"{\"mandatory\":true}",
				"Invalid value for Attribute sequence: 0");

		AttributeHelper.expectExceptionOnCreateInvalidAttribute(
				attributeService, "abc.a", -1, "pa", AttributeType.TEXT,
				"{\"mandatory\":true}",
				"Invalid value for Attribute sequence: -1");

		AttributeHelper.expectExceptionOnCreateInvalidAttribute(
				attributeService, "abc.a", 2, "pa", AttributeType.TEXT,
				"{\"mandatory\":true}",
				"Invalid value for Attribute sequence: 2");

		AttributeHelper.expectExceptionOnCreateInvalidAttribute(
				attributeService, "abc.a", 1, "", AttributeType.TEXT,
				"{\"mandatory\":true}", "The name of a Attribute is mandatory");

		// It is impossible to make a test case with the AttributeType being
		// equal to an empty String. Attribute.type is defined as enum

		AttributeHelper.expectExceptionOnCreateInvalidAttribute(
				attributeService, "abc.a", 1, "pa", null,
				"{\"mandatory\":true}", "The type of a Attribute is mandatory");

		// It is impossible to make a test case with the AttributeType being
		// equal to String "ABC". Attribute.type is defined as enum

		AttributeHelper.expectExceptionOnCreateInvalidAttribute(
				attributeService, "abc.a", 1, "pa", AttributeType.TEXT, "ABC",
				"Invalid value for Attribute configuration: ABC");
	}

	@Test
	public void invalidAttributeName() {
		ClassHelper.createClass(classService, "abc", "a");

		AttributeHelper.expectExceptionOnCreateInvalidAttribute(
				attributeService, "abc.a", 1, "aaa$", AttributeType.TEXT,
				"{\"mandatory\":true}",
				"Invalid value for Attribute name: aaa$");

		AttributeHelper.expectExceptionOnCreateInvalidAttribute(
				attributeService, "abc.a", 1, "aaa#", AttributeType.TEXT,
				"{\"mandatory\":true}",
				"Invalid value for Attribute name: aaa#");

		AttributeHelper.expectExceptionOnCreateInvalidAttribute(
				attributeService, "abc.a", 1, "aaa=", AttributeType.TEXT,
				"{\"mandatory\":true}",
				"Invalid value for Attribute name: aaa=");

		AttributeHelper.expectExceptionOnCreateInvalidAttribute(
				attributeService, "abc.a", 1, "aaa'", AttributeType.TEXT,
				"{\"mandatory\":true}",
				"Invalid value for Attribute name: aaa'");

		AttributeHelper.expectExceptionOnCreateInvalidAttribute(
				attributeService, "abc.a", 1, "aaa.a", AttributeType.TEXT,
				"{\"mandatory\":true}",
				"Invalid value for Attribute name: aaa.a");

		AttributeHelper.expectExceptionOnCreateInvalidAttribute(
				attributeService, "abc.a", 1, "aaa*", AttributeType.TEXT,
				"{\"mandatory\":true}",
				"Invalid value for Attribute name: aaa*");
	}

	@Test
	public void validateSeveralAttributesInTheSameClass() {
		ClassHelper.createClass(classService, "abc", "a");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				null, "pa", AttributeType.TEXT, "");
		Assert.assertEquals(new Integer(1), this.attributeService
				.findAttributeByNameAndClassFullName("pa", "abc.a")
				.getSequence());

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				null, "pb", AttributeType.LONGTEXT, "");
		Assert.assertEquals(new Integer(2), this.attributeService
				.findAttributeByNameAndClassFullName("pb", "abc.a")
				.getSequence());

		ClassHelper.createClass(classService, "", "b");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "b",
				new Integer(1), "pa", AttributeType.LONGTEXT, "");
		AttributeHelper.createAndVerifyOneAttribute(attributeService, "b",
				new Integer(1), "pb", AttributeType.LONGTEXT, "");
		Assert.assertEquals(new Integer(2), this.attributeService
				.findAttributeByNameAndClassFullName("pa", "b").getSequence());
		Assert.assertEquals(new Integer(1), this.attributeService
				.findAttributeByNameAndClassFullName("pb", "b").getSequence());

		ClassHelper.createClass(classService, "", "c");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "c",
				new Integer(1), "pa", AttributeType.TEXT, "");
		AttributeHelper.createAndVerifyOneAttribute(attributeService, "c",
				new Integer(2), "pb", AttributeType.LONGTEXT, "");
		AttributeHelper.createAndVerifyOneAttribute(attributeService, "c",
				new Integer(2), "pc", AttributeType.LONGTEXT, "");
		Assert.assertEquals(new Integer(1), this.attributeService
				.findAttributeByNameAndClassFullName("pa", "c").getSequence());
		Assert.assertEquals(new Integer(3), this.attributeService
				.findAttributeByNameAndClassFullName("pb", "c").getSequence());
		Assert.assertEquals(new Integer(2), this.attributeService
				.findAttributeByNameAndClassFullName("pc", "c").getSequence());
	}

	@Test
	public void validateAttributeDuplicationInTheSameClass() {
		ClassHelper.createClass(classService, "abc", "a");
		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				null, "pa", AttributeType.TEXT, "");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(attributeService,
						"abc.a", null, "pa", AttributeType.LONGTEXT, "",
						"Attribute duplication on abc.a Class. It already has an attribute pa.");
		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(attributeService,
						"abc.a", null, "pa", AttributeType.TEXT, "",
						"Attribute duplication on abc.a Class. It already has an attribute pa.");
		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(attributeService,
						"abc.a", null, "pA", AttributeType.TEXT, "",
						"Attribute duplication on abc.a Class. It already has an attribute pa.");
		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(attributeService,
						"abc.a", null, "PA", AttributeType.TEXT, "",
						"Attribute duplication on abc.a Class. It already has an attribute pa.");
	}

	@Test
	public void invalidClass() {
		AttributeHelper.expectExceptionOnCreateInvalidAttribute(
				attributeService, "a", null, "abc123", AttributeType.TEXT, "",
				"Invalid Class: a");

		ClassHelper.createClass(classService, "abc", "a");

		AttributeHelper.expectExceptionOnCreateInvalidAttribute(
				attributeService, "abca", null, "abc123", AttributeType.TEXT,
				"", "Invalid Class: abca");
		AttributeHelper.expectExceptionOnCreateInvalidAttribute(
				attributeService, "a", null, "abc123", AttributeType.TEXT, "",
				"Invalid Class: a");
		AttributeHelper.expectExceptionOnCreateInvalidAttribute(
				attributeService, "abc.b", null, "abc123", AttributeType.TEXT,
				"", "Invalid Class: abc.b");

		ClassHelper.createClass(classService, "", "b");

		AttributeHelper.expectExceptionOnCreateInvalidAttribute(
				attributeService, "a", null, "abc123", null, "",
				"Invalid Class: a");
		AttributeHelper.expectExceptionOnCreateInvalidAttribute(
				attributeService, "abc.b", null, "abc123", null, "",
				"Invalid Class: abc.b");
	}

	@Test
	public void validateConfigurationForTextAttributeType() {
		ClassHelper.createClass(classService, "abc", "a");

		AttributeHelper
				.createAndVerifyOneAttribute(attributeService, "abc.a", 1,
						"pa", AttributeType.TEXT,
						"{\"regex\":\"(\\\\w[-.\\\\w]*\\\\w@\\\\w[-.\\\\w]\\\\w.\\\\w{2,3})\"}");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pb", AttributeType.TEXT, "{\"minlength\":10}");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pc", AttributeType.TEXT, "{\"minlength\":100}");

		AttributeHelper
				.createAndVerifyOneAttribute(
						attributeService,
						"abc.a",
						1,
						"pd",
						AttributeType.TEXT,
						"{\"mandatory\": true, \"regex\": \"(\\\\w[-._\\\\w]\\\\w@\\\\w[-.\\\\w]*\\\\w.\\\\w{2,3})\", \"minlength\": 5,\"maxlength\": 15}");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pe", AttributeType.TEXT, "");

		AttributeHelper
				.createAndVerifyOneAttribute(
						attributeService,
						"abc.a",
						1,
						"pf",
						AttributeType.TEXT,
						"{\"default\": \"abc@abc.com\",\"regex\": \"(\\\\w[-.\\\\w]\\\\w@\\\\w[-._\\\\w]\\\\w.\\\\w{2,3})\",\"minlength\": 5,\"maxlength\": 15}");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pg", AttributeType.TEXT, "{\"default\":\"abc\"}");

	}

	@Test
	public void invalidConfigurationForTextAttributeType() {
		ClassHelper.createClass(classService, "abc", "a");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						null,
						"pa",
						AttributeType.TEXT,
						"{\"mandatory\":10}",
						"Invalid configuration for attribute pa: the mandatory value must be true or false literals");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						null,
						"pa",
						AttributeType.TEXT,
						"{\"mandatory\":\"true\"}",
						"Invalid configuration for attribute pa: the mandatory value must be true or false literals");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(attributeService,
						"abc.a", null, "pa", AttributeType.TEXT,
						"{\"default\":10}",
						"Invalid configuration for attribute pa: the default value must be a string");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(attributeService,
						"abc.a", null, "pa", AttributeType.TEXT,
						"{\"regex\":10}",
						"Invalid configuration for attribute pa: the regex value must be a string");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						null,
						"pa",
						AttributeType.TEXT,
						"{\"minlength\":\"abc\"}",
						"Invalid configuration for attribute pa: the minlength value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						null,
						"pa",
						AttributeType.TEXT,
						"{\"minlength\":10.0}",
						"Invalid configuration for attribute pa: the minlength value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						null,
						"pa",
						AttributeType.TEXT,
						"{\"maxlength\":\"abc\"}",
						"Invalid configuration for attribute pa: the maxlength value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						null,
						"pa",
						AttributeType.TEXT,
						"{\"maxlength\":10.0}",
						"Invalid configuration for attribute pa: the maxlength value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						null,
						"pa",
						AttributeType.TEXT,
						"{\"default\":\"abc\", \"regex\":\"(\\\\w[-.\\\\w]*\\\\w@\\\\w[-.\\\\w]*\\\\w.\\\\w{2,3})\"}",
						"Invalid configuration for attribute pa: the default value does not match regex configuration");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						null,
						"pa",
						AttributeType.TEXT,
						"{\"default\":\"abc\", \"minlength\":5}",
						"Invalid configuration for attribute pa: the default value is smaller than minlength");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						null,
						"pa",
						AttributeType.TEXT,
						"{\"default\":\"abcabc\", \"maxlength\":5}",
						"Invalid configuration for attribute pa: the default value is greater than maxlength");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(attributeService,
						"abc.a", null, "pa", AttributeType.TEXT,
						"{\"minlength\":50, \"maxlength\":10}",
						"Invalid configuration for attribute pa: minlength is greater than maxlength");

		// I think the message that validation should be 'Invalid configuration
		// for pa attribute: the default value is smaller than minlength "as
		// specified in a previous validations

		// expectExceptionOnCreateInvalidAttribute(attributeService,
		// "abc.a",
		// null,
		// "pa",
		// AttributeType.TEXT,
		// "{\"default\":\"abc\", \"minlength\":9, \"maxlength\":50}",
		// "Invalid configuration for attribute pa: the default value is lower than minlength");
	}
}