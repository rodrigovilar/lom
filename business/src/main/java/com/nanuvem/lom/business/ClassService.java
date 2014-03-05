package com.nanuvem.lom.business;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

import com.nanuvem.lom.kernel.ClassServiceImpl;
import com.nanuvem.lom.kernel.dao.memory.MemoryDaoFactory;

@Path("/class")
public class ClassService {
	
	//TODO Flexibilizar a escolha da factory
	ClassServiceImpl impl = new ClassServiceImpl(new MemoryDaoFactory());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getClasses() {
		List<com.nanuvem.lom.kernel.Class> classes = impl.listAll();
		ArrayNode classesNode = JsonNodeFactory.instance.arrayNode();
		
		for(com.nanuvem.lom.kernel.Class c: classes ){
			addClassNode(classesNode, c);
		}
		
		return classesNode.toString();
	}

	private void addClassNode(ArrayNode classesNode, com.nanuvem.lom.kernel.Class c) {
		ObjectNode classNode = JsonNodeFactory.instance.objectNode();
		classNode.put("id", c.getId());
		classNode.put("name", c.getName());
		classNode.put("fullName", c.getFullName());
		classNode.put("namespace", c.getNamespace());
		classNode.put("version", c.getVersion());
		classesNode.add(classNode);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{fullName}/instances")
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
