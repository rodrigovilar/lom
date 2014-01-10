package com.nanuvem.lom.lomgui;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/widget")
public class WidgetService {

	@Context
	private HttpServletRequest servletRequest;

	@GET
	@Produces("text/plain; charset=utf-8")
	@Path("/root")
	public Response getRootWidget() {
		String result = FileSystemUtil.getWidgetScript(servletRequest, "UlRootWidget");
		return Response.ok(result).build();
	}

	@GET
	@Produces("text/plain; charset=utf-8")
	@Path("/class/{fullName}")
	public Response getClassWidget(@PathParam("fullName") String fullName) {
		String result = FileSystemUtil.getWidgetScript(servletRequest, "TableInstanceListing");
		return Response.ok(result).build();
	}
}
