package com.nanuvem.lom.kernel;

import java.util.List;

import com.nanuvem.lom.kernel.dao.DaoFactory;
import com.nanuvem.lom.kernel.dao.EntityDao;

public class EntityService {
	
	
	private EntityDao dao;

	
	public EntityService(DaoFactory factory) {
		this.dao = factory.createEntityDao();
	}

	public void create(Entity entity) {
		dao.create(entity);
	}

	public List<Entity> listAll() {
		return dao.listAll();
	}

	public void remove(Entity entity) {
		dao.remove(entity);
	}
	
}
