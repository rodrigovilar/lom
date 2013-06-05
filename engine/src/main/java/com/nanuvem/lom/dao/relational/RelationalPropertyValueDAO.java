package com.nanuvem.lom.dao.relational;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nanuvem.lom.dao.PropertyValueDAO;
import com.nanuvem.lom.dao.typesquare.Instance;
import com.nanuvem.lom.dao.typesquare.PropertyValue;

@Repository
public class RelationalPropertyValueDAO implements PropertyValueDAO {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void init(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}

	@Override
	public List<PropertyValue> findPropertyValuesByInstance(Instance instance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removePropertyValue(PropertyValue propertyValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public long countPropertyValues() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PropertyValue findPropertyValue(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PropertyValue> findAllPropertyValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PropertyValue> findPropertyValueEntries(int firstResult,
			int maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void savePropertyValue(PropertyValue propertyValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public PropertyValue updatePropertyValue(PropertyValue propertyValue) {
		// TODO Auto-generated method stub
		return null;
	}
}
