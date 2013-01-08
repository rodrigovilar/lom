package com.nanuvem.lom.dao;

import com.nanuvem.lom.model.Property;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Property.class)
public interface PropertyDAO {
}
