package com.nanuvem.lom.dao.relational;

import java.util.List;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nanuvem.lom.dao.EntityDAO;
import com.nanuvem.lom.dao.typesquare.Entity;

@Repository
public class RelationalEntityDAO implements EntityDAO {

	private EntityManager entityManager;

	public RelationalEntityDAO() {
		entityManager = Entity.entityManager();
	}

	@Override
	public List<Entity> findAllEntities() {
		// TODO Auto-generated method stub
		return Entity.findAllEntitys();
	}

	@Override
	public List<Entity> findEntitiesByNameLike(String name) {
		// TODO Auto-generated method stub
		return Entity.findEntitysByNameLike(name).getResultList();

	}

	@Override
	public List<Entity> findEntitiesByNamespaceEquals(String namespace) {
		// TODO Auto-generated method stub
		return Entity.findEntitysByNamespaceEquals(namespace).getResultList();

	}

	@Override
	public List<Entity> findEntitiesByNamespaceLike(String namespace) {
		// TODO Auto-generated method stub
		return Entity.findEntitysByNamespaceLike(namespace).getResultList();
	}

	@Override
	public void saveEntity(Entity entity) {
		// TODO Auto-generated method stub
		String tableName = this.generateTableName(entity);
		System.err.println("-tableName: "+tableName);
		System.err.println("-nome da entity: "+entity.getName());
		this.entityManager.createNativeQuery("create table "+tableName);
		entity.persist();
	}

	@Override
	public long countEntities() {
		// TODO Auto-generated method stub
		return Entity.countEntitys();
	}

	@Override
	public void removeEntity(Entity entity) {
		// TODO Auto-generated method stub
		String tableName = this.generateTableName(entity);
		System.err.println("tableName: "+tableName);
		System.err.println("nome da entity: "+entity.getName());
		this.entityManager.createNativeQuery("drop table " + tableName);
		entity.remove();
		
	}

	@Override
	public Entity findEntity(Long id) {
		// TODO Auto-generated method stub
		return Entity.findEntity(id);
	}

	@Override
	public Entity update(Entity entity) {
		// TODO Auto-generated method stub
		Entity oldEntity = this.findEntity(entity.getId());
		String oldTableName = this.generateTableName(oldEntity);
		String newTableName = this.generateTableName(entity);
		String sql = "alter table " +oldTableName+ "rename to "+ newTableName;
		this.entityManager.createNativeQuery(sql);
		return entity.merge();
	}
	
	private String generateTableName(Entity entity){
		return "LOM_"+entity.getNamespace()+"_"+entity.getName();
	}
	
	@Override
	public List<Entity> findEntityEntries(int firstResult, int maxResults) {
		// TODO Auto-generated method stub
		return Entity.findEntityEntries(firstResult, maxResults);
	}

}
