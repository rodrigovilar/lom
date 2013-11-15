package com.nanuvem.lom.kernel.dao.memory;

import java.util.ArrayList;
import java.util.List;

import com.nanuvem.lom.kernel.Attribute;
import com.nanuvem.lom.kernel.dao.AttributeDao;

public class MemoryAttributeDao implements AttributeDao {

	private Long id = 1L;

	private List<Attribute> attributes = new ArrayList<Attribute>();

	public void create(Attribute attribute) {
		attribute.setId(id++);
		attribute.setVersion(0);
		this.attributes.add(attribute);
	}

	public List<Attribute> listAllAttributes(String fullClassName) {
		List<Attribute> resultList = new ArrayList<Attribute>();
		for (Attribute attribute : this.attributes) {
			if (attribute.getClazz().getFullName().equals(fullClassName)) {
				resultList.add(attribute);
			}
		}
		return resultList;
	}
}
