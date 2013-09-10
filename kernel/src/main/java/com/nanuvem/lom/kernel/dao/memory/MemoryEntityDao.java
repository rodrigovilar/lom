package com.nanuvem.lom.kernel.dao.memory;

import java.util.ArrayList;
import java.util.List;

import com.nanuvem.lom.kernel.Entity;
import com.nanuvem.lom.kernel.dao.EntityDao;

public class MemoryEntityDao implements EntityDao {

	private Long id = 1L;

	private List<Entity> entities = new ArrayList<Entity>();

	public void create(Entity entity) {
		entity.setId(id++);
		entity.setVersion(0);
		entities.add(entity);
	}

	public List<Entity> listAll() {
		return entities;
	}

	public void remove(Entity e) {
		for (Entity entity : entities) {
			if (entity.getId().equals(e.getId())) {
				entities.remove(entity);
				return;
			}
		}
	}

	public Entity update(String namespace, String name, Long id, Integer version) {
		for (Entity entity : entities) {
			if (entity.getId().equals(id)) {
				Entity e = new Entity();
				e.setName(name);
				e.setNamespace(namespace);
				e.setVersion(version + 1);
				e.setId(id);
				entities.remove(entity);
				entities.add(e);
				return e;
			}
		}
		return null;
	}

	public Entity update(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	public Entity findEntityById(Long id) {
		for (Entity entity : entities) {
			if (entity.getId().equals(id)) {
				return entity;
			}
		}
		return null;

	}

	public List<Entity> listEntitiesByFragmentOfNameAndPackage(
			String namespaceFragment, String nameFragment) {
		List<Entity> entities = new ArrayList<Entity>();
		boolean entityNamespaceContainsFragmentLike = false;
		boolean entityNameContainsFragmentLike = false;
		
		if (namespaceFragment == null)
			namespaceFragment = "";

		if (nameFragment == null)
			nameFragment = "";

		for (Entity e : this.listAll()) {
			entityNamespaceContainsFragmentLike = e.getNamespace()
					.toLowerCase().contains(namespaceFragment.toLowerCase());

			entityNameContainsFragmentLike = e.getName().toLowerCase()
					.contains(nameFragment.toLowerCase());

			if (entityNamespaceContainsFragmentLike
					&& entityNameContainsFragmentLike)
				entities.add(e);
		}
		return entities;
	}

	public Entity readEntityByNamespaceAndName(String namespace, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteEntity(String namespaceAndName) {
		// TODO Auto-generated method stub
		
	}
}
