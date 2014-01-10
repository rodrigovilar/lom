package com.nanuvem.lom.lomgui;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

		addClassNode(noClasses, 1, "Cliente");
		addClassNode(noClasses, 2, "Produto");
		addClassNode(noClasses, 3, "Funcionario");
		addClassNode(noClasses, 4, "Fornecedor");

		return noClasses.toString();
	}

	private void addClassNode(ArrayNode classesNode, int classId, String className) {
		ObjectNode classNode = JsonNodeFactory.instance.objectNode();
		classNode.put("id", classId);
		classNode.put("name", className);
		classNode.put("fullName", className);
		classesNode.add(classNode);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/class/{fullName}/instances")
	public String getInstances(@PathParam("fullName") String fullName) {
		ObjectNode result = JsonNodeFactory.instance.objectNode();

		ArrayNode attributesNode = JsonNodeFactory.instance.arrayNode();
		result.put("attributes", attributesNode);

		addAttributes(attributesNode, "nome", "String");
		addAttributes(attributesNode, "cpf", "String");
		addAttributes(attributesNode, "dataNasc", "Date");

		ArrayNode instancesNode = JsonNodeFactory.instance.arrayNode();
		result.put("instances", instancesNode);

		addInstance(instancesNode, 1, "Jose", "0000000", "12/12/12");
        addInstance(instancesNode, 2, "Maria", "1111111", "11/11/11");
        
		return result.toString();
	}

	private void addAttributes(ArrayNode attributesNode, String name, String type) {
		ObjectNode attributeNode = JsonNodeFactory.instance.objectNode();
		attributeNode.put("name", name);
		attributeNode.put("type", type);
		attributesNode.add(attributeNode);
	}

	private void addInstance(ArrayNode instancesNode, int id, String nome, String cpf, String dataNasc) {
		ObjectNode instanceNode = JsonNodeFactory.instance.objectNode();
		instanceNode.put("id", id);
		instanceNode.put("nome", nome);
		instanceNode.put("cpf", cpf);
		instanceNode.put("dataNasc", dataNasc);
		instancesNode.add(instanceNode);
	}
}
