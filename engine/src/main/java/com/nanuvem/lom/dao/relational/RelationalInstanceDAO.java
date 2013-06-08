package com.nanuvem.lom.dao.relational;

import java.util.List;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nanuvem.lom.dao.InstanceDAO;
import com.nanuvem.lom.dao.typesquare.Entity;
import com.nanuvem.lom.dao.typesquare.Instance;

@Repository
public class RelationalInstanceDAO implements InstanceDAO {

	private EntityManager entityManager;

	public RelationalInstanceDAO() {
		entityManager = Instance.entityManager();
	}

	@Override
	public List<Instance> findInstancesByEntity(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeInstance(Instance instance) {
		// TODO Auto-generated method stub
	}

	@Override
	public long countInstances() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Instance findInstance(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Instance> findAllInstances() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Instance> findInstanceEntries(int firstResult, int maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveInstance(Instance instance) {
		// TODO Auto-generated method stub
		
		String tableName = DDLHelper.generateTableNameForAnEntity(instance.getEntity());
		String sql = "insert into "+tableName+" ";

	}

	@Override
	public Instance updateInstance(Instance instance) {
		// TODO Auto-generated method stub
		return null;
	}
}
