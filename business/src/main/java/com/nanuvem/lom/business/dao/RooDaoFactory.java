package com.nanuvem.lom.business.dao;

import com.nanuvem.lom.kernel.dao.AttributeDao;
import com.nanuvem.lom.kernel.dao.ClassDao;
import com.nanuvem.lom.kernel.dao.DaoFactory;

public class RooDaoFactory implements DaoFactory {

	@Override
	public AttributeDao createAttributeDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassDao createEntityDao() {
		return new RooClassDao();
	}

}
