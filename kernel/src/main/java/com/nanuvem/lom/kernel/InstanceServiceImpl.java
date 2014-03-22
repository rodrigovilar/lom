package com.nanuvem.lom.kernel;

import java.util.List;
import java.util.regex.Pattern;

import com.nanuvem.lom.kernel.dao.DaoFactory;
import com.nanuvem.lom.kernel.dao.ClassDao;
import com.nanuvem.lom.kernel.dao.InstanceDao;

public class InstanceServiceImpl {

	private InstanceDao dao;

	public InstanceServiceImpl(DaoFactory factory) {
		this.dao = factory.createInstanceDao();
	}

	public void create(Instance instance) {

	}

	public Instance findInstanceById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
