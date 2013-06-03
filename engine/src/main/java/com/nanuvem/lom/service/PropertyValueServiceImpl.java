package com.nanuvem.lom.service;

import java.util.List;

import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.nanuvem.lom.dao.PropertyValueDAO;
import com.nanuvem.lom.dao.typesquare.Instance;
import com.nanuvem.lom.dao.typesquare.PropertyValue;
import com.nanuvem.lom.dao.typesquare.TypeSquarePropertyValueDAO;

public class PropertyValueServiceImpl implements PropertyValueService {

	private PropertyValueDAO dao = new TypeSquarePropertyValueDAO();
	
	public List<PropertyValue> findPropertyValuesByInstance(Instance instance) {

		if (Instance.findInstance(instance.getId()) == null) {
			throw new PropertyValueNotFoundException("Invalid instance!");
		}

		return dao.findPropertyValuesByInstance(instance);
	}

	public void deletePropertyValue(PropertyValue propertyValue) {
		try {
			dao.removePropertyValue(propertyValue);
		} catch (InvalidDataAccessApiUsageException e) {
			throw new PropertyValueNotFoundException(e.getMessage());
		}

	}

	public long countAllPropertyValues() {
		return dao.countPropertyValues();
	}

	public PropertyValue findPropertyValue(Long id) {
		return dao.findPropertyValue(id);
	}

	public List<PropertyValue> findAllPropertyValues() {
		return dao.findAllPropertyValues();
	}

	public List<PropertyValue> findPropertyValueEntries(int firstResult,
			int maxResults) {
		return dao.findPropertyValueEntries(firstResult, maxResults);
	}

	public void savePropertyValue(PropertyValue propertyValue) {
		dao.savePropertyValue(propertyValue);
	}

	public PropertyValue updatePropertyValue(PropertyValue propertyValue) {
		return dao.updatePropertyValue(propertyValue);
	}

}
