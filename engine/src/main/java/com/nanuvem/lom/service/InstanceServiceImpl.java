package com.nanuvem.lom.service;

import java.util.List;

import com.nanuvem.lom.model.Entity;
import com.nanuvem.lom.model.Instance;


public class InstanceServiceImpl implements InstanceService {

	@Override
	public List<Instance> findInstancesByEntity(Entity entity) {
		return Instance.findInstancesByEntity(entity).getResultList();
	}

}
