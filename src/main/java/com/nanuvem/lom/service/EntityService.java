package com.nanuvem.lom.service;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import com.nanuvem.lom.model.Entity;

@RooService(domainTypes = { com.nanuvem.lom.model.Entity.class })
public interface EntityService {

	public List<Entity> findEntitiesByFragmentOfName(String fragmentOfName);

	public List<Entity> findEntitiesByFragmentOfNamespace(String fragmentOfNamespace);

	public List<Entity> findEntitiesByEmptyName();

	public List<Entity> findEntitiesByEmptyNamespace();

	public List<Entity> findEntitiesByNameWithSpace();

	public List<Entity> findEntitiesByNamespaceWithSpace();
}
