package com.nanuvem.lom.business;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nanuvem.lom.business.dao.RooDaoFactory;
import com.nanuvem.lom.kernel.Class;

@Service
@Transactional
public class ClassServiceImpl  {

	com.nanuvem.lom.kernel.ClassServiceImpl kernelService = new com.nanuvem.lom.kernel.ClassServiceImpl(
			new RooDaoFactory());

	public void create(com.nanuvem.lom.kernel.Class clazz) {
		kernelService.create(clazz);
	}

	public List<Class> listAll() {
		return kernelService.listAll();
	}

}
