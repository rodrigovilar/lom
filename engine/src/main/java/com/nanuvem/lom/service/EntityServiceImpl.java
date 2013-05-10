package com.nanuvem.lom.service;

import java.util.List;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import org.springframework.orm.jpa.JpaSystemException;

import com.nanuvem.lom.model.Entity;

public class EntityServiceImpl implements EntityService {

	public List<Entity> findEntitysByNameLike(String name) {
		if (name == null || name.equals("")) {
			return Entity.findAllEntitys();
		}
		return Entity.findEntitysByNameLike(name).getResultList();
	}

	public List<Entity> findEntitysByNamespaceEquals(String namespace) {
		return Entity.findEntitysByNamespaceEquals(namespace).getResultList();
	}

	public List<Entity> findEntitysByNamespaceLike(String namespace) {
		if (namespace == null) {
			return Entity.findEntitysByNamespaceEquals(null).getResultList();
		}

		if (namespace.equals("")) {
			return Entity.findAllEntitys();
		}
		return Entity.findEntitysByNamespaceLike(namespace).getResultList();
	}

	public void saveEntity(Entity entity) {
		try {
			validateName(entity);
			validateNamespace(entity);
			entity.persist();
		} catch (Exception e) {
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
		String namespace = entity.getNamespace();

		if (namespace == null) {
			return;
		}

		if (namespace.equals("")
				|| Pattern.matches("[a-zA-Z0-9 _]+", namespace)) {
			validateNameWithinNamespace(entity);
		} else {
			throw new ValidationException("Invalid characters in namespace");
		}

	}
}