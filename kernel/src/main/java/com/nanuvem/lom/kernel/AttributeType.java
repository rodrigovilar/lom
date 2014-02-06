package com.nanuvem.lom.kernel;

import java.util.ArrayList;
import java.util.List;

public enum AttributeType {
	TEXT, LONGTEXT, PASSWORD, OBJECT, INTEGER, REAL, DATE, TIME, LIST, MAP, BINARY;

	public static List<AttributeType> getList() {
		List<AttributeType> list = new ArrayList<AttributeType>();

		for (AttributeType operadoraTelefonica : values()) {
			list.add(operadoraTelefonica);
		}
		return list;
	}

}
