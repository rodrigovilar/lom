package com.nanuvem.lom.kernel.dao.memory;

import java.util.ArrayList;
import java.util.List;

import com.nanuvem.lom.kernel.Entity;
import com.nanuvem.lom.kernel.MetadataException;
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
		return new ArrayList<Entity>(this.entities);
	}

	public Entity update(String namespace, String name, Long id, Integer version) {
		for (Entity e : this.listAll()) {
			if (e.getId().equals(id)) {
				e.setName(name);
				e.setNamespace(namespace);
				e.setVersion(version);
				return e;
			}
		}
		throw new MetadataException("Entity does not exits!");
	}

	public Entity update(Entity entity) {
		for (Entity e : this.listAll()) {
			if (e.getId().equals(entity.getId())) {
				this.entities.remove(e);
				this.entities.add(entity);
				return entity;
			}
		}
		throw new MetadataException("Entity does not exits!");
	}

	public Entity findEntityById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Entity> listEntitiesByFragmentOfNameAndPackage(
			String namespaceFragment, String nameFragment) {
		List<Entity> results = new ArrayList<Entity>();
		for (Entity e : this.entities) {
			if (e.getNamespace().toLowerCase()
					.contains(namespaceFragment.toLowerCase())
					&& e.getName().toLowerCase()
							.contains(nameFragment.toLowerCase())) {
				results.add(e);
			}
		}
		return results;
	}

	public Entity readEntityByNamespaceAndName(String namespace, String name) {
		for (Entity e : this.entities) {
			if (e.getNamespace().equalsIgnoreCase(namespace)
					&& e.getName().equalsIgnoreCase(name)) {
				return e;
			}
		}
		return null;
	}

	public void delete(String namespaceAndName) {
		// TODO Auto-generated method stub

	}

	public void delete(Entity entity) {
		for (int i = 0; i < this.entities.size(); i++) {
			Entity e = this.entities.get(i);
			if (e.getName().equals(entity.getName())
					&& e.getNamespace().equals(entity.getNamespace())) {
				this.entities.remove(e);
			}
		}

	}

}
