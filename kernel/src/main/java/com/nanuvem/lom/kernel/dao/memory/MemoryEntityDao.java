package com.nanuvem.lom.kernel.dao.memory;

import java.util.ArrayList;
import java.util.List;

import com.nanuvem.lom.kernel.Entity;
import com.nanuvem.lom.kernel.dao.EntityDao;

public class MemoryEntityDao implements EntityDao {
	
	private Long id = 1L;
	
	
	private List<Entity> entities = new ArrayList<Entity>();

	public void create(Entity entity) {
		entity.setId(id++);
		entity.setVersion(0);
		entities.add(entity);
	}

	public List<Entity> listAll() {
		return entities;
	}

	public void remove(Entity e) {
		for (Entity entity : entities) {
			if (entity.getId().equals(e.getId())) {
				entities.remove(entity);
				return;
			}
		}
	}

}
