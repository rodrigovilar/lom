package com.nanuvem.lom.kernel;

import java.util.List;

import com.nanuvem.lom.kernel.dao.AttributeDao;
import com.nanuvem.lom.kernel.dao.DaoFactory;

public class AttributeServiceImpl {

	private AttributeDao attributeDao;

	public AttributeServiceImpl(DaoFactory dao){
		this.attributeDao = dao.createAttributeDao();
	}

	public void create(Attribute attribute) {
		this.attributeDao.create(attribute);
	}

	public List<Attribute> listAllAttributes(String fullClassName) {
		return this.attributeDao.listAllAttributes(fullClassName);
	}
}
