package com.nanuvem.lom.service;

import java.util.List;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import org.springframework.orm.jpa.JpaSystemException;

import com.nanuvem.lom.dao.EntityDAO;
import com.nanuvem.lom.dao.relational.RelationalEntityDAO;
import com.nanuvem.lom.dao.typesquare.Entity;
import com.nanuvem.lom.dao.typesquare.TypeSquareEntityDAO;

public class EntityServiceImpl implements EntityService {

	private EntityDAO dao = new TypeSquareEntityDAO();
	private RelationalEntityDAO relationalDao = new RelationalEntityDAO();

	public List<Entity> findEntitysByNameLike(String name) {
		if (name == null || name.equals("")) {
			return dao.findAllEntities();
		}
		return dao.findEntitiesByNameLike(name);
	}

	public List<Entity> findEntitysByNamespaceEquals(String namespace) {
		return dao.findEntitiesByNamespaceEquals(namespace);
	}

	public List<Entity> findEntitysByNamespaceLike(String namespace) {
		if (namespace == null) {
			return dao.findEntitiesByNamespaceEquals(null);
		}

		if (namespace.equals("")) {
			return dao.findAllEntities();
		}
		return dao.findEntitiesByNamespaceLike(namespace);
	}

	public void saveEntity(Entity entity) {
		try {
			validateName(entity);
			validateNamespace(entity);
			// dao.saveEntity(entity);
			relationalDao.saveEntity(entity);
		} catch (Exception e) {
			throw new ValidationException(e.getMessage());
		}
	}

	private void validateNameWithinNamespace(Entity entity)
			throws JpaSystemException {
		if (entity.getName().equals("") || entity.getName() == null) {
			throw new ValidationException("Without name!");
		}

		List<Entity> entitiesByName = dao.findEntitiesByNameLike(entity
				.getName());

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

	public long countAllEntitys() {
		return dao.countEntities();
	}

	public void deleteEntity(Entity entity) {
		dao.removeEntity(entity);
	}

	public Entity findEntity(Long id) {
		return dao.findEntity(id);
	}

	public List<Entity> findAllEntitys() {
		return dao.findAllEntities();
	}

	public List<Entity> findEntityEntries(int firstResult, int maxResults) {
		return dao.findEntityEntries(firstResult, maxResults);
	}

	public Entity updateEntity(Entity entity) {
		return dao.update(entity);
	}

}