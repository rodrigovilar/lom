package com.nanuvem.lom.kernel;

import static com.nanuvem.lom.kernel.AttributeHelper.LONGTEXT;
import static com.nanuvem.lom.kernel.AttributeHelper.TEXT;
import static com.nanuvem.lom.kernel.AttributeHelper.createOneAttribute;
import static com.nanuvem.lom.kernel.AttributeHelper.expectExceptionOnUpdateInvalidAttribute;
import static com.nanuvem.lom.kernel.AttributeHelper.updateAttribute;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nanuvem.lom.kernel.dao.memory.MemoryDaoFactory;

public class UpdateAttributeServiceTest {

	private static final String TEXT_CONFIGURATION_PARTIAL = "{\"minlength\": 5,\"maxlength\": 15}";

	private static final String TEXT_CONFIGURATION_COMPLETE = "{\"mandatory\": false, \"default\": \"abc@abc.com\", "
			+ "\"regex\": \"(\\\\w[-._\\\\w]\\\\w@\\\\w[-.\\\\w]*\\\\w.\\\\w{2,3})\", "
			+ "\"minlength\": 5,\"maxlength\": 15}";

	private static final String CONFIGURATION_MANDATORY_TRUE = "{\"mandatory\":true}";

	private ClassServiceImpl classService;
	private AttributeServiceImpl attributeService;

	@Before
	public void init() {
		MemoryDaoFactory daoFactory = new MemoryDaoFactory();
		this.classService = new ClassServiceImpl(daoFactory);
		this.attributeService = new AttributeServiceImpl(daoFactory);
	}

	@Test
	public void validNewName() {
		ClassHelper.createClass(classService, "abc", "a");
		Attribute createdAttribute = createOneAttribute(attributeService,
				"abc.a", null, "pa", LONGTEXT, null);

		Attribute updatedAttribute = updateAttribute(attributeService, "abc.a",
				createdAttribute, 1, "pb", LONGTEXT, null);
		verifyUpdatedAttribute(createdAttribute, updatedAttribute);
	}

	@Test
	public void invalidNewName() {
		ClassHelper.createClass(classService, "abc", "a");
		Attribute createdAttribute = createOneAttribute(attributeService,
				"abc.a", null, "pa", LONGTEXT, null);
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "",
				"The name of an Attribute is mandatory");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, null,
				"The name of an Attribute is mandatory");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "p a",
				"Invalid value for Attribute name: p a");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a$",
				"Invalid value for Attribute name: a$");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a#",
				"Invalid value for Attribute name: a#");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a=",
				"Invalid value for Attribute name: a=");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a'",
				"Invalid value for Attribute name: a'");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a.",
				"Invalid value for Attribute name: a.");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a.a",
				"Invalid value for Attribute name: a.a");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a/a",
				"Invalid value for Attribute name: a/a");
		expectExceptionOnUpdateWithInvalidNewName(createdAttribute, "a*",
				"Invalid value for Attribute name: a*");
	}

