package com.nanuvem.lom.model;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson(deepSerialize = true)
@Table(
		name="Entity",
		uniqueConstraints=
			@UniqueConstraint(columnNames={"namespace", "name"})
		)
public class Entity {

    @NotNull
    private String name;

    private String namespace;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entity")
    private Set<Property> properties = new HashSet<Property>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entity")
    private Set<Instance> instances = new HashSet<Instance>();
    
    public void setName(String name) {
    	if(!Pattern.matches("[a-zA-Z0-9 _]+", name)) {
    		throw new ValidationException();
    	}
        this.name = name;
    }
    
    public void setNamespace(String namespace) {
    	if(Pattern.matches("[a-zA-Z0-9 _]+", namespace) && (namespace.equals(""))) {
    		throw new ValidationException();
    	}
    	this.namespace = namespace;
    }
    
}
