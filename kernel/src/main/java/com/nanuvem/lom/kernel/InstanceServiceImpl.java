package com.nanuvem.lom.kernel;

import com.nanuvem.lom.kernel.dao.AttributeValueDao;
import com.nanuvem.lom.kernel.dao.DaoFactory;
import com.nanuvem.lom.kernel.dao.InstanceDao;

public class InstanceServiceImpl {

	private InstanceDao instanceDao;
	private AttributeValueDao attributeValueDao;
	
	private ClassServiceImpl classService;

	InstanceServiceImpl(DaoFactory daoFactory, ClassServiceImpl classService) {
		this.classService = classService;
		this.instanceDao = daoFactory.createInstanceDao();
		this.attributeValueDao = daoFactory.createAttributeValueDao();
	}

	public void create(Instance instance) {
		if (instance.getClazz() == null) {
			throw new MetadataException(
					"Invalid value for Instance class: The class is mandatory");
		}

		Class clazz;
		try {
			clazz = this.classService.readClass(instance.getClazz()
					.getFullName());
		} catch (MetadataException e) {
			throw new MetadataException("Unknown class: "
					+ instance.getClazz().getFullName());
		}
		instance.setClazz(clazz);

		for (AttributeValue attributeValue : instance.getValues()) {
			if (!(clazz.getAttributes().contains(attributeValue.getAttribute()))) {
				throw new MetadataException("Unknown attribute for "
						+ instance.getClazz().getFullName() + ": "
						+ attributeValue.getAttribute().getName());
			}
		}
		this.attributeValueDao.create(instance.getValues());
		this.instanceDao.create(instance);
	}

	public Instance findInstanceById(Long id) {
		return this.instanceDao.findInstanceById(id);
	}
}
