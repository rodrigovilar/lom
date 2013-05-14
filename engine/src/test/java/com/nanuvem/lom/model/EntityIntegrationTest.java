package com.nanuvem.lom.model;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.validation.ValidationException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

import com.nanuvem.lom.service.EntityNotFoundException;
import com.nanuvem.lom.service.EntityService;

@RooIntegrationTest(entity = Entity.class)
public class EntityIntegrationTest {
	private Entity entity;

	@Autowired
	private EntityService entityService;

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

	@Test(expected = EntityNotFoundException.class)
	public void testDeleteEntity() {
		entity = CommonCreateMethodsForTesting.createEntity("nome", "pacote");
		Assert.assertNotNull(
				"Data on demand for 'Entity' failed to initialize correctly",
				entity);
		entityService.saveEntity(entity);
		Long id = entity.getId();
		Assert.assertNotNull(
				"Data on demand for 'Entity' failed to provide an identifier",
				entity.getId());
		entity = entityService.findEntity(id);
		entityService.deleteEntity(entity);
		entity.flush();
		Assert.assertNull("Failed to remove 'Entity' with identifier '" + id
				+ "'", entityService.findEntity(id));
	}

	/* CREATE ENTITY */
	@Test
	public void validNameAndNamespace() {
		entity = CommonCreateMethodsForTesting
				.createEntity("name", "namespace");
		entityService.saveEntity(entity);
		Assert.assertNotNull(
				"Data on demand for 'Entity' failed to provide an identifier",
				entity.getId());

	}

	@Test
	public void withoutNamespace() {
		entity.setName("name");
		entityService.saveEntity(entity);
	}

	@Test
	public void twoEntitiesSameNameDifferentNamespaces() {
		entity = CommonCreateMethodsForTesting.createEntity("samename",
				"namespace1");
		entityService.saveEntity(entity);

		Entity entity2 = CommonCreateMethodsForTesting.createEntity("samename",
				"namespace2");
		entityService.saveEntity(entity2);

		Entity entity_return = Entity.findEntity(entity.getId());
		Assert.assertEquals(entity, entity_return);
	}

	@Test
	public void nameAndNamespaceWithSpaces() {
		entity = this.createEntity("name with spaces", "namespace with spaces");
		entityService.saveEntity(entity);
		Assert.assertEquals(entity, Entity.findEntity(entity.getId()));
	}

	@Test(expected = ValidationException.class)
	public void withoutName() {
		entity.setNamespace("has only namespace");
		entityService.saveEntity(entity);
	}

	@Test(expected = ValidationException.class)
	public void twoEntitiesWithSameNameInDefaultPackage() {
		entity = CommonCreateMethodsForTesting.createEntity("samename", "");
		entityService.saveEntity(entity);

		Entity entity2 = CommonCreateMethodsForTesting.createEntity("samename",
				"");
		entityService.saveEntity(entity2);
	}

	@Test(expected = ValidationException.class)
	public void twoEntitiesWithSameNameInNonDefaultPackage() {
		entity = CommonCreateMethodsForTesting.createEntity("sameName",
				"sameNamespace");
		entityService.saveEntity(entity);

		Entity entity2 = CommonCreateMethodsForTesting.createEntity("sameName",
				"sameNamespace");
		entityService.saveEntity(entity2);
	}

	@Test
	public void nameOrNamespaceWithInvalidChar() throws Exception {
		try {
			entity = CommonCreateMethodsForTesting.createEntity(
					"wrongname@#$#@", "wrongnamespace@@#$%");
			entityService.saveEntity(entity);
			Assert.fail();
		} catch (ValidationException e) {
			List<Entity> allEntitiesList = Entity.findAllEntitys();
			Assert.assertFalse(allEntitiesList.contains(entity));
		}
	}

