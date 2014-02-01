package com.nanuvem.lom.kernel;

public class ClassHelper {

	public static Class createClass(ClassServiceImpl classService, String namespace, String name) {
		Class clazz = new Class();
		clazz.setNamespace(namespace);
		clazz.setName(name);
		classService.create(clazz);
		return clazz;
	}

}
