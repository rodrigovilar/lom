package com.nanuvem.lom.model;

import javax.persistence.ManyToOne;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class PropertyValue {

    private String _value;

    @ManyToOne
    private Instance instance;

    @ManyToOne
    private Property property;
}