//	@Test
//	public void validNewSequence() {
//		ClassHelper.createClass(classService, "abc", "a");
//		Attribute createdAttribute1 = createOneAttribute(attributeService,
//				"abc.a", null, "pa", TEXT, null);
//		Attribute createdAttribute2 = createOneAttribute(attributeService,
//				"abc.a", null, "pb", TEXT, null);
//
//		Attribute updatedAttribute1 = updateAttribute(attributeService,
//				"abc.a", createdAttribute1, 2, "pa", LONGTEXT, null);
//		verifyUpdatedAttribute(createdAttribute1, updatedAttribute1);
//
//		Attribute updatedAttribute2 = updateAttribute(attributeService,
//				"abc.a", createdAttribute2, 2, "pb", LONGTEXT, null);
//		verifyUpdatedAttribute(createdAttribute2, updatedAttribute2);
//
//		Attribute updatedAttribute3 = updateAttribute(attributeService,
//				"abc.a", updatedAttribute2, 1, "pb", LONGTEXT, null);
//		verifyUpdatedAttribute(updatedAttribute2, updatedAttribute3);
//
//		Attribute updatedAttribute4 = updateAttribute(attributeService,
//				"abc.a", updatedAttribute3, null, "pb", LONGTEXT, null);
//		verifyUpdatedAttribute(updatedAttribute3, updatedAttribute4);
//		Assert.assertEquals(1, (int) updatedAttribute4.getSequence());
//	}
//
//	@Test
//	public void invalidNewSequence() {
//		ClassHelper.createClass(classService, "abc", "a");
//		Attribute createdAttribute = createOneAttribute(attributeService,
//				"abc.a", null, "pa", TEXT, null);
//		createOneAttribute(attributeService, "abc.a", null, "pb", TEXT, null);
//
//		expectExceptionOnUpdateInvalidAttribute(attributeService, "abc.a",
//				createdAttribute, -1, "pa", LONGTEXT, null,
//				"Invalid value for Attribute sequence: -1");
//		expectExceptionOnUpdateInvalidAttribute(attributeService, "abc.a",
//				createdAttribute, 0, "pa", LONGTEXT, null,
//				"Invalid value for Attribute sequence: 0");
//		expectExceptionOnUpdateInvalidAttribute(attributeService, "abc.a",
//				createdAttribute, 3, "pa", LONGTEXT, null,
//				"Invalid value for Attribute sequence: 3");
//	}
//
//	@Test
//	public void changeType() {
//		ClassHelper.createClass(classService, "abc", "a");
//		Attribute createdAttribute = createOneAttribute(attributeService,
//				"abc.a", null, "pa", TEXT, null);
//
//		expectExceptionOnUpdateInvalidAttribute(attributeService, "abc.a",
//				createdAttribute, 1, "pa", null, null,
//				"Can not change the type of an attribute");
//		expectExceptionOnUpdateInvalidAttribute(attributeService, "abc.a",
//				createdAttribute, 1, "pa", LONGTEXT, null,
//				"Can not change the type of an attribute");
//	}
//
//	@Test
//	public void renamingConflicts() {
//		ClassHelper.createClass(classService, "abc", "a");
//		ClassHelper.createClass(classService, "abc", "b");
//
//		createOneAttribute(attributeService, "abc.a", null, "pa", TEXT, null);
//
//		Attribute createdAttribute2 = createOneAttribute(attributeService,
//				"abc.b", null, "pb", TEXT, null);
//		Attribute createdAttribute3 = createOneAttribute(attributeService,
//				"abc.a", null, "pc", TEXT, null);
//
//		Attribute updatedAttribute2 = updateAttribute(attributeService,
//				"abc.b", createdAttribute2, 1, "pa", TEXT, null);
//		verifyUpdatedAttribute(createdAttribute2, updatedAttribute2);
//
//		expectExceptionOnUpdateInvalidAttribute(attributeService, "abc.a",
//				createdAttribute3, 1, "pa", TEXT, null,
//				"Attribute duplication on abc.a Class. It already has an attribute pa.");
//
//		expectExceptionOnUpdateInvalidAttribute(attributeService, "abc.a",
//				createdAttribute3, 1, "pA", TEXT, null,
//				"Attribute duplication on abc.a Class. It already has an attribute pa.");
//
//		expectExceptionOnUpdateInvalidAttribute(attributeService, "abc.a",
//				createdAttribute3, 1, "PA", TEXT, null,
//				"Attribute duplication on abc.a Class. It already has an attribute pa.");
//	}
//
//	@Test
//	public void genericChangeConfiguration() {
//		ClassHelper.createClass(classService, "abc", "a");
//
//		Attribute createdAttribute1 = createOneAttribute(attributeService,
//				"abc.a", null, "pa", TEXT, CONFIGURATION_MANDATORY_TRUE);
//
//		Attribute createdAttribute2 = createOneAttribute(attributeService,
//				"abc.a", null, "pb", TEXT, null);
//
//		Attribute updatedAttribute11 = updateAttribute(attributeService,
//				"abc.a", createdAttribute1, 1, "pa", TEXT,
//				"{\"mandatory\":false}");
//		verifyUpdatedAttribute(createdAttribute1, updatedAttribute11);
//
//		Attribute updatedAttribute12 = updateAttribute(attributeService,
//				"abc.a", updatedAttribute11, 1, "pa", TEXT, null);
//		verifyUpdatedAttribute(createdAttribute1, updatedAttribute12);
//
//		Attribute updatedAttribute2 = updateAttribute(attributeService,
//				"abc.a", createdAttribute2, 2, "pb", TEXT,
//				CONFIGURATION_MANDATORY_TRUE);
//		verifyUpdatedAttribute(createdAttribute2, updatedAttribute2);
//	}
//
//	@Test
//	public void validChangeConfigurationForTextAttributeType() {
//		ClassHelper.createClass(classService, "abc", "a");
//
//		Attribute createdAttribute1 = createOneAttribute(attributeService,
//				"abc.a", null, "pa", TEXT, CONFIGURATION_MANDATORY_TRUE);
//		Attribute createdAttribute2 = createOneAttribute(attributeService,
//				"abc.a", null, "pb", TEXT, TEXT_CONFIGURATION_COMPLETE);
//
//		Attribute updatedAttribute11 = updateAttribute(attributeService,
//				"abc.a", createdAttribute1, 1, "pa", TEXT,
//				TEXT_CONFIGURATION_PARTIAL);
//		verifyUpdatedAttribute(createdAttribute1, updatedAttribute11);
//
//		Attribute updatedAttribute12 = updateAttribute(attributeService,
//				"abc.a", updatedAttribute11, 1, "pa", TEXT,
//				TEXT_CONFIGURATION_COMPLETE);
//		verifyUpdatedAttribute(updatedAttribute11, updatedAttribute12);
//
//		Attribute updatedAttribute21 = updateAttribute(attributeService,
//				"abc.a", createdAttribute2, 2, "pb", TEXT,
//				TEXT_CONFIGURATION_PARTIAL);
//		verifyUpdatedAttribute(createdAttribute2, updatedAttribute21);
//
//		Attribute updatedAttribute22 = updateAttribute(attributeService,
//				"abc.a", updatedAttribute21, 2, "pb", TEXT,
//				CONFIGURATION_MANDATORY_TRUE);
//		verifyUpdatedAttribute(updatedAttribute21, updatedAttribute22);
//
//		Attribute updatedAttribute23 = updateAttribute(attributeService,
//				"abc.a", updatedAttribute22, 2, "pb", TEXT,
//				"{\"default\":\"abc\"}");
//		verifyUpdatedAttribute(updatedAttribute22, updatedAttribute23);
//
//		Attribute updatedAttribute24 = updateAttribute(attributeService,
//				"abc.a", updatedAttribute23, 2, "pb", TEXT,
//				"{\"default\":\"123\"}");
//		verifyUpdatedAttribute(updatedAttribute23, updatedAttribute24);
//	}
//
//	@Test
//	public void invalidChangeConfigurationForTextAttributeType() {
//		ClassHelper.createClass(classService, "abc", "a");
//		Attribute createdAttribute = createOneAttribute(attributeService,
//				"abc.a", null, "pa", TEXT, null);
//
//		expectExceptionOnUpdateInvalidAttribute(
//				attributeService,
//				"abc.a",
//				createdAttribute,
//				1,
//				"pa",
//				TEXT,
//				"{\"mandatory\":10}",
//				"Invalid configuration for attribute pa: the mandatory value must be true or false literals");
//		expectExceptionOnUpdateInvalidAttribute(
//				attributeService,
//				"abc.a",
//				createdAttribute,
//				1,
//				"pa",
//				TEXT,
//				"{\"mandatory\":\"true\"}",
//				"Invalid configuration for attribute pa: the mandatory value must be true or false literals");
//		expectExceptionOnUpdateInvalidAttribute(attributeService, "abc.a",
//				createdAttribute, 1, "pa", TEXT, "{\"default\":10}",
//				"Invalid configuration for attribute pa: the default value must be a string");
//		expectExceptionOnUpdateInvalidAttribute(attributeService, "abc.a",
//				createdAttribute, 1, "pa", TEXT, "{\"regex\":10}",
//				"Invalid configuration for attribute pa: the regex value must be a string");
//		expectExceptionOnUpdateInvalidAttribute(
//				attributeService,
//				"abc.a",
//				createdAttribute,
//				1,
//				"pa",
//				TEXT,
//				"{\"minlength\":\"abc\"}",
//				"Invalid configuration for attribute pa: the minlength value must be an integer number");
//		expectExceptionOnUpdateInvalidAttribute(
//				attributeService,
//				"abc.a",
//				createdAttribute,
//				1,
//				"pa",
//				TEXT,
//				"{\"minlength\":10.0}",
//				"Invalid configuration for attribute pa: the minlength value must be an integer number");
//		expectExceptionOnUpdateInvalidAttribute(
//				attributeService,
//				"abc.a",
//				createdAttribute,
//				1,
//				"pa",
//				TEXT,
//				"{\"maxlength\":\"abc\"}",
//				"Invalid configuration for attribute pa: the maxlength value must be an integer number");
//		expectExceptionOnUpdateInvalidAttribute(
//				attributeService,
//				"abc.a",
//				createdAttribute,
//				1,
//				"pa",
//				TEXT,
//				"{\"maxlength\":10.0}",
//				"Invalid configuration for attribute pa: the maxlength value must be an integer number");
//		expectExceptionOnUpdateInvalidAttribute(
//				attributeService,
//				"abc.a",
//				createdAttribute,
//				1,
//				"pa",
//				TEXT,
//				"{\"default\":\"abc\", \"regex\":\"(\\\\w[-.\\\\w]*\\\\w@\\\\w[-.\\\\w]*\\\\w.\\\\w{2,3})\"}",
//				"Invalid configuration for attribute pa: the default value does not match regex configuration");
//		expectExceptionOnUpdateInvalidAttribute(
//				attributeService,
//				"abc.a",
//				createdAttribute,
//				1,
//				"pa",
//				TEXT,
//				"{\"default\":\"abc\", \"minlength\":5}",
//				"Invalid configuration for attribute pa: the default value is smaller than minlength");
//		expectExceptionOnUpdateInvalidAttribute(
//				attributeService,
//				"abc.a",
//				createdAttribute,
//				1,
//				"pa",
//				TEXT,
//				"{\"default\":\"abcabc\", \"maxlength\":5}",
//				"Invalid configuration for attribute pa: the default value is greater than maxlength");
//		expectExceptionOnUpdateInvalidAttribute(attributeService, "abc.a",
//				createdAttribute, 1, "pa", TEXT,
//				"{\"minlength\":50, \"maxlength\":10}",
//				"Invalid configuration for attribute pa: minlength is greater than maxlength");
//		expectExceptionOnUpdateInvalidAttribute(
//				attributeService,
//				"abc.a",
//				createdAttribute,
//				1,
//				"pa",
//				TEXT,
//				"{\"default\":\"abc\", \"minlength\":9, \"maxlength\":50}",
//				"Invalid configuration for attribute pa: the default value is smaller than minlength");
//	}

	private void expectExceptionOnUpdateWithInvalidNewName(
			Attribute createdAttribute, String invalidNewName,
			String exceptedMessage) {
		expectExceptionOnUpdateInvalidAttribute(attributeService, "abc.a",
				createdAttribute, 1, invalidNewName, LONGTEXT, null,
				exceptedMessage);
	}

	private void verifyUpdatedAttribute(Attribute createdAttribute,
			Attribute updatedAttribute) {
		Assert.assertNotNull("updatedAttribute.id should not be null",
				updatedAttribute.getId());
		Assert.assertEquals("updatedAttribute.version should be 1", 1,
				(int) updatedAttribute.getVersion());
		Attribute listedAttribute = attributeService
				.findAttributeById(createdAttribute.getId());
		Assert.assertEquals("listedAttribute should be to updatedAttribute",
				updatedAttribute, listedAttribute);
		Assert.assertFalse("listedAttribute should be to createdAttribute",
				createdAttribute.equals(listedAttribute));
	}
}
