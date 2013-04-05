package com.nanuvem.lom.model;

import java.util.ArrayList;
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
import org.springframework.roo.addon.tostring.RooToString;

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
		if (this.isValidName(name)) {
			this.name = name;
		} else {
			throw new ValidationException("Parameter is invalid!");
		}
	}

	public void setNamespace(String namespace) {
		if (this.isValidNamespace(namespace)) {
			this.namespace = namespace;
		} else {
			throw new ValidationException("Parameter is invalid!");
		}
	}

	public void persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();

		if (this.isValidName(this.name) == false) {
			throw new ValidationException("Invalid characters in name");
		}

		if (this.isValidNamespace(this.namespace) == false) {
			throw new ValidationException("Invalid characters in namespace");
		}

		try {
			this.entityManager.persist(this);
		} catch (Exception e) {
			throw new ValidationException(e.getMessage());
		}
	}

	private void isValidNameInThisNamespace(String name, String namespace) {
		if (name.equals("") || name == null) {
			// Tivemos que colocar dessa maneira pois o
			// Hibernate lança exceção ao tentarmos enviar um "" pra ele fazer
			// uma busca. A exceção lançada era "InvalidArgumentException"
			name = " ";
		}

		List<Entity> entitiesByName = Entity.findEntitysByNameLike(name)
				.getResultList();
		if (entitiesByName.size() > 0) {
			for (Entity e : entitiesByName) {
				if (e.name.equalsIgnoreCase(name)
						&& e.namespace.equalsIgnoreCase(namespace)
						&& e.getId() != this.getId()) {
					throw new ValidationException(
							"Entity with same name already exists in this namespace!");
				}
			}
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

	public boolean isValidName(String name) {
		if (Pattern.matches("[a-zA-Z0-9 _]+", name)) {
			this.isValidNameInThisNamespace(name, this.namespace);
			return true;
		} else {
			return false;
		}
	}

	public boolean isValidNamespace(String namespace) {
		if (Pattern.matches("[a-zA-Z0-9 _]+", namespace)
				|| namespace.equals("")) {
			this.isValidNameInThisNamespace(this.name, namespace);
			return true;
		} else {
			return false;
		}
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
