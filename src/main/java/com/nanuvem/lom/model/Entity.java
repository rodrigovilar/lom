package com.nanuvem.lom.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
//@Table(name = "Entity", uniqueConstraints = @javax.persistence.UniqueConstraint({ "namespace", "name" }))
@RooJpaActiveRecord(finders = { "findEntitysByNameEquals", "findEntitysByNameLike", "findEntitysByNamespaceLike", "findEntitysByNamespaceEquals" })
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

    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        
        if (!this.isValidName()) {
            throw new ValidationException("Invalid characters in name");
        }
        if (!this.isValidNamespace()) {
            throw new ValidationException("Invalid characters in namespace");
        }
        List<Entity> entitys = Entity.findEntitysByNameLike(this.name.toLowerCase()).getResultList();
        
        if (entitys.size() > 0){
        	throw new ValidationException("Entity with same name already exists!");
        }
        
        try {
            this.entityManager.persist(this);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public boolean validateNameAndNamespace() {
        if (this.isValidName() && this.isValidNamespace()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isValidName() {
        if (Pattern.matches("[a-zA-Z0-9 _]+", this.name)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isValidNamespace() {
        if (Pattern.matches("[a-zA-Z0-9 _]+", this.namespace)) {
            return true;
        } else {
            return false;
        }
    }
}
