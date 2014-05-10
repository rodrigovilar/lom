package com.nanuvem.lom.kernel.dao;

import java.util.List;

import com.nanuvem.lom.kernel.Class;

public interface ClassDao {

	void create(Class clazz);

	List<Class> listAll();

	void delete(Class clazz);

	Class update(String namespace, String name, Long id, Integer version);

	Class update(Class clazz);

	Class findClassById(Long id);

	List<Class> listClassesByFragmentOfNameAndPackage(String namespaceFragment,
			String nameFragment);

	Class readClassByFullName(String classFullName);

	void delete(String namespaceAndName);
}
