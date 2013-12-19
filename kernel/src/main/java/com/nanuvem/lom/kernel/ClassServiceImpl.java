package com.nanuvem.lom.kernel;

import java.util.List;
import java.util.regex.Pattern;

import com.nanuvem.lom.kernel.dao.DaoFactory;
import com.nanuvem.lom.kernel.dao.ClassDao;

public class ClassServiceImpl {

	private ClassDao dao;

	static final String PREVIOUS_NAME_DEFAULT_OF_THE_CLASSFULLNAME = "default";

	public ClassServiceImpl(DaoFactory factory) {
		this.dao = factory.createClassDao();
	}

	private void validateClass(Class clazz) {
		if (clazz.getName() == null || clazz.getName().equals("")) {
			throw new MetadataException("The name of an Class is mandatory");
		}

		if (clazz.getNamespace() == null || clazz.getNamespace().equals("")) {
			clazz.setNamespace(PREVIOUS_NAME_DEFAULT_OF_THE_CLASSFULLNAME);
		}

		if (!Pattern.matches("[a-zA-Z1-9.]{1,}", clazz.getNamespace())) {
			throw new MetadataException("Invalid value for Class namespace: "
					+ clazz.getNamespace());
		}

		if (!Pattern.matches("[a-zA-Z1-9]{1,}", clazz.getName())) {
			throw new MetadataException("Invalid value for Class name: "
					+ clazz.getName());
		}

		String readClassQuery = clazz.getNamespace() + "." + clazz.getName();
		Class found = null;
		try {
			found = this.readClass(readClassQuery);
		} catch (MetadataException me) {
			found = null;
		}

		if (found != null && found.getName().equals(clazz.getName())
				&& found.getNamespace().equals(clazz.getNamespace())) {
			StringBuilder message = new StringBuilder();
			message.append("The ");
			if (!clazz.getNamespace().equals(
					PREVIOUS_NAME_DEFAULT_OF_THE_CLASSFULLNAME)) {
				message.append(clazz.getNamespace());
				message.append(".");
			}
			message.append(clazz.getName());
			message.append(" Class already exists");
			throw new MetadataException(message.toString());
		}
	}

	public void create(Class clazz) {
		this.validateClass(clazz);
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

		return this.dao.listClassesByFragmentOfNameAndPackage(
				namespaceFragment, nameFragment);
	}

	// There is no test case for classFullName = null. How should the message
	// being thrown exception in this case?
	public Class readClass(String classFullName) {
		String namespace = null;
		String name = null;

		if (classFullName.contains(".")) {
			namespace = classFullName.substring(0,
					classFullName.lastIndexOf("."));
			name = classFullName.substring(classFullName.lastIndexOf(".") + 1,
					classFullName.length());
		} else {
			namespace = "default";
			name = classFullName;
		}

		if (!Pattern.matches("[a-zA-Z1-9.]{1,}", namespace)
				&& !namespace.isEmpty()) {
			this.formatStringAndThrowsExceptionInvalidKeyForClass(classFullName);
		}

		if (!Pattern.matches("[a-zA-Z1-9]{1,}", name) && !name.isEmpty()) {
			this.formatStringAndThrowsExceptionInvalidKeyForClass(classFullName);
		}

		if (namespace.isEmpty()) {
			namespace = "default";
		}

		Class classByNamespaceAndName = this.dao.readClassByFullName(namespace
				+ "." + name);

		if (classByNamespaceAndName == null) {
			if (classFullName.startsWith(".")) {
				classFullName = classFullName.substring(1);
			}
			if (classFullName.endsWith(".")) {
				classFullName = classFullName.substring(0,
						classFullName.length() - 1);
			}
			throw new MetadataException("Class not found: " + classFullName);
		}
		return classByNamespaceAndName;
	}

	// Validate name this method
	private void formatStringAndThrowsExceptionInvalidKeyForClass(String value) {
		if (value.startsWith(".")) {
			value = value.substring(1);
		}
		if (value.endsWith(".")) {
			value = value.substring(0, value.length() - 1);
		}
		throw new MetadataException("Invalid key for Class: " + value);

	}

	public void delete(String string) {
		Class readClass = this.readClass(string);
		this.dao.delete(readClass);
	}

	public Class update(Class updateClass) {
		this.validateClassOnUpdate(updateClass);
		this.validateClass(updateClass);
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
		this.validateClass(updateClass);
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

	public Class findClassById(Long id) {
		return this.dao.findClassById(id);
	}

	public Class findClassByFullName(String classFullName) {
		return this.dao.readClassByFullName(classFullName);
	}
}