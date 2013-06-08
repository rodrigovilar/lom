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
		return Entity.findAllEntitys();
	}

	@Override
	public List<Entity> findEntitiesByNameLike(String name) {
		return Entity.findEntitysByNameLike(name).getResultList();

	}

	@Override
	public List<Entity> findEntitiesByNamespaceEquals(String namespace) {
		return Entity.findEntitysByNamespaceEquals(namespace).getResultList();

	}

	@Override
	public List<Entity> findEntitiesByNamespaceLike(String namespace) {
		return Entity.findEntitysByNamespaceLike(namespace).getResultList();
	}

	@Override
	public void saveEntity(Entity entity) {
		String tableName = DDLHelper.generateTableNameForAnEntity(entity);
		this.entityManager.createNativeQuery("create table " + tableName + "(\n" +
				"lom_id INT\n" +
				");	")
				.executeUpdate();
		entity.persist();
	}

	@Override
	public long countEntities() {
		return Entity.countEntitys();
	}

	@Override
	public void removeEntity(Entity entity) {
		String tableName = DDLHelper.generateTableNameForAnEntity(entity);
		entity.remove();
		entity.flush();
		entity.clear();
		this.entityManager.createNativeQuery("drop table " + tableName)
				.executeUpdate();
	}

	@Override
	public Entity findEntity(Long id) {
		return Entity.findEntity(id);
	}

	@Override
	public Entity update(Entity entity) {
		entityManager.clear();
		Entity oldEntity = this.findEntity(entity.getId());
		String oldTableName = DDLHelper.generateTableNameForAnEntity(oldEntity);
		String newTableName = DDLHelper.generateTableNameForAnEntity(entity);
		String sql = "alter table " + oldTableName + " rename to "
				+ newTableName;
		Entity e = entity.merge();
		e.flush();
		this.entityManager.createNativeQuery(sql).executeUpdate();
		return e;
	}

	@Override
	public List<Entity> findEntityEntries(int firstResult, int maxResults) {
		return Entity.findEntityEntries(firstResult, maxResults);
	}

}
