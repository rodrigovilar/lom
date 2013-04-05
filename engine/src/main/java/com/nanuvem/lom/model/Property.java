package com.nanuvem.lom.model;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.PropertyNotFoundException;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findPropertysByEntity",
		"findPropertysByEntityAndNameLike" })
public class Property {

	@NotNull
	private String name = "";

	private String configuration = "";

	@ManyToOne
	private Entity entity;

	@NotNull
	@Enumerated(EnumType.STRING)
	private PropertyType type;

	@Transactional
	public void persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.isValidName() == false) {
			throw new ValidationException("Invalid characters in name");
		}
		if (this.isValidConfiguration() == false) {
			throw new ValidationException("Invalid configuration");
		}
		Entity entity = Entity.findEntity(this.entity.getId());
		Set<Property> properties = entity.getProperties();
		for (Property p : properties) {
			if (p.getName().equalsIgnoreCase(this.name)) {
				throw new ValidationException(
						"Property with same name and same entity already exists!");
			}
		}
		properties.add(this);
		this.entityManager.persist(this);
	}

	public boolean validateNameAndNamespace() {
		if (this.isValidName() && this.isValidConfiguration()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isValidName() {
		if (Pattern.matches("[a-zA-Z0-9 _]+", this.name)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isValidConfiguration() {
		boolean isValid = false;
		if (this.configuration == null || this.configuration.equals("")) {
			return true;
		}

		// isValid = Pattern.matches("[a-zA-Z0-9 _]+", this.configuration);
		try {
			final JsonParser parser = new ObjectMapper().getJsonFactory()
					.createJsonParser(this.configuration);
			
			JsonToken token = parser.nextToken();
			
			while (token != null) {
				token = parser.nextToken();
			}
			isValid = true;
		} catch (JsonParseException jpe) {
		} catch (IOException ioe) {
		}
		
		
		return isValid;
	}

	public static com.nanuvem.lom.model.Property findProperty(Long id) {
		if (id == null)
			return null;
		Property property = entityManager().find(Property.class, id);
		if (property == null) {
			throw new PropertyNotFoundException(
					"Property not found with this id!");
		}
		return property;
	}

	public static TypedQuery<com.nanuvem.lom.model.Property> findPropertysByEntity(
			Entity entity) {
		if (entity == null)
			throw new IllegalArgumentException(
					"The entity argument is required");
		if (Entity.findEntity(entity.getId()) == null) {
			throw new EntityNotFoundException("Entity not found!");
		}
		EntityManager em = Property.entityManager();
		TypedQuery<Property> q = em.createQuery(
				"SELECT o FROM Property AS o WHERE o.entity = :entity",
				Property.class);
		q.setParameter("entity", entity);
		return q;
	}
}
