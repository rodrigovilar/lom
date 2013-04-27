package com.nanuvem.lom.service;

import java.util.Set;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import org.json.JSONException;
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
		} catch (JSONException je) {
			throw new ValidationException(je.getMessage());
		}
	}

	public void validateRegexConfiguration(Property property) {
		JSONObject jsonObject = new JSONObject(property.getConfiguration());

		if (jsonObject.has("regex")) {
			jsonObject.getString("regex");

		}
	}

	public void validateMinConfiguration(Property property) {
		JSONObject jsonObject = new JSONObject(property.getConfiguration());
		if (jsonObject.has("minsize")) {
			jsonObject.getInt("minsize");

		}
	}

	public void validateMaxConfiguration(Property property) {
		JSONObject jsonObject = new JSONObject(property.getConfiguration());
		if (jsonObject.has("maxsize")) {
			jsonObject.getInt("maxsize");

		}
	}

	public void validateMandatoryConfiguration(Property property) {
		JSONObject jsonObject = new JSONObject(property.getConfiguration());
		if (jsonObject.has("mandatory")) {
			jsonObject.getBoolean("mandatory");
		}
	}

	public void validateConfigurationAndPropertyType(Property property) {
		JSONObject jsonObject = new JSONObject(property.getConfiguration());
		if (jsonObject.has("configuration")) {
			jsonObject.getString("configuration");

		}

	}

	public void validatePropertyInEntityProperties(Property property) {
		Entity entity = Entity.findEntity(property.getEntity().getId());
		if (entity == null) {
			throw new ValidationException("Property with a null entity!");
		}
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

			property.setConfiguration("{}");
		}

		JSONObject jsonObject = new JSONObject(property.getConfiguration());
		JSONObject.testValidity(jsonObject);

		this.validateRegexConfiguration(property);
		this.validateMaxConfiguration(property);
		this.validateMinConfiguration(property);
		this.validateMandatoryConfiguration(property);
	}

	public void validateName(Property property) {
		if (!Pattern.matches("[a-zA-Z0-9 _]+", property.getName())) {
			throw new ValidationException(
					"Property name contains invalid characters!");
		}
	}

}
