package com.nanuvem.lom.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import com.nanuvem.lom.model.Entity;


public class EntityServiceImpl implements EntityService {
	
    private static final String DEFAULT_PACKAGE = " ";
    
    public List<Entity> findEntitiesByFragmentOfName(String fragmentOfName){
    	List<Entity> listOfEntitiesByFragmentOfName = new ArrayList<Entity>();
    	
    	for(Entity e : this.findAllEntitys()){
    		CharSequence chars = fragmentOfName.subSequence(0, fragmentOfName.length());
    		if(e.getName().contains(chars)){
    			listOfEntitiesByFragmentOfName.add(e);
    		}
    	}
    	if(listOfEntitiesByFragmentOfName.size() == 0){
    		throw new ValidationException("The fragmente is invalid!");
    	}
    	return listOfEntitiesByFragmentOfName;
    }
    
    public List<Entity> findEntitiesByFragmentOfNamespace(String fragmentOfNamespace){
    	List<Entity> listOfEntitiesByFragmentOfNamespace = new ArrayList<Entity>();
    	
    	for(Entity e : this.findAllEntitys()){
    		CharSequence chars = fragmentOfNamespace.subSequence(0, fragmentOfNamespace.length());
    		if(e.getNamespace().contains(chars)){
    			listOfEntitiesByFragmentOfNamespace.add(e);
    		}
    	}
    	
    	return listOfEntitiesByFragmentOfNamespace;
	}
    
    public List<Entity> findEntitiesByEmptyName(){
    	List<Entity> listOfEntitiesByEmptyName = new ArrayList<Entity>();
    	for (Entity e : this.findAllEntitys()){
    		
    		if(e.getName().equals(" ")){
    			listOfEntitiesByEmptyName.add(e);
    		}
    	}
    	return listOfEntitiesByEmptyName;
    }
    
    public List<Entity> findEntitiesByEmptyNamespace(){
    	List<Entity> listOfEntitiesByEmptyName = new ArrayList<Entity>();
    	for (Entity e : this.findAllEntitys()){
    		if(e.getNamespace().isEmpty()){
    			listOfEntitiesByEmptyName.add(e);
    		}
    	}
    	return listOfEntitiesByEmptyName;
    }
    
    public List<Entity> findEntitiesByNameWithSpace(){
    	
    	List<Entity> listOfEntitiesByEmptyName = new ArrayList<Entity>();
    	for (Entity e : this.findAllEntitys()){
    		if(e.getName().contains(" ")){
    			listOfEntitiesByEmptyName.add(e);
    		}
    	}
    	return listOfEntitiesByEmptyName;
    }
    
    public List<Entity> findEntitiesByNamespaceWithSpace(){
    	List<Entity> listOfEntitiesByEmptyName = new ArrayList<Entity>();
    	for (Entity e : this.findAllEntitys()){
    		if(e.getNamespace().contains(" ")){
    			listOfEntitiesByEmptyName.add(e);
    		}
    	}
    	return listOfEntitiesByEmptyName;
    }
    
    public Entity findEntity(Long id) {
    	Entity e = entityDAO.findOne(id);
        if(e == null){
        	throw new EntityNotFoundException("Entity not found with the Id!");
        }
        return e;
    }
    
    
	public void saveEntity(Entity entity) {
    	verifyDefaultPackage(entity);
    	try{
    		
    		boolean isNameValid = this.verifyWrongCharsInString(entity.getName());
    		boolean isNamespaceValid = this.verifyWrongCharsInString(entity.getNamespace());
    		
    		if(isNameValid == false || isNamespaceValid == false){
    			throw new ValidationException("Invalid Name or Namespace!");
    		}
    		
    		verifyEntityNameAlreadyRegistered(entity);
    		
    		
    		entityDAO.save(entity);
    		
    		
    	}catch(Exception e) {
    		throw new ValidationException(e.getMessage());
    	}
    }

	private void verifyEntityNameAlreadyRegistered(Entity entity) {
		String entityName = entity.getName();
		String entityNamespace = entity.getNamespace();
		
		List<Entity> listOfAllEntities = entityDAO.findAll();
		
		for(Entity e:listOfAllEntities){
			boolean isSameNameIgnoreCase = entityName.equalsIgnoreCase(e.getName());
			boolean isSameNamespace = entityNamespace.equalsIgnoreCase(e.getNamespace());
			if(isSameNameIgnoreCase && isSameNamespace){
				throw new ValidationException("Entity already registered!");
			}
			
		}
	}

	private void verifyDefaultPackage(Entity entity) {
		if(entity.getNamespace() == null){
    		entity.setNamespace(DEFAULT_PACKAGE);
    	}
	}
	
	private boolean verifyWrongCharsInString(String string){
		for(int i = 0; i < string.length();++i){
			char ch = string.charAt(i);
			
			if(Character.isLetterOrDigit(ch) || 
			   Character.isSpaceChar(ch) || 
			   String.valueOf(ch).equals("_")){
				continue;
			}else if(Character.isDefined(ch)){
				return false;
			}
		}
		
		return true;
	}
    
    
}
