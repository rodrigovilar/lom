package com.nanuvem.lom.kernel.dao.memory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;

import com.nanuvem.lom.kernel.Class;
import com.nanuvem.lom.kernel.MetadataException;
import com.nanuvem.lom.kernel.dao.ClassDao;

public class MemoryEntityDao implements ClassDao {

	private Long id = 1L;

	private List<Class> entities = new ArrayList<Class>();

	public void create(Class entity) {
		entity.setId(id++);
		entity.setVersion(0);
		
		// this fixes a interesting bug for update entity
		Class clone = (Class) SerializationUtils.clone(entity);
		entities.add(clone);
	}

	public List<Class> listAll() {
		return new ArrayList<Class>(this.entities);
	}

	// TODO refactoring later
	public Class update(String namespace, String name, Long id, Integer version) {
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
		Class entity = new Class();
		entity.setId(id);
		entity.setName(name);
		entity.setNamespace(namespace);
		entity.setVersion(version);
		return this.update(entity);
	}

	public Class update(Class entity) {
		for (Class e : this.listAll()) {
			if (e.getId().equals(entity.getId())) {
				if (e.getVersion() > entity.getVersion()) {
					throw new MetadataException(
							"Updating a deprecated version of Class "
									+ e.getNamespace()
									+ "."
									+ e.getName()
									+ ". Get the Class again to obtain the newest version and proceed updating.");
				}
				this.entities.remove(e);
				this.entities.add(entity);
				return entity;
			}
		}
		throw new MetadataException("Invalid id for Class "
				+ entity.getNamespace() + "." + entity.getName());
	}

	public Class findClassById(Long id) {
		for (Class e : this.entities) {
			if (e.getId().equals(id)) {
				return e;
			}
		}
		return null;
	}

	public List<Class> listEntitiesByFragmentOfNameAndPackage(
			String namespaceFragment, String nameFragment) {
		List<Class> results = new ArrayList<Class>();
		for (Class e : this.entities) {
			if (e.getNamespace().toLowerCase()
					.contains(namespaceFragment.toLowerCase())
					&& e.getName().toLowerCase()
							.contains(nameFragment.toLowerCase())) {
				results.add(e);
			}
		}
		return results;
	}

	public Class readEntityByNamespaceAndName(String namespace, String name) {
		for (Class e : this.entities) {
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

	public void delete(Class entity) {
		for (int i = 0; i < this.entities.size(); i++) {
			Class e = this.entities.get(i);
			if (e.getName().equals(entity.getName())
					&& e.getNamespace().equals(entity.getNamespace())) {
				this.entities.remove(e);
			}
		}

	}

}
