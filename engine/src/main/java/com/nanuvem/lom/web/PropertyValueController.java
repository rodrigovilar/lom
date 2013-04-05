package com.nanuvem.lom.web;

import com.nanuvem.lom.model.PropertyValue;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = PropertyValue.class)
@Controller
@RequestMapping("/values")
public class PropertyValueController {
}
