
package com.nanuvem.lom.lomgui;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/data")
public class BusinessServiceAdapter {
    
    @GET 
    @Produces("text/plain")
    public String getIt() {
        return "Hi there!";
    }
}
