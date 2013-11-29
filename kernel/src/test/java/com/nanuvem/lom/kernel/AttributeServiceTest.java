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
				"{mandatory:true}");

		this.createClass("abc", "b");
		this.createAndVerifyOneAttribute("abc.b", 1, "pe",
				AttributeType.LONGTEXT, "{mandatory:false}");

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
				AttributeType.TEXT, "{mandatory:true}",
				"Invalid value for Attribute sequence: 0");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", -1, "pa",
				AttributeType.TEXT, "{mandatory:true}",
				"Invalid value for Attribute sequence: -1");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", 2, "pa",
				AttributeType.TEXT, "{mandatory:true}",
				"Invalid value for Attribute sequence: 2");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", 1, "",
				AttributeType.TEXT, "{mandatory:true}",
				"The name of a Attribute is mandatory");

		// It is impossible to make a test case with the AttributeType being
		// equal to an empty String. Attribute.type is defined as enum

		this.expectExceptionOnCreateInvalidAttribute("abc.a", 1, "pa", null,
				"{mandatory:true}", "The type of a Attribute is mandatory");

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
				AttributeType.TEXT, "{mandatory:true}",
				"Invalid value for Attribute name: aaa$");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", 1, "aaa#",
				AttributeType.TEXT, "{mandatory:true}",
				"Invalid value for Attribute name: aaa#");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", 1, "aaa=",
				AttributeType.TEXT, "{mandatory:true}",
				"Invalid value for Attribute name: aaa=");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", 1, "aaa'",
				AttributeType.TEXT, "{mandatory:true}",
				"Invalid value for Attribute name: aaa'");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", 1, "aaa.a",
				AttributeType.TEXT, "{mandatory:true}",
				"Invalid value for Attribute name: aaa.a");

		this.expectExceptionOnCreateInvalidAttribute("abc.a", 1, "aaa*",
				AttributeType.TEXT, "{mandatory:true}",
				"Invalid value for Attribute name: aaa*");
	}

	@Test
	public void validateSeveralAttributesInTheSameEntity() {
		this.createClass("abc", "a");
		
		this.createAndVerifyOneAttribute("abc.a", null, "pa", AttributeType.TEXT, "");
		Assert.assertEquals(new Integer(1), this.attributeService.findAttributeByNameAndClassFullname("pa", "abc.a").getSequence());

		this.createAndVerifyOneAttribute("abc.a", null, "pb", AttributeType.LONGTEXT, "");		
		Assert.assertEquals(new Integer(2), this.attributeService.findAttributeByNameAndClassFullname("pb", "abc.a").getSequence());

		this.createClass("", "b");
		
		this.createAndVerifyOneAttribute("b", new Integer(1), "pa", AttributeType.LONGTEXT, "");
		this.createAndVerifyOneAttribute("b", new Integer(1), "pb",	AttributeType.LONGTEXT, "");
		Assert.assertEquals(new Integer(2), this.attributeService.findAttributeByNameAndClassFullname("pa", "b").getSequence());
		Assert.assertEquals(new Integer(1), this.attributeService.findAttributeByNameAndClassFullname("pb", "b").getSequence());
		
		this.createClass("", "c");
		
		this.createAndVerifyOneAttribute("c", new Integer(1), "pa", AttributeType.TEXT, "");
		this.createAndVerifyOneAttribute("c", new Integer(2), "pb", AttributeType.LONGTEXT, "");
		this.createAndVerifyOneAttribute("c", new Integer(2), "pc", AttributeType.LONGTEXT, "");
		Assert.assertEquals(new Integer(1), this.attributeService.findAttributeByNameAndClassFullname("pa", "c").getSequence());
		Assert.assertEquals(new Integer(3), this.attributeService.findAttributeByNameAndClassFullname("pb", "c").getSequence());
		Assert.assertEquals(new Integer(2), this.attributeService.findAttributeByNameAndClassFullname("pc", "c").getSequence());
	}

	private void createAndVerifyOneAttribute(String fullClassName,
			Integer attributeSequence, String attributeName,
			AttributeType attributeType, String attributeConfiguration) {

		Attribute attributeCreated = this.createOneAttribute(fullClassName,
				attributeSequence, attributeName, attributeType,
				attributeConfiguration);

		Assert.assertNotNull(attributeCreated.getId());
		Assert.assertEquals(new Integer(0), attributeCreated.getVersion());
		Assert.assertEquals(attributeCreated, this.attributeService
				.findAttributeById(attributeCreated.getId()));
	}

	private void expectExceptionOnCreateInvalidAttribute(String fullClassName,
			Integer attributeSequence, String attributeName,
			AttributeType attributeType, String attributeConfiguration,
			String exceptedMessage) {

		try {
			this.createOneAttribute(fullClassName, attributeSequence,
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

	private Attribute createOneAttribute(String fullClassName,
			Integer attributeSequence, String attributeName,
			AttributeType attributeType, String attributeConfiguration) {

		Class clazz = this.classService.readClass(fullClassName);
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