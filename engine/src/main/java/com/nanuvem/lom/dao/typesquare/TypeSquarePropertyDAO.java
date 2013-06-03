package com.nanuvem.lom.dao.typesquare;

import java.util.List;

import com.nanuvem.lom.dao.PropertyDAO;

public class TypeSquarePropertyDAO implements PropertyDAO {

	@Override
	public void saveProperty(Property property) {
		property.persist();
	}

	@Override
	public Entity findEntity(Long id) {
		return Entity.findEntity(id);
	}

	@Override
	public void removeProperty(Property property) {
		property.remove();		
	}

	@Override
	public long countProperties() {
		return Property.countPropertys();
	}

	@Override
	public Property findProperty(Long id) {
		return Property.findProperty(id);
	}

	@Override
	public List<Property> findAllProperties() {
		return Property.findAllPropertys();
	}

	@Override
	public List<Property> findPropertyEntries(int firstResult, int maxResults) {
		return Property.findPropertyEntries(firstResult, maxResults);
	}

	@Override
	public Property updateProperty(Property property) {
		return property.merge();
	}

}
