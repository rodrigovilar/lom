package com.nanuvem.lom.kernel.dao.memory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;

import com.nanuvem.lom.kernel.Attribute;
import com.nanuvem.lom.kernel.Class;
import com.nanuvem.lom.kernel.Instance;
import com.nanuvem.lom.kernel.dao.ClassDao;
import com.nanuvem.lom.kernel.dao.InstanceDao;

public class MemoryInstanceDao implements InstanceDao {

	private Long id = 1L;
	private ClassDao classDao;

	public MemoryInstanceDao(ClassDao classDao) {
		this.classDao = classDao;
	}

	public void create(Instance instance) {
		instance.setId(id++);
		instance.setVersion(0);

		Class clazz = this.classDao.findClassById(instance.getClazz().getId());
		instance.setClazz(clazz);

		clazz.getInstances().add(instance);
		this.classDao.update(clazz);
	}

	public List<Instance> listAllInstances(String fullClassName) {
		Class clazz = this.classDao.readClassByFullName(fullClassName);

		List<Instance> cloneInstances = new ArrayList<Instance>();
		for (Instance it : clazz.getInstances()) {
			cloneInstances.add((Instance) SerializationUtils.clone(it));
		}
		return cloneInstances;
	}

	public Instance findInstanceById(Long id) {
		List<Class> classes = this.classDao.listAll();

		for (Class clazzEach : classes) {
			for (Instance instanceEach : clazzEach.getInstances()) {
				if (instanceEach.getId().equals(id)) {
					return instanceEach;
				}
			}
		}
		return null;
	}

	public void delete(Instance instance) {
		// TODO Auto-generated method stub
		
	}

	public Instance update(Instance instance) {
		// TODO Auto-generated method stub
		return null;
	}


}