package com.nanuvem.lom.model;

import java.util.Set;

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

}
