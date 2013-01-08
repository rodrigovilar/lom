package com.nanuvem.lom.dao;

import com.nanuvem.lom.model.Entity;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Entity.class)
public interface EntityDAO {
}
