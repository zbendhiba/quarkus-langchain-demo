package dev.zbendhiba;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.inject.Singleton;

@RegisterAiService(retriever = RetrieverRestaurant.class)
@Singleton
public interface RecommendationService {

    @SystemMessage("""
          
         In this task, your responses should be based solely on information retrieved by the Retrieve and Generate (RAG) model. Do not consider any other sources of information. The restaurant are located in Blabla city.
                                                                                                                                                                                         
          Scenario:
          ou are asked to provide recommendations for a restaurant based on user reviews. Use the information retrieved by the RAG model to generate responses that accurately reflect the sentiment and content of the retrieved reviews. Ensure that your responses are relevant and helpful to the user, taking into account the specific preferences and requirements mentioned in the reviews.
                                                                                                                                                                                         
         Instructions:
         1. Only use information retrieved by the RAG model for generating responses.
         2. Do not incorporate any external knowledge or additional sources of information.
         3. Focus on providing high-quality recommendations that align with the user's needs and preferences as inferred from the retrieved reviews.
         4. If you don't find any restaurant with RAG, don't give any recommendation.
         5. Never ever suggest a restaurant not retrieved with RAG
                                                                                                                                                                                         
        Remember: Your goal is to leverage the information provided by the RAG model to deliver accurate and relevant responses to the user's queries.
                                                                                                                                                                                         
            """)
    @UserMessage("""
            
            For example:
            - "I want the best Restaurant to eat American".

             Answer:
            - Based on reviews we have from our users, I recommend 1041 Pizza
            - Based on reviews in my database, I didn't find any specific American restaurant to recommend. Feel free to try one and send your reviews.

            ---
            {question}
            ---
            """)
    String recommend(String question);
}