	@Test(expected = ValidationException.class)
	public void caseInsensitiveName() {
		entity = CommonCreateMethodsForTesting
				.createEntity("NAME", "namespace");
		entityService.saveEntity(entity);

		Entity entity2 = CommonCreateMethodsForTesting.createEntity("name",
				"namespace");
		entityService.saveEntity(entity2);
	}

	@Test(expected = ValidationException.class)
	public void caseInsensitiveNamespace() {
		Entity entity_1 = new Entity();
		entity_1.setName("name");
		entity_1.setNamespace("NAMESPACE");
		entityService.saveEntity(entity_1);

		Entity entity_2 = new Entity();
		entity_2.setName("name");
		entity_2.setNamespace("namespace");
		entityService.saveEntity(entity_2);
	}

	@Test
	public void updateWithValidNewNameAndNewPackage() {
		entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		entityService.saveEntity(entity);
		entity.setName("abc");
		entity.setNamespace("cde");
		Entity entityFound = Entity.findEntity(entity.getId());
		Assert.assertEquals("abc", entityFound.getName());
		Assert.assertEquals("cde", entityFound.getNamespace());

	}

	@Test
	public void updateRemovingPackage() {
		Entity entity_1 = CommonCreateMethodsForTesting.createEntity("test",
				"namespaceTest");
		entityService.saveEntity(entity_1);
		entity_1.setNamespace("");
		Entity entityFound = Entity.findEntity(entity_1.getId());
		Assert.assertEquals("", entityFound.getNamespace());
	}

	@Test
	public void updateRenameTwoEntitiesWithSameNameInDifferentPackage() {
		entity = CommonCreateMethodsForTesting.createEntity("aaaaa", "bbbbb");
		entityService.saveEntity(entity);

		Entity entity2 = CommonCreateMethodsForTesting.createEntity("ccccc",
				"ddddd");
		entityService.saveEntity(entity2);

		entity.setName("ccccc");
		// entityService.saveEntity(entity);

		Entity entity_found = Entity.findEntity(entity.getId());
		Entity entity2_found = Entity.findEntity(entity2.getId());

		Assert.assertEquals(entity_found.getName(), entity2_found.getName());
		Assert.assertEquals("bbbbb", entity_found.getNamespace());
		Assert.assertEquals("ddddd", entity2_found.getNamespace());
	}

	@Test(expected = ValidationException.class)
	public void updateRenameForcingCaseInsensitiveName() {
		entity = CommonCreateMethodsForTesting.createEntity("aaaaa", "bbbbb");
		entityService.saveEntity(entity);

		Entity entity2 = CommonCreateMethodsForTesting.createEntity("uuuuu",
				"bbbbb");
		entityService.saveEntity(entity2);

		entity2.setName("AaAaA");
		entityService.saveEntity(entity2);
	}

	@Test(expected = ValidationException.class)
	public void updateRenameForcingCaseInsensitivePackage() {
		entity = CommonCreateMethodsForTesting.createEntity("aaaaa", "bbbbb");
		entityService.saveEntity(entity);

		Entity entity2 = CommonCreateMethodsForTesting.createEntity("aaaaa",
				"ccccc");
		entityService.saveEntity(entity2);

		entity2.setNamespace("bbbbb");
		entityService.saveEntity(entity2);
	}

	@Test(expected = ValidationException.class)
	public void updateRenamingCausingTwoEntitiesWithSameNameInDefaultPackage() {
		entity = CommonCreateMethodsForTesting.createEntity("aaaaa", "");
		entityService.saveEntity(entity);

		Entity entity2 = CommonCreateMethodsForTesting
				.createEntity("ccccc", "");
		entityService.saveEntity(entity2);

		entity2.setName("aaaaa");
		entityService.saveEntity(entity2);
	}

