package com.nanuvem.lom.dao.typesquare;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.equals.RooEquals;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.nanuvem.lom.service.EntityNotFoundException;

@RooJson(deepSerialize = true)
@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findEntitysByNameEquals",
		"findEntitysByNameLike", "findEntitysByNamespaceLike",
		"findEntitysByNamespaceEquals" })
@Table(name = "Entity", uniqueConstraints = @UniqueConstraint(columnNames = {
		"namespace", "name" }))
@RooEquals
public class Entity {

	@NotNull
	private String name;

	private String namespace;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "entity")
	private Set<Property> properties = new HashSet<Property>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "entity")
	private Set<Instance> instances = new HashSet<Instance>();

	public static Entity findEntity(Long id) {
		if (id == null)
			return null;
		Entity found = entityManager().find(Entity.class, id);
		if (found == null)
			throw new EntityNotFoundException("Entity not found!");
		return found;
	}

	public List<Property> findPropertiesByName(String fragmentOfName) {
		if (fragmentOfName == null || fragmentOfName.equals("")) {
			return Property.findPropertysByEntity(this).getResultList();
		}
		return Property.findPropertysByEntityAndNameLike(this, fragmentOfName)
				.getResultList();
	}

	public static TypedQuery<Entity> findEntitysByNamespaceEquals(
			String namespace) {
		EntityManager em = Entity.entityManager();
		TypedQuery<Entity> q;

		if (namespace == null) {
			q = em.createQuery(
					"SELECT o FROM Entity AS o WHERE o.namespace IS NULL",
					Entity.class);

		} else {
			q = em.createQuery(
					"SELECT o FROM Entity AS o WHERE o.namespace = :namespace",
					Entity.class);
			q.setParameter("namespace", namespace);
		}

		return q;
	}

}
