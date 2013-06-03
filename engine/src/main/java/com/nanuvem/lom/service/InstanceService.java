package com.nanuvem.lom.service;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import com.nanuvem.lom.dao.typesquare.Entity;
import com.nanuvem.lom.dao.typesquare.Instance;

@RooService(domainTypes = { com.nanuvem.lom.dao.typesquare.Instance.class })
public interface InstanceService {

	List<Instance> findInstancesByEntity(Entity entity);

}
