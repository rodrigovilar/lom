package com.nanuvem.lom.service;

import java.util.Set;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import org.json.JSONObject;

import com.nanuvem.lom.model.Entity;
import com.nanuvem.lom.model.Property;

public class PropertyServiceImpl implements PropertyService {

	public void saveProperty(Property property) {
		try {
			this.validateName(property);
			this.validateConfiguration(property);
			this.validatePropertyInEntityProperties(property);
			property.persist();
		} catch (ValidationException e) {
			throw e;
		}
	}

	public void validatePropertyInEntityProperties(Property property){
		Entity entity = Entity.findEntity(property.getEntity().getId());
		Set<Property> properties = entity.getProperties();
		for (Property p : properties) {
			if (p.getName().equalsIgnoreCase(property.getName())) {
				throw new ValidationException(
						"Property with same name and same entity already exists!");
			}
		}
		properties.add(property);
	}
	
	public void validateConfiguration(Property property) {
		if (property.getConfiguration() == null
				|| property.getConfiguration().equals("")) {
			
			property.setConfiguration("{\"default\":default}");
		}

		JSONObject jsonObject = new JSONObject(property.getConfiguration());
		JSONObject.testValidity(jsonObject);
	}

	public void validateName(Property property) {
		if (Pattern.matches("[a-zA-Z0-9 _]+", property.getName())) {

		} else {
			throw new ValidationException(
					"Property name contains invalid characters!");
		}
	}

}
