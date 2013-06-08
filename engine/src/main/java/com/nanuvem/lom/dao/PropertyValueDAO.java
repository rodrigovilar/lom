package com.nanuvem.lom.dao;

import java.util.List;

import com.nanuvem.lom.dao.typesquare.Instance;
import com.nanuvem.lom.dao.typesquare.PropertyValue;

public interface PropertyValueDAO {

	List<PropertyValue> findPropertyValuesByInstance(Instance instance);

	void removePropertyValue(PropertyValue propertyValue);

	long countPropertyValues();

	PropertyValue findPropertyValue(Long id);

	List<PropertyValue> findAllPropertyValues();

	List<PropertyValue> findPropertyValueEntries(int firstResult, int maxResults);

	void savePropertyValue(PropertyValue propertyValue);

	PropertyValue updatePropertyValue(PropertyValue propertyValue);

}
