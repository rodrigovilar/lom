package com.nanuvem.lom.dao;

import java.util.List;

import com.nanuvem.lom.dao.typesquare.Entity;

public interface EntityDAO {

	List<Entity> findAllEntities();

	List<Entity> findEntitiesByNameLike(String name);

	List<Entity> findEntitiesByNamespaceEquals(String namespace);

	List<Entity> findEntitiesByNamespaceLike(String namespace);

	void saveEntity(Entity entity);

	long countEntities();

	void removeEntity(Entity entity);

	Entity findEntity(Long id);

	Entity update(Entity entity);

	List<Entity> findEntityEntries(int firstResult, int maxResults);

	
}
