package com.nanuvem.lom.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.EntityNotFoundException;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.nanuvem.lom.service.EntityServiceImpl;

@RooJson(deepSerialize=true)
@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findEntitysByNameEquals",
		"findEntitysByNameLike", "findEntitysByNamespaceLike",
		"findEntitysByNamespaceEquals" })
@Table(name = "Entity", uniqueConstraints = @UniqueConstraint(columnNames = {
		"namespace", "name" }))
public class Entity {

	@NotNull
	private String name = "";

	private String namespace = "";

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "entity")
	private Set<Property> properties = new HashSet<Property>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "entity")
	private Set<Instance> instances = new HashSet<Instance>();

	public void setName(String name) {
		this.name = name;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public void persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();

		try {
			this.entityManager.persist(this);
		} catch (Exception e) {
			throw new ValidationException(e.getMessage());
		}
	}
	
	public static Entity findEntity(Long id) {
		if (id == null)
			return null;
		Entity found = entityManager().find(Entity.class, id);
		if (found == null)
			throw new EntityNotFoundException("Entity not found!");
		return found;
	}
	
	public static List<Entity> findEntitiesByEmptyName() {
		return Entity.findEntitysByNameEquals(" ").getResultList();
	}

	public static List<Entity> findEntitiesByEmptyNamespace() {
		return Entity.findEntitysByNamespaceEquals(" ").getResultList();
	}

	public static List<Entity> findEntitiesByNameWithSpace() {
		return Entity.findEntitysByNameLike(" ").getResultList();
	}

	public static List<Entity> findEntitiesByNamespaceWithSpace() {
		return Entity.findEntitysByNamespaceLike(" ").getResultList();
	}

	public List<Property> findPropertiesByName(String fragmentOfName) {
		if (fragmentOfName.length() == 0) {
			return Property.findPropertysByEntity(this).getResultList();
		}
		return Property.findPropertysByEntityAndNameLike(this, fragmentOfName)
				.getResultList();
	}
}
