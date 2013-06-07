package com.nanuvem.lom.dao.relational;

import java.util.List;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nanuvem.lom.dao.PropertyDAO;
import com.nanuvem.lom.dao.typesquare.Entity;
import com.nanuvem.lom.dao.typesquare.Property;
import com.nanuvem.lom.dao.typesquare.PropertyType;

@Repository
public class RelationalPropertyDAO implements PropertyDAO {

	private EntityManager entityManager;

	public RelationalPropertyDAO() {
		entityManager = Entity.entityManager();
	}

	@Override
	public long countProperties() {
		return Property.countPropertys();
	}

	@Override
	public Property findProperty(Long id) {
		
		return Property.findProperty(id);
	}

	@Override
	public List<Property> findAllProperties() {
		return Property.findAllPropertys();
	}

	@Override
	public List<Property> findPropertyEntries(int firstResult, int maxResults) {
		return Property.findPropertyEntries(firstResult, maxResults);
	}

	@Override
	public void removeProperty(Property property) {
		// TODO Auto-generated method stub
		String tableName = DDLHelper.generateTableNameForAnEntity(property
				.getEntity());
		String sql = "alter table " + tableName + " drop column "
				+ property.getName();
		this.entityManager.createNativeQuery(sql).executeUpdate();
		property.remove();
	}

	@Override
	public Property updateProperty(Property property) {
		// TODO Auto-generated method stub
		entityManager.clear();
		String tableName = DDLHelper.generateTableNameForAnEntity(property
				.getEntity());

		String datatype = this.parsePropertyTypeToHSQLDBDatatype(property);

		String sql = "alter table " + tableName + " alter "
				+ property.getName() + " " + datatype;
		this.entityManager.createNativeQuery(sql).executeUpdate();
		return property.merge();
	}

	@Override
	public void saveProperty(Property property) {
		// TODO Auto-generated method stub
		String tableName = DDLHelper.generateTableNameForAnEntity(property
				.getEntity());
		String datatype = this.parsePropertyTypeToHSQLDBDatatype(property);
		String sql = "alter table " + tableName + " add " + property.getName().replace(" ", "_")
				+ " " + datatype;
		this.entityManager.createNativeQuery(sql).executeUpdate();
		property.persist();

	}

	private String parsePropertyTypeToMysqlDatatype(Property property) {
		String datatype = "";
		switch (property.getType()) {
		case TEXT:
			datatype = "VARCHAR(30)";
			break;
		case LONG_TEXT:
			datatype = "TEXT";
			break;
		case PASSWORD:
			datatype = "VARCHAR(30)";
			break;
		case STRING_OBJECT:
			datatype = "VARCHAR(600)";
			break;
		case _BOOLEAN:
			datatype = "VARCHAR(1)";
			break;
		case _INTEGER:
			datatype = "INT";
			break;
		case _REAL:
			datatype = "DOUBLE";
			break;
		case _DATE:
			datatype = "DATE";
			break;
		case _TIME:
			datatype = "DATETIME";
			break;
		case LIST:
			datatype = "";
			break;
		case _MAP:
			datatype = "";
			break;
		case _BINARY:
			datatype = "BIN";
			break;
		default:
			datatype = "VARCHAR";
		}
		return datatype;
	}

	private String parsePropertyTypeToHSQLDBDatatype(Property property) {
		String datatype = "";
		switch (property.getType()) {
		case TEXT:
			datatype = "VARCHAR(30)";
			break;
		case LONG_TEXT:
			datatype = "TEXT";
			break;
		case PASSWORD:
			datatype = "VARCHAR(30)";
			break;
		case STRING_OBJECT:
			datatype = "VARCHAR(600)";
			break;
		case _BOOLEAN:
			datatype = "VARCHAR(1)";
			break;
		case _INTEGER:
			datatype = "INT";
			break;
		case _REAL:
			datatype = "DOUBLE";
			break;
		case _DATE:
			datatype = "DATE";
			break;
		case _TIME:
			datatype = "TIME";
			break;
		case LIST:
			datatype = "";
			break;
		case _MAP:
			datatype = "";
			break;
		case _BINARY:
			datatype = "BINARY";
			break;
		default:
			datatype = "VARCHAR(20)";
		}
		return datatype;
	}

}
