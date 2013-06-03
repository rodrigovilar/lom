package com.nanuvem.lom.service;

import java.util.List;

import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.nanuvem.lom.dao.EntityDAO;
import com.nanuvem.lom.dao.InstanceDAO;
import com.nanuvem.lom.dao.typesquare.Entity;
import com.nanuvem.lom.dao.typesquare.Instance;
import com.nanuvem.lom.dao.typesquare.TypeSquareEntityDAO;
import com.nanuvem.lom.dao.typesquare.TypeSquareInstanceDAO;

public class InstanceServiceImpl implements InstanceService {

	private InstanceDAO dao = new TypeSquareInstanceDAO();
	
	@Override
	public List<Instance> findInstancesByEntity(Entity entity) {
		List<Instance> resultList = dao.findInstancesByEntity(entity);
		if (resultList.size() != 0) {
			return resultList;
		}
		throw new InstanceNotFoundException("Entity with an unknow id!");
	}

	public void deleteInstance(Instance instance) {
		try {
			dao.removeInstance(instance);
		} catch (InvalidDataAccessApiUsageException e) {
			throw new InstanceNotFoundException(e.getMessage());
		}

	}
	
	public long countAllInstances() {
        return dao.countInstances();
    }
    
    public Instance findInstance(Long id) {
        return dao.findInstance(id);
    }
    
    public List<Instance> findAllInstances() {
        return dao.findAllInstances();
    }
    
    public List<Instance> findInstanceEntries(int firstResult, int maxResults) {
        return dao.findInstanceEntries(firstResult, maxResults);
    }
    
    public void saveInstance(Instance instance) {
        dao.saveInstance(instance);
    }
    
    public Instance updateInstance(Instance instance) {
        return dao.updateInstance(instance);
    }
	
}
