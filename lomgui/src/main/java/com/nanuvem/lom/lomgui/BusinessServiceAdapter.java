package com.nanuvem.lom.lomgui;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

@Path("/data")
public class BusinessServiceAdapter {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/class")
	public String getClasses() {
		ArrayNode noClasses = JsonNodeFactory.instance.arrayNode();

		addEntityNode(noClasses, 1, "Cliente");
		addEntityNode(noClasses, 2, "Produto");
		addEntityNode(noClasses, 3, "Funcionario");
		addEntityNode(noClasses, 4, "Fornecedor");

		return noClasses.toString();
	}

	private void addEntityNode(ArrayNode noClasses, int classId, String className) {
		ObjectNode noEntidade = JsonNodeFactory.instance.objectNode();
		noEntidade.put("id", classId);
		noEntidade.put("name", className);
		noClasses.add(noEntidade);
	}
}
