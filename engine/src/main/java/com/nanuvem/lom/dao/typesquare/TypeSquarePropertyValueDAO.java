package com.nanuvem.lom.dao.typesquare;

import java.util.List;

import com.nanuvem.lom.dao.PropertyValueDAO;

public class TypeSquarePropertyValueDAO implements PropertyValueDAO {

	@Override
	public List<PropertyValue> findPropertyValuesByInstance(Instance instance) {
		return PropertyValue.findPropertyValuesByInstance(instance)
				.getResultList();
	}

	@Override
	public void removePropertyValue(PropertyValue propertyValue) {
		propertyValue.remove();
		
	}

	@Override
	public long countPropertyValues() {
		return PropertyValue.countPropertyValues();
	}

	@Override
	public PropertyValue findPropertyValue(Long id) {
		return PropertyValue.findPropertyValue(id);
	}

	@Override
	public List<PropertyValue> findAllPropertyValues() {
		return PropertyValue.findAllPropertyValues();
	}

	@Override
	public List<PropertyValue> findPropertyValueEntries(int firstResult,
			int maxResults) {
		return PropertyValue.findPropertyValueEntries(firstResult, maxResults);
	}

	@Override
	public void savePropertyValue(PropertyValue propertyValue) {
		propertyValue.persist();
		
	}

	@Override
	public PropertyValue updatePropertyValue(PropertyValue propertyValue) {
		return propertyValue.merge();
	}

}
