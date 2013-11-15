package com.nanuvem.lom.kernel;

import java.util.List;
import java.util.regex.Pattern;

import com.nanuvem.lom.kernel.dao.DaoFactory;
import com.nanuvem.lom.kernel.dao.ClassDao;

public class ClassServiceImpl {

	private ClassDao dao;

	public ClassServiceImpl(DaoFactory factory) {
		this.dao = factory.createEntityDao();
	}

	private void validateEntity(Class clazz) {
		if (clazz.getName() == null || clazz.getName().equals("")) {
			throw new MetadataException("The name of an Class is mandatory");
		}

		if (clazz.getNamespace() == null || clazz.getNamespace().equals("")) {
			clazz.setNamespace("default");
		}

		if (!Pattern.matches("[a-zA-Z1-9.]{1,}", clazz.getNamespace())) {
			throw new MetadataException("Invalid value for Class namespace: "
					+ clazz.getNamespace());
		}

		if (!Pattern.matches("[a-zA-Z1-9]{1,}", clazz.getName())) {
			throw new MetadataException("Invalid value for Class name: "
					+ clazz.getName());
		}

		String readEntityQuery = clazz.getNamespace() + "." + clazz.getName();
		Class found = null;
		try {
			found = this.readClass(readEntityQuery);
		} catch (MetadataException me) {
			found = null;
		}

		if (found != null && found.getName().equals(clazz.getName())
				&& found.getNamespace().equals(clazz.getNamespace())) {
			StringBuilder message = new StringBuilder();
			message.append("The ");
			if (!clazz.getNamespace().equals("default")) {
				message.append(clazz.getNamespace());
				message.append(".");
			}
			message.append(clazz.getName());
			message.append(" Class already exists");
			throw new MetadataException(message.toString());
		}
	}

	public void create(Class clazz) {
		this.validateEntity(clazz);
		this.dao.create(clazz);
	}

	public List<Class> listAll() {
		return this.dao.listAll();
	}

	public List<Class> listClassesByFragmentOfNameAndPackage(
			String namespaceFragment, String nameFragment) {
		if (namespaceFragment == null) {
			namespaceFragment = "";
		}

		if (nameFragment == null) {
			nameFragment = "";
		}

		if (!Pattern.matches("[a-zA-Z1-9.]{1,}", namespaceFragment)
				&& !namespaceFragment.isEmpty()) {
			throw new MetadataException("Invalid value for Class namespace: "
					+ namespaceFragment);
		}

		if (!Pattern.matches("[a-zA-Z1-9]{1,}", nameFragment)
				&& !nameFragment.isEmpty()) {
			throw new MetadataException("Invalid value for Class name: "
					+ nameFragment);
		}

		return this.dao.listEntitiesByFragmentOfNameAndPackage(
				namespaceFragment, nameFragment);
	}

	public Class readClass(String string) {
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
                throw new MetadataException("Invalid key for Class: " + string);
        }

        if (!Pattern.matches("[a-zA-Z1-9]{1,}", name) && !name.isEmpty()) {
                if (string.startsWith(".")) {
                        string = string.substring(1);
                }
                if (string.endsWith(".")) {
                        string = string.substring(0, string.length() - 1);
                }
                throw new MetadataException("Invalid key for Class: " + string);
        }

        if (namespace.isEmpty()) {
                namespace = "default";
        }

        Class entityByNamespaceAndName = this.dao
                        .readEntityByNamespaceAndName(namespace, name);

        if (entityByNamespaceAndName == null) {
                if (string.startsWith(".")) {
                        string = string.substring(1);
                }
                if (string.endsWith(".")) {
                        string = string.substring(0, string.length() - 1);
                }
                throw new MetadataException("Class not found: " + string);
        }
        return entityByNamespaceAndName;
}

	public void delete(String string) {
		Class readClass = this.readClass(string);
		this.dao.delete(readClass);
	}

	public Class update(Class updateClass) {
		this.validateClassOnUpdate(updateClass);
		this.validateEntity(updateClass);
		return this.dao.update(updateClass);
	}

	public void delete(Class clazz) {
		this.dao.delete(clazz);
	}

	public Class update(String namespace, String name, Long id, Integer version) {
		Class updateClass = new Class();
		updateClass.setId(id);
		updateClass.setName(name);
		updateClass.setNamespace(namespace);
		updateClass.setVersion(version);
		this.validateClassOnUpdate(updateClass);
		this.validateEntity(updateClass);
		return this.dao.update(namespace, name, id, version);
	}

	private void validateClassOnUpdate(Class updateClass) {
		if (updateClass.getId() == null && updateClass.getVersion() == null) {
			throw new MetadataException(
					"The version and id of an Class are mandatory on update");
		} else if (updateClass.getId() == null) {
			throw new MetadataException(
					"The id of an Class is mandatory on update");
		} else if (updateClass.getVersion() == null) {
			throw new MetadataException(
					"The version of an Class is mandatory on update");
		}
	}

	public Class findEntityById(Long id) {
		return this.dao.findClassById(id);
	}

}
