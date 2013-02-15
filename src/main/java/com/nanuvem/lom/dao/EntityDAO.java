package com.nanuvem.lom.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.nanuvem.lom.model.Entity;

@RooJpaRepository(domainType = Entity.class)

public interface EntityDAO {
	//@Query("select e from Entity as e where e.name LIKE %:name%")
	//@Transactional(readOnly = true)
	//List<Entity> findEntitiesByName(@Param("name") String name);
}
