package com.nanuvem.lom.kernel;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nanuvem.lom.kernel.dao.DaoFactory;
import com.nanuvem.lom.kernel.dao.EntityDao;

public class EntityService {

	private EntityDao dao;

	public EntityService(DaoFactory factory) {
		this.dao = factory.createEntityDao();
	}

	public void create(Entity entity) {
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
		Entity found = this.readEntity(readEntityQuery);

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

		this.dao.create(entity);
	}

	public List<Entity> listAll() {
		return this.dao.listAll();
	}

	public List<Entity> listEntitiesByFragmentOfNameAndPackage(
			String namespaceFragment, String nameFragment) {
		// TODO Auto-generated method stub
		return null;
	}

	public Entity readEntity(String string) {
		String namespace = string.substring(0, string.lastIndexOf("."));
		String name = string.substring(string.lastIndexOf(".") + 1,
				string.length());
		return this.dao.readEntityByNamespaceAndName(namespace, name);
	}

	public void delete(String string) {
		// TODO Auto-generated method stub
	}

	public Entity update(Entity updateEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(Entity entity) {
		this.dao.delete(entity);
	}

}
