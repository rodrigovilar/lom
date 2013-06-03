package com.nanuvem.lom.dao.typesquare;

import javax.persistence.ManyToOne;
import org.springframework.roo.addon.equals.RooEquals;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.nanuvem.lom.service.EntityNotFoundException;
import com.nanuvem.lom.service.PropertyValueNotFoundException;

@RooJson(deepSerialize = true)
@RooJavaBean
@RooToString
@RooEquals
@RooJpaActiveRecord(finders = { "findPropertyValuesByInstance" })
public class PropertyValue {

	private String _value;

	@ManyToOne
	private Instance instance;

	@ManyToOne
	private Property property;

	public static PropertyValue findPropertyValue(Long id) {
		if (id == null)
			return null;

		PropertyValue found = entityManager().find(PropertyValue.class, id);
		if (found == null)
			throw new PropertyValueNotFoundException(
					"Property Value not found!");

		return found;
	}

}
