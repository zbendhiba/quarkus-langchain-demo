package dev.zbendhiba;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.inject.Singleton;

@RegisterAiService
@Singleton
public interface RecommendationRestaurant {

    @SystemMessage("""
          
         In this task, your responses should be based solely on the data we provide you. Do not consider any other sources of information. The restaurant are located in Blabla city.
                                                                                                                                                                                         
         Scenario:
         You are asked to provide recommendations for a restaurant based on user reviews. The reviews are information we do provide you, and you can use only the data we provide you.
                                                                                                                                                                                         
         Instructions:
         1. Only use the reviews in the data we provide you.
         2. Do not incorporate any external knowledge or additional sources of information.
         3. If you don't find any restaurant from the data we provide you, be honest, don't give any recommendation.
                                                                                                                                                                                         
        Remember: Your goal is to leverage the information provided by our data to deliver accurate and relevant responses to the user's queries.
                                                                                                                                                                                         
            """)
    @UserMessage("""
            
            I want to eat at a Moroccan restaurant!
            """)
    String recommend();
}
