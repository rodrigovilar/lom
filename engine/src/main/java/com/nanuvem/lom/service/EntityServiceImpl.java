package com.nanuvem.lom.service;

import java.util.List;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import com.nanuvem.lom.model.Entity;

public class EntityServiceImpl implements EntityService {

	public void saveEntity(Entity entity) {		
		if (isValidName(entity) == false) {
			throw new ValidationException("Invalid characters in name");
		}

		if (isValidNamespace(entity) == false) {
			throw new ValidationException("Invalid characters in namespace");
		}
		
		entity.persist();
	}

	private static void isValidNameInThisNamespace(String name, String namespace, Entity entity) {
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
		
		}catch(org.springframework.orm.jpa.JpaSystemException e){
			throw new ValidationException("Entity with same name already exists in this namespace!");
		}
	}

	public static boolean isValidName(Entity entity) {
		if (Pattern.matches("[a-zA-Z0-9 _]+", entity.getName())) {
			isValidNameInThisNamespace(entity.getName(), entity.getNamespace(), entity);
			return true;
		} else {
			return false;
		}
	}

	public static boolean isValidNamespace(Entity entity) {
		if (Pattern.matches("[a-zA-Z0-9 _]+", entity.getNamespace())
				|| entity.getNamespace().equals("")) {
			isValidNameInThisNamespace(entity.getName(), entity.getNamespace(), entity);
			return true;
		} else {
			return false;
		}
	}

}
