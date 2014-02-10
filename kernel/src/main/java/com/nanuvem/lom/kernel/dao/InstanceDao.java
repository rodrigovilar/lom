package com.nanuvem.lom.kernel.dao;

import java.util.List;

import com.nanuvem.lom.kernel.Class;
import com.nanuvem.lom.kernel.Instance;

public interface InstanceDao {

	void create(Instance instance);

	void delete(Instance instance);

	Instance update(Instance instance);

	Instance findInstanceById(Long id);

}
