package com.nanuvem.lom.dao;

import java.util.List;

import com.nanuvem.lom.dao.typesquare.Entity;
import com.nanuvem.lom.dao.typesquare.Property;

public interface PropertyDAO {

	void saveProperty(Property property);

	Entity findEntity(Long id);
	
	void removeProperty(Property property);

	long countProperties();

	Property findProperty(Long id);

	List<Property> findAllProperties();

	List<Property> findPropertyEntries(int firstResult, int maxResults);

	Property updateProperty(Property property);

}
