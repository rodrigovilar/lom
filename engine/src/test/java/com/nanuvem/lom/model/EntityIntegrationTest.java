package com.nanuvem.lom.model;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.validation.ValidationException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = Entity.class)
public class EntityIntegrationTest {
	private Entity entity;

	private Entity createEntity(String name, String namespace) {
		Entity entity = new Entity();
		entity.setName(name);
		entity.setNamespace(namespace);
		return entity;
	}

	@Before
	public void init() {
		entity = new Entity();
	}

	/* CREATE ENTITY */

	@Test
	public void validNameAndNamespace() {
		entity = this.createEntity("name", "namespace");
		entity.persist();
	}

	@Test
	public void withoutNamespace() {
		entity.setName("name");
		entity.persist();
	}

	@Test
	public void twoEntitiesSameNameDifferentNamespaces() {
		entity = this.createEntity("samename", "namespace1");
		entity.persist();

		Entity entity2 = this.createEntity("samename", "namespace2");
		entity2.persist();

		Entity entity_return = Entity.findEntity(entity.getId());
		Assert.assertEquals(entity, entity_return);
	}

	@Test
	public void nameAndNamespaceWithSpaces() {
		entity = this.createEntity("name with spaces", "namespace with spaces");
		entity.persist();
		Assert.assertEquals(entity, Entity.findEntity(entity.getId()));
	}

	@Test(expected = ValidationException.class)
	public void withoutName() {
		entity.setNamespace("has only namespace");
		entity.persist();
	}

	@Test(expected = ValidationException.class)
	public void twoEntitiesWithSameNameInDefaultPackage() {
		entity = this.createEntity("samename", "");
		entity.persist();

		Entity entity2 = this.createEntity("samename", "");
		entity2.persist();
	}

	@Test(expected = ValidationException.class)
	public void twoEntitiesWithSameNameInNonDefaultPackage() {
		entity = this.createEntity("sameName", "sameNamespace");
		entity.persist();

		Entity entity2 = this.createEntity("sameName", "sameNamespace");
		entity2.persist();
	}

	@Test
	public void nameOrNamespaceWithInvalidChar() throws Exception {
		try {
			entity = this.createEntity("wrongname@#$#@", "wrongnamespace@@#$%");
			Assert.fail();
		} catch (ValidationException e) {
			List<Entity> allEntitiesList = Entity.findAllEntitys();
			Assert.assertFalse(allEntitiesList.contains(entity));
		}
	}

	@Test(expected = ValidationException.class)
	public void caseInsensitiveName() {
		entity = this.createEntity("NAME", "namespace");
		entity.persist();

		Entity entity2 = this.createEntity("name", "namespace");
		entity2.persist();
	}

	@Test(expected = ValidationException.class)
	public void caseInsensitiveNamespace() {
		Entity entity_1 = new Entity();
		entity_1.setName("name");
		entity_1.setNamespace("NAMESPACE");
		entity_1.persist();

		Entity entity_2 = new Entity();
		entity_2.setName("name");
		entity_2.setNamespace("namespace");
	}

	@Test
	public void updateWithValidNewNameAndNewPackage() {
		entity = this.createEntity("entity", "namespace");
		entity.persist();
		entity.setName("abc");
		entity.setNamespace("cde");
		Entity entityFound = Entity.findEntity(entity.getId());
		Assert.assertEquals("abc", entityFound.getName());
		Assert.assertEquals("cde", entityFound.getNamespace());

	}

	@Test
	public void updateRemovingPackage() {
		Entity entity_1 = this.createEntity("test", "namespaceTest");
		entity_1.persist();
		entity_1.setNamespace("");
		Entity entityFound = Entity.findEntity(entity_1.getId());
		Assert.assertEquals("", entityFound.getNamespace());
	}

	@Test
	public void updateRenameTwoEntitiesWithSameNameInDifferentPackage() {
		entity = this.createEntity("aaaaa", "bbbbb");
		entity.persist();

		Entity entity2 = this.createEntity("ccccc", "ddddd");
		entity2.persist();

		entity.setName("ccccc");

		Entity entity_found = Entity.findEntity(entity.getId());
		Entity entity2_found = Entity.findEntity(entity2.getId());
		Assert.assertEquals(entity_found.getName(), entity2_found.getName());
		Assert.assertEquals("bbbbb", entity_found.getNamespace());
		Assert.assertEquals("ddddd", entity2_found.getNamespace());
	}

