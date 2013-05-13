package com.nanuvem.lom.guirenderer.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@RequestMapping("/entities")
@Controller
public class EntityController {

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
    public ResponseEntity<String> index() {
		
		ArrayNode noEntidades = JsonNodeFactory.instance.arrayNode();
		
		addEntityNode(noEntidades, 1, "Cliente");
		addEntityNode(noEntidades, 2, "Produto");
		addEntityNode(noEntidades, 3, "Funcionario");
		addEntityNode(noEntidades, 4, "Fornecedor");
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		
		return new ResponseEntity<String>(noEntidades.toString(), headers, HttpStatus.OK);
    }

	private void addEntityNode(ArrayNode noEntidades, int idEntidade, String nameEntidade) {
		ObjectNode noEntidade = JsonNodeFactory.instance.objectNode();
		noEntidade.put("id", idEntidade);
		noEntidade.put("name", nameEntidade);
		noEntidades.add(noEntidade);
	}

	@RequestMapping(value = "/{id}/instances", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getEntityInstances(@PathVariable("id") Long id) {
		ObjectNode noResult = JsonNodeFactory.instance.objectNode();
		ArrayNode noProperties = JsonNodeFactory.instance.arrayNode();
		ArrayNode noInstances = JsonNodeFactory.instance.arrayNode();
		

		noResult.put("instances", noInstances);
		noResult.put("entityId", id);
		noResult.put("properties", noProperties);

		addInstance(noInstances, 1, "Jose", "0000000", "12/12/12");
		addInstance(noInstances, 2, "Maria", "1111111", "11/11/11");


		addProperty(noProperties, 1, "nome", "String");
		addProperty(noProperties, 2, "cpf", "String");
		addProperty(noProperties, 3, "dataNasc", "Date");


		 

		



		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/javascript; charset=utf-8");
		return new ResponseEntity<String>(noResult.toString(), headers, HttpStatus.OK);

	}



	private void addProperty(ArrayNode noProperties, int id, String name, String type  ){

		ObjectNode noProperty = JsonNodeFactory.instance.objectNode();

		noProperty.put("id",id);
		noProperty.put( "name",name); 
		noProperty.put( "type", type);
		
		noProperties.add(noProperty);


	}

	private void addValue(ArrayNode noValues, String key, String value){

		ObjectNode noValue = JsonNodeFactory.instance.objectNode();

		noValue.put("key", key);
		noValue.put("value", value);

		noValues.add(noValue);

	}

	private void addInstance(ArrayNode noInstances, int id, String nome, String cpf, String dataNasc  ){
		ObjectNode noInstance = JsonNodeFactory.instance.objectNode();		
		noInstance.put("id", id);

		ArrayNode noValues = JsonNodeFactory.instance.arrayNode();
 		noInstance.put("values", noValues);

 
		addValue(noValues, "nome", nome );
		addValue(noValues, "cpf", cpf);
		addValue(noValues, "dataNasc", dataNasc);


		noInstances.add(noInstance);
}
}
