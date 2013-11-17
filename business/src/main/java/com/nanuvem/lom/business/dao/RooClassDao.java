package com.nanuvem.lom.business.dao;

import java.util.List;

import com.nanuvem.lom.kernel.Class;
import com.nanuvem.lom.kernel.dao.ClassDao;

public class RooClassDao implements ClassDao {

	@Override
	public void create(Class arg0) {
		Clazz clazz = new Clazz();
		clazz.setId(arg0.getId());
		clazz.setName(arg0.getName());
		clazz.setNamespace(arg0.getNamespace());
		clazz.setVersion(arg0.getVersion());
		
		clazz.persist();
	}

	@Override
	public void delete(Class arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Class findClassById(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Class> listAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Class> listEntitiesByFragmentOfNameAndPackage(String arg0,
			String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class readEntityByNamespaceAndName(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class update(Class arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class update(String arg0, String arg1, Long arg2, Integer arg3) {
		// TODO Auto-generated method stub
		return null;
	}

}
