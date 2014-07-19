package com.nanuvem.lom.kernel.dao.memory;

import com.nanuvem.lom.kernel.dao.AttributeDao;
import com.nanuvem.lom.kernel.dao.AttributeValueDao;
import com.nanuvem.lom.kernel.dao.ClassDao;
import com.nanuvem.lom.kernel.dao.DaoFactory;
import com.nanuvem.lom.kernel.dao.InstanceDao;

public class MemoryDaoFactory implements DaoFactory {

	private MemoryClassDao classDao;
	private MemoryAttributeDao attributeDao;
	private MemoryInstanceDao instanceDao;
	private MemoryAttributeValueDao attributeValueDao;

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

	public InstanceDao createInstanceDao() {
		if (this.instanceDao == null) {
			this.instanceDao = new MemoryInstanceDao(this.createClassDao());
		}
		return this.instanceDao;

	}

	public AttributeValueDao createAttributeValueDao() {
		if (this.attributeValueDao == null) {
			this.attributeValueDao = new MemoryAttributeValueDao();
		}
		return this.attributeValueDao;
	}

}
