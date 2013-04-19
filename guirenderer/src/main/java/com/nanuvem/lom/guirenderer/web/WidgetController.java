package com.nanuvem.lom.guirenderer.web;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/widgets")
@Controller
public class WidgetController {

	@RequestMapping
	@ResponseBody
	public ResponseEntity<String> getRootWidget(HttpServletRequest servletRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/javascript; charset=utf-8");
		return new ResponseEntity<String>(getWidgetScript(servletRequest,
				"UlRootWidget"), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/entity/{id}", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getEntityWidget(@PathVariable("id") Long id,
			HttpServletRequest servletRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/javascript; charset=utf-8");
		return new ResponseEntity<String>(getEntityWidget(servletRequest, id,
				"TableInstanceListing"), headers, HttpStatus.OK);

	}

	private String getEntityWidget(HttpServletRequest servletRequest, Long id,
			String filename) {
		String result = getWidgetScript(servletRequest, filename);
		return result.replace("'##ENTITY_ID##'", id.toString());
	}

	private String getWidgetScript(HttpServletRequest servletRequest,
			String filename) {

		StringBuilder result = new StringBuilder();
		String filepath = mountFilePath(servletRequest, filename);
		BufferedReader in = null;
		FileReader fileReader = null;
		
		try {
			fileReader = new FileReader(filepath);
			in = new BufferedReader(fileReader);

			while (in.ready()) {
				result.append(in.readLine());

			}
			
		} catch (IOException e) {
			return e.getMessage();

		} finally {
			String error = closeStream(in, fileReader);
			if (error != null) {
				return error;
			}
		}

		return result.toString();
	}

	private String mountFilePath(HttpServletRequest servletRequest,
			String filename) {
		String filepath = servletRequest.getSession().getServletContext()
				.getRealPath("/")
				+ java.io.File.separator
				+ "WEB-INF"
				+ java.io.File.separator
				+ "scripts" + java.io.File.separator + filename + ".js";
		return filepath;
	}

	private String closeStream(BufferedReader in, FileReader fileReader) {
		try {
			if (in != null) {
				in.close();
			}
			if (fileReader != null) {
				fileReader.close();
			}
		} catch (IOException e) {
			return e.getMessage();
		}
		return null;
	}
}