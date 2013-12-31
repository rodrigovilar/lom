package com.nanuvem.lom.kernel;

import static org.junit.Assert.fail;
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
		this.createClass("abc", "a");
		this.createAndVerifyOneAttribute("abc.a", 1, "pa", AttributeType.TEXT,
				"{\"mandatory\":true}");

		this.createClass("abc", "b");
		this.createAndVerifyOneAttribute("abc.b", 1, "pe",
				AttributeType.LONGTEXT, "{\"mandatory\":false}");

		this.createClass("abc", "c");
		this.createAndVerifyOneAttribute("abc.c", 1, "pa", AttributeType.TEXT,
				"");

		this.createClass("abc", "d");
		this.createAndVerifyOneAttribute("abc.d", 1, "pa", AttributeType.TEXT,
				null);
	}

	@Test
	public void invalidAttributeData() {
		this.createClass("abc", "a");
		this.expectExceptionOnCreateInvalidAttribute("abc.a", 0, "pa",
				AttributeType.TEXT, "{\"mandatory\":true}",
				"Invalid value for Attribute sequence: 0");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", -1, "pa",
				AttributeType.TEXT, "{\"mandatory\":true}",
				"Invalid value for Attribute sequence: -1");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", 2, "pa",
				AttributeType.TEXT, "{\"mandatory\":true}",
				"Invalid value for Attribute sequence: 2");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", 1, "",
				AttributeType.TEXT, "{\"mandatory\":true}",
				"The name of a Attribute is mandatory");

		// It is impossible to make a test case with the AttributeType being
		// equal to an empty String. Attribute.type is defined as enum

		this.expectExceptionOnCreateInvalidAttribute("abc.a", 1, "pa", null,
				"{\"mandatory\":true}", "The type of a Attribute is mandatory");

		// It is impossible to make a test case with the AttributeType being
		// equal to String "ABC". Attribute.type is defined as enum

		this.expectExceptionOnCreateInvalidAttribute("abc.a", 1, "pa",
				AttributeType.TEXT, "ABC",
				"Invalid value for Attribute configuration: ABC");
	}

	@Test
	public void invalidAttributeName() {
		this.createClass("abc", "a");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", 1, "aaa$",
				AttributeType.TEXT, "{\"mandatory\":true}",
				"Invalid value for Attribute name: aaa$");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", 1, "aaa#",
				AttributeType.TEXT, "{\"mandatory\":true}",
				"Invalid value for Attribute name: aaa#");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", 1, "aaa=",
				AttributeType.TEXT, "{\"mandatory\":true}",
				"Invalid value for Attribute name: aaa=");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", 1, "aaa'",
				AttributeType.TEXT, "{\"mandatory\":true}",
				"Invalid value for Attribute name: aaa'");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", 1, "aaa.a",
				AttributeType.TEXT, "{\"mandatory\":true}",
				"Invalid value for Attribute name: aaa.a");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", 1, "aaa*",
				AttributeType.TEXT, "{\"mandatory\":true}",
				"Invalid value for Attribute name: aaa*");
	}

	@Test
	public void validateSeveralAttributesInTheSameClass() {
		this.createClass("abc", "a");

		this.createAndVerifyOneAttribute("abc.a", null, "pa",
				AttributeType.TEXT, "");
		Assert.assertEquals(new Integer(1), this.attributeService
				.findAttributeByNameAndClassFullName("pa", "abc.a")
				.getSequence());

		this.createAndVerifyOneAttribute("abc.a", null, "pb",
				AttributeType.LONGTEXT, "");
		Assert.assertEquals(new Integer(2), this.attributeService
				.findAttributeByNameAndClassFullName("pb", "abc.a")
				.getSequence());

		this.createClass("", "b");

		this.createAndVerifyOneAttribute("b", new Integer(1), "pa",
				AttributeType.LONGTEXT, "");
		this.createAndVerifyOneAttribute("b", new Integer(1), "pb",
				AttributeType.LONGTEXT, "");
		Assert.assertEquals(new Integer(2), this.attributeService
				.findAttributeByNameAndClassFullName("pa", "b").getSequence());
		Assert.assertEquals(new Integer(1), this.attributeService
				.findAttributeByNameAndClassFullName("pb", "b").getSequence());

		this.createClass("", "c");

		this.createAndVerifyOneAttribute("c", new Integer(1), "pa",
				AttributeType.TEXT, "");
		this.createAndVerifyOneAttribute("c", new Integer(2), "pb",
				AttributeType.LONGTEXT, "");
		this.createAndVerifyOneAttribute("c", new Integer(2), "pc",
				AttributeType.LONGTEXT, "");
		Assert.assertEquals(new Integer(1), this.attributeService
				.findAttributeByNameAndClassFullName("pa", "c").getSequence());
		Assert.assertEquals(new Integer(3), this.attributeService
				.findAttributeByNameAndClassFullName("pb", "c").getSequence());
		Assert.assertEquals(new Integer(2), this.attributeService
				.findAttributeByNameAndClassFullName("pc", "c").getSequence());
	}

	@Test
	public void validateAttributeDuplicationInTheSameClass() {
		this.createClass("abc", "a");
		this.createAndVerifyOneAttribute("abc.a", null, "pa",
				AttributeType.TEXT, "");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa",
				AttributeType.LONGTEXT, "",
				"Attribute duplication on abc.a Class. It already has an attribute pa.");
		this.expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa",
				AttributeType.TEXT, "",
				"Attribute duplication on abc.a Class. It already has an attribute pa.");
		this.expectExceptionOnCreateInvalidAttribute("abc.a", null, "pA",
				AttributeType.TEXT, "",
				"Attribute duplication on abc.a Class. It already has an attribute pa.");
		this.expectExceptionOnCreateInvalidAttribute("abc.a", null, "PA",
				AttributeType.TEXT, "",
				"Attribute duplication on abc.a Class. It already has an attribute pa.");
	}

	@Test
	public void invalidClass() {
		this.expectExceptionOnCreateInvalidAttribute("a", null, "abc123",
				AttributeType.TEXT, "", "Invalid Class: a");

		this.createClass("abc", "a");

		this.expectExceptionOnCreateInvalidAttribute("abca", null, "abc123",
				AttributeType.TEXT, "", "Invalid Class: abca");
		this.expectExceptionOnCreateInvalidAttribute("a", null, "abc123",
				AttributeType.TEXT, "", "Invalid Class: a");
		this.expectExceptionOnCreateInvalidAttribute("abc.b", null, "abc123",
				AttributeType.TEXT, "", "Invalid Class: abc.b");

		this.createClass("", "b");

		this.expectExceptionOnCreateInvalidAttribute("a", null, "abc123", null,
				"", "Invalid Class: a");
		this.expectExceptionOnCreateInvalidAttribute("abc.b", null, "abc123",
				null, "", "Invalid Class: abc.b");
	}

	@Test
	public void validateConfigurationForTextAttributeType() {
		this.createClass("abc", "a");

		this.createAndVerifyOneAttribute("abc.a", 1, "pa", AttributeType.TEXT,
				"{\"regex\":\"(\\\\w[-.\\\\w]*\\\\w@\\\\w[-.\\\\w]\\\\w.\\\\w{2,3})\"}");

		this.createAndVerifyOneAttribute("abc.a", 1, "pb", AttributeType.TEXT,
				"{\"minlength\":10}");

		this.createAndVerifyOneAttribute("abc.a", 1, "pc", AttributeType.TEXT,
				"{\"minlength\":100}");

		this.createAndVerifyOneAttribute(
				"abc.a",
				1,
				"pd",
				AttributeType.TEXT,
				"{\"mandatory\": true, \"regex\": \"(\\\\w[-._\\\\w]\\\\w@\\\\w[-.\\\\w]*\\\\w.\\\\w{2,3})\", \"minlength\": 5,\"maxlength\": 15}");

		this.createAndVerifyOneAttribute("abc.a", 1, "pe", AttributeType.TEXT,
				"");

		this.createAndVerifyOneAttribute(
				"abc.a",
				1,
				"pf",
				AttributeType.TEXT,
				"{\"default\": \"abc@abc.com\",\"regex\": \"(\\\\w[-.\\\\w]\\\\w@\\\\w[-._\\\\w]\\\\w.\\\\w{2,3})\",\"minlength\": 5,\"maxlength\": 15}");

		this.createAndVerifyOneAttribute("abc.a", 1, "pg", AttributeType.TEXT,
				"{\"default\":\"abc\"}");

	}

	@Test
	public void invalidConfigurationForTextAttributeType() {
		this.createClass("abc", "a");

		this.expectExceptionOnCreateInvalidAttribute(
				"abc.a",
				null,
				"pa",
				AttributeType.TEXT,
				"{\"mandatory\":10}",
				"Invalid configuration for attribute pa: the mandatory value must be true or false literals");

		this.expectExceptionOnCreateInvalidAttribute(
				"abc.a",
				null,
				"pa",
				AttributeType.TEXT,
				"{\"mandatory\":\"true\"}",
				"Invalid configuration for attribute pa: the mandatory value must be true or false literals");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa",
				AttributeType.TEXT, "{\"default\":10}",
				"Invalid configuration for attribute pa: the default value must be a string");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa",
				AttributeType.TEXT, "{\"regex\":10}",
				"Invalid configuration for attribute pa: the regex value must be a string");

		this.expectExceptionOnCreateInvalidAttribute(
				"abc.a",
				null,
				"pa",
				AttributeType.TEXT,
				"{\"minlength\":\"abc\"}",
				"Invalid configuration for attribute pa: the minlength value must be an integer number");

		this.expectExceptionOnCreateInvalidAttribute(
				"abc.a",
				null,
				"pa",
				AttributeType.TEXT,
				"{\"minlength\":10.0}",
				"Invalid configuration for attribute pa: the minlength value must be an integer number");

		this.expectExceptionOnCreateInvalidAttribute(
				"abc.a",
				null,
				"pa",
				AttributeType.TEXT,
				"{\"maxlength\":\"abc\"}",
				"Invalid configuration for attribute pa: the maxlength value must be an integer number");

		this.expectExceptionOnCreateInvalidAttribute(
				"abc.a",
				null,
				"pa",
				AttributeType.TEXT,
				"{\"maxlength\":10.0}",
				"Invalid configuration for attribute pa: the maxlength value must be an integer number");

		this.expectExceptionOnCreateInvalidAttribute(
				"abc.a",
				null,
				"pa",
				AttributeType.TEXT,
				"{\"default\":\"abc\", \"regex\":\"(\\\\w[-.\\\\w]*\\\\w@\\\\w[-.\\\\w]*\\\\w.\\\\w{2,3})\"}",
				"Invalid configuration for attribute pa: the default value does not match regex configuration");

		this.expectExceptionOnCreateInvalidAttribute(
				"abc.a",
				null,
				"pa",
				AttributeType.TEXT,
				"{\"default\":\"abc\", \"minlength\":5}",
				"Invalid configuration for attribute pa: the default value is smaller than minlength");

		this.expectExceptionOnCreateInvalidAttribute(
				"abc.a",
				null,
				"pa",
				AttributeType.TEXT,
				"{\"default\":\"abcabc\", \"maxlength\":5}",
				"Invalid configuration for attribute pa: the default value is greater than maxlength");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", null, "pa",
				AttributeType.TEXT, "{\"minlength\":50, \"maxlength\":10}",
				"Invalid configuration for attribute pa: minlength is greater than maxlength");

		// I think the message that validation should be 'Invalid configuration
		// for pa attribute: the default value is smaller than minlength "as
		// specified in a previous validations

		// this.expectExceptionOnCreateInvalidAttribute(
		// "abc.a",
		// null,
		// "pa",
		// AttributeType.TEXT,
		// "{\"default\":\"abc\", \"minlength\":9, \"maxlength\":50}",
		// "Invalid configuration for attribute pa: the default value is lower than minlength");
	}

	private void createAndVerifyOneAttribute(String classFullName,
			Integer attributeSequence, String attributeName,
			AttributeType attributeType, String attributeConfiguration) {

		Attribute createdAttribute = this.createOneAttribute(classFullName,
				attributeSequence, attributeName, attributeType,
				attributeConfiguration);

		Assert.assertNotNull(createdAttribute.getId());
		Assert.assertEquals(new Integer(0), createdAttribute.getVersion());
		Assert.assertEquals(createdAttribute, this.attributeService
				.findAttributeById(createdAttribute.getId()));
	}

	private void expectExceptionOnCreateInvalidAttribute(String classFullName,
			Integer attributeSequence, String attributeName,
			AttributeType attributeType, String attributeConfiguration,
			String exceptedMessage) {

		try {
			this.createOneAttribute(classFullName, attributeSequence,
					attributeName, attributeType, attributeConfiguration);
			fail();
		} catch (MetadataException metadataException) {
			Assert.assertEquals(exceptedMessage, metadataException.getMessage());
		}
	}

	private Class createClass(String namespace, String name) {
		Class clazz = new Class();
		clazz.setNamespace(namespace);
		clazz.setName(name);
		classService.create(clazz);
		return clazz;
	}

	private Attribute createOneAttribute(String classFullName,
			Integer attributeSequence, String attributeName,
			AttributeType attributeType, String attributeConfiguration) {

		String namespace = null;
		String name = null;

		if (classFullName.contains(".")) {
			namespace = classFullName.substring(0,
					classFullName.lastIndexOf("."));
			name = classFullName.substring(classFullName.lastIndexOf(".") + 1,
					classFullName.length());
		} else {
			name = classFullName;
		}

		Class clazz = new Class();
		clazz.setNamespace(namespace);
		clazz.setName(name);
		Attribute attribute = new Attribute();
		attribute.setName(attributeName);

		attribute.setClazz(clazz);
		attribute.setSequence(attributeSequence);
		attribute.setType(attributeType);
		attribute.setConfiguration(attributeConfiguration);
		this.attributeService.create(attribute);

		return attribute;
	}
}