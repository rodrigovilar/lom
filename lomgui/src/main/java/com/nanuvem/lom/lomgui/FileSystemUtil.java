package com.nanuvem.lom.lomgui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class FileSystemUtil {

	static String getWidgetScript(HttpServletRequest servletRequest, String filename) {
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
	

	static String mountFilePath(HttpServletRequest servletRequest, String filename) {
		String filepath =
				servletRequest.getSession().getServletContext().getRealPath("/") + java.io.File.separator + "js"
						+ java.io.File.separator + filename + ".js";
		return filepath;
	}
	
	static String closeStream(BufferedReader in, FileReader fileReader) {
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
