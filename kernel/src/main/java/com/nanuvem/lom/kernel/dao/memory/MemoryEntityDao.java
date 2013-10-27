package com.nanuvem.lom.kernel.dao.memory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;

import com.nanuvem.lom.kernel.Entity;
import com.nanuvem.lom.kernel.MetadataException;
import com.nanuvem.lom.kernel.dao.EntityDao;

public class MemoryEntityDao implements EntityDao {

	private Long id = 1L;

	private List<Entity> entities = new ArrayList<Entity>();

	public void create(Entity entity) {
		entity.setId(id++);
		entity.setVersion(0);

		// this fixes a interesting bug for update entity
		Entity clone = (Entity) SerializationUtils.clone(entity);
		entities.add(clone);
	}

	public List<Entity> listAll() {
		return new ArrayList<Entity>(this.entities);
	}

	// TODO refactoring later
	public Entity update(String namespace, String name, Long id, Integer version) {
		// for (Entity e : this.listAll()) {
		// if (e.getId().equals(id)) {
		// if (e.getVersion() > version) {
		// throw new MetadataException(
		// "Updating a deprecated version of Entity "
		// + namespace
		// + "."
		// + name
		// +
		// "Get the entity again to obtain the newest version and proceed updating.");
		// }
		// e.setName(name);
		// e.setNamespace(namespace);
		// e.setVersion(version);
		// return e;
		// }
		// }
		// throw new MetadataException("Invalid id for Entity " + namespace +
		// "."
		// + name);
		Entity entity = new Entity();
		entity.setId(id);
		entity.setName(name);
		entity.setNamespace(namespace);
		entity.setVersion(version);
		return this.update(entity);
	}

	public Entity update(Entity entity) {
		for (Entity e : this.listAll()) {
			if (e.getId().equals(entity.getId())) {
				if (e.getVersion() > entity.getVersion()) {
					throw new MetadataException(
							"Updating a deprecated version of Entity "
									+ e.getNamespace()
									+ "."
									+ e.getName()
									+ ". Get the entity again to obtain the newest version and proceed updating.");
				}
				this.entities.remove(e);
				this.entities.add(entity);
				return entity;
			}
		}
		throw new MetadataException("Invalid id for Entity "
				+ entity.getNamespace() + "." + entity.getName());
	}

	public Entity findEntityById(Long id) {
		for (Entity e : this.entities) {
			if (e.getId().equals(id)) {
				return e;
			}
		}
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
			if (namespace.equalsIgnoreCase(e.getNamespace())
					&& name.equalsIgnoreCase(e.getName())) {
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
