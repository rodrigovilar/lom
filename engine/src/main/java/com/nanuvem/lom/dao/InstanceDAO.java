package com.nanuvem.lom.dao;

import java.util.List;

import com.nanuvem.lom.dao.typesquare.Entity;
import com.nanuvem.lom.dao.typesquare.Instance;

public interface InstanceDAO {

	List<Instance> findInstancesByEntity(Entity entity);

	void removeInstance(Instance instance);

	long countInstances();

	Instance findInstance(Long id);

	List<Instance> findAllInstances();

	List<Instance> findInstanceEntries(int firstResult, int maxResults);

	void saveInstance(Instance instance);

	Instance updateInstance(Instance instance);

}