	@Test(expected = ValidationException.class)
	public void updateRenamingCausingTwoEntitiesWithSameNameInNonDefaultPackage() {
		entity = CommonCreateMethodsForTesting.createEntity("aaaaa", "bbbbb");
		entityService.saveEntity(entity);

		Entity entity2 = CommonCreateMethodsForTesting.createEntity("ccccc",
				"bbbbb");
		entityService.saveEntity(entity2);

		entity2.setName("aaaaa");
		entityService.saveEntity(entity2);
	}

	@Test
	public void updateNameAndNamespaceWithSpaces() {
		entity = CommonCreateMethodsForTesting.createEntity("aaaaa", "bbbbb");
		entityService.saveEntity(entity);
		entity.setName("n a m e");
		entity.setNamespace("n a m e s p a c e");

		Entity entity_found = Entity.findEntity(entity.getId());

		Assert.assertEquals("n a m e", entity_found.getName());
		Assert.assertEquals("n a m e s p a c e", entity_found.getNamespace());
	}

	@Test(expected = ValidationException.class)
	public void updateRemoveName() {
		entity = CommonCreateMethodsForTesting.createEntity("aaaaa", "bbbbb");
		entityService.saveEntity(entity);
		entity.setName("");
		entityService.saveEntity(entity);
	}

	@Test(expected = ValidationException.class)
	public void updateRenameCausingNameWithInvalidChar() {
		entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		entityService.saveEntity(entity);
		entity.setName("@#$%");
		entityService.saveEntity(entity);
	}

	@Test(expected = ValidationException.class)
	public void updateRenameCausingNamespaceWithInvalidChar() {
		entity = CommonCreateMethodsForTesting.createEntity("aaaaa", "@#$%^&*");
		entityService.saveEntity(entity);
	}

	@Test(expected = EntityNotFoundException.class)
	public void testRemove() {
		Entity entity = CommonCreateMethodsForTesting.createEntity("entity",
				"namespace");
		entityService.saveEntity(entity);
		Long id = entity.getId();
		entity.remove();
		Entity found = Entity.findEntity(id);
		Assert.assertNull(found);
	}

	@Test(expected = EntityNotFoundException.class)
	public void deleteEntityDeletedByIdYet() {
		entity = CommonCreateMethodsForTesting.createEntity("aaaaa", "bbbbb");
		entityService.saveEntity(entity);
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
		Entity entity_1 = CommonCreateMethodsForTesting.createEntity(
				"entity_1", "namespace_entity_1");
		entityService.saveEntity(entity_1);

		Entity entity_2 = CommonCreateMethodsForTesting.createEntity(
				"entity_2", "namespace_entity_2");
		entityService.saveEntity(entity_2);

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

		Entity entity_1 = CommonCreateMethodsForTesting.createEntity("Carro",
				"namespace_entity_1");
		entityService.saveEntity(entity_1);

		Entity entity_2 = CommonCreateMethodsForTesting.createEntity("Car",
				"namespace_entity_2");
		entityService.saveEntity(entity_2);

		Entity entity_3 = CommonCreateMethodsForTesting.createEntity("Bike",
				"namespace_entity_3");
		entityService.saveEntity(entity_3);

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

		Entity entity_1 = CommonCreateMethodsForTesting.createEntity("Carro",
				"packet");
		entityService.saveEntity(entity_1);

		Entity entity_2 = CommonCreateMethodsForTesting.createEntity("Car",
				"packet");
		entityService.saveEntity(entity_2);

		Entity entity_3 = CommonCreateMethodsForTesting.createEntity("Bike",
				"namespace");
		entityService.saveEntity(entity_3);

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
		Entity entity_1 = CommonCreateMethodsForTesting.createEntity("Entity1",
				"namespace_entity_1");
		entityService.saveEntity(entity_1);

		Entity entity_2 = CommonCreateMethodsForTesting.createEntity("Entity2",
				"namespace_entity_2");
		entityService.saveEntity(entity_2);

		List<Entity> entities = entityService.findEntitysByNameLike("");

		Assert.assertTrue(entities.contains(entity_1));
		Assert.assertTrue(entities.contains(entity_2));
	}

