package com.nanuvem.lom.model;

import javax.validation.ValidationException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = Entity.class)
public class EntityIntegrationTest {

private Entity entity;

	@Before
	public void init() {
    	entity = new Entity();		
	}
	
    @Test
    public void validNameAndNamespace() {
    	entity.setName("name");
    	entity.setNamespace("namespace");
		entityService.saveEntity(entity);
    }
    
    @Test
    public void withoutNamespace() {
    	entity.setName("name");
		entityService.saveEntity(entity);
    }
    
    @Test
    public void twoEntitiesSameNameDifferentNamespaces() {
    	entity.setName("sameName");
    	entity.setNamespace("namespace1");
		entityService.saveEntity(entity);
		
		Entity entity2 = new Entity();
    	entity2.setName("sameName");
    	entity2.setNamespace("namespace2");
		entityService.saveEntity(entity2);    	
    }

    @Test
    public void nameAndNamespaceWithSpaces() {
    	entity.setName("name with spaces");
    	entity.setNamespace("namespace with spaces");
		entityService.saveEntity(entity);
    }

    @Test(expected=ValidationException.class)
    public void withoutName() {
    	entity.setNamespace("has only namespace");
		entityService.saveEntity(entity);
    }
}
