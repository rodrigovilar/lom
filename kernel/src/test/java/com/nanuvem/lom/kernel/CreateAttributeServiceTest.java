package com.nanuvem.lom.kernel;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.nanuvem.lom.kernel.dao.memory.MemoryDaoFactory;

public class CreateAttributeServiceTest {

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

	@Test
	public void validConfigurationForLongTextAttributeType() {
		ClassHelper.createClass(classService, "abc", "a");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "p1", AttributeType.LONGTEXT, "{\"minlength\":10}");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "p2", AttributeType.LONGTEXT, "{\"maxlength\":100000}");

		AttributeHelper
				.createAndVerifyOneAttribute(
						attributeService,
						"abc.a",
						1,
						"p3",
						AttributeType.LONGTEXT,
						"{\"mandatory\":true, \"default\":\"long text\", \"minlength\":5, \"maxlength\":150000}");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "p4", AttributeType.LONGTEXT, "");
	}

	@Test
	public void invalidConfigurationForLongTextAttributeType() {
		ClassHelper.createClass(classService, "abc", "a");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(attributeService,
						"abc.a", 1, "pa", AttributeType.LONGTEXT,
						"{\"default\":10}",
						"Invalid configuration for attribute pa: the default value must be a string");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.LONGTEXT,
						"{\"minlength\":\"abc\"}",
						"Invalid configuration for attribute pa: the minlength value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.LONGTEXT,
						"{\"minlength\":10.0}",
						"Invalid configuration for attribute pa: the minlength value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.LONGTEXT,
						"{\"maxlength\":\"abc\"}",
						"Invalid configuration for attribute pa: the maxlength value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.LONGTEXT,
						"{\"maxlength\":10.0}",
						"Invalid configuration for attribute pa: the maxlength value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.LONGTEXT,
						"{\"default\":\"abc\", \"minlength\":5}",
						"Invalid configuration for attribute pa: the default value is smaller than minlength");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.LONGTEXT,
						"{\"default\":\"abcabc\", \"maxlength\":5}",
						"Invalid configuration for attribute pa: the default value is greater than maxlength");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(attributeService,
						"abc.a", 1, "pa", AttributeType.LONGTEXT,
						"{\"minlength\":50, \"maxlength\":10}",
						"Invalid configuration for attribute pa: minlength is greater than maxlength");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.LONGTEXT,
						"{\"default\":\"abc\",\"minlength\":9, \"maxlength\":10}",
						"Invalid configuration for attribute pa: the default value is smaller than minlength");
	}

	@Test
	public void validConfigurationForPasswordAttributeType() {
		ClassHelper.createClass(classService, "abc", "a");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pa", AttributeType.PASSWORD, "{\"minlength\":10}");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pb", AttributeType.PASSWORD, "{\"maxlength\":100000}");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pc", AttributeType.PASSWORD, "{\"maxlength\":100000}");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pd", AttributeType.PASSWORD, "{\"minUppers:\":1}");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pe", AttributeType.PASSWORD, "{\"minNumbers\":2}");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pf", AttributeType.PASSWORD, "{\"minSymbols\":3}");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pg", AttributeType.PASSWORD, "{\"maxRepeat\":2}");

		AttributeHelper
				.createAndVerifyOneAttribute(
						attributeService,
						"abc.a",
						1,
						"ph",
						AttributeType.PASSWORD,
						"{\"mandatory\":true, \"minlength\":5, \"maxlength\":12, "
								+ "\"minUppers\":1, \"minNumbers\":2, \"minSymbols\":3, "
								+ "\"maxRepeat\":2}");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pi", AttributeType.PASSWORD, "");
		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pj", AttributeType.PASSWORD,
				"{\"default\":\"Abcdef\", \"minUppers\":1}");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pk", AttributeType.PASSWORD,
				"{\"default\":\"abcdef10\", \"minNumbers\":2}");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pl", AttributeType.PASSWORD,
				"{\"default\":\"abcdef!@#\", \"minSymbols\":3}");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pm", AttributeType.PASSWORD,
				"{\"default\":\"aabcdef\", \"maxRepeat\":2}");

		AttributeHelper
				.createAndVerifyOneAttribute(
						attributeService,
						"abc.a",
						1,
						"pn",
						AttributeType.PASSWORD,
						"{\"default\":\"Abbcdef12!@#\", \"minUppers\":1, "
								+ "\"minNumbers\":2, \"minSymbols\":3, \"maxRepeat\":2}");
	}

	@Test
	public void invalidConfigurationForPasswordAttributeType() {
		ClassHelper.createClass(classService, "abc", "a");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(attributeService,
						"abc.a", 1, "pa", AttributeType.PASSWORD,
						"{\"default\":10}",
						"Invalid configuration for attribute pa: the default value must be a string");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"minlength\":\"abc\"}",
						"Invalid configuration for attribute pa: the minlength value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"minlength\":10.0}",
						"Invalid configuration for attribute pa: the minlength value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"maxlength\":\"abc\"}",
						"Invalid configuration for attribute pa: the maxlength value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"maxlength\":10.0}",
						"Invalid configuration for attribute pa: the maxlength value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"minUppers\":\"abc\"}",
						"Invalid configuration for attribute pa: the minUppers value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"minUppers\":10.0}",
						"Invalid configuration for attribute pa: the minUppers value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"minNumbers\":\"abc\"}",
						"Invalid configuration for attribute pa: the minNumbers value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"minNumbers\":10.0}",
						"Invalid configuration for attribute pa: the minNumbers value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"minSymbols\":\"abc\"}",
						"Invalid configuration for attribute pa: the minSymbols value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"minSymbols\":10.0}",
						"Invalid configuration for attribute pa: the minSymbols value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"maxRepeat\":\"abc\"}",
						"Invalid configuration for attribute pa: the maxRepeat value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"maxRepeat\":10.0}",
						"Invalid configuration for attribute pa: the maxRepeat value must be an integer number");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"default\":\"abc\", \"minlength\":5}",
						"Invalid configuration for attribute pa: the default value is smaller than minlength");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"default\":\"abcabc\", \"maxlength\":5}",
						"Invalid configuration for attribute pa: the default value is greater than maxlength");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(attributeService,
						"abc.a", 1, "pa", AttributeType.PASSWORD,
						"{\"minlength\":50, \"maxlength\":10}",
						"Invalid configuration for attribute pa: minlength is greater than maxlength");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"default\":\"abc\", \"minlength\":9, \"maxlength\":10}",
						"Invalid configuration for attribute pa: the default value is smaller than minlength");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"default\":\"abcdef\", \"minUppers\":1}",
						"Invalid configuration for attribute pa: the default value must have at least 1 upper case character");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"default\":\"abcDef\", \"minUppers\":2}",
						"Invalid configuration for attribute pa: the default value must have at least 2 upper case characters");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"default\":\"abcdef\", \"minNumbers\":1}",
						"Invalid configuration for attribute pa: the default value must have at least 1 numerical character");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"default\":\"abc1\", \"minNumbers\":2}",
						"Invalid configuration for attribute pa: the default value must have at least 2 numerical characters");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"default\":\"abcdef\", \"minSymbols\":1}",
						"Invalid configuration for attribute pa: the default value must have at least 1 symbol character");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"default\":\"!abc\", \"minSymbols\":2}",
						"Invalid configuration for attribute pa: the default value must have at least 2 symbol characters");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"default\":\"aabcdef\", \"maxRepeat\":0}",
						"Invalid configuration for attribute pa: the default value must not have repeated characters");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"default\":\"abcccd\", \"maxRepeat\":1}",
						"Invalid configuration for attribute pa: the default value must not have more than 2 repeated characters");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"default\":\"abccccd\", \"maxRepeat\":2}",
						"Invalid configuration for attribute pa: the default value must not have more than 3 repeated characters");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"default\":\"Abcdef1!@\", \"minUppers\":1, \"minNumbers\":1, \"minSymbols\":3,\"maxRepeat\":1}",
						"Invalid configuration for attribute pa: the default value must have at least 3 symbol characters");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						1,
						"pa",
						AttributeType.PASSWORD,
						"{\"default\":\"abccc\", \"minUppers\":1, \"minNumbers\":2, \"minSymbols\":3,\"maxRepeat\":1}",
						"Invalid configuration for attribute pa: "
								+ "the default value must have at least 1 upper case character, "
								+ "the default value must have at least 2 numerical characters, "
								+ "the default value must have at least 3 symbol characters, "
								+ "the default value must not have more than 2 repeated characters");
	}

	@Test
	@Ignore
	public void validConfigurationForObjectAttributeType() {
		ClassHelper.createClass(classService, "abc", "a");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pa", AttributeType.OBJECT,
				"{\"schema\":OBJECT WITH ONE ATTRIBUTE}");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pb", AttributeType.OBJECT,
				"{\"schema\":OBJECT WITH TWO ATTRIBUTE}");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pc", AttributeType.OBJECT,
				"{\"schema\":ARRAY OF OBJECTS WITH TWO ATTRIBUTES}");

		AttributeHelper.createAndVerifyOneAttribute(attributeService, "abc.a",
				1, "pd", AttributeType.OBJECT,
				"{\"schema\":OBJECT WITH ONE SUB OBJECT}");

		AttributeHelper
				.createAndVerifyOneAttribute(attributeService, "abc.a", 1,
						"pa", AttributeType.OBJECT,
						"{\"default\":OBJECT WITH ONE ATTRIBUTE, \"schema\":OBJECT WITH ONE ATTRIBUTE}");

		AttributeHelper
				.createAndVerifyOneAttribute(
						attributeService,
						"abc.a",
						1,
						"pb",
						AttributeType.OBJECT,
						"{\"default\":OBJECT WITH TWO ATTRIBUTES, \"schema\":OBJECT WITH TWO ATTRIBUTE}");

		AttributeHelper
				.createAndVerifyOneAttribute(
						attributeService,
						"abc.a",
						1,
						"pc",
						AttributeType.OBJECT,
						"{\"default\":ARRAY OF OBJECTS WITH TWO ATTRIBUTES, \"schema\":ARRAY OF OBJECTS WITH TWO ATTRIBUTES}");

		AttributeHelper
				.createAndVerifyOneAttribute(
						attributeService,
						"abc.a",
						1,
						"pd",
						AttributeType.OBJECT,
						"{\"default\":OBJECT WITH ONE SUB OBJECT, \"schema\":OBJECT WITH ONE SUB OBJECT}");

	}

	@Test
	@Ignore
	public void invalidConfigurationForObjectAttributeType() {
		ClassHelper.createClass(classService, "abc", "a");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(attributeService,
						"abc.a", null, "pa", AttributeType.OBJECT,
						"{\"default\":10}",
						"Invalid configuration for attribute pa: the default value must be a string");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						null,
						"pa",
						AttributeType.OBJECT,
						"{\"default\":'ABC'}",
						"Invalid configuration for attribute pa: the default value must use JSON format");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						null,
						"pa",
						AttributeType.OBJECT,
						"{\"default\":'{}', \"schema\":OBJECT WITH ONE ATTRIBUTE}",
						"Invalid configuration for attribute pa: the default value must match the JSON schema");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						null,
						"pa",
						AttributeType.OBJECT,
						"{\"default\":OBJECT WITH A WRONG ATTRIBUTE NAME, \"schema\":OBJECT WITH ONE ATTRIBUTE}",
						"Invalid configuration for attribute pa: the default value must match the JSON schema");

		AttributeHelper
				.expectExceptionOnCreateInvalidAttribute(
						attributeService,
						"abc.a",
						null,
						"pa",
						AttributeType.OBJECT,
						"{\"default\":OBJECT WITH A WRONG ATTRIBUTE TYPE, \"schema\":OBJECT WITH ONE ATTRIBUTE}",
						"Invalid configuration for attribute pa: the default value must match the JSON schema");

	}
}