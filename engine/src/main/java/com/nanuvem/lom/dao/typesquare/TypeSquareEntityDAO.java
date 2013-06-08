package com.nanuvem.lom.dao.typesquare;

import java.util.List;

import com.nanuvem.lom.dao.EntityDAO;

public class TypeSquareEntityDAO implements EntityDAO {

	@Override
	public List<Entity> findAllEntities() {
		return Entity.findAllEntitys();
	}

	@Override
	public List<Entity> findEntitiesByNameLike(String name) {
		return Entity.findEntitysByNameLike(name).getResultList();
	}

	@Override
	public List<Entity> findEntitiesByNamespaceEquals(String namespace) {
		return Entity.findEntitysByNamespaceEquals(namespace).getResultList();
	}

	@Override
	public List<Entity> findEntitiesByNamespaceLike(String namespace) {
		return Entity.findEntitysByNamespaceLike(namespace).getResultList();
	}

	@Override
	public void saveEntity(Entity entity) {
		entity.persist();
	}

	@Override
	public long countEntities() {
		return Entity.countEntitys();
	}

	@Override
	public void removeEntity(Entity entity) {
		entity.remove();
	}

	@Override
	public Entity findEntity(Long id) {
		return Entity.findEntity(id);
	}

	@Override
	public Entity update(Entity entity) {
		return entity.merge();
	}

	@Override
	public List<Entity> findEntityEntries(int firstResult, int maxResults) {
		return Entity.findEntityEntries(firstResult, maxResults);
	}
	
}
