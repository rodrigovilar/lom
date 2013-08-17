package com.nanuvem.lom.kernel.dao;

import java.util.List;

import com.nanuvem.lom.kernel.Entity;
import com.nanuvem.lom.kernel.EntityDTO;

public interface EntityDao {

	void create(Entity entity);

	List<Entity> listAll();

	void remove(Entity entity);

	Entity update(String namespace, String name, Long id, Integer version);

	Entity update(EntityDTO entityDTO);

	Entity findEntityById(Long id);

	List<Entity> listEntitiesByFragmentOfNameAndPackage(String namespaceFragment,
			String nameFragment);
}
