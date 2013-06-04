package com.nanuvem.lom.service;

import java.util.List;

import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.nanuvem.lom.model.Instance;
import com.nanuvem.lom.model.PropertyValue;

public class PropertyValueServiceImpl implements PropertyValueService {

	public List<PropertyValue> findPropertyValuesByInstance(Instance instance) {

		if (Instance.findInstance(instance.getId()) == null) {
			throw new PropertyValueNotFoundException("Invalid instance!");
		}

		return PropertyValue.findPropertyValuesByInstance(instance)
				.getResultList();
	}
	
	public void deletePropertyValue(PropertyValue propertyValue) {
		try {
			propertyValue.remove();
		} catch (InvalidDataAccessApiUsageException e) {
			throw new PropertyValueNotFoundException(e.getMessage());
		}

	}
	
}
