package com.nanuvem.lom.service;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import com.nanuvem.lom.dao.typesquare.Instance;
import com.nanuvem.lom.dao.typesquare.PropertyValue;

@RooService(domainTypes = { com.nanuvem.lom.dao.typesquare.PropertyValue.class })
public interface PropertyValueService {

	List<PropertyValue> findPropertyValuesByInstance(Instance instance);
}
