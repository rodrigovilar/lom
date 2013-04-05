package com.nanuvem.lom.guirenderer.web;

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
}
