package com.nanuvem.lom.model;

import java.util.Set;

import com.nanuvem.lom.dao.typesquare.Entity;
import com.nanuvem.lom.dao.typesquare.Instance;
import com.nanuvem.lom.dao.typesquare.Property;
import com.nanuvem.lom.dao.typesquare.PropertyType;
import com.nanuvem.lom.dao.typesquare.PropertyValue;

import junit.framework.Assert;

public class CommonCreateMethodsForTesting {

	public static Entity createEntity(String name, String namespace) {
		Entity entity = new Entity();
		entity.setName(name);
		entity.setNamespace(namespace);

		return entity;
	}

	public static Property createProperty(String name, String configuration,
			PropertyType type, Entity entity) {
		Property property = new Property();
		property.setName(name);
		property.setConfiguration(configuration);
		property.setType(type);
		property.setEntity(entity);
		return property;
	}

	public static Instance createInstance(Entity entity) {
		Instance instance = new Instance();
		instance.setEntity(entity);
		return instance;
	}

	public static PropertyValue createPropertyValue(String _value,
			Instance instance, Property property) {
		PropertyValue propertyValue = new PropertyValue();
		propertyValue.set_value(_value);
		propertyValue.setInstance(instance);
		Set<PropertyValue> propertyValues = instance.get_values();
		propertyValues.add(propertyValue);
		instance.set_values(propertyValues);
		propertyValue.setProperty(property);
		return propertyValue;
	}
	
	
	

}
