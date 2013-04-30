package com.nanuvem.lom.service;

import java.util.List;


import org.springframework.roo.addon.layers.service.RooService;

import com.nanuvem.lom.model.Entity;
import com.nanuvem.lom.model.Instance;

@RooService(domainTypes = { com.nanuvem.lom.model.Instance.class })
public interface InstanceService {

	List<Instance> findInstancesByEntity(Entity entity);
		
}
