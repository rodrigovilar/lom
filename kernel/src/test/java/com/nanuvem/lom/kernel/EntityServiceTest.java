package com.nanuvem.lom.kernel;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nanuvem.lom.kernel.dao.memory.MemoryDaoFactory;

public class EntityServiceTest {

	private EntityService service;

	@Before
	public void init() {
		service = new EntityService(new MemoryDaoFactory());
	}

	@Test
	public void validNameAndNamespace() {
		createAndVerifyOneEntity("abc", "abc");
		createAndVerifyOneEntity("a.b.c", "abc");
		createAndVerifyOneEntity("a", "a");
		createAndVerifyOneEntity("abc123", "aaa");
		createAndVerifyOneEntity("abc", "abc1122");
	}

	@Test
	public void withoutNamespace() {
		createAndVerifyOneEntity("", "abc");
		createAndVerifyOneEntity(null, "abc");
		createAndVerifyOneEntity("", "a1");
		createAndVerifyOneEntity(null, "a1");
	}

	@Test
	public void twoEntitiesWithSameNameInDifferentNamespaces() {
		createAndVerifyTwoEntities("p1", "name", "p2", "name");
		createAndVerifyTwoEntities(null, "name", "p1", "name");
		createAndVerifyTwoEntities("a", "name", "a.b", "name");
	}

	@Test
	public void nameAndNamespaceWithSpaces() {
		this.expectExceptionOnCreateInvalidEntity("name space", "name",
				"Invalid value for Entity namespace: name space");
		this.expectExceptionOnCreateInvalidEntity("namespace", "na me",
				"Invalid value for Entity name: namespace.na me");
	}

	@Test
	public void withoutName() {
		this.expectExceptionOnCreateInvalidEntity("namespace", null,
				"The name of an Entity is mandatory");
		this.expectExceptionOnCreateInvalidEntity("namespace", "",
				"The name of an Entity is mandatory");
		this.expectExceptionOnCreateInvalidEntity(null, null,
				"The name of an Entity is mandatory");
		this.expectExceptionOnCreateInvalidEntity("", null,
				"The name of an Entity is mandatory");
	}

	@Test
	public void twoEntitiesWithSameNameInDefaultNamespace() {
		this.createAndVerifyOneEntity(null, "aaa");
		this.expectExceptionOnCreateInvalidEntity(null, "aaa",
				"The aaa entity already exists");
		this.expectExceptionOnCreateInvalidEntity("", "aaa",
				"The aaa entity already exists");
	}

	@Test
	public void twoEntitiesWithSameNameInAnonDefaultNamespace() {
		this.createAndVerifyOneEntity("a", "aaa");
		this.expectExceptionOnCreateInvalidEntity("a", "aaa",
				"The a.aaa entity already exists");
	}

	@Test
	public void nameAndNamespaceWithInvalidChars() {
		this.expectExceptionOnCreateInvalidEntity("a", "aaa$", "Invalid value for Entity name: aaa$");
		this.expectExceptionOnCreateInvalidEntity("a", "aaa#", "Invalid value for Entity name: aaa#");
		this.expectExceptionOnCreateInvalidEntity("a", "aaa=", "Invalid value for Entity name: aaa=");
		this.expectExceptionOnCreateInvalidEntity("a", "aaa.a", "Invalid value for Entity name: aaa.a");
		this.expectExceptionOnCreateInvalidEntity("a", "aaa/a", "Invalid value for Entity name: aaa/a");
		this.expectExceptionOnCreateInvalidEntity("a", "aaa*", "Invalid value for Entity name: aaa*");
		this.expectExceptionOnCreateInvalidEntity("a", "aaa'", "Invalid value for Entity name: aaa'");
		this.expectExceptionOnCreateInvalidEntity("a$", "aaa", "Invalid value for Entity namespace: a$");
		this.expectExceptionOnCreateInvalidEntity("a#", "aaa", "Invalid value for Entity namespace: a#");
		this.expectExceptionOnCreateInvalidEntity("a=", "aaa", "Invalid value for Entity namespace: a=");
		this.expectExceptionOnCreateInvalidEntity("a'", "aaa", "Invalid value for Entity namespace: a'");
		this.expectExceptionOnCreateInvalidEntity("a.", "aaa", "Invalid value for Entity namespace: a.");
		this.expectExceptionOnCreateInvalidEntity("a/a", "aaa", "Invalid value for Entity namespace: a/a");
		this.expectExceptionOnCreateInvalidEntity("a*", "aaa", "Invalid value for Entity namespace: a*");	
	}

	private void expectExceptionOnCreateInvalidEntity(String namespace,
			String name, String expectedMessage) {
		try {
			createAndVerifyOneEntity(namespace, name);
			fail();
		} catch (MetadataException e) {
			Assert.assertEquals(expectedMessage, e.getMessage());
		}
	}

	private void createAndVerifyTwoEntities(String string, String string2,
			String string3, String string4) {
		Entity entity1 = new Entity();
		service.create(entity1);

		Entity entity2 = new Entity();
		service.create(entity2);

		Assert.assertNotNull(entity1.getId());
		Assert.assertNotNull(entity2.getId());

		Assert.assertEquals((Integer) 0, entity1.getVersion());
		Assert.assertEquals((Integer) 0, entity2.getVersion());

		List<Entity> entities = service.listAll();
		Assert.assertEquals(2, entities.size());
		Assert.assertEquals(entity1, entities.get(0));
		Assert.assertEquals(entity2, entities.get(1));

		service.remove(entity1);
		service.remove(entity2);
	}

	private void createAndVerifyOneEntity(String name, String namespace) {
		Entity entity = new Entity();
		entity.setNamespace(namespace);
		entity.setName(name);
		service.create(entity);

		Assert.assertNotNull(entity.getId());
		Assert.assertEquals((Integer) 0, entity.getVersion());

		List<Entity> entities = service.listAll();
		Assert.assertEquals(1, entities.size());
		Assert.assertEquals(entity, entities.get(0));

		service.remove(entity);
	}

}
