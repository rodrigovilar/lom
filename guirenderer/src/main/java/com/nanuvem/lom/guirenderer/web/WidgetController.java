package com.nanuvem.lom.guirenderer.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@RequestMapping("/widgets")
@Controller
public class WidgetController {

	@RequestMapping
	@ResponseBody
    public ResponseEntity<String> index() {		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/javascript; charset=utf-8");
		return new ResponseEntity<String>(getRootWidget(), headers, HttpStatus.OK);
    }

	private String getRootWidget() {
		return "(function() {\n" +
				"  var UlRootWidget;\n" +

				"  UlRootWidget = (function() {\n" +
				"    UlRootWidget.prototype.draw = function(jsonObj) {\n" +
				"      var ul;\n" +
				"      ul = $(\"<ul>\");\n" +
				"      $(\"div\").append(ul);\n" +
				"      return $.each(jsonObj, function(i, entity) {\n" +
				"        return ul.append($(\"<li>\" + entity.name + \"</li>\"));\n" +
				"      });\n" +
				"    };\n" +

				"    function UlRootWidget() {\n" +
				"      var _this = this;\n" +
				"      $.getJSON('entities', function(jsonObj) {\n" +
				"        return _this.draw(jsonObj);\n" +
				"      });\n" +
				"    }\n" +
				"    return UlRootWidget;\n" +
				"  })();\n" +

				"  $(function() {\n" +
				"    return new UlRootWidget;\n" +
				"  });\n" +
				"}).call(this);\n";
	}	
}