	@Test(expected = ValidationException.class)
	public void updateRenameForcingCaseInsensitiveName() {
		entity = this.createEntity("aaaaa", "bbbbb");
		entity.persist();

		Entity entity2 = this.createEntity("uuuuu", "bbbbb");
		entity2.persist();

		entity2.setName("AaAaA");
	}

	@Test(expected = ValidationException.class)
	public void updateRenameForcingCaseInsensitivePackage() {
		entity = this.createEntity("aaaaa", "bbbbb");
		entity.persist();

		Entity entity2 = this.createEntity("aaaaa", "ccccc");
		entity2.persist();

		entity2.setNamespace("bbbbb");
	}

	@Test(expected = ValidationException.class)
	public void updateRenamingCausingTwoEntitiesWithSameNameInDefaultPackage() {
		entity = this.createEntity("aaaaa", "");
		entity.persist();

		Entity entity2 = this.createEntity("ccccc", "");
		entity2.persist();

		entity2.setName("aaaaa");
	}

	@Test(expected = ValidationException.class)
	public void updateRenamingCausingTwoEntitiesWithSameNameInNonDefaultPackage() {
		entity = this.createEntity("aaaaa", "bbbbb");
		entity.persist();

		Entity entity2 = this.createEntity("ccccc", "bbbbb");
		entity2.persist();

		entity2.setName("aaaaa");
	}

	@Test
	public void updateNameAndNamespaceWithSpaces() {
		entity = this.createEntity("aaaaa", "bbbbb");
		entity.persist();
		entity.setName("n a m e");
		entity.setNamespace("n a m e s p a c e");

		Entity entity_found = Entity.findEntity(entity.getId());

		Assert.assertEquals("n a m e", entity_found.getName());
		Assert.assertEquals("n a m e s p a c e", entity_found.getNamespace());
	}

	@Test(expected = ValidationException.class)
	public void updateRemoveName() {
		entity = this.createEntity("aaaaa", "bbbbb");
		entity.persist();
		entity.setName("");
	}

	@Test(expected = ValidationException.class)
	public void updateRenameCausingNameWithInvalidChar() {
		entity = this.createEntity("entity", "namespace");
		entity.persist();
		entity.setName("@#$%");
	}

	@Test(expected = ValidationException.class)
	public void updateRenameCausingNamespaceWithInvalidChar() {
		entity = this.createEntity("aaaaa", "@#$%^&*");
		entity.persist();
	}

	@Test(expected = EntityNotFoundException.class)
	public void testRemove() {
		Entity entity = this.createEntity("entity", "namespace");
		entity.persist();
		Long id = entity.getId();
		entity.remove();
		Entity found = Entity.findEntity(id);
		Assert.assertNull(found);
	}

	@Test(expected = EntityNotFoundException.class)
	public void deleteEntityDeletedByIdYet() {
		entity = this.createEntity("aaaaa", "bbbbb");
		entity.persist();
		Entity found = Entity.findEntity(entity.getId());
		found.remove();
		entity.remove();
	}

	@Test(expected = EntityNotFoundException.class)
	public void deleteEntityByUnknownId() {
		Entity.findEntity((long) -123456789);
	}

	/* READ ENTITIES */
	@Test
	public void listAllEntities() {
		Entity entity_1 = new Entity();
		entity_1.setName("entity_1");
		entity_1.setNamespace("namespace_entity_1");
		entity_1.persist();

		Entity entity_2 = new Entity();
		entity_2.setName("entity_2");
		entity_2.setNamespace("namespace_entity_2");
		entity_2.persist();

		List<Entity> allEntitiesList = Entity.findAllEntitys();

		Assert.assertEquals(allEntitiesList.get(0).getName(),
				entity_1.getName());
		Assert.assertEquals(allEntitiesList.get(1).getName(),
				entity_2.getName());
		Assert.assertTrue(allEntitiesList.contains(entity_1));
		Assert.assertTrue(allEntitiesList.contains(entity_2));
	}

	@Test
	public void listEntitiesByValidFragmentOfName() {
		String fragmentOfName = "Ca";

		Entity entity_1 = this.createEntity("Carro", "namespace_entity_1");
		entity_1.persist();

		Entity entity_2 = this.createEntity("Car", "namespace_entity_2");
		entity_2.persist();

		Entity entity_3 = this.createEntity("Bike", "namespace_entity_3");
		entity_3.persist();

		TypedQuery<Entity> allEntities = Entity
				.findEntitysByNameLike(fragmentOfName);

		List<Entity> allEntitiesList = allEntities.getResultList();
		Assert.assertTrue(allEntitiesList.contains(entity_1));
		Assert.assertTrue(allEntitiesList.contains(entity_2));
		Assert.assertFalse(allEntitiesList.contains(entity_3));
		Assert.assertEquals(2, allEntitiesList.size());
	}

