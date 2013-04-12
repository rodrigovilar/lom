package com.nanuvem.lom.service;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import com.nanuvem.lom.model.Entity;

@RooService(domainTypes = { com.nanuvem.lom.model.Entity.class })
public interface EntityService {
	
    List<Entity> findEntitysByNameLike(String name);

    List<Entity> findEntitysByNamespaceEquals(String namespace);

    List<Entity> findEntitysByNamespaceLike(String namespace);

}
