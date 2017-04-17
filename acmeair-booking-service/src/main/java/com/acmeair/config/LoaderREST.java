package com.acmeair.config;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.acmeair.loader.Loader;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;


@Path("/info/loader")
@Api(value = "Customer Service User Information Loading Service", produces = MediaType.TEXT_PLAIN)
public class LoaderREST {
	
	@Autowired
	private Loader loader;	
	
	@GET
	@Path("/query")
	@Produces(MediaType.TEXT_PLAIN)
	public Response queryLoader() {			
		String response = loader.queryLoader();
		return Response.ok(response).build();	
	}
	
	
	@GET
	@Path("/load")
	@Produces(MediaType.TEXT_PLAIN)
	public Response loadDB(@DefaultValue("-1") @QueryParam("numCustomers") long numCustomers) {	
		String response = loader.loadDB(numCustomers);
		return Response.ok(response).build();	
	}
}
