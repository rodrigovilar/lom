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

	private void validateEntity(Entity entity) {
		if (entity.getName() == null || entity.getName().equals("")) {
			throw new MetadataException("The name of an Entity is mandatory");
		}

		if (entity.getNamespace() == null || entity.getNamespace().equals("")) {
			entity.setNamespace("default");
		}

		if (!Pattern.matches("[a-zA-Z1-9.]{1,}", entity.getNamespace())) {
			throw new MetadataException("Invalid value for Entity namespace: "
					+ entity.getNamespace());
		}

		if (!Pattern.matches("[a-zA-Z1-9]{1,}", entity.getName())) {
			throw new MetadataException("Invalid value for Entity name: "
					+ entity.getName());
		}

		String readEntityQuery = entity.getNamespace() + "." + entity.getName();
		Entity found = null;
		try {
			found = this.readEntity(readEntityQuery);
		} catch (MetadataException me) {
			found = null;
		}

		if (found != null && found.getName().equals(entity.getName())
				&& found.getNamespace().equals(entity.getNamespace())) {
			StringBuilder message = new StringBuilder();
			message.append("The ");
			if (!entity.getNamespace().equals("default")) {
				message.append(entity.getNamespace());
				message.append(".");
			}
			message.append(entity.getName());
			message.append(" entity already exists");
			throw new MetadataException(message.toString());
		}
	}

	public void create(Entity entity) {
		this.validateEntity(entity);
		this.dao.create(entity);
	}

	public List<Entity> listAll() {
		return this.dao.listAll();
	}

	public List<Entity> listEntitiesByFragmentOfNameAndPackage(
			String namespaceFragment, String nameFragment) {
		if (namespaceFragment == null) {
			namespaceFragment = "";
		}

		if (nameFragment == null) {
			nameFragment = "";
		}

		if (!Pattern.matches("[a-zA-Z1-9.]{1,}", namespaceFragment)
				&& !namespaceFragment.isEmpty()) {
			throw new MetadataException("Invalid value for Entity namespace: "
					+ namespaceFragment);
		}

		if (!Pattern.matches("[a-zA-Z1-9]{1,}", nameFragment)
				&& !nameFragment.isEmpty()) {
			throw new MetadataException("Invalid value for Entity name: "
					+ nameFragment);
		}

		return this.dao.listEntitiesByFragmentOfNameAndPackage(
				namespaceFragment, nameFragment);
	}

	public Entity readEntity(String string) {
		String namespace = null;
		String name = null;

		if (string.contains(".")) {
			namespace = string.substring(0, string.lastIndexOf("."));
			name = string.substring(string.lastIndexOf(".") + 1,
					string.length());
		} else {
			namespace = "default";
			name = string;
		}

		if (!Pattern.matches("[a-zA-Z1-9.]{1,}", namespace)
				&& !namespace.isEmpty()) {
			if (string.startsWith(".")) {
				string = string.substring(1);
			}
			if (string.endsWith(".")) {
				string = string.substring(0, string.length() - 1);
			}
			throw new MetadataException("Invalid key for Entity: " + string);
		}

		if (!Pattern.matches("[a-zA-Z1-9]{1,}", name) && !name.isEmpty()) {
			if (string.startsWith(".")) {
				string = string.substring(1);
			}
			if (string.endsWith(".")) {
				string = string.substring(0, string.length() - 1);
			}
			throw new MetadataException("Invalid key for Entity: " + string);
		}

		if (namespace.isEmpty()) {
			namespace = "default";
		}
		
		Entity entityByNamespaceAndName = this.dao
				.readEntityByNamespaceAndName(namespace, name);

		if (entityByNamespaceAndName == null) {
			if (string.startsWith(".")) {
				string = string.substring(1);
			}
			if (string.endsWith(".")) {
				string = string.substring(0, string.length() - 1);
			}
			throw new MetadataException("Entity not found: " + string);
		}
		return entityByNamespaceAndName;
	}

	public void delete(String string) {
		Entity readEntity = this.readEntity(string);
		this.dao.delete(readEntity);
	}

	public Entity update(Entity updateEntity) {
		this.validateEntity(updateEntity);
		return this.dao.update(updateEntity);
	}

	public void delete(Entity entity) {
		this.dao.delete(entity);
	}

	public Entity update(String namespace, String name, Long id, Integer version) {
		Entity updateEntity = new Entity();
		updateEntity.setId(id);
		updateEntity.setName(name);
		updateEntity.setNamespace(namespace);
		updateEntity.setVersion(version);
		this.validateEntity(updateEntity);
		return this.dao.update(namespace, name, id, version);
	}

}