	@Test
	public void listEntitiesByValidFragmentOfNamespace() {
		String fragmentOfNamespace = "pack";

		Entity entity_1 = this.createEntity("Carro", "packet");
		entity_1.persist();

		Entity entity_2 = this.createEntity("Car", "packet");
		entity_2.persist();

		Entity entity_3 = this.createEntity("Bike", "namespace");
		entity_3.persist();

		TypedQuery<Entity> allEntities = Entity
				.findEntitysByNamespaceLike(fragmentOfNamespace);

		List<Entity> allEntitiesList = allEntities.getResultList();
		Assert.assertTrue(allEntitiesList.contains(entity_1));
		Assert.assertTrue(allEntitiesList.contains(entity_2));
		Assert.assertFalse(allEntitiesList.contains(entity_3));
		Assert.assertEquals(2, allEntitiesList.size());
	}

	@Test
	public void listEntitiesByEmptyName() {
		Entity entity_1 = this.createEntity(" ", "namespace_entity_1");
		entity_1.persist();

		Entity entity_2 = this.createEntity("Entity", "namespace_entity_2");
		entity_2.persist();

		List<Entity> entities = Entity.findEntitiesByEmptyName();

		Assert.assertTrue(entities.contains(entity_1));
		Assert.assertFalse(entities.contains(entity_2));
	}

	@Test
	public void listEntitiesByEmptyNamespace() {
		Entity entity_1 = this.createEntity("name", " ");
		entity_1.persist();

		Entity entity_2 = this.createEntity("Entity", "namespace_entity_2");
		entity_2.persist();

		List<Entity> entities = Entity.findEntitiesByEmptyNamespace();

		Assert.assertTrue(entities.contains(entity_1));
		Assert.assertFalse(entities.contains(entity_2));
	}

	@Test
	public void listEntitiesByNameWithSpace() {
		Entity entity_1 = this.createEntity("Name with spaces", "");
		entity_1.persist();

		Entity entity_2 = this.createEntity("Entity", "");
		entity_2.persist();

		List<Entity> entities = Entity.findEntitiesByNameWithSpace();

		Assert.assertTrue(entities.contains(entity_1));
		Assert.assertFalse(entities.contains(entity_2));
	}

	@Test
	public void listEntitiesByNamespaceWithSpace() {
		Entity entity_1 = this.createEntity("Name", "Namespace with spaces");
		entity_1.persist();

		Entity entity_2 = this.createEntity("Entity", "");
		entity_2.persist();

		List<Entity> entities = Entity.findEntitiesByNamespaceWithSpace();

		Assert.assertTrue(entities.contains(entity_1));
		Assert.assertFalse(entities.contains(entity_2));
	}

	@Test
	public void listEntitiesForcingCaseInsensitiveName() {
		Entity entity_1 = this.createEntity("NAME", "Namespace");
		entity_1.persist();

		Entity entity_2 = this.createEntity("Entity", "Namespace");
		entity_2.persist();

		List<Entity> entitiesList = Entity.findEntitysByNameLike("name")
				.getResultList();
		Assert.assertTrue(entitiesList.contains(entity_1));
		Assert.assertFalse(entitiesList.contains(entity_2));
	}

	@Test
	public void getEntityByID() {
		entity = this.createEntity("name", "namespace");
		entity.persist();
		long id = entity.getId();
		Assert.assertEquals(entity, Entity.findEntity(id));
	}

	@Test
	public void listEntitiesUsingInvalidFragmentOfName() {
		String fragmentOfName = "INVALID_FRAGMENT";

		Entity entity_1 = this.createEntity("Bus", "namespace");
		entity_1.persist();

		Entity entity_2 = this.createEntity("Car", "namespace");
		entity_2.persist();

		List<Entity> entities = Entity.findEntitysByNameLike(fragmentOfName)
				.getResultList();

		Assert.assertFalse(entities.contains(entity_1));
		Assert.assertFalse(entities.contains(entity_2));
		Assert.assertEquals(0, entities.size());
	}

	@Test(expected = EntityNotFoundException.class)
	public void getEntityWithAnUnknowId() {
		Entity entity_1 = this.createEntity("Bus", "Namespace");
		entity_1.persist();
		long id = 10;
		Assert.assertNull(Entity.findEntity(id));
	}

}
