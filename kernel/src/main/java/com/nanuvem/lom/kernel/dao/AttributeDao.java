package com.nanuvem.lom.kernel.dao;

import java.util.List;

import com.nanuvem.lom.kernel.Attribute;

public interface AttributeDao {

	void create(Attribute attribute);

	List<Attribute> listAllAttributes(String classFullName);

	Attribute findAttributeById(Long id);

	Attribute findAttributeByNameAndClassFullName(String nameAttribute,	String classFullName);

	Attribute update(Attribute attribute);

}
