package com.nanuvem.lom.kernel.dao;

public interface DaoFactory {

	ClassDao createEntityDao();

	AttributeDao createAttributeDao();
	
}
