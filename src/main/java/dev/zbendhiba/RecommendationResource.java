package dev.zbendhiba;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/recommend")
public class RecommendationResource {

    @Inject
    RecommendationService recommendationService;

    @POST
    public String triage(String question) {
        return recommendationService.recommend(question);
    }

}
