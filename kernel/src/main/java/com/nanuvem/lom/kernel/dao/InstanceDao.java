package com.nanuvem.lom.kernel.dao;

import com.nanuvem.lom.kernel.Instance;

public interface InstanceDao {

	void create(Instance instance);

	void delete(Instance instance);

	Instance update(Instance instance);

	Instance findInstanceById(Long id);

}
