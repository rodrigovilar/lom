package com.nanuvem.lom.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Property {

    @NotNull
    private String name;

    private String configuration;

    @ManyToOne
    private Entity entity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PropertyType type;
}
