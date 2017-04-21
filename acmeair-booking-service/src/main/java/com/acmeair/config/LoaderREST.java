package com.acmeair.config;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.acmeair.loader.Loader;
import com.huawei.paas.cse.provider.rest.common.RestSchema;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;

@RestSchema(schemaId = "loader")
@Path("/info/loader")
@Api(value = "Customer Service User Information Loading Service", produces = MediaType.TEXT_PLAIN)
public class LoaderREST {

    @Autowired
    private Loader loader;

    @GET
    @Path("/query")
    @Produces(MediaType.TEXT_PLAIN)
    public String queryLoader() {
        String response = loader.queryLoader();
        return response;
    }

    @GET
    @Path("/load")
    @Produces(MediaType.TEXT_PLAIN)
    public String loadDB(@DefaultValue("-1") @QueryParam("numCustomers") long numCustomers) {
        String response = loader.loadDB(numCustomers);
        return response;
    }
}
