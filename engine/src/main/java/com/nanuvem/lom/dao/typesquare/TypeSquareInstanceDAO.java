package com.nanuvem.lom.dao.typesquare;

import java.util.List;

import com.nanuvem.lom.dao.InstanceDAO;

public class TypeSquareInstanceDAO implements InstanceDAO {

	@Override
	public List<Instance> findInstancesByEntity(Entity entity) {
		return Instance.findInstancesByEntity(entity).getResultList();
	}

	@Override
	public void removeInstance(Instance instance) {
		instance.remove();

	}

	@Override
	public long countInstances() {
		return Instance.countInstances();
	}

	@Override
	public Instance findInstance(Long id) {
		return Instance.findInstance(id);
	}

	@Override
	public List<Instance> findAllInstances() {
		return Instance.findAllInstances();
	}

	public List<Instance> findInstanceEntries(int firstResult, int maxResults) {
		return Instance.findInstanceEntries(firstResult, maxResults);
	}

	@Override
	public void saveInstance(Instance instance) {
		instance.persist();
	}

	@Override
	public Instance updateInstance(Instance instance) {
		return instance.merge();
	}

}
