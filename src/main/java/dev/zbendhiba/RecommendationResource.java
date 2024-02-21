package dev.zbendhiba;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@ApplicationScoped
@Path("/")
public class RecommendationResource {
    @GET
    @Path("hello")
    public String hello(){
        return "Hello world!";
    }

}
