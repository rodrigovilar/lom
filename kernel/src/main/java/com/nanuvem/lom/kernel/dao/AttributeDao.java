package com.nanuvem.lom.kernel.dao;

import java.util.List;

import com.nanuvem.lom.kernel.Attribute;

public interface AttributeDao {

	void create(Attribute attribute);

	List<Attribute> listAllAttributes(String fullClassName);

	Attribute findAttributeById(Long id);

	Attribute findAttributeByNameAndFullnameClass(String nameAttribute,	String fullnameClass);

}
