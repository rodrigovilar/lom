package com.nanuvem.lom.dao.relational;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nanuvem.lom.dao.PropertyDAO;
import com.nanuvem.lom.dao.typesquare.Entity;
import com.nanuvem.lom.dao.typesquare.Property;

@Repository
public class RelationalPropertyDAO implements PropertyDAO {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void init(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}

	@Override
	public void saveProperty(Property property) {
		// TODO Auto-generated method stub

	}

	@Override
	public Entity findEntity(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeProperty(Property property) {
		// TODO Auto-generated method stub
		String entityName = property.getEntity().getName();
		String sql = "alter table" + entityName + "drop column"
				+ property.getName();
		this.jdbcTemplate.execute(sql);

	}

	@Override
	public long countProperties() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Property findProperty(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Property> findAllProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Property> findPropertyEntries(int firstResult, int maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Property updateProperty(Property property) {
		// TODO Auto-generated method stub
		return null;
	}
}
