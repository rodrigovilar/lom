package com.nanuvem.lom.service;

import java.util.List;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import org.springframework.orm.jpa.JpaSystemException;

import com.nanuvem.lom.model.Entity;

public class EntityServiceImpl implements EntityService {

	public void saveEntity(Entity entity) {
		try {
			isNameValid(entity);
			isNamespaceValid(entity);
			entity.persist();
		} catch (JpaSystemException e) {
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
		if (entitiesByName.size() > 0) {
			for (Entity e : entitiesByName) {
				if (e.getName().equalsIgnoreCase(entity.getName())
						&& e.getNamespace().equalsIgnoreCase(
								entity.getNamespace())
						&& e.getId() != entity.getId()) {
					throw new ValidationException(
							"Entity with same name already exists in this namespace!");
				}
			}
		}

	}

	public void isNameValid(Entity entity) {
		if (Pattern.matches("[a-zA-Z0-9 _]+", entity.getName())) {
			validateNameWithinNamespace(entity);
		} else {
			throw new ValidationException("Invalid characters in name");
		}
	}

	public void isNamespaceValid(Entity entity) {
		if (Pattern.matches("[a-zA-Z0-9 _]+", entity.getNamespace())
				|| entity.getNamespace().equals("")) {
			validateNameWithinNamespace(entity);
		} else {
			throw new ValidationException("Invalid characters in name");
		}

	}

}
