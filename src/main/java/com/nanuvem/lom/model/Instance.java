package com.nanuvem.lom.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
public class Instance {

    @ManyToOne
    private Entity entity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "instance")
    private Set<PropertyValue> _values = new HashSet<PropertyValue>();
}
