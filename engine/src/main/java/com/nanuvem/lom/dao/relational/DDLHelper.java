package com.nanuvem.lom.dao.relational;

import com.nanuvem.lom.dao.typesquare.Entity;

public class DDLHelper {

	public static String generateTableNameForAnEntity(Entity entity) {
		String name = entity.getName();
		name = name.replace(" ", "_");
		String namespace = entity.getNamespace();
		if (!(namespace == null))
			namespace = namespace.replace(" ", "_");
		else
			namespace = "DefaultNamespace";
		return "LOM_" + namespace + "_" + name;
	}
}
