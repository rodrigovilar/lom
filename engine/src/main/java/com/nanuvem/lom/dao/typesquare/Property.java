package com.nanuvem.lom.dao.typesquare;

import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.equals.RooEquals;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.nanuvem.lom.service.EntityNotFoundException;
import com.nanuvem.lom.service.PropertyNotFoundException;

@RooJson(deepSerialize = true)
@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findPropertysByEntity",
		"findPropertysByEntityAndNameLike" })
@RooEquals
public class Property {

	@NotNull
	private String name = "";

	private String configuration = "";

	@ManyToOne
	private Entity entity;

	@NotNull
	@Enumerated(EnumType.STRING)
	private PropertyType type;

	public static com.nanuvem.lom.dao.typesquare.Property findProperty(Long id) {
		if (id == null)
			return null;
		Property property = entityManager().find(Property.class, id);
		if (property == null) {
			throw new PropertyNotFoundException(
					"Property not found with this id!");
		}
		return property;
	}

	public static TypedQuery<com.nanuvem.lom.dao.typesquare.Property> findPropertysByEntity(
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
