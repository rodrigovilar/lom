package com.nanuvem.lom.kernel;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.nanuvem.lom.kernel.dao.memory.MemoryDaoFactory;

public class AttributeServiceTest {

	private ClassServiceImpl entityService;
	private AttributeServiceImpl attributeService;

	@Before
	public void init() {
		this.entityService = new ClassServiceImpl(new MemoryDaoFactory());
		this.attributeService = new AttributeServiceImpl(new MemoryDaoFactory());
	}

	@Test
	public void validAropertyData() {
		this.createEntity("abc", "a");
		this.createAndVerifyOneAttribute("abc.a", 1, "pa", AttributeType.TEXT,
				"{mandatory:true}");
		this.createEntity("abc", "b");
		this.createAndVerifyOneAttribute("abc.b", 1, "pe",
				AttributeType.LONGTEXT, "{mandatory:false}");
		this.createEntity("abc", "c");
		this.createAndVerifyOneAttribute("abc.c", 1, "pa", AttributeType.TEXT,
				"");
		this.createEntity("abc", "d");
		this.createAndVerifyOneAttribute("abc.d", 1, "pa", AttributeType.TEXT,
				null);
	}

	private void createAndVerifyOneAttribute(String fullClassName,
			Integer attributeSequence, String attributeName,
			AttributeType attributeType, String attributeConfiguration) {

		Attribute attribute = new Attribute();
		attribute.setName(attributeName);
		Class clazz = this.entityService.readClass(fullClassName);
		attribute.setClazz(clazz);
		attribute.setSequence(attributeSequence);
		attribute.setType(attributeType);
		attribute.setConfiguration(attributeConfiguration);
		this.attributeService.create(attribute);
		List<Attribute> listAllAttributes = this.attributeService
				.listAllAttributes(fullClassName);

		Assert.assertNotNull(attribute.getId());
		Assert.assertEquals(new Integer(0), attribute.getVersion());
		Assert.assertEquals(listAllAttributes.get(0), attribute);
	}

	private Class createEntity(String namespace, String name) {
		Class entity = new Class();
		entity.setNamespace(namespace);
		entity.setName(name);
		entityService.create(entity);
		return entity;
	}
}