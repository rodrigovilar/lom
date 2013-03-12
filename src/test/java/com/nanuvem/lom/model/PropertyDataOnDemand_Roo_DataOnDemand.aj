// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.nanuvem.lom.model;

import com.nanuvem.lom.model.EntityDataOnDemand;
import com.nanuvem.lom.model.Property;
import com.nanuvem.lom.model.PropertyDataOnDemand;
import com.nanuvem.lom.model.PropertyType;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect PropertyDataOnDemand_Roo_DataOnDemand {
    
    declare @type: PropertyDataOnDemand: @Component;
    
    private Random PropertyDataOnDemand.rnd = new SecureRandom();
    
    private List<Property> PropertyDataOnDemand.data;
    
    @Autowired
    EntityDataOnDemand PropertyDataOnDemand.entityDataOnDemand;
    
    public Property PropertyDataOnDemand.getNewTransientProperty(int index) {
        Property obj = new Property();
        setConfiguration(obj, index);
        setName(obj, index);
        setType(obj, index);
        return obj;
    }
    
    public void PropertyDataOnDemand.setConfiguration(Property obj, int index) {
        String configuration = "configuration_" + index;
        obj.setConfiguration(configuration);
    }
    
    public void PropertyDataOnDemand.setName(Property obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }
    
    public void PropertyDataOnDemand.setType(Property obj, int index) {
        PropertyType type = PropertyType.class.getEnumConstants()[0];
        obj.setType(type);
    }
    
    public Property PropertyDataOnDemand.getSpecificProperty(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Property obj = data.get(index);
        Long id = obj.getId();
        return Property.findProperty(id);
    }
    
    public Property PropertyDataOnDemand.getRandomProperty() {
        init();
        Property obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Property.findProperty(id);
    }
    
    public boolean PropertyDataOnDemand.modifyProperty(Property obj) {
        return false;
    }
    
    public void PropertyDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Property.findPropertyEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Property' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Property>();
        for (int i = 0; i < 10; i++) {
            Property obj = getNewTransientProperty(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}
