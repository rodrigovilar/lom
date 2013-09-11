package com.nanuvem.lom.kernel;

import java.util.List;
import java.util.regex.Pattern;

import com.nanuvem.lom.kernel.dao.DaoFactory;
import com.nanuvem.lom.kernel.dao.EntityDao;

public class EntityService {

	private EntityDao dao;

	public EntityService(DaoFactory factory) {
		this.dao = factory.createEntityDao();
	}

	public void create(Entity entity) {
		if (this.validateEntity(entity)) {
			dao.create(entity);
		}
	}

	private boolean validateEntity(Entity entity) {
		if (entity.getName() == null || entity.getName().equals("")) {
			throw new MetadataException("The name of an Entity is mandatory");
		}
		validateName(entity);
		validateNamespace(entity);
		return true;
	}

	public List<Entity> listAll() {
		return dao.listAll();
	}

	public void remove(Entity entity) {
		dao.remove(entity);
	}

	public Entity update(String namespace, String name, Long id, Integer version) {
		return dao.update(namespace, name, id, version);
	}

	public Entity update(Entity entity) {
		return dao.update(entity);
	}

	public Entity findEntityById(Long id) {
		return dao.findEntityById(id);
	}

	public List<Entity> listEntitiesByFragmentOfNameAndPackage(
			String namespaceFragment, String nameFragment) {
		return dao.listEntitiesByFragmentOfNameAndPackage(namespaceFragment,
				nameFragment);
	}
	//TODO - refactoring of validations
	public Entity readEntityByNamespaceAndName(String namespace, String name) {
		if (!(Pattern.matches("[a-zA-Z0-9_]+", name) && Pattern.matches(
				"[a-zA-Z0-9_]+", namespace))) {
			StringBuilder builder = new StringBuilder();
			builder.append("Invalid key for Entity: ");
			if (!namespace.isEmpty()) {
				builder.append(namespace);
			}
			if (!namespace.isEmpty() && !name.isEmpty()){
				builder.append(".");
			}
			throw new MetadataException(builder.toString()+name);
		}
		
		Entity entity = dao.readEntityByNamespaceAndName(namespace, name);
		if (entity != null){
			return entity;
		}

		StringBuilder builder = new StringBuilder();
		builder.append("Entity not found: ");
		if (namespace.length() > 0) {
			builder.append(namespace);
		}
		if (!namespace.isEmpty() && !name.isEmpty()){
			builder.append(".");
		}
		throw new MetadataException(builder.toString() + name);
	
	}

	public void deleteEntity(String namespaceAndName) {
		dao.deleteEntity(namespaceAndName);
	}

	private static boolean safeEquals(String a, String b) {
		if (a == null && b == null) {
			return true;
		}

		if (a == null) {
			return false;
		}

		return a.equalsIgnoreCase(b);
	}

	private void validateNameWithinNamespace(Entity entity) {
		List<Entity> entitiesByName = dao.listAll();

		for (Entity e : entitiesByName) {
			boolean nameIsEqualsIgnoreCase = entity.getName().equalsIgnoreCase(
					e.getName());
			boolean namespaceIsEqualsIgnoreCase = safeEquals(e.getNamespace(),
					entity.getNamespace());
			boolean idNotEquals = e.getId() != entity.getId();
			if (nameIsEqualsIgnoreCase && namespaceIsEqualsIgnoreCase
					&& idNotEquals) {
				throw new MetadataException(
						"Entity with same name already exists in this namespace!");
			}
		}

	}

	public void validateName(Entity entity) {
		if (Pattern.matches("[a-zA-Z0-9_]+", entity.getName())) {
			validateNameWithinNamespace(entity);
		} else {
			throw new MetadataException("Invalid value for Entity name: "
					+ entity.getName());
		}
	}

	public void validateNamespace(Entity entity) {
		String namespace = entity.getNamespace();

		if (namespace == null) {
			entity.setNamespace("");
			return;
		}
		if (namespace.equals("") || Pattern.matches("[a-zA-Z0-9_]+", namespace)) {
			validateNameWithinNamespace(entity);
		} else {
			throw new MetadataException("Invalid value for Entity namespace: "
					+ entity.getNamespace());
		}
	}
}
