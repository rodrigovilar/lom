package com.nanuvem.lom;

import com.nanuvem.lom.model.Property;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = Property.class)
@Controller
@RequestMapping("/propertys")
public class PropertyController {
}
