package com.nanuvem.lom.kernel;

import com.nanuvem.lom.kernel.dao.DaoFactory;

public class ServiceFactory {

	private DaoFactory daoFactory;

	private ClassServiceImpl classService;
	private AttributeServiceImpl attributeService;
	private InstanceServiceImpl instanceService;

	public ServiceFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public ClassServiceImpl getClassService() {
		if (this.classService == null) {
			this.classService = new ClassServiceImpl(this.daoFactory);
		}
		return this.classService;
	}

	public AttributeServiceImpl getAttributeService() {
		if (this.attributeService == null) {
			this.attributeService = new AttributeServiceImpl(this.daoFactory,
					this.getClassService());
		}
		return this.attributeService;
	}

	public InstanceServiceImpl getInstanceService() {
		if (this.instanceService == null) {
			this.instanceService = new InstanceServiceImpl(this.daoFactory,
					this.getClassService());
		}
		return this.instanceService;
	}

}
