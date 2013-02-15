package com.nanuvem.lom.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import junit.framework.Assert;

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
    	entity.setName("samename");
    	entity.setNamespace("namespace1");
		entityService.saveEntity(entity);
		
		Entity entity2 = new Entity();
    	entity2.setName("samename");
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
    
    
    
    //Continued by Sinval Vieira
    //TODO Extract Method pra criar uma Entity
    
    @Test(expected=ValidationException.class)
    public void twoEntitiesWithSameNameInDefaultPackage(){
    	String name = "samename";
    	
    	entity.setName(name);
    	entity.setNamespace("");
    	entityService.saveEntity(entity);
    	
    	
    	Entity entity2 = new Entity();
    	entity2.setName(name);
    	entity2.setNamespace("");
    	entityService.saveEntity(entity2);
    	
    }
    
    @Test(expected=ValidationException.class)
    public void twoEntitiesWithSameNameInNonDefaultPackage(){
    	String namespace = "samenamespace";
    	String name = "sameName";
    	
    	entity.setName(name);
    	entity.setNamespace(namespace);
    	entityService.saveEntity(entity);
    	
    	
    	Entity entity2 = new Entity();
    	entity2.setName(name);
    	entity2.setNamespace(namespace);
    	entityService.saveEntity(entity2);	
    }
    
    @Test//(expected=ValidationException.class)
    public void nameOrNamespaceWithInvalidChar() throws Exception{
    	entity.setName("wrongname");
    	entity.setNamespace("wrongnamespace@@#$%");
    	try{
    		entityService.saveEntity(entity);
    		Assert.fail();
    	}catch(ValidationException e){
    		List<Entity> allEntitiesList = entityService.findAllEntitys();
        	Assert.assertFalse(allEntitiesList.contains(entity));
    	}
    	
    }
    
    
    @Test(expected=ValidationException.class)
    public void caseInsensitiveName(){
    	Entity entity_1 = new Entity();
    	entity_1.setName("NAME");
    	entity_1.setNamespace("namespace");
    	entityService.saveEntity(entity_1);
    	
    	Entity entity_2 = new Entity();
    	entity_2.setName("name");
    	entity_2.setNamespace("namespace");
    	entityService.saveEntity(entity_2);
    }
    
    @Test(expected=ValidationException.class)
    public void caseInsensitiveNamespace(){
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
    public void updateNamespaceValidation() {
    	entity.setName("firstname");
    	entity.setNamespace("firstnamespace");
		entityService.saveEntity(entity);
    	Entity found = entityService.findEntity(entity.getId());
    	found.setNamespace("secondnamespace");
    	entityService.updateEntity(found);   	
    	Assert.assertEquals("secondnamespace", entity.getNamespace());
    }
    
    @Test
    public void setPackage() {
    	entity.setName("samename");
    	entity.setNamespace("namespacea");
		entityService.saveEntity(entity);
    	Entity found = entityService.findEntity(entity.getId());
    	Assert.assertEquals("namespacea", found.getNamespace());
    }
    
    @Test
    public void removePackage() {
    	entity.setName("firstname");
    	entity.setNamespace("firstnamespace");
    	entityService.saveEntity(entity);
    	Entity found = entityService.findEntity(entity.getId());
    	found.setNamespace("");
    	entityService.updateEntity(found);
    	Assert.assertEquals("", entity.getNamespace());
    }

    @Test
    public void renameTwoEntityWithSameNameInDifferentPackages() {
    	entity.setName("firstname");
    	entity.setNamespace("firstnamespace");
    	entityService.saveEntity(entity);
    	Entity otherEntity = new Entity();
    	otherEntity.setName("secondname");
    	otherEntity.setNamespace("secondnamespace");
    	entityService.saveEntity(otherEntity);
    	Entity found = entityService.findEntity(otherEntity.getId());
    	found.setName("firstname");
    	entityService.updateEntity(otherEntity);
    	Assert.assertEquals("firstname", entity.getName());
    	Assert.assertEquals("firstnamespace", entity.getNamespace());
    	Assert.assertEquals("firstname", otherEntity.getName());
    	Assert.assertEquals("secondnamespace", otherEntity.getNamespace());
    }
    
    @Test //(expected=ValidationException.class)
    public void changeNameWithSpace() {
    	entity.setName("firstname");
    	entity.setNamespace("firstnamespace");
    	entityService.saveEntity(entity);
    	Entity found = entityService.findEntity(entity.getId());
    	found.setName("name with spaces");
    	
    }
    
    @Test//(expected=ValidationException.class)
    public void changePackageWithSpace() {
    	entity.setName("firstname");
    	entity.setNamespace("firstnamespace");
    	entityService.saveEntity(entity);
    	Entity found = entityService.findEntity(entity.getId());
    	found.setNamespace("namespace with spaces");
    	Assert.assertEquals("namespace with spaces", found.getNamespace());
    }

    @Test (expected=ValidationException.class)
    public void removeName() {
    	entity.setName("firstname");
    	entity.setNamespace("firstnamespace");
    	entityService.saveEntity(entity);
    	Entity found = entityService.findEntity(entity.getId());
    	found.setName("");
    }
    
    @Test (expected=ValidationException.class)
    public void removeCausingTwoEntitiesWithSameNameInDefaultPackage() {
    	entity.setName("firstname");
    	entity.setNamespace("firstnamespace");
    	entityService.saveEntity(entity);
    	Entity other = new Entity();
    	other.setName("firstname");
    	other.setNamespace("");
    	entityService.saveEntity(other);
    	entity.setNamespace("");
    	entityService.saveEntity(entity);
    }

    @Test (expected=ValidationException.class)
    public void removeCausingTwoEntitiesWithSameNameInSomeNotDefaultPackage() {
    	entity.setName("firstname");
    	entity.setNamespace("firstnamespace");
    	entityService.saveEntity(entity);
    	Entity other = new Entity();
    	other.setName("firstname");
    	other.setNamespace("firstnamespace");
    	entityService.saveEntity(other);
    	
    }

    @Test (expected=ValidationException.class)
    public void setNameWithSymbol() {
    	entity.setName("first*Name");
    }
    
    @Test (expected=ValidationException.class)
    public void setNameWithCedilla() {
    	entity.setName("firstçName");
    }

    //Temos uma dúvida quanto a estes testes
    
   /* @Test (expected=ValidationException.class)
    public void setNameUsingSensitiveCase() {
    	entity.setName("firstName");
    }
    
    @Test (expected=ValidationException.class)
    public void setPackageUsingSensitiveCase() {
    	entity.setNamespace("nameSpace");
    }*/
    
    @Test
    public void listAllEntities(){
    	Entity entity_1 = new Entity();
    	entity_1.setName("entity_1");
    	entity_1.setNamespace("namespace_entity_1");
    	entityService.saveEntity(entity_1);
    	
    	Entity entity_2 = new Entity();
    	entity_2.setName("entity_2");
    	entity_2.setNamespace("namespace_entity_2");
    	entityService.saveEntity(entity_2);
    	
    	
    	List<Entity> allEntitiesList = entityService.findAllEntitys();
    	
    	Assert.assertEquals(allEntitiesList.get(0).getName(), entity_1.getName());
    	Assert.assertEquals(allEntitiesList.get(1).getName(), entity_2.getName());
    	Assert.assertTrue(allEntitiesList.contains(entity_1));
    	Assert.assertTrue(allEntitiesList.contains(entity_2));
    }
    
    @Test
    public void listEntitiesByValidFragmentOfName(){
    	String fragmentOfName = "Ca";
    	
    	Entity entity_1 = new Entity();
    	entity_1.setName("Caminhao");
    	entity_1.setNamespace("namespace_entity_1");
    	entityService.saveEntity(entity_1);
    	
    	Entity entity_2 = new Entity();
    	entity_2.setName("Carro");
    	entity_2.setNamespace("namespace_entity_2");
    	entityService.saveEntity(entity_2);
    	
    	Entity entity_3 = new Entity();
    	entity_3.setName("Moto");
    	entity_3.setNamespace("namespace_entity_3");
    	entityService.saveEntity(entity_3);
    	
    	
    	List<Entity> allEntitiesList = 
    			entityService.findEntitiesByFragmentOfName(fragmentOfName);
    	
    	Assert.assertTrue(allEntitiesList.contains(entity_1));
    	Assert.assertTrue(allEntitiesList.contains(entity_2));
    	Assert.assertFalse(allEntitiesList.contains(entity_3));
    	Assert.assertEquals(2, allEntitiesList.size());
    }
    
    @Test
    public void listEntitiesByValidFragmentOfNamespace(){
    	String fragmentOfNamespace = "Pa";
    	
    	Entity entity_1 = new Entity();
    	entity_1.setName("Entity_1");
    	entity_1.setNamespace("Packet_Name");
    	entityService.saveEntity(entity_1);
    	
    	Entity entity_2 = new Entity();
    	entity_2.setName("Entity_2");
    	entity_2.setNamespace("Packet_Name");
    	entityService.saveEntity(entity_2);
    	
    	Entity entity_3 = new Entity();
    	entity_3.setName("Entity_3");
    	entity_3.setNamespace("Namespace");
    	entityService.saveEntity(entity_3);
    	
    	
    	List<Entity> entities = 
    			entityService.findEntitiesByFragmentOfNamespace(fragmentOfNamespace);
    	
    	Assert.assertTrue(entities.contains(entity_1));
    	Assert.assertTrue(entities.contains(entity_2));
    	Assert.assertFalse(entities.contains(entity_3));
    	Assert.assertEquals(2, entities.size());
    	
    }
    
    @Test
    public void listEntitiesByEmptyName(){
    	
    	Entity entity_1 = new Entity();
    	entity_1.setName(" ");
    	entity_1.setNamespace("namespace_entity_1");
    	entityService.saveEntity(entity_1);
    	
    	Entity entity_2 = new Entity();
    	entity_2.setName("Entity");
    	entity_2.setNamespace("namespace_entity_2");
    	entityService.saveEntity(entity_2);
    	List<Entity> entities = 
    			entityService.findEntitiesByEmptyName();
    	
    	Assert.assertTrue(entities.contains(entity_1));
    	Assert.assertFalse(entities.contains(entity_2));
    }
    
    @Test
    public void listEntitiesByEmptyNamespace(){
    	Entity entity_1 = new Entity();
    	entity_1.setName("name");
    	entity_1.setNamespace("");
    	entityService.saveEntity(entity_1);
    	
    	Entity entity_2 = new Entity();
    	entity_2.setName("Entity");
    	entity_2.setNamespace("Entity_namespace");
    	entityService.saveEntity(entity_2);
    	List<Entity> entities = 
    			entityService.findEntitiesByEmptyNamespace();
    	
    	Assert.assertTrue(entities.contains(entity_1));
    	Assert.assertFalse(entities.contains(entity_2));
    	
    }
    
    
    @Test
    public void listEntitiesByFragmentOfNameWithSpaces(){
    	Entity entity_1 = new Entity();
    	entity_1.setName("Name with spaces");
    	entity_1.setNamespace("");
    	entityService.saveEntity(entity_1);
    	
    	Entity entity_2 = new Entity();
    	entity_2.setName("Entity");
    	entity_2.setNamespace("");
    	entityService.saveEntity(entity_2);
    	List<Entity> entities = 
    			entityService.findEntitiesByNameWithSpace();
    	
    	Assert.assertTrue(entities.contains(entity_1));
    	Assert.assertFalse(entities.contains(entity_2));
    	
    }
    
    @Test
    public void listEntitiesByFragmentOfNamespaceWithSpaces(){
    	Entity entity_1 = new Entity();
    	entity_1.setName("Name");
    	entity_1.setNamespace("Namespace with spaces");
    	entityService.saveEntity(entity_1);
    	
    	Entity entity_2 = new Entity();
    	entity_2.setName("Entity");
    	entity_2.setNamespace("");
    	entityService.saveEntity(entity_2);
    	List<Entity> entities = 
    			entityService.findEntitiesByNamespaceWithSpace();
    	
    	Assert.assertTrue(entities.contains(entity_1));
    	Assert.assertFalse(entities.contains(entity_2));
    	
    }
    
    @Test
    public void listEntitiesForcingCaseIncensitiveName(){
    	Entity entity_1 = new Entity();
    	entity_1.setName("Name");
    	entity_1.setNamespace("Namespace with spaces");
    	entityService.saveEntity(entity_1);
    	
    	Entity entity_2 = new Entity();
    	entity_2.setName("Entity");
    	entity_2.setNamespace("");
    	entityService.saveEntity(entity_2);
    	List<Entity> entities = 
    			entityService.findEntitiesByNamespaceWithSpace();
    	
    	Assert.assertTrue(entities.contains(entity_1));
    	Assert.assertFalse(entities.contains(entity_2));
    	
    	
    }
    
    
    @Test
    public void getEntityByID(){
    	entity.setName("name");
    	entity.setNamespace("namespace");
    	entityService.saveEntity(entity);
    	long id = entity.getId();
    	Assert.assertEquals(entity, entityService.findEntity(id));
    }
    
    
    
    @Test(expected=ValidationException.class)
    public void listEntitiesUsingInvalidFragmentOfName(){
    	String fragmentOfName = "INVALID_FRAGMENT";
    	
    	Entity entity_1 = new Entity();
    	entity_1.setName("Bus");
    	entity_1.setNamespace("namespace_entity_1");
    	entityService.saveEntity(entity_1);
    	
    	Entity entity_2 = new Entity();
    	entity_2.setName("Car");
    	entity_2.setNamespace("namespace_entity_2");
    	entityService.saveEntity(entity_2);
    	
    	
    	List<Entity> entities = 
    			entityService.findEntitiesByFragmentOfName(fragmentOfName);
    	
    	Assert.assertFalse(entities.contains(entity_1));
    	Assert.assertFalse(entities.contains(entity_2));
    	Assert.assertEquals(0, entities.size());
    }
    
    @Test(expected=EntityNotFoundException.class)
    public void getEntityWithAnUnknowId(){
    	Entity entity_1 = new Entity();
    	entity_1.setName("bus");
    	entity_1.setNamespace("namespace_entity_1");
    	entityService.saveEntity(entity_1);
    	long id = 0;
    	entityService.findEntity(id);
    }
   
}
