package com.nanuvem.lom.kernel.dao.memory;

import com.nanuvem.lom.kernel.dao.AttributeDao;
import com.nanuvem.lom.kernel.dao.ClassDao;
import com.nanuvem.lom.kernel.dao.DaoFactory;

public class MemoryDaoFactory implements DaoFactory {

	private MemoryClassDao classDao;
	private MemoryAttributeDao attributeDao;

	public ClassDao createClassDao() {
		if (classDao == null) {
			this.classDao = new MemoryClassDao();
		}
		return this.classDao;
	}

	public AttributeDao createAttributeDao() {
		if (this.attributeDao == null) {
			this.attributeDao = new MemoryAttributeDao(this.createClassDao());
		}
		return this.attributeDao;
	}

}
