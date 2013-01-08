package com.nanuvem.lom.dao;

import com.nanuvem.lom.model.Instance;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Instance.class)
public interface InstanceDAO {
}
