package com.nanuvem.lom.model;

//import org.aspectj.lang.annotation.Before;
import java.util.List;

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
    	/*Assert.assertEquals(entity.getName(), entity_return.getName());
    	Assert.assertEquals(entity.getNamespace(), entity_return.getNamespace());*/
    	
	}
    
	@Test
	 public void nameAndNamespaceWithSpaces() {
		entity = this.createEntity("name with spaces", "namespace with spaces");
		entity.persist();
		Assert.assertEquals(entity, Entity.findEntity(entity.getId()));
	 }

	 @Test(expected=ValidationException.class)
	 public void withoutName() {
	 	entity.setNamespace("has only namespace");
	 	entity.persist();
	 }
	 
	//Continued by Sinval Vieira
	//TODO Extract Method pra criar uma Entity
    
	 @Test(expected=ValidationException.class)
	 public void twoEntitiesWithSameNameInDefaultPackage(){	    	
		 /*try{*/
		 entity = this.createEntity("samename", "");
		 entity.persist();
		 
		 Entity entity2 = this.createEntity("samename", "");
		 entity2.persist();
		 /*}catch(Exception e){
			 System.err.println(e.getMessage());
		 }*/
	 }
	 
	 @Test(expected=ValidationException.class)
	 public void twoEntitiesWithSameNameInNonDefaultPackage(){
		 entity = this.createEntity("sameName", "sameNamespace");
		 entity.persist();
		 
		 Entity entity2 = this.createEntity("sameName", "sameNamespace");
		 entity2.persist();	
	 }
	 
	 @Test
	 public void nameOrNamespaceWithInvalidChar() throws Exception{
		 entity.setName("wrongname@#$#@");
		 entity.setNamespace("wrongnamespace@@#$%");
		 try{
			 entity.persist();
			 Assert.fail();
		 }catch(ValidationException e){
			 List<Entity> allEntitiesList = Entity.findAllEntitys();
		     Assert.assertFalse(allEntitiesList.contains(entity));			 
		 }
	 }
	 
	 @Test//(expected=ValidationException.class)
	 public void caseInsensitiveName(){
		 entity = this.createEntity("NAME", "namespace");
		 entity.persist();
		 
		 Entity entity2 = this.createEntity("name", "namespace");
		 try{
			 entity2.persist();
			 Assert.fail();
		 }catch(ValidationException e){
			 List<Entity> allEntitiesList = Entity.findAllEntitys();
		     Assert.assertFalse(allEntitiesList.contains(entity2));			 
		 }
		 

		 
	 }
	 
}
