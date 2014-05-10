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
		Class clazz = this.classService.readClass(instance.getClazz()
				.getFullName());
		instance.setClazz(clazz);

		for (AttributeValue attributeValue : instance.getValues()) {
			if (!clazz.getAttributes().contains(attributeValue)) {
				throw new MetadataException("Unknown attribute for "
						+ instance.getClazz().getFullName() + ": "
						+ attributeValue.getAttribute().getName());
			}
		}

		this.dao.create(instance);
	}

	public Instance findInstanceById(Long id) {
		return this.dao.findInstanceById(id);
	}
}
