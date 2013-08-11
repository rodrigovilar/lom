package com.nanuvem.lom.kernel.dao;

import java.util.List;

import com.nanuvem.lom.kernel.Entity;

public interface EntityDao {

	void create(Entity entity);

	List<Entity> listAll();

	void remove(Entity entity);

	Entity update(String namespace, String name, Long id, Integer version);
}
