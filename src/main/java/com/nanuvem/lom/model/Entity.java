package com.nanuvem.lom.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
public class Entity {

    @NotNull
    private String name;

    private String namespace;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entity")
    private Set<Property> properties = new HashSet<Property>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entity")
    private Set<Instance> instances = new HashSet<Instance>();
}
