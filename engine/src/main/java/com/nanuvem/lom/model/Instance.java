package com.nanuvem.lom.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.nanuvem.lom.service.InstanceNotFoundException;

@RooJson(deepSerialize = true)
@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findInstancesByEntity" })
public class Instance {

	@ManyToOne
	private Entity entity;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "instance")
	private Set<PropertyValue> _values = new HashSet<PropertyValue>();

	public static Instance findInstance(Long id) {
		if (id == null)
			return null;
		Instance instance = entityManager().find(Instance.class, id);
		if (instance == null) {
			throw new InstanceNotFoundException(
					"Instance not found with this id!");
		}
		return instance;
	}
}
