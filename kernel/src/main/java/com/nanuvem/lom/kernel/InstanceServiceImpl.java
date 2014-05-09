package com.nanuvem.lom.kernel;

import com.nanuvem.lom.kernel.dao.DaoFactory;
import com.nanuvem.lom.kernel.dao.InstanceDao;

public class InstanceServiceImpl {

	private InstanceDao dao;
	private ClassServiceImpl classService;
	
	public InstanceServiceImpl(DaoFactory daoFactory) {
		this.classService = new ClassServiceImpl(daoFactory);
		this.dao = daoFactory.createInstanceDao();
	}

	public void create(Instance instance) {
		Class clazz = this.classService.readClass(instance.getClazz().getFullName());
		if(clazz != null){
			instance.setClazz(clazz);
			this.dao.create(instance);			
		}
	}

	public Instance findInstanceById(Long id) {
		return this.dao.findInstanceById(id);
	}
}