	@Test
	public void listEntitiesByEmptyNamespace() {
		Entity entity_1 = CommonCreateMethodsForTesting.createEntity("name",
				null);
		entityService.saveEntity(entity_1);

		Entity entity_2 = CommonCreateMethodsForTesting.createEntity("Entity",
				"namespace_entity_2");
		entityService.saveEntity(entity_2);

		List<Entity> entities = entityService
				.findEntitysByNamespaceEquals(null);

		Assert.assertTrue(entities.contains(entity_1));
		Assert.assertFalse(entities.contains(entity_2));
	}

	@Test
	public void listEntitiesByNameWithSpace() {
		Entity entity_1 = CommonCreateMethodsForTesting.createEntity(
				"Name with spaces", "");
		entityService.saveEntity(entity_1);

		Entity entity_2 = CommonCreateMethodsForTesting.createEntity("Entity",
				"");
		entityService.saveEntity(entity_2);

		List<Entity> entities = entityService.findEntitysByNameLike(" with");

		Assert.assertTrue(entities.contains(entity_1));
		Assert.assertFalse(entities.contains(entity_2));
	}

	@Test
	public void listEntitiesByNamespaceWithSpace() {
		Entity entity_1 = CommonCreateMethodsForTesting.createEntity("Name",
				"Namespace with spaces");
		entityService.saveEntity(entity_1);

		Entity entity_2 = CommonCreateMethodsForTesting.createEntity("Entity",
				"");
		entityService.saveEntity(entity_2);

		Entity entity_3 = CommonCreateMethodsForTesting.createEntity("Entity2",
				"namespace");
		entityService.saveEntity(entity_3);

		List<Entity> entities = entityService
				.findEntitysByNamespaceLike(" with");

		Assert.assertTrue(entities.contains(entity_1));
		Assert.assertFalse(entities.contains(entity_2));
	}

	@Test
	public void listEntitiesForcingCaseInsensitiveName() {
		Entity entity_1 = CommonCreateMethodsForTesting.createEntity("NAME",
				"Namespace");
		entityService.saveEntity(entity_1);

		Entity entity_2 = CommonCreateMethodsForTesting.createEntity("Entity",
				"Namespace");
		entityService.saveEntity(entity_2);

		List<Entity> entitiesList = Entity.findEntitysByNameLike("name")
				.getResultList();
		Assert.assertTrue(entitiesList.contains(entity_1));
		Assert.assertFalse(entitiesList.contains(entity_2));
	}

	@Test
	public void getEntityByID() {
		entity = CommonCreateMethodsForTesting
				.createEntity("name", "namespace");
		entityService.saveEntity(entity);
		long id = entity.getId();
		Assert.assertEquals(entity, Entity.findEntity(id));
	}

	@Test
	public void listEntitiesUsingInvalidFragmentOfName() {
		String fragmentOfName = "INVALID_FRAGMENT";

		Entity entity_1 = CommonCreateMethodsForTesting.createEntity("Bus",
				"namespace");
		entityService.saveEntity(entity_1);

		Entity entity_2 = CommonCreateMethodsForTesting.createEntity("Car",
				"namespace");
		entityService.saveEntity(entity_2);

		List<Entity> entities = Entity.findEntitysByNameLike(fragmentOfName)
				.getResultList();

		Assert.assertFalse(entities.contains(entity_1));
		Assert.assertFalse(entities.contains(entity_2));
		Assert.assertEquals(0, entities.size());
	}

	@Test(expected = EntityNotFoundException.class)
	public void getEntityWithAnUnknowId() {
		Entity entity_1 = CommonCreateMethodsForTesting.createEntity("Bus",
				"Namespace");
		entityService.saveEntity(entity_1);
		long id = 10;
		Assert.assertNull(Entity.findEntity(id));
	}

}
