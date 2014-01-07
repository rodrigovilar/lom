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

	@Test
	public void validNewSequence() {
		ClassHelper.createClass(classService, "abc", "a");
		Attribute createdAttribute1 = createOneAttribute(attributeService,
				"abc.a", null, "pa", TEXT, null);
		Attribute createdAttribute2 = createOneAttribute(attributeService,
				"abc.a", null, "pb", TEXT, null);

		Attribute updatedAttribute1 = updateAttribute(attributeService,
				"abc.a", createdAttribute1, 2, "pa", LONGTEXT, null);
		verifyUpdatedAttribute(createdAttribute1, updatedAttribute1);

		Attribute updatedAttribute2 = updateAttribute(attributeService,
				"abc.a", createdAttribute2, 2, "pb", LONGTEXT, null);
		verifyUpdatedAttribute(createdAttribute2, updatedAttribute2);

		Attribute updatedAttribute3 = updateAttribute(attributeService,
				"abc.a", updatedAttribute2, 1, "pb", LONGTEXT, null);
		verifyUpdatedAttribute(updatedAttribute2, updatedAttribute3);

		Attribute updatedAttribute4 = updateAttribute(attributeService,
				"abc.a", updatedAttribute3, null, "pb", LONGTEXT, null);
		verifyUpdatedAttribute(updatedAttribute3, updatedAttribute4);
		Assert.assertEquals(1, (int) updatedAttribute4.getSequence());
	}

	@Test
	public void invalidNewSequence() {
		ClassHelper.createClass(classService, "abc", "a");
		Attribute createdAttribute = createOneAttribute(attributeService,
				"abc.a", null, "pa", TEXT, null);
		createOneAttribute(attributeService, "abc.a", null, "pb", TEXT, null);

		expectExceptionOnUpdateInvalidAttribute(attributeService, "abc.a",
				createdAttribute, -1, "pa", LONGTEXT, null,
				"Invalid value for Attribute sequence: -1");
		expectExceptionOnUpdateInvalidAttribute(attributeService, "abc.a",
				createdAttribute, 0, "pa", LONGTEXT, null,
				"Invalid value for Attribute sequence: 0");
		expectExceptionOnUpdateInvalidAttribute(attributeService, "abc.a",
				createdAttribute, 3, "pa", LONGTEXT, null,
				"Invalid value for Attribute sequence: 3");
	}

	@Test
	public void changeType() {
		ClassHelper.createClass(classService, "abc", "a");
		Attribute createdAttribute = createOneAttribute(attributeService,
				"abc.a", null, "pa", TEXT, null);

		expectExceptionOnUpdateInvalidAttribute(attributeService, "abc.a",
				createdAttribute, 1, "pa", null, null,
				"Can not change the type of an attribute");
		expectExceptionOnUpdateInvalidAttribute(attributeService, "abc.a",
				createdAttribute, 1, "pa", LONGTEXT, null,
				"Can not change the type of an attribute");
	}

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
