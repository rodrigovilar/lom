
package com.nanuvem.lom.lomgui;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/widget")
public class WidgetService {
    
    @GET 
    @Produces("text/javascript; charset=utf-8")
    @Path("/root")
    public Response getRootWidget() {
        return Response.ok("window.alert('123')").build();
    }
}
