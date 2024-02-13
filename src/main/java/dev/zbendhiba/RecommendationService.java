package dev.zbendhiba;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.inject.Singleton;

@RegisterAiService(retriever = RetrieverRestaurant.class)
@Singleton
public interface RecommendationService {

    @SystemMessage("""
          
         In this task, your responses should be based solely on the data provided. Do not consider any other sources of information. The restaurant are located in Blabla city.
                                                                                                                                                                                         
         Scenario:
         You are asked to provide recommendations for a restaurant based on user reviews. Use the information retrieved by the data to generate responses that accurately reflect the sentiment and content of the retrieved reviews. Ensure that your responses are relevant and helpful to the user, taking into account the specific preferences and requirements mentioned in the reviews.
                                                                                                                                                                                         
         Instructions:
         1. Only use the reviews in the data provided.
         2. Do not incorporate any external knowledge or additional sources of information.
         3. Focus on providing high-quality recommendations that align with the user's needs and preferences as inferred from the retrieved reviews.
         4. If you don't find any restaurant in data, don't give any recommendation.
                                                                                                                                                                                         
        Remember: Your goal is to leverage the information provided by data to deliver accurate and relevant responses to the user's queries.
                                                                                                                                                                                         
            """)
    @UserMessage("""
            
            For example:
            - "I want the best Restaurant to eat American".
            - "I want recommendation for a restaurant with Chineese food"

             Answer:
            - Based on reviews we have from our users, I recommend 1041 Pizza for American restaurant at Blabla city.
            - Based on reviews in my database, I didn't find any specific American restaurant to recommend. Feel free to try one and send your reviews.

            ---
            {question}
            ---
            """)
    String recommend(String question);
}
