package com.nanuvem.lom.service;

import java.util.List;

import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.nanuvem.lom.model.Entity;
import com.nanuvem.lom.model.Instance;


public class InstanceServiceImpl implements InstanceService {

	@Override
	public List<Instance> findInstancesByEntity(Entity entity) {
		try{
			return Instance.findInstancesByEntity(entity).getResultList();
		}catch (InvalidDataAccessApiUsageException e){
			throw new InstanceNotFoundException(e.getMessage());
		}
	}
	
	public void deleteInstance(Instance instance) {
		try{
			instance.remove();
		}catch(InvalidDataAccessApiUsageException e){
			throw new InstanceNotFoundException(e.getMessage());
		}
		
    }
}
