package dev.zbendhiba;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@ApplicationScoped
@Path("/")
public class RecommendationResource {

    @Inject
    RecommendationRestaurant recommendationRestaurant;

    @POST
    @Path("hello")
    public String hello(String question){
        return recommendationRestaurant.recommend(question, "Mika");
    }

}
