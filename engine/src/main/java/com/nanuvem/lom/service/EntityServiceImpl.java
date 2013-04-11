package com.nanuvem.lom.service;

import java.util.List;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import com.nanuvem.lom.model.Entity;

public class EntityServiceImpl implements EntityService {

	public void saveEntity(Entity entity) {		
		if (isNameValid(entity) == false) {
			throw new ValidationException("Invalid characters in name");
		}

		if (isNamespaceValid(entity) == false) {
			throw new ValidationException("Invalid characters in namespace");
		}
		
		entity.persist();
	}

	private static boolean isNameValidInThisNamespace(Entity entity) {
		if (entity.getName().equals("") || entity.getName() == null) {
			throw new ValidationException("Without name!");
		}
	
		try{
			List<Entity> entitiesByName = Entity.findEntitysByNameLike(entity.getName())
					.getResultList();
			if (entitiesByName.size() > 0) {
				for (Entity e : entitiesByName) {
					if (e.getName().equalsIgnoreCase(entity.getName())
							&& e.getNamespace().equalsIgnoreCase(entity.getNamespace())
							&& e.getId() != entity.getId()) {
						throw new ValidationException(
								"Entity with same name already exists in this namespace!");
					}
				}
			}
			return true;
		}catch(org.springframework.orm.jpa.JpaSystemException e){
			throw new ValidationException("Entity with same name already exists in this namespace!");
		}
	}

	public static boolean isNameValid(Entity entity) {
		if (Pattern.matches("[a-zA-Z0-9 _]+", entity.getName())) {
			isNameValidInThisNamespace(entity);
			return true;
		} 
		return false;
	}

	public static boolean isNamespaceValid(Entity entity) {
		if (Pattern.matches("[a-zA-Z0-9 _]+", entity.getNamespace())
				|| entity.getNamespace().equals("")) {
			return isNameValidInThisNamespace(entity);
		} else {
			return false;
		}
	}

}
