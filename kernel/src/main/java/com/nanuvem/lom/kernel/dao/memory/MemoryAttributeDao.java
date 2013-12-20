package com.nanuvem.lom.kernel.dao.memory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;

import com.nanuvem.lom.kernel.Attribute;
import com.nanuvem.lom.kernel.Class;
import com.nanuvem.lom.kernel.dao.AttributeDao;
import com.nanuvem.lom.kernel.dao.ClassDao;

public class MemoryAttributeDao implements AttributeDao {

	private Long id = 1L;
	private ClassDao classDao;

	public MemoryAttributeDao(ClassDao classDao) {
		this.classDao = classDao;
	}

	public void create(Attribute attribute) {
		attribute.setId(id++);
		attribute.setVersion(0);

		Class clazz = attribute.getClazz();
		attribute.setClazz(clazz);

		int i = 0;
		for (; i < clazz.getAttributes().size(); i++) {
			if (attribute.getSequence().equals(
					clazz.getAttributes().get(i).getSequence())) {
				break;
			}
		}

		i++;
		if (i == 0) {
			clazz.getAttributes().add(i, attribute);
		} else {
			clazz.getAttributes().add(i - 1, attribute);
		}

		for (; i < clazz.getAttributes().size(); i++) {
			Attribute attributeNext = null;
			try {
				attributeNext = clazz.getAttributes().get(i);
			} catch (IndexOutOfBoundsException e) {
				break;
			}

			if (attributeNext.getSequence().equals(
					clazz.getAttributes().get(i - 1).getSequence())) {
				attributeNext.setSequence(attributeNext.getSequence() + 1);
			}
		}
		this.classDao.update(clazz);
	}

	public List<Attribute> listAllAttributes(String classFullName) {
		Class clazz = this.classDao.readClassByFullName(classFullName);

		List<Attribute> cloneAttributes = new ArrayList<Attribute>();
		for (Attribute at : clazz.getAttributes()) {
			cloneAttributes.add((Attribute) SerializationUtils.clone(at));
		}
		return cloneAttributes;
	}

	public Attribute findAttributeById(Long id) {
		List<Class> classes = this.classDao.listAll();

		for (Class clazzEach : classes) {
			for (Attribute attributeEach : clazzEach.getAttributes()) {
				if (attributeEach.getId().equals(id)) {
					return attributeEach;
				}
			}
		}
		return null;
	}

	public Attribute findAttributeByNameAndClassFullName(String nameAttribute,
			String classFullName) {

		Class clazzFound = this.classDao.readClassByFullName(classFullName);
		if (clazzFound.getAttributes() != null) {
			for (Attribute attributeEach : clazzFound.getAttributes()) {
				if (attributeEach.getName().equalsIgnoreCase(nameAttribute)) {
					return (Attribute) SerializationUtils.clone(attributeEach);
				}
			}
		}
		return null;
	}
}