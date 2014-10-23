package com.nanuvem.lom.kernel.dao.memory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;

import com.nanuvem.lom.kernel.Class;
import com.nanuvem.lom.kernel.ClassServiceImpl;
import com.nanuvem.lom.kernel.MetadataException;
import com.nanuvem.lom.kernel.dao.ClassDao;

public class MemoryClassDao implements ClassDao {

	private Long id = 1L;

	private List<Class> classes = new ArrayList<Class>();

	public void create(Class clazz) {
		clazz.setId(id++);
		clazz.setVersion(0);

		// this fixes a interesting bug for update class
		Class clone = (Class) SerializationUtils.clone(clazz);
		classes.add(clone);
	}

	public List<Class> listAll() {
		List<Class> classesReturn = new ArrayList<Class>();

		for (Class clazz : this.classes) {
			classesReturn.add((Class) SerializationUtils.clone(clazz));
		}
		return classesReturn;
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
		Class clazz = new Class();
		clazz.setId(id);
		clazz.setName(name);
		clazz.setNamespace(namespace);
		clazz.setVersion(version);
		return this.update(clazz);
	}

	public Class update(Class clazz) {
		for (Class e : this.listAll()) {
			if (e.getId().equals(clazz.getId())) {
				if (e.getVersion() > clazz.getVersion()) {
					throw new MetadataException(
							"Updating a deprecated version of Class "
									+ e.getNamespace()
									+ "."
									+ e.getName()
									+ ". Get the Class again to obtain the newest version and proceed updating.");
				}
				this.classes.remove(e);
				this.classes.add(clazz);
				return (Class) SerializationUtils.clone(clazz);
			}
		}
		throw new MetadataException("Invalid id for Class "
				+ clazz.getNamespace() + "." + clazz.getName());
	}

	public Class findClassById(Long id) {
		for (Class e : this.classes) {
			if (e.getId().equals(id)) {
				return (Class) SerializationUtils.clone(e);
			}
		}
		return null;
	}

	public List<Class> listClassesByFragmentOfNameAndPackage(
			String namespaceFragment, String nameFragment) {
		List<Class> results = new ArrayList<Class>();
		for (Class e : this.classes) {
			if (e.getNamespace().toLowerCase()
					.contains(namespaceFragment.toLowerCase())
					&& e.getName().toLowerCase()
							.contains(nameFragment.toLowerCase())) {
				results.add((Class) SerializationUtils.clone(e));
			}
		}
		return results;
	}

	public Class readClassByFullName(String classFullName) {
		String namespace = null;
		String name = null;

		if (classFullName.contains(".")) {
			namespace = classFullName.substring(0,
					classFullName.lastIndexOf("."));
			name = classFullName.substring(classFullName.lastIndexOf(".") + 1,
					classFullName.length());
		} else {
			namespace = ClassServiceImpl.DEFAULT_NAMESPACE;
			name = classFullName;
		}

		for (Class clazz : this.classes) {
			if ((namespace + "." + name).equalsIgnoreCase(clazz.getFullName())) {
				return (Class) SerializationUtils.clone(clazz);
			}
		}
		return null;
	}

	public void delete(String namespaceAndName) {
		// TODO Auto-generated method stub
	}

	public void delete(Class clazz) {
		for (int i = 0; i < this.classes.size(); i++) {
			Class e = this.classes.get(i);
			if (e.getName().equals(clazz.getName())
					&& e.getNamespace().equals(clazz.getNamespace())) {
				this.classes.remove(e);
			}
		}
	}
}
