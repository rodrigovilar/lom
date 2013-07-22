package com.nanuvem.lom.kernel.dao.memory;

import com.nanuvem.lom.kernel.dao.DaoFactory;
import com.nanuvem.lom.kernel.dao.EntityDao;

public class MemoryDaoFactory implements DaoFactory {

	public EntityDao createEntityDao() {
		return new MemoryEntityDao();
	}

}
