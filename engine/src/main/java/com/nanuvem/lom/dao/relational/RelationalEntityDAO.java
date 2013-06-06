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
		String tableName = this.generateTableName(entity);
		this.entityManager.createNativeQuery("create table " + tableName)
				.executeUpdate();
		entity.persist();
	}

	@Override
	public long countEntities() {
		// TODO Auto-generated method stub
		return Entity.countEntitys();
	}

	@Override
	public void removeEntity(Entity entity) {
		String tableName = this.generateTableName(entity);
		this.entityManager.createNativeQuery("drop table " + tableName)
				.executeUpdate();
		entity.remove();
	}

	@Override
	public Entity findEntity(Long id) {
		return Entity.findEntity(id);
	}

	@Override
	public Entity update(Entity entity) {
		entityManager.clear();
		Entity oldEntity = this.findEntity(entity.getId());
		String oldTableName = this.generateTableName(oldEntity);
		String newTableName = this.generateTableName(entity);
		String sql = "alter table " + oldTableName + " rename to "
				+ newTableName;
		this.entityManager.createNativeQuery(sql).executeUpdate();
		return entity.merge();
	}

	private String generateTableName(Entity entity) {
		String name = entity.getName();
		name = name.replace(" ", "_");
		String namespace = entity.getNamespace();
		if (!(namespace == null))
			namespace = namespace.replace(" ", "_");
		else
			namespace = "DefaultNamespace";
		return "LOM_" + namespace + "_" + name;
	}

	@Override
	public List<Entity> findEntityEntries(int firstResult, int maxResults) {
		return Entity.findEntityEntries(firstResult, maxResults);
	}

}
