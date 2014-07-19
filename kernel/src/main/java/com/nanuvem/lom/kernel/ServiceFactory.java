package com.nanuvem.lom.kernel;

import com.nanuvem.lom.kernel.dao.DaoFactory;
import com.nanuvem.lom.kernel.validator.deployer.Deployers;

public class ServiceFactory {

	private DaoFactory daoFactory;

	private ClassServiceImpl classService;
	private AttributeServiceImpl attributeService;
	private InstanceServiceImpl instanceService;
	private Deployers deployers;

	public ServiceFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
		this.deployers = new Deployers();
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
					this.getClassService(), deployers);
		}
		return this.attributeService;
	}

	public InstanceServiceImpl getInstanceService() {
		if (this.instanceService == null) {
			this.instanceService = new InstanceServiceImpl(this.daoFactory,
					this.getClassService(), deployers);
		}
		return this.instanceService;
	}
}
