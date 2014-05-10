package com.nanuvem.lom.kernel.dao;

public interface DaoFactory {

	ClassDao createClassDao();

	AttributeDao createAttributeDao();

	InstanceDao createInstanceDao();

}
