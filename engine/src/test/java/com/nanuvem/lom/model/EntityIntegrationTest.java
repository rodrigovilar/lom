package com.nanuvem.lom.model;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.validation.ValidationException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

import com.nanuvem.lom.service.EntityService;
import com.nanuvem.lom.service.EntityServiceImpl;

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
	
	@Test(expected=EntityNotFoundException.class)
    public void testDeleteEntity() {
        entity = this.createEntity("nome", "pacote");
        Assert.assertNotNull("Data on demand for 'Entity' failed to initialize correctly", entity);
        entityService.saveEntity(entity);
        Long id = entity.getId();
        Assert.assertNotNull("Data on demand for 'Entity' failed to provide an identifier", entity.getId());
        entity = entityService.findEntity(id);
        entityService.deleteEntity(entity);
        entity.flush();
        Assert.assertNull("Failed to remove 'Entity' with identifier '" + id + "'", entityService.findEntity(id));
    }
	
	/* CREATE ENTITY */
	@Test
	public void validNameAndNamespace() {
		entity = this.createEntity("name", "namespace");
		entityService.saveEntity(entity);
        Assert.assertNotNull("Data on demand for 'Entity' failed to provide an identifier", entity.getId());

	}

	@Test
	public void withoutNamespace() {
		entity.setName("name");
		entityService.saveEntity(entity);
	}

	@Test
	public void twoEntitiesSameNameDifferentNamespaces() {
		entity = this.createEntity("samename", "namespace1");
		entityService.saveEntity(entity);

		Entity entity2 = this.createEntity("samename", "namespace2");
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
		entity = this.createEntity("samename", "");
		entityService.saveEntity(entity);

		Entity entity2 = this.createEntity("samename", "");
		entityService.saveEntity(entity2);
	}

	@Test(expected = ValidationException.class)
	public void twoEntitiesWithSameNameInNonDefaultPackage() {
		entity = this.createEntity("sameName", "sameNamespace");
		entityService.saveEntity(entity);

		Entity entity2 = this.createEntity("sameName", "sameNamespace");
		entityService.saveEntity(entity2);
	}

	@Test
	public void nameOrNamespaceWithInvalidChar() throws Exception {
		try {
			entity = this.createEntity("wrongname@#$#@", "wrongnamespace@@#$%");
			entityService.saveEntity(entity);
			Assert.fail();
		} catch (ValidationException e) {
			List<Entity> allEntitiesList = Entity.findAllEntitys();
			Assert.assertFalse(allEntitiesList.contains(entity));
		}
	}

	@Test(expected = ValidationException.class)
	public void caseInsensitiveName() {
		entity = this.createEntity("NAME", "namespace");
		entityService.saveEntity(entity);

		Entity entity2 = this.createEntity("name", "namespace");
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
		entity = this.createEntity("entity", "namespace");
		entityService.saveEntity(entity);
		entity.setName("abc");
		entity.setNamespace("cde");
		Entity entityFound = Entity.findEntity(entity.getId());
		Assert.assertEquals("abc", entityFound.getName());
		Assert.assertEquals("cde", entityFound.getNamespace());

	}

	@Test
	public void updateRemovingPackage() {
		Entity entity_1 = this.createEntity("test", "namespaceTest");
		entityService.saveEntity(entity_1);
		entity_1.setNamespace("");
		Entity entityFound = Entity.findEntity(entity_1.getId());
		Assert.assertEquals("", entityFound.getNamespace());
	}

	@Test
	public void updateRenameTwoEntitiesWithSameNameInDifferentPackage() {
		entity = this.createEntity("aaaaa", "bbbbb");
		entityService.saveEntity(entity);

		Entity entity2 = this.createEntity("ccccc", "ddddd");
		entityService.saveEntity(entity2);

		entity.setName("ccccc");
		//entityService.saveEntity(entity);
		
		Entity entity_found = Entity.findEntity(entity.getId());
		Entity entity2_found = Entity.findEntity(entity2.getId());
		
		Assert.assertEquals(entity_found.getName(), entity2_found.getName());
		Assert.assertEquals("bbbbb", entity_found.getNamespace());
		Assert.assertEquals("ddddd", entity2_found.getNamespace());
	}

	@Test(expected = ValidationException.class)
	public void updateRenameForcingCaseInsensitiveName() {
		entity = this.createEntity("aaaaa", "bbbbb");
		entityService.saveEntity(entity);

		Entity entity2 = this.createEntity("uuuuu", "bbbbb");
		entityService.saveEntity(entity2);

		entity2.setName("AaAaA");
		entityService.saveEntity(entity2);
	}

	@Test(expected = ValidationException.class)
	public void updateRenameForcingCaseInsensitivePackage() {
		entity = this.createEntity("aaaaa", "bbbbb");
		entityService.saveEntity(entity);

		Entity entity2 = this.createEntity("aaaaa", "ccccc");
		entityService.saveEntity(entity2);

		entity2.setNamespace("bbbbb");
		entityService.saveEntity(entity2);
	}

	@Test(expected = ValidationException.class)
	public void updateRenamingCausingTwoEntitiesWithSameNameInDefaultPackage() {
		entity = this.createEntity("aaaaa", "");
		entityService.saveEntity(entity);

		Entity entity2 = this.createEntity("ccccc", "");
		entityService.saveEntity(entity2);

		entity2.setName("aaaaa");
		entityService.saveEntity(entity2);
	}

	@Test(expected = ValidationException.class)
	public void updateRenamingCausingTwoEntitiesWithSameNameInNonDefaultPackage() {
		entity = this.createEntity("aaaaa", "bbbbb");
		entityService.saveEntity(entity);

		Entity entity2 = this.createEntity("ccccc", "bbbbb");
		entityService.saveEntity(entity2);

		entity2.setName("aaaaa");
		entityService.saveEntity(entity2);
	}

	@Test
	public void updateNameAndNamespaceWithSpaces() {
		entity = this.createEntity("aaaaa", "bbbbb");
		entityService.saveEntity(entity);
		entity.setName("n a m e");
		entity.setNamespace("n a m e s p a c e");

		Entity entity_found = Entity.findEntity(entity.getId());

		Assert.assertEquals("n a m e", entity_found.getName());
		Assert.assertEquals("n a m e s p a c e", entity_found.getNamespace());
	}

	@Test(expected = ValidationException.class)
	public void updateRemoveName() {
		entity = this.createEntity("aaaaa", "bbbbb");
		entityService.saveEntity(entity);
		entity.setName("");
		entityService.saveEntity(entity);
	}

	@Test(expected = ValidationException.class)
	public void updateRenameCausingNameWithInvalidChar() {
		entity = this.createEntity("entity", "namespace");
		entityService.saveEntity(entity);
		entity.setName("@#$%");
		entityService.saveEntity(entity);
	}

	@Test(expected = ValidationException.class)
	public void updateRenameCausingNamespaceWithInvalidChar() {
		entity = this.createEntity("aaaaa", "@#$%^&*");
		entityService.saveEntity(entity);
	}

	@Test(expected = EntityNotFoundException.class)
	public void testRemove() {
		Entity entity = this.createEntity("entity", "namespace");
		entityService.saveEntity(entity);
		Long id = entity.getId();
		entity.remove();
		Entity found = Entity.findEntity(id);
		Assert.assertNull(found);
	}

	@Test(expected = EntityNotFoundException.class)
	public void deleteEntityDeletedByIdYet() {
		entity = this.createEntity("aaaaa", "bbbbb");
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
		Entity entity_1 = new Entity();
		entity_1.setName("entity_1");
		entity_1.setNamespace("namespace_entity_1");
		entityService.saveEntity(entity_1);

		Entity entity_2 = new Entity();
		entity_2.setName("entity_2");
		entity_2.setNamespace("namespace_entity_2");
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

		Entity entity_1 = this.createEntity("Carro", "namespace_entity_1");
		entityService.saveEntity(entity_1);

		Entity entity_2 = this.createEntity("Car", "namespace_entity_2");
		entityService.saveEntity(entity_2);

		Entity entity_3 = this.createEntity("Bike", "namespace_entity_3");
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

		Entity entity_1 = this.createEntity("Carro", "packet");
		entityService.saveEntity(entity_1);

		Entity entity_2 = this.createEntity("Car", "packet");
		entityService.saveEntity(entity_2);

		Entity entity_3 = this.createEntity("Bike", "namespace");
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
		Entity entity_1 = this.createEntity(" ", "namespace_entity_1");
		entityService.saveEntity(entity_1);

		Entity entity_2 = this.createEntity("Entity", "namespace_entity_2");
		entityService.saveEntity(entity_2);

		List<Entity> entities = Entity.findEntitiesByEmptyName();

		Assert.assertTrue(entities.contains(entity_1));
		Assert.assertFalse(entities.contains(entity_2));
	}

	@Test
	public void listEntitiesByEmptyNamespace() {
		Entity entity_1 = this.createEntity("name", " ");
		entityService.saveEntity(entity_1);

		Entity entity_2 = this.createEntity("Entity", "namespace_entity_2");
		entityService.saveEntity(entity_2);

		List<Entity> entities = Entity.findEntitiesByEmptyNamespace();

		Assert.assertTrue(entities.contains(entity_1));
		Assert.assertFalse(entities.contains(entity_2));
	}

	@Test
	public void listEntitiesByNameWithSpace() {
		Entity entity_1 = this.createEntity("Name with spaces", "");
		entityService.saveEntity(entity_1);

		Entity entity_2 = this.createEntity("Entity", "");
		entityService.saveEntity(entity_2);

		List<Entity> entities = Entity.findEntitiesByNameWithSpace();

		Assert.assertTrue(entities.contains(entity_1));
		Assert.assertFalse(entities.contains(entity_2));
	}

	@Test
	public void listEntitiesByNamespaceWithSpace() {
		Entity entity_1 = this.createEntity("Name", "Namespace with spaces");
		entityService.saveEntity(entity_1);

		Entity entity_2 = this.createEntity("Entity", "");
		entityService.saveEntity(entity_2);

		List<Entity> entities = Entity.findEntitiesByNamespaceWithSpace();

		Assert.assertTrue(entities.contains(entity_1));
		Assert.assertFalse(entities.contains(entity_2));
	}

	@Test
	public void listEntitiesForcingCaseInsensitiveName() {
		Entity entity_1 = this.createEntity("NAME", "Namespace");
		entityService.saveEntity(entity_1);

		Entity entity_2 = this.createEntity("Entity", "Namespace");
		entityService.saveEntity(entity_2);

		List<Entity> entitiesList = Entity.findEntitysByNameLike("name")
				.getResultList();
		Assert.assertTrue(entitiesList.contains(entity_1));
		Assert.assertFalse(entitiesList.contains(entity_2));
	}

	@Test
	public void getEntityByID() {
		entity = this.createEntity("name", "namespace");
		entityService.saveEntity(entity);
		long id = entity.getId();
		Assert.assertEquals(entity, Entity.findEntity(id));
	}

	@Test
	public void listEntitiesUsingInvalidFragmentOfName() {
		String fragmentOfName = "INVALID_FRAGMENT";

		Entity entity_1 = this.createEntity("Bus", "namespace");
		entityService.saveEntity(entity_1);

		Entity entity_2 = this.createEntity("Car", "namespace");
		entityService.saveEntity(entity_2);

		List<Entity> entities = Entity.findEntitysByNameLike(fragmentOfName)
				.getResultList();

		Assert.assertFalse(entities.contains(entity_1));
		Assert.assertFalse(entities.contains(entity_2));
		Assert.assertEquals(0, entities.size());
	}

	@Test(expected = EntityNotFoundException.class)
	public void getEntityWithAnUnknowId() {
		Entity entity_1 = this.createEntity("Bus", "Namespace");
		entityService.saveEntity(entity_1);
		long id = 10;
		Assert.assertNull(Entity.findEntity(id));
	}

}
