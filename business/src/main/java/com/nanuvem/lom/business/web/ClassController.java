package com.nanuvem.lom.business.web;

import java.util.Collection;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nanuvem.lom.business.ClassServiceImpl;

@Controller
@RequestMapping("/classes")
public class ClassController {

	private ClassServiceImpl service;

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        List<com.nanuvem.lom.kernel.Class> result = service.listAll();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(toJsonArray(result), headers, HttpStatus.OK);
    }
    
	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		com.nanuvem.lom.kernel.Class clazz = fromJsonToClass(json);
		service.create(clazz);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	public static com.nanuvem.lom.kernel.Class fromJsonToClass(String json) {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonFactory factory = objectMapper.getJsonFactory();

		try {
			JsonNode classJSON = objectMapper.readTree(factory
					.createJsonParser(json));
			com.nanuvem.lom.kernel.Class clazz = new com.nanuvem.lom.kernel.Class();

			if (classJSON.has("id")) {
				clazz.setId(classJSON.get("id").asLong());
			}

			if (classJSON.has("version")) {
				clazz.setVersion(classJSON.get("version").asInt());
			}

			if (classJSON.has("name")) {
				clazz.setName(classJSON.get("name").asText());
			}

			if (classJSON.has("namespace")) {
				clazz.setNamespace(classJSON.get("namespace").asText());
			}

			return clazz;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
    private static ObjectNode class2json(com.nanuvem.lom.kernel.Class clazz) {
		ObjectNode noClass = JsonNodeFactory.instance.objectNode();
		
		noClass.put("id", clazz.getId());
		noClass.put("name", clazz.getName());			
		noClass.put("namespace", clazz.getNamespace());
		noClass.put("version", clazz.getVersion());
		
		return noClass;
	}

    
    public static String toJsonArray(Collection<com.nanuvem.lom.kernel.Class> collection) {
    	ArrayNode classesArray = JsonNodeFactory.instance.arrayNode();

		for (com.nanuvem.lom.kernel.Class clazz : collection) {
			ObjectNode noClass = class2json(clazz);
			classesArray.add(noClass);
		}

    	return classesArray.toString();
    }


}
