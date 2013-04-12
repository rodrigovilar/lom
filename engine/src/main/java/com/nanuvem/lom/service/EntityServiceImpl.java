package com.nanuvem.lom.service;

import java.util.List;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import org.springframework.orm.jpa.JpaSystemException;

import com.nanuvem.lom.model.Entity;

public class EntityServiceImpl implements EntityService {

	public void saveEntity(Entity entity) {
		try {
			validateName(entity);
			validateNamespace(entity);
			entity.persist();
		} catch (JpaSystemException e) {
			throw new ValidationException(e.getMessage());
		} catch (Exception e){
			throw new ValidationException(e.getMessage());
		}
	}

	private void validateNameWithinNamespace(Entity entity)
			throws JpaSystemException {
		if (entity.getName().equals("") || entity.getName() == null) {
			throw new ValidationException("Without name!");
		}

		List<Entity> entitiesByName = Entity.findEntitysByNameLike(
				entity.getName()).getResultList();

		for (Entity e : entitiesByName) {
			boolean nameIsEqualsIgnoreCase = e.getName().equalsIgnoreCase(
					entity.getName());
			boolean namespaceIsEqualsIgnoreCase = e.getNamespace()
					.equalsIgnoreCase(entity.getNamespace());
			boolean idIsEquals = e.getId() != entity.getId();
			if (nameIsEqualsIgnoreCase && namespaceIsEqualsIgnoreCase
					&& idIsEquals) {
				throw new ValidationException(
						"Entity with same name already exists in this namespace!");
			}
		}

	}

	public void validateName(Entity entity) {
		if (Pattern.matches("[a-zA-Z0-9 _]+", entity.getName())) {
			validateNameWithinNamespace(entity);
		} else {
			throw new ValidationException("Invalid characters in name");
		}
	}

	public void validateNamespace(Entity entity) {
		if (Pattern.matches("[a-zA-Z0-9 _]+", entity.getNamespace())
				|| entity.getNamespace().equals("")) {
			validateNameWithinNamespace(entity);
		} else {
			throw new ValidationException("Invalid characters in namespace");
		}

	}

}
