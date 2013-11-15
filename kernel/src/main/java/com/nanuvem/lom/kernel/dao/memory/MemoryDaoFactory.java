package com.nanuvem.lom.kernel.dao.memory;

import com.nanuvem.lom.kernel.dao.AttributeDao;
import com.nanuvem.lom.kernel.dao.DaoFactory;
import com.nanuvem.lom.kernel.dao.ClassDao;

public class MemoryDaoFactory implements DaoFactory {

	public ClassDao createEntityDao() {
		return new MemoryEntityDao();
	}

	public AttributeDao createAttributeDao() {
		return new MemoryAttributeDao();
	}

}
