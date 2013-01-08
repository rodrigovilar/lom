package com.nanuvem.lom.dao;

import com.nanuvem.lom.model.PropertyValue;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = PropertyValue.class)
public interface PropertyValueDAO {
}
