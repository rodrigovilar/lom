package com.nanuvem.lom.lomgui;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/widget")
public class WidgetService {
	
	@Context
	private HttpServletRequest servletRequest;

	@GET
	@Produces("text/javascript; charset=utf-8")
	@Path("/root")
	public Response getRootWidget() {
		String result = FileSystemUtil.getWidgetScript(servletRequest, "UlRootWidget");
		return Response.ok(result).build();
	}
}
