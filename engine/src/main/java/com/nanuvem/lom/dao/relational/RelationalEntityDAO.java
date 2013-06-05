package com.nanuvem.lom.dao.relational;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nanuvem.lom.dao.EntityDAO;
import com.nanuvem.lom.dao.typesquare.Entity;

@Repository
public class RelationalEntityDAO implements EntityDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void init(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}
	
	
	public void setDataSource(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}
	
	@Override
	public List<Entity> findAllEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Entity> findEntitiesByNameLike(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Entity> findEntitiesByNamespaceEquals(String namespace) {
		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public List<Entity> findEntitiesByNamespaceLike(String namespace) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveEntity(Entity entity) {
		// TODO Auto-generated method stub
		this.jdbcTemplate.execute("create table " + entity.getName());
	}

	@Override
	public long countEntities() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeEntity(Entity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public Entity findEntity(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entity update(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Entity> findEntityEntries(int firstResult, int maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

}
