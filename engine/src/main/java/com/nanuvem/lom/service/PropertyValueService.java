package com.nanuvem.lom.service;

import java.util.List;

import org.springframework.roo.addon.layers.service.RooService;

import com.nanuvem.lom.model.Instance;
import com.nanuvem.lom.model.PropertyValue;

@RooService(domainTypes = { com.nanuvem.lom.model.PropertyValue.class })
public interface PropertyValueService {

	List<PropertyValue> findPropertyValuesByInstance(Instance instance);
}
