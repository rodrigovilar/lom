package com.nanuvem.lom;

import com.nanuvem.lom.model.Entity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = Entity.class)
@Controller
@RequestMapping("/entitys")
public class EntityController {
}
